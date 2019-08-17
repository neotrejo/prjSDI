/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.queue;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;

/**
 *
 * @author SDI
 */
public class EventQueueServer extends Thread {

    JTextArea txtA;
    private ServerSocket listener;

    public EventQueueServer(JTextArea txtA) {
        this.txtA = txtA;
        createServer();
    }

    public EventQueueServer() {
        createServer();
    }

    private void createServer() {
        try {
            listener = new ServerSocket(QueueConfig.SERVER_PORT);
        } catch (IOException ex) {
            Logger.getLogger(EventQueueServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        try {

            while (true) {
                if (txtA != null) {
                    String auxText = txtA.getText();
                    if (auxText.equals("")) {
                        txtA.setText("Monitoreando...");
                    }
                }
                System.out.println("Esperando conexiones EventQueueServer...");

                Socket socket = listener.accept();

                QueueEventReceiver singleClient = new QueueEventReceiver(socket, txtA);

            }

        } catch (IOException ex) {
            Logger.getLogger(EventQueueServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
