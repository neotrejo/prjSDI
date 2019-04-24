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

/**
 *
 * @author SDI
 */
public class FileServer extends Thread{
    
    private ServerSocket listener;
    
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
                
                FileServerHandler singleClient = new FileServerHandler(socket);    
                
            }
            
        }catch(IOException ex){
            Logger.getLogger(FileServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
