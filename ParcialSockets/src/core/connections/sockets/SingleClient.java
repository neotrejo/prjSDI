/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.connections.sockets;

import core.data.Config;
import core.utils.GenericUtils;
import core.utils.MyLogger;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author Luis
 */
public class SingleClient extends Thread{
    
    private Socket socket;
    
    public SingleClient(Socket socket){
        this.socket = socket;
        this.start();
    }
    
    @Override
    public void run(){
        try {
            
            
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String input = in.readLine();

            if (input != null) {
                
                MyLogger.log(input);
                
                String response = DispatcherService.dispatchService(input,socket.getInetAddress().getHostAddress());
                
                if(response != null){
                    
                    if(response.equals("download")){
                        
                        JSONParser parser = new JSONParser();
                        JSONObject obj = (JSONObject)parser.parse(input);           
                        String filePath = (String) obj.get("path");
                        filePath = GenericUtils.normalize(Config.SHARED_FOLDER)+filePath;
                        File file = new File(filePath);
            
                        long length = file.length();
                        byte[] bytes = new byte[8192];
                        InputStream ins = new FileInputStream(file);
                        OutputStream out = socket.getOutputStream();

                        int count;
                        while ((count = ins.read(bytes)) > 0) {
                            out.write(bytes, 0, count);
                        }
                       
                        out.close();

                    }else{                  
                        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                        out.println(response);
                    }
                }
                
                in.close();
                socket.close();
            }

            
                      
        }catch(Exception ex){
                Logger.getLogger(SingleClient.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            try {
                socket.close();
            } catch (IOException ex) {
                Logger.getLogger(SingleClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
