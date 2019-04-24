/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.queue;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 
 */
public class NotificationsHandler extends Thread implements Observer{
    
    private Socket socket;
    private PrintWriter out;
    private EventQueue queue;
     
    public NotificationsHandler(Socket socket){
        
        this.socket = socket;
        this.queue = EventQueue.getInstance();
        this.queue.addObserver(this);
        
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        this.start();
    }
    
    @Override
    public void run(){
        try {
                        
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String input = in.readLine();
            

            if (input != null) {
                System.out.println("Data :"+input);
                 
                in.close();
                socket.close();
            }

                      
        }catch(Exception ex){
                Logger.getLogger(NotificationsHandler.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            try {
                socket.close();
            } catch (IOException ex) {
                Logger.getLogger(NotificationsHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {

        try {
            System.out.println("Nuevo evento"+arg.toString());
            
            out.println(arg.toString());
             
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
