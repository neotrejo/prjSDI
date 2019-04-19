/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.services;

import core.connections.sockets.RequestSocket;
import core.data.Archivo;
import core.db.dao.DAOArchivo;
import core.utils.JSONUtils;
import core.utils.MyLogger;
import java.util.ArrayList;
import javax.swing.JCheckBox;

/**
 *
 * @author luismartin
 */
public class SearchService {
    
    private DAOArchivo daoArchivo;
    private RequestSocket requestSocket;
    
    public SearchService(){
        daoArchivo = new DAOArchivo();
    }
    
    public String search(String fileName, Boolean pdf, Boolean zip, Boolean jpeg){
       ArrayList<Archivo> archivos = daoArchivo.findSimilarFiles(fileName,pdf,zip,jpeg);
       String jsonString = JSONUtils.getFilesAsJSON(archivos);
       return jsonString;
    }
    
    /*public String search(String fileName, Boolean flag){
        System.out.println("\n\n\n\n");
        System.out.println("Check box activos: PDF-no hay");
        if(flag==false){
           ArrayList<Archivo> archivos = daoArchivo.findSimilarFiles(fileName);
           String jsonString = JSONUtils.getFilesAsJSON(archivos);
           return jsonString;
       }
       else{
           ArrayList<Archivo> archivos = daoArchivo.findEqualFiles(fileName);
           String jsonString = JSONUtils.getFilesAsJSON(archivos);
           return jsonString;
       }
       //String jsonString = JSONUtils.getFilesAsJSON(archivos);
       //return jsonString;
    }*/
    
    public String search(String fileName, boolean flag, boolean pdf, boolean gif, boolean jpeg){
        //System.out.println("\n\n\n\nCheck box activos: PDF-"+pdf+", gif-"+gif+" jpeg-"+jpeg);
        if(flag==false){
           ArrayList<Archivo> archivos = daoArchivo.findSimilarFiles(fileName,pdf,gif,jpeg);
           String jsonString = JSONUtils.getFilesAsJSON(archivos);
           return jsonString;
       }
       else{
           ArrayList<Archivo> archivos = daoArchivo.findEqualFiles(fileName,pdf,gif,jpeg);
           String jsonString = JSONUtils.getFilesAsJSON(archivos);
           return jsonString;
       }
       //String jsonString = JSONUtils.getFilesAsJSON(archivos);
       //return jsonString;
    }
    
    public void sendSearchResults(String address,String data){
        requestSocket = new RequestSocket();
        requestSocket.sendData(address, data);
    }
    
}
