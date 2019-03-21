/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.connections.sockets;

import core.data.Config;
import core.db.dao.DAODescarga;
import core.main.Dashboard;
import core.main.Descargas;
import core.main.ExploradorGlobal;
import core.main.listener.DownloadListener;
import core.main.listener.GenericListener;
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
public class RequestSocket implements Runnable{
    
    private BufferedReader in;
    private PrintWriter out;
    private DownloadListener listener;
    private GenericListener gListener;
    private int BUFFER_SIZE = 8192;
    private DAODescarga daoDescarga;
    
    public RequestSocket(){
        listener = Descargas.getInstance();
        gListener = ExploradorGlobal.getInstance();
        daoDescarga = new DAODescarga();
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
    
    public void requestFile(String address,String path,String data,long startByte,boolean isNew, int fId){

        FileDownloader fDownloader = new FileDownloader(listener, gListener, daoDescarga, out, address, path, data, startByte,isNew,fId);
        fDownloader.start();       
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

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
