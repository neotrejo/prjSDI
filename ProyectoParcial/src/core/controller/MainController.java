/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.controller;

import core.data.Config;
import core.connections.multicast.MulticastServer;
import core.connections.rmi.remote.RMIServer;
import core.connections.rmi.remote.RemoteMachine;
import core.connections.sockets.RequestSocket;
import core.data.Classroom;
import core.data.Descarga;
import core.data.ServerFile;
import core.data.User;
import core.data.Subject;
import core.db.dao.DAOArchivo;
import core.db.dao.DAOClassroom;
import core.db.dao.DAODescarga;
import core.db.dao.DAOSession;
import core.db.dao.DAOSubject;
import core.db.dao.DAOTransferencias;
import core.db.dao.DAOUser;
import core.main.Descargas;
import core.utils.JSONUtils;
import core.utils.MyLogger;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 
 */
public class MainController {
    
        private static MulticastServer multicastServer = new MulticastServer();       
        private static RequestSocket requestSocket; //= new RequestSocket();
        private static DAODescarga daoDescarga = new DAODescarga();
        private static DAOTransferencias daoTransferencias = new DAOTransferencias();
        private static DAOUser daoUser = new DAOUser();
        private static DAOSubject daoSubject = new DAOSubject();
        private static DAOSession daoSession = new DAOSession();
        private static DAOClassroom daoClassroom = new DAOClassroom();
        
        public MainController(){
            
        }
    
        public static void startListenForMulticast(){          
            multicastServer.start();
        }
        
        public static void startDownload(String file,String addr){
           new RequestSocket().requestFile(addr, file, JSONUtils.getDownloadJSON(file,0),0,true,0);
        }
        
        public static void startDownload(String file,String addr,long start, int fId){
           new RequestSocket().requestFile(addr, file, JSONUtils.getDownloadJSON(file,start),start,false,fId);
        }
        
        public static void updateSharedFolder(String sharedFolder) {
            new DAOArchivo().updateSharedFolder(sharedFolder);
        }
          
        public static ArrayList<ServerFile> getFolderContent(String addr,String path){
            
            try {
                /*
                String requestType = JSONUtils.getSubdirJSON(path);
                
                String jsonResponse = requestSocket.requestService(addr, requestType);
                
                */
                
                RemoteMachine rm = RMIServer.find(addr);
                
                ArrayList<ServerFile> files = rm.list(path);
                
                return files; //JSONUtils.getFilesFromJSON(jsonResponse);
            } catch (RemoteException ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
            return null;
        }
       
        public static ArrayList<ServerFile> getRootContent(String addr){
            
            try {
                RemoteMachine rm = RMIServer.find(addr);
                
                ArrayList<ServerFile> files = rm.list();
                /*String requestType = JSONUtils.getRootJSON();
                
                String jsonResponse = requestSocket.requestService(addr, requestType);
                */
                
                return files; //JSONUtils.getFilesFromJSON(jsonResponse);
            } catch (RemoteException ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
            return null;
        }
        
        
        ///-------------------------------------
        
        public static ArrayList<Descarga> getDescargas() {
            return daoDescarga.getDescargas();
        } 
        
        public static void borrarDescarga(String name,String path){
            daoDescarga.borrarDescarga(name, path);
        }
        
        public static void borrarDescarga(int id){
            daoDescarga.borrarDescarga(id);
        }
        
        public static void borrarDescargas(){
            daoDescarga.borrarDescargas();
        }
        
        public static ArrayList<Descarga> getTransferencias() {
            return daoTransferencias.getTransferencias();
        } 
        
        //---------------------------------------------------
        public static Boolean addUser(String name, String username, String pass,String email, String hostcomputer, String sharedfolder,String fingerprint){
            if(daoUser.findByUserAndPass(username, pass)==null){
                daoUser.insertUser(name, username, pass, email, hostcomputer, sharedfolder, fingerprint);
                return true;
            }else{
                return false;
            }
        }
        
        public static User existUser(String username, String password){
            User user = null;
            user = daoUser.findByUserAndPass(username, password);
            return user;
        }
        
        public static Boolean addSubject(String name, String pass,String description, String sharedfolder, String user_id){
            if(daoSubject.findByNameAndPass(name, pass)==null){
                daoSubject.insertSubject(name, pass, description, sharedfolder, user_id);
                return true;
            }else{
                return false;
            }
        }
        
        public static Subject existSubject(String name, String password){
            Subject subject = null;
            subject = daoSubject.findByNameAndPass(name, password);
            return subject;
        }
        
        public static ArrayList<Subject> getSubjects(String user_id) {
            return daoSubject.getByUserId(user_id);
        } 
        
        public static Boolean addSession(String date, String startTime,String durationHrs, String classRoom, String subject){
//            if(daoSubject.findByNameAndPass(name, pass)==null){
//                daoSubject.insertSubject(name, pass, description, sharedfolder);
//                return true;
//            }else{
//                return false;
//            }
            return false;
        }
        
        public static ArrayList<Classroom> getClassrooms() {
            return daoClassroom.getClassrooms();
        } 
           
}
