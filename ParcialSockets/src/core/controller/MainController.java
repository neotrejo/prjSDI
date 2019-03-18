/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.controller;

import core.data.Config;
import core.connections.multicast.MulticastServer;
import core.connections.sockets.RequestSocket;
import core.data.ServerFile;
import core.main.Descargas;
import core.utils.JSONUtils;
import core.utils.MyLogger;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Luis
 */
public class MainController {
    
        private static MulticastServer multicastServer = new MulticastServer();
        
        private static RequestSocket requestSocket = new RequestSocket();
        
        public MainController(){
            requestSocket = new RequestSocket();
        }
    
        public static void startListenForMulticast(){          
            multicastServer.start();
        }
        
        public static void startDownload(String file,String addr){
           requestSocket.requestFile(addr, file, JSONUtils.getDownloadJSON(file,0));
        }
          
        public static ServerFile[] getFolderContent(String addr,String path){
            
            String requestType = JSONUtils.getSubdirJSON(path);
            
            String jsonResponse = requestSocket.requestService(addr, requestType);
            
            
            return JSONUtils.getFilesFromJSON(jsonResponse);
        }
       
        public static ServerFile[] getRootContent(String addr){
            
            String requestType = JSONUtils.getRootJSON();
            
            String jsonResponse = requestSocket.requestService(addr, requestType);
            
            
            return JSONUtils.getFilesFromJSON(jsonResponse);
        }
           
}
