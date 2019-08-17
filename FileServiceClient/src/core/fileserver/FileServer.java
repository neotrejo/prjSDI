/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.fileserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;

/**
 *
 * @author luismartin
 */
public class FileServer extends Thread{
    
    private ServerSocket listener;
    private JTextArea txtArea;
    
    public FileServer(JTextArea txt){
        this.txtArea = txt;
        createServer();
    }
    public FileServer(){
       
        createServer();
    }
    
    private void createServer(){
        try {
           
            listener = new ServerSocket(5002);
        } catch (IOException ex) {
            Logger.getLogger(FileServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void run(){
        try {
            
            while (true) {
                
                System.out.println("Esperando solicitud de archivo...");
                
                Socket socket = listener.accept();  
                
                FileServerHandler singleClient = new FileServerHandler(socket, txtArea);    
                
            }
            
        }catch(IOException ex){
            Logger.getLogger(FileServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
