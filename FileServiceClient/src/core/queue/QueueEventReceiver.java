/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.queue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 
 */
public class QueueEventReceiver extends Thread{
    
    private Socket socket;
    private EventQueue queue;
    
    public QueueEventReceiver(Socket socket){
        this.socket = socket;
        this.queue = EventQueue.getInstance();
        this.start();
    }
    
    @Override
    public void run(){
        try {
                        
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String input = in.readLine();
            

            if (input != null) {
                System.out.println("Data :"+input);
                
                queue.addEvent(input);
                
                in.close();
                socket.close();
            }

                      
        }catch(Exception ex){
                Logger.getLogger(QueueEventReceiver.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            try {
                socket.close();
            } catch (IOException ex) {
                Logger.getLogger(QueueEventReceiver.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
