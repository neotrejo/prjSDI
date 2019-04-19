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

/**
 *
 * @author luismartin
 */
public class EventQueueServer extends Thread{
    
    private ServerSocket listener;
    
    public EventQueueServer(){
        createServer();
    }
    
    private void createServer(){
        try {
            listener = new ServerSocket(QueueConfig.SERVER_PORT);
        } catch (IOException ex) {
            Logger.getLogger(EventQueueServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void run(){
        try {
            
            while (true) {
                
                System.out.println("Esperando conexiones...");
                
                Socket socket = listener.accept();  
                
                QueueEventReceiver singleClient = new QueueEventReceiver(socket);    
                
            }
            
        }catch(IOException ex){
            Logger.getLogger(EventQueueServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
