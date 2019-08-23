/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.server;

import core.data.ModelAgent;
import core.queue.EventQueueServer;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;

/**
 *
 * @author Diana
 */
public class ActionServer extends Thread{

    private ServerSocket listener;
    JTextArea txtA;
    private ModelAgent agent;
    
    public ActionServer(JTextArea txtA, ModelAgent agentType, int port) {
        this.txtA = txtA;
        this.agent = agentType;
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
                System.out.println("Esperando conexiones "+getClass().getName()+"...");

                Socket socket = listener.accept();

                ActionClient singleClient = new ActionClient(socket, txtA,  agent);

            }

        } catch (IOException ex) {
            Logger.getLogger(EventQueueServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
