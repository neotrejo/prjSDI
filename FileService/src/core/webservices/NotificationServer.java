/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.webservices;

import core.controller.MainController;
import core.data.MessageACL;
import core.data.Session;
import core.data.User;
import core.utils.CheckFile;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;

/**
 *
 * @author SDI
 */
public class NotificationServer extends Thread {

    private Socket socket;
    private String path = QueueConfig.SHARED_FOLDER;
    private String address;
    private int port;
    private User user;
    private JTextArea txtArea;

    public NotificationServer(String address, int port, User user, JTextArea txtArea) {
        this.address = address;
        this.port = port;
        this.user = user;
        this.txtArea = txtArea;
        while (!createServer()) {
            System.out.println("Trying to connect to FileServiceClient application on " + address + ":" + port + ".");
            System.out.println("Retry in 5 seconds...");
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException ex) {
                Logger.getLogger(NotificationServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private boolean createServer() {
        try {
            socket = new Socket(address, port);
            System.out.println(address + "-" + port);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public void run() {
        Session session = null;
        String dirDownload;
        Boolean download;
        try {

            while (true) {

                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                String input = br.readLine();

                if (input != null) {
                    System.out.println("New event: " + input);
                    ///////////////////////////////////////////////
                    JSONParser parser = new JSONParser();
                    JSONObject obj = (JSONObject) parser.parse(input);
                    MessageACL msgRec = new MessageACL(obj);

                    setMessage(user.getLocation() + " <= " + msgRec.getSender() + "   " + msgRec.getPerformative());

                    switch (msgRec.getPerformative()) {
                        case "INFORM_IF":
                            ///////////////                             
                            MessageACL msgSend = new MessageACL();
                            msgSend.setSender(user.getLocation());
                            msgSend.setReceiver(msgRec.getSender());

                            if (isIpAddress(msgRec.getSender())) {
                               

                                switch (msgRec.getOntology()) {
                                    case "ADD":
                                        session = MainController.getSessionByID(msgRec.getContent());

                                        dirDownload = user.getSharedfolder() + "/cursos/" + session.getCourseId();
                                        FileUtils.createPath(dirDownload);

                                        msgSend.setOntology(msgSend.DOWNLOAD);
                                        msgSend.setPerformative(msgSend.REQUEST);
                                        msgSend.setContent(session.getPathFile());

                                        new RequestFile(msgSend.toJSONString(), dirDownload + "/" + session.getFile(), msgSend.getReceiver(),true).start();

                                        break;
                                    case "UPDATE":
                                        JSONObject jsonObject = (JSONObject) parser.parse(msgRec.getContent());
                                        String idSession = (String) jsonObject.get("idSession");
                                        String fileName = (String) jsonObject.get("fileName");
                                        long size = (Long) jsonObject.get("size");

                                        session = MainController.getSessionByID(idSession);
                                        dirDownload = user.getSharedfolder() + "/cursos/" + session.getCourseId();
                                        FileUtils.createPath(dirDownload);

                                        CheckFile cFile = new CheckFile(dirDownload + "/" + session.getFile());
                                        if (cFile.getSize()== size && cFile.getNameFile().equals(fileName)) {
                                            msgSend.setOntology("-");
                                            msgSend.setPerformative(msgSend.REFUSE);
                                            msgSend.setContent("-");
                                            download=false;
                                        } else {
                                            msgSend.setOntology(msgSend.DOWNLOAD);
                                            msgSend.setPerformative(msgSend.REQUEST);
                                            msgSend.setContent(session.getPathFile());
                                            download=true;
                                        }
                                        new RequestFile(msgSend.toJSONString(), dirDownload + "/" + session.getFile(), msgSend.getReceiver(),download).start();
                                        break;
                                }
                                 setMessage(msgSend.getSender() + " => " + msgSend.getReceiver() + "   " + msgSend.getPerformative());
                            } else {
                                setMessage("Agente remitente desconocido...");
                            }
                            break;
                        case "INFORM":
                            break;

                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean isIpAddress(String input) {
        try {
            InetAddress inetAddress = InetAddress.getByName(input);
        } catch (UnknownHostException ex) {
            Logger.getLogger(NotificationServer.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    public void setMessage(String msg) {
        if (txtArea != null) {
            String aux = txtArea.getText();
            txtArea.setText(aux + "\n" + msg);
        }
    }

}
