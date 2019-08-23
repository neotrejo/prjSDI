/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.queue;

import core.data.ModelAgent;
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
    private ModelAgent agent;

    public EventQueueServer(JTextArea txtA, ModelAgent agentType, int port) {
        this.txtA = txtA;
        this.agent = agentType;
        createServer(port);
    }

    public EventQueueServer(int port) {
        createServer(port);
    }

    private void createServer(int port) {
        try {
            listener = new ServerSocket(port);
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

                QueueEventReceiver singleClient = new QueueEventReceiver(socket, txtA, agent);

            }

        } catch (IOException ex) {
            Logger.getLogger(EventQueueServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}