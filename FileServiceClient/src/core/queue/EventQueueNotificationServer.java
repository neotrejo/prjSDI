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
 * @author Martin
 */
public class EventQueueNotificationServer extends Thread{
    
    private ServerSocket listener;
    
    public EventQueueNotificationServer(int port){
        createServer(port);
    }
    
    private void createServer(int port){
        try {
            listener = new ServerSocket(port);
        } catch (IOException ex) {
            Logger.getLogger(EventQueueNotificationServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void run(){
        try {
            
            while (true) {
                
                System.out.println("Esperando cliente para notificar...");
                
                Socket socket = listener.accept();  
                
                NotificationsHandler singleClient = new NotificationsHandler(socket);    
                
            }
            
        }catch(IOException ex){
            Logger.getLogger(EventQueueNotificationServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
