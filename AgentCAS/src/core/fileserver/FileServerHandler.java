/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.fileserver;

import core.controller.MainController;
import core.data.MessageACL;
import core.data.ModelAgent;
import core.data.YellowPage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author SDI
 */
public class FileServerHandler extends Thread implements Observer {

    private Socket socket;
    private JTextArea txtArea;
    private ModelAgent agent;

    public FileServerHandler(Socket socket, JTextArea txtArea, ModelAgent agent) {
        this.txtArea = txtArea;
        this.socket = socket;
        this.agent = agent;
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
        try {

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String input = in.readLine();

            if (input != null) {
                System.out.println("File requested :" + input);

                ///////////////////////////////////////////////
                JSONParser parser = new JSONParser();
                JSONObject obj = (JSONObject) parser.parse(input);
                MessageACL msgRec = new MessageACL(obj);

                setMessage(msgRec.getReceiver() + " <= " + msgRec.getSender() + "   " + msgRec.getPerformative());

                //////////////////////////////////////////////////
                switch (msgRec.getPerformative()) {
                    case "REQUEST":
                        OutputStream out = socket.getOutputStream();

                        String path = msgRec.getContent();

                        RandomAccessFile raf = new RandomAccessFile(path, "r");
                        int file_size = (int) raf.length();
                        byte buffer[] = new byte[1024];

                        int len = raf.read(buffer);

                        while (len > 0) {
                            out.write(buffer, 0, len);
                            len = raf.read(buffer);
                        }

                        System.out.println("File sent");
                        raf.close();
                        out.close();

                        in.close();
                        socket.close();
                        MessageACL msgSend = new MessageACL();
                        msgSend.setSender(msgRec.getReceiver());
                        msgSend.setReceiver(msgRec.getSender());
                        msgSend.setPerformative(msgSend.AGREE);
                        msgSend.setOntology("-");
                        msgSend.setTypeSender(agent.getType());                        
                         YellowPage yp = MainController.getYPByIP(msgRec.getSender(), msgRec.getTypeSender());
                         sendMessage(msgSend.getReceiver(), Integer.parseInt(yp.getPort()), msgSend.toJSONString());
                        break;
                    case "REFUSE":
                        break;
                }
            }

        } catch (Exception ex) {
            Logger.getLogger(FileServerHandler.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                socket.close();
            } catch (IOException ex) {
                Logger.getLogger(FileServerHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {

        try {
            System.out.println("Nuevo evento" + arg.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setMessage(String msg) {
        if (txtArea != null) {
            String aux = txtArea.getText();
            txtArea.setText(aux + "\n" + msg);
        }
    }

}
