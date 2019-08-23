/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.queue;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;

/**
 *
 * @author Martin
 */
public class EventQueueNotificationServer extends Thread {

    private ServerSocket listener;
    private JTextArea txtArea;

    public EventQueueNotificationServer(int port) {
        createServer(port);
    }

    public EventQueueNotificationServer(int port, JTextArea txtArea) {
        createServer(port);
        this.txtArea = txtArea;
    }



    private void createServer(int port) {
        try {
            listener = new ServerSocket(port);

        } catch (IOException ex) {
            Logger.getLogger(EventQueueNotificationServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        try {

            while (true) {

                System.out.println("Esperando cliente para notificar...");

                Socket socket = listener.accept();

                NotificationsHandler singleClient = new NotificationsHandler(socket, this.txtArea);

            }

        } catch (IOException ex) {
            Logger.getLogger(EventQueueNotificationServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
