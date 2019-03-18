/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.connections.sockets;

import core.data.Config;
import core.main.Descargas;
import core.main.DownloadListener;
import core.utils.GenericUtils;
import core.utils.JSONUtils;
import core.utils.MyLogger;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author Luis
 */
public class RequestSocket{
    
    private BufferedReader in;
    private PrintWriter out;
    private DownloadListener listener;
    private int BUFFER_SIZE = 8192;
    
    public RequestSocket(){
        listener = Descargas.getInstance();
    }
    
    public void sendData(String address,String data){
        try{
            Socket socket = new Socket(address, Config.SERVER_PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            out.println(data);
        }catch (Exception ex) {
            Logger.getLogger(RequestSocket.class.getName()).log(Level.SEVERE, null, ex);
        }     
    }
    
    public void requestFile(String address,String path,String data){

        new Thread(new Runnable() {
            @Override
            public void run() {
                       try{
                           
                            //Socket socket = new Socket(address, Config.SERVER_PORT);
                            Socket socket = new Socket();
                            socket.connect(new InetSocketAddress(address, Config.SERVER_PORT));

                            out = new PrintWriter(socket.getOutputStream(), true);
                            InputStream in = socket.getInputStream();
                            //6360840+1
                            out.println(JSONUtils.getDownloadJSON(path,0));
                            
                            InputStreamReader ir = new InputStreamReader(in);
                            BufferedReader br = new BufferedReader(ir);
                            String size = br.readLine();
                            
                            System.out.println("size: "+size);

                            File f = new File(path);
                            String fname = f.getName();
                                                
                            FileOutputStream fout = new FileOutputStream(GenericUtils.normalize(Config.SHARED_FOLDER)+f.getName(),true);
                            File destFile = new File (GenericUtils.normalize(Config.SHARED_FOLDER)+f.getName());

                            byte[] bytes = new byte[BUFFER_SIZE];

                            int count;
                            long remain = 0; //Long.parseLong(size);
                            
                            listener.downloadStart(fname);
                            
                            while ((count = in.read(bytes)) > 0) {
                                fout.write(bytes, 0, count);
                                remain+=count;
                                System.out.println(""+((remain*100)/Long.parseLong(size)));
                            }
                            
                            
                            
                            
                            listener.downloadEnd(fname);
                            System.out.println(Long.parseLong(size)+","+remain+","+destFile.length());                           
                            
                            fout.flush();
                            fout.close();
                            
                            ir.close();
                            br.close();
                            
                            out.close();
                            in.close();
                            socket.close();
                                                        
                        }catch (Exception ex) {
                            System.out.println(ex);
                            Logger.getLogger(RequestSocket.class.getName()).log(Level.SEVERE, null, ex);
                        }   
            }
        }).start();
    
       
    }
    
    public String requestService(String address,String data){
        
        try{
            Socket socket = new Socket(address, Config.SERVER_PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            out.println(data);

            String finalResult = "";
            String responseLine;
            
            while ((responseLine = in.readLine()) != null) {
                    MyLogger.log("Server: " + responseLine);
                    finalResult += responseLine;
            }
            
            return finalResult;

        }catch (Exception ex) {
            Logger.getLogger(RequestSocket.class.getName()).log(Level.SEVERE, null, ex);
        }    
        
        return "";
    }

}
