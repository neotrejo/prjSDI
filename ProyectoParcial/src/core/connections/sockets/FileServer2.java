/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.connections.sockets;

import core.data.Config;
import core.utils.MyLogger;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author luismartin
 */
public class FileServer2 extends Thread{
    
    private ServerSocket listener;
    
    public FileServer2(){
        createServer();
    }
    
    private void createServer(){
        try {
            listener = new ServerSocket(Config.SERVER_PORT);
        } catch (IOException ex) {
            Logger.getLogger(FileServer2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void run(){
        try {
            
            while (true) {
                MyLogger.log("Esperando conexiones...");
                
                Socket socket = listener.accept();  
                
                SingleClient singleClient = new SingleClient(socket);         
            }
            
        }catch(IOException ex){
            Logger.getLogger(FileServer2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
