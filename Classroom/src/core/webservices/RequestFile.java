/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.webservices;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author SDI
 */
public class RequestFile extends Thread{
    
    private Socket socket;
    private PrintWriter out;
    private FileOutputStream fos;
    private String filePath;
    private String fileName;
    
    public RequestFile(String filePath, String fileName){
        this.fileName = fileName;
        this.filePath = filePath;
        createServer();
    }
    
    private void createServer(){
        try {
            socket = new Socket("10.0.5.182", 5002);
            
            fos = new FileOutputStream(fileName);
            
            out = new PrintWriter(socket.getOutputStream());
            
            out.println(filePath);
            
            out.flush();
            
            //out.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        try {
           // while(true){
                InputStream br = socket.getInputStream();
                byte buffer[] = new byte[1024];
                
                int len = br.read(buffer);
                
                while(len > 0){
                    
                    fos.write(buffer,0,len);
                    //buffer = new byte[1024];
                    len = br.read(buffer);
                }
                fos.flush();
                fos.close();
                
            //}
        } catch (Exception e) {
            e.printStackTrace();
        }
    
    }
    
    
}
