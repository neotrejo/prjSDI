/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.fileserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Luis
 */
public class FileServerHandler extends Thread implements Observer{
    
    private Socket socket;
     
    public FileServerHandler(Socket socket){
        
        this.socket = socket;
        
        this.start();
    }
    
    @Override
    public void run(){
        try {
                        
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String input = in.readLine();
            

            if (input != null) {
                System.out.println("File requested :"+input);
                
                OutputStream out = socket.getOutputStream();
                
                String path = input;
                
                RandomAccessFile raf = new RandomAccessFile(path, "r");
                int file_size = (int)raf.length();
                byte buffer[] = new byte[1024];
                
                int len = raf.read(buffer);
                
                while(len > 0){
                    out.write(buffer,0,len);
                    len = raf.read(buffer);
                }
                
                System.out.println("File sent");
                raf.close();
                out.close();
                
                in.close();
                socket.close();
            }

                      
        }catch(Exception ex){
                Logger.getLogger(FileServerHandler.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            try {
                socket.close();
            } catch (IOException ex) {
                Logger.getLogger(FileServerHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {

        try {
            System.out.println("Nuevo evento"+arg.toString());
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
