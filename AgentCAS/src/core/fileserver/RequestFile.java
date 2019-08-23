/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.fileserver;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author luismartin
 */
public class RequestFile extends Thread{
    
    private Socket socket;
    private PrintWriter out;
    private FileOutputStream fos;
    private String msgACL;
    private String fileName;
    private String ipServer;
    
    public RequestFile(String msgACL, String fileName, String ipServer){
        this.fileName = fileName;
        this.msgACL = msgACL;
        this.ipServer = ipServer;
        createServer();
    }
    
    private void createServer(){
        try {
            socket = new Socket(ipServer, 5002);
            
            fos = new FileOutputStream(fileName);
            
            out = new PrintWriter(socket.getOutputStream());
            
            out.println(msgACL);
            
            out.flush();           
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
                //setMessage(receiver + " => " + sender + "   " + "CONFIRM");
            //}
        } catch (Exception e) {
            e.printStackTrace();
        }
    
    }
    
    
}
