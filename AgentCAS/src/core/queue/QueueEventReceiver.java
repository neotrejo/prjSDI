/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.queue;

import core.controller.MainController;
import core.data.Course;
import core.data.MessageACL;
import core.data.ModelAgent;
import core.data.Session;
import core.data.Subscription;
import core.data.YellowPage;
import core.fileserver.CheckFile;
import core.fileserver.FileUtils;
import core.fileserver.RequestFile;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author
 */
public class QueueEventReceiver extends Thread {

    private Socket socket;
    private EventQueue queue;
    private JTextArea txtArea;
    private ModelAgent agent;

    public QueueEventReceiver(Socket socket) {
        this.socket = socket;
        this.queue = EventQueue.getInstance();
        this.start();
    }

    public QueueEventReceiver(Socket socket, JTextArea txtArea, ModelAgent agentType) {
        this.txtArea = txtArea;
        this.socket = socket;
        this.agent = agentType;
        this.queue = EventQueue.getInstance();
        this.start();
    }

    public void sendMessage(String ip, int port, String message) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // TODO code application logic here
                    Socket sender = new Socket(ip, port);
                    OutputStream os = sender.getOutputStream();
                    PrintWriter pw = new PrintWriter(os);

                    pw.println(message);
                    pw.flush();

                    os.close();
                    pw.close();
                    sender.close();

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }).start();

    }

    @Override
    public void run() {
        Session session;
        String dirDownload;
        try {

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String input = in.readLine();

            if (input != null) {
                System.out.println(input);
                JSONParser parser = new JSONParser();
                JSONObject obj = (JSONObject) parser.parse(input);
                MessageACL msgRec = new MessageACL(obj);

                System.out.println("QueueEventReceiver :" + input);

                if (this.txtArea != null) {

                    setMessage(msgRec.getSender() + " => " + msgRec.getReceiver() + "    " + msgRec.getPerformative());

                    switch (msgRec.getPerformative()) {
                        case "INFORM_IF":
                            ///////////////                           
                            if (msgRec.getReceiver().equals(agent.getIp())) {
                                if (agent.getUser() != null) {
                                    dirDownload = agent.getPathRootFolder();
                                } else {
                                    dirDownload = agent.getPathRootFolder() + "/cursos/";
                                }
                                MessageACL msgSend = new MessageACL(obj);
                                msgSend.setSender(msgRec.getReceiver());
                                msgSend.setReceiver(msgRec.getSender());
                                switch (msgRec.getOntology()) {
                                    case "ADD":
                                        session = MainController.getSessionByID(msgRec.getContent());
                                        addAndUpdate(session, msgSend, msgRec, dirDownload, true);
                                        break;
                                    case "UPDATE":
                                        JSONObject jsonObject = (JSONObject) parser.parse(msgRec.getContent());
                                        String idSession = (String) jsonObject.get("idSession");
                                        String fileName = (String) jsonObject.get("fileName");
                                        long size = (Long) jsonObject.get("size");

                                        session = MainController.getSessionByID(idSession);

                                        CheckFile cFile = new CheckFile(dirDownload + "/" + session.getFile());
                                        if (cFile.getSize() == size && cFile.getNameFile().equals(fileName)) {
                                            addAndUpdate(session, msgSend, msgRec, dirDownload, false);
                                        } else {
                                            addAndUpdate(session, msgSend, msgRec, dirDownload, true);
                                        }
                                        break;
                                }
                                setMessage(msgSend.getSender() + " => " + msgSend.getReceiver() + "   " + msgSend.getPerformative());
                            } else {
                                //Mensaje de la interfaz conferencista, audiencia o salon
                                if (msgRec.getReceiver() == "multicast") {
                                    ArrayList<YellowPage> multicast = MainController.getAllYellowPageButNot(agent.getType());
                                    for (YellowPage yp : multicast) {
                                        msgRec.setReceiver(yp.getHostname());
                                        sendMessage(yp.getHostname(), Integer.parseInt(yp.getPort()), msgRec.toJSONString());
                                    }
                                }
                            }
                            break;
                        case "INFORM":
                            break;
                        case "REFUSE":
                            break;

                    }
                }
//                queue.addEvent(input);
                in.close();
                socket.close();
            }

        } catch (Exception ex) {
            Logger.getLogger(QueueEventReceiver.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                socket.close();
            } catch (IOException ex) {
                Logger.getLogger(QueueEventReceiver.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public boolean isIpAddress(String input) {
        try {
            InetAddress inetAddress = InetAddress.getByName(input);
        } catch (UnknownHostException ex) {
            Logger.getLogger(QueueEventReceiver.class.getName()).log(Level.SEVERE, null, ex);
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

    public void addAndUpdate(Session session, MessageACL msgSend, MessageACL msgRec, String dirDownload, Boolean download) {

        if (agent.getType().equals(YellowPage.AUDIENCIA)) {  //IDENTIFICAR SI LE INTERESA EL ADD O UPDATE
            Session sess = MainController.getSession(msgRec.getContent());
            Subscription subscip = MainController.getSubscriptionSubjectUser(sess.getCourseId(), agent.getUser().getId());
            if (subscip == null) {
                download = false;
            }
        }
        if (agent.getType().equals(YellowPage.CONFERENCISTA)) {
            Course curso = MainController.getSubjectNameUserId(session.getCourseId(), agent.getUser().getId());
            if (curso == null) {
                download = false;
            }
        }

        dirDownload = dirDownload + "" + session.getCourseId();
        FileUtils.createPath(dirDownload);

        if (download) {
            msgSend.setOntology(msgSend.DOWNLOAD);
            msgSend.setPerformative(msgSend.REQUEST);
            msgSend.setContent(session.getPathFile());
            new RequestFile(msgSend.toJSONString(), dirDownload + "/" + session.getFile(), msgSend.getReceiver()).start();
        } else {
            msgSend.setOntology("-");
            msgSend.setPerformative(msgSend.REFUSE);
            msgSend.setContent("-");
            YellowPage yp = MainController.getYPByIP(msgRec.getSender(), msgRec.getTypeSender());
            sendMessage(msgSend.getReceiver(), Integer.parseInt(yp.getPort()), msgRec.toJSONString());
        }
    }

}
