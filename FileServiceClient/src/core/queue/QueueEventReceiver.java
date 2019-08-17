/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.queue;

import core.data.MessageACL;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author 
 */
public class QueueEventReceiver extends Thread{
    
    private Socket socket;
    private EventQueue queue;
    private JTextArea txtArea;
    
    public QueueEventReceiver(Socket socket){
        this.socket = socket;
        this.queue = EventQueue.getInstance();
        this.start();
    }
    
    public QueueEventReceiver(Socket socket, JTextArea txtArea){
        this.txtArea = txtArea;
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
                System.out.println(input);
                JSONParser parser = new JSONParser();
                JSONObject obj = (JSONObject)parser.parse(input);
                MessageACL msgRec = new MessageACL(obj);
                
                System.out.println("Data receiver :"+input);
                if (this.txtArea != null) {
                     String msg = txtArea.getText();
                     this.txtArea.setText(msg+"\n"+msgRec.getSender()+" => "+msgRec.getReceiver()+"    "+msgRec.getPerformative());
                 }
                
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
