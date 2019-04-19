/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.connections.sockets;

import core.crypt.CryptCipher;
import core.data.Config;
import core.db.dao.DAOTransferencias;
import core.main.ExploradorGlobal;
import core.main.listener.DownloadListener;
import core.main.listener.GenericListener;
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
import java.io.RandomAccessFile;
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
    private final int BUFFER_SIZE = 8192;
    private DAOTransferencias daoTransferencias;
    private DownloadListener listener;
    private GenericListener gListener;
    
    public SingleClient(Socket socket){
        this.socket = socket;
        this.daoTransferencias = new DAOTransferencias();
//        this.listener = Transferencias.getInstance();
//        this.gListener = ExploradorGlobal.getInstance();
        this.start();
    }
    
    @Override
    public void run(){
        try {
            
            
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String input = in.readLine();
            

            if (input != null) {
                MyLogger.log("Data "+input);
                input = CryptCipher.decrypt(input);

                String response = DispatcherService.dispatchService(input,socket.getInetAddress().getHostAddress());
                
                if(response != null){
                    
                    if(response.equals("download")){
                        
                        JSONParser parser = new JSONParser();
                        JSONObject obj = (JSONObject)parser.parse(input);           
                        String filePath = (String) obj.get("path");
                        Long seek = (Long) obj.get("start");
                        MyLogger.log("Se comenzara en: "+seek);
                        filePath = GenericUtils.normalize(Config.SHARED_FOLDER)+filePath;
                                               
                        OutputStream out = socket.getOutputStream();
                        PrintWriter pw = new PrintWriter(socket.getOutputStream());
                        
                        File f = new File(filePath);
                        String fname = f.getName();
                        RandomAccessFile raf = new RandomAccessFile(filePath, "r");
                        raf.seek(seek);
                        MyLogger.log("size: "+raf.length());
                        pw.println(raf.length());
                        pw.flush();
                        
                        
                        
                        int id = daoTransferencias.insertarTransferencia(fname,filePath,socket.getInetAddress().toString(),0,"Enviando",0, raf.length());
                        
//                        Notificacion.getInstance().startNotificacion("Transfiriendo el archivo "+fname,"Destino: "+socket.getInetAddress().toString());
                        listener.downloadStart(id,f.getName(),filePath, socket.getInetAddress().toString(),raf.length(),filePath,null);
                        gListener.startEvent(this, "", 1);
                        
                        byte buffer[] = new byte[BUFFER_SIZE];
                        int len = raf.read(buffer);
                        long total = 0;
                        long size = raf.length();
                        int percent = 0;
                        
                        while(len > 0){
                            out.write(buffer,0,len);
                            percent = (int) ((total*100)/size);
                            total += len;
                            len = raf.read(buffer);
                            listener.updatePercent(id,fname,percent,total);
                        }
                        
                        daoTransferencias.updateTransferencia(id, 100, "Completada", total);
                        listener.downloadEnd(id,fname);
                        gListener.endEvent(this, "", 1);
                        
                        System.out.println("total: "+total);
                        
                        
                        raf.close();
                        
                        pw.close();
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
