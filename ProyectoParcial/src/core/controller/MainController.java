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
import core.data.FileD;
import core.data.FilesSession;
import core.data.ServerFile;
import core.data.Session;
import core.data.User;
import core.data.Subject;
import core.data.Subscription;
import core.data.Subscriptor;
import core.db.dao.DAOArchivo;
import core.db.dao.DAOClassroom;
import core.db.dao.DAODescarga;
import core.db.dao.DAOFile;
import core.db.dao.DAOFilesSession;
import core.db.dao.DAOSession;
import core.db.dao.DAOSubject;
import core.db.dao.DAOSubscription;
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
        private static DAOFile daoFile = new DAOFile();
        private static DAOFilesSession daoFileSession = new DAOFilesSession();
        private static DAOSubscription daoSubscription = new DAOSubscription();
        
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
        public static void addUser(String name, String username, String pass,String email, String hostcomputer, String sharedfolder,String fingerprint){
                daoUser.insertUser(name, username, pass, email, hostcomputer, sharedfolder, fingerprint);
        } 
        
        public static void updateUser(String id, String name, String pass, String email, String hostcomputer, String sharedfolder){
                daoUser.updateUser(id, name, pass, email, hostcomputer, sharedfolder);
            
        }
        
        public static User existUser(String username, String password){         
            return daoUser.findByUserAndPass(username, password);
        }
        
        public static User getUserId(String id){         
            return daoUser.findById(id);
        }
        
        public static void addSubject(String name, String pass,String description, String sharedfolder, String user_id){
                daoSubject.insertSubject(name, pass, description, sharedfolder, user_id);
        }
        
        public static void updateSubject(String id,String name, String password,String description, String sharedfolder){
            daoSubject.updateSubject(id, name, password, description, sharedfolder);
        }
        
        public static ArrayList<Subscriptor> getSubscriptorToSubject(String user_id, String subject_id){
            return daoSubject.findSubscriptors(subject_id, user_id);
        }
        
        public static void deleteSubject (int id){
            daoSubject.deleteSubject(id);
        }
        
        public static Subject existSubjectNamePass(String name, String password){     
            return daoSubject.findByNameAndPass(name, password);
        }
        public static Subject existSubjectName(String name){     
            return daoSubject.findByName(name);
        }
        
        public static ArrayList<Subject> getSubjectsUser(String user_id) {
            return daoSubject.getByUserId(user_id);
        } 
        
        public static Subject getSubjectNameUserId (String name, String user_id){
            return daoSubject.getByNameAndUserId(name,user_id);
        }
        
        public static Subject existSubjectNameNotId (String name, String id){
            return daoSubject.findByNameNotId(name, id);
        }
        
        public static Subject getSubjectId (String id){
            return daoSubject.findById(id);
        }
        
        public static ArrayList<Subject> getSubjectsNotUser(String user_id){
            return daoSubject.getByNotUserId(user_id);
        }      
        
        public static int addSession(String date, String startTime,String durationHrs, String classRoom_id, String subject_id){
               return daoSession.insertSession(date, startTime, durationHrs, classRoom_id, subject_id);
        }
        
        public static void updateSession(String id,String date, String startTime,String durationHrs, String classRoom_id, String subject_id){
            daoSession.updateSession(id, date, startTime, durationHrs, classRoom_id, subject_id);
        }
        
        public static ArrayList<Session> getSessions(String user_id) {
            return daoSession.getByUserId(user_id);
        }
        public static Session getSession(String id) {
            return daoSession.findById(id);
        }
        
        public static ArrayList<Classroom> getClassrooms() {
            return daoClassroom.getClassrooms();
        }
        
        public static Classroom getClassroomName(String name) {
            return daoClassroom.getByName(name);
        }
        public static Classroom getClassroomId(String id) {
            return daoClassroom.getById(id);
        }
        
        public static int addFile(String name,String currentPage, String path){
            return daoFile.insertFile(name, currentPage, path);
        }
        
        public static void updateFile( String id, String name,String currentPage, String path){
            daoFile.updateFile(id, name, currentPage, path);
        }
        
        public static FileD getFile (String id){
            return daoFile.findById(id);
        }     
        
        public static void addFileSession(String session_id,String file_id, String deleted){
             daoFileSession.insertFilesSession(file_id, session_id, deleted);
        }
        
        public static FilesSession getFilesSession(String session_id){
            return daoFileSession.findBySession(session_id);
        }
        
        public static void deleteFilesSession (int id){
            daoFileSession.deleteFilesSession(id);
        }
        
        public static void addSubscription (String subject_id, String user_id, String deleted){
            daoSubscription.insertSubscription(subject_id, user_id, deleted);
        }
        
        public static Subscription getSubscriptionId(String id){
            return daoSubscription.findById(id);
        }
        
        public static Subscription getSubscriptionSubjectUser(String subject_id, String user_id){
            return daoSubscription.findBySubjectUser(subject_id,user_id);
        }
        public static ArrayList<Subscription> getSubscriptionUserId(String user_id){
            return daoSubscription.findByUserId(user_id);
        }
        
        public static void deleteSubscription (int id){
            daoSubscription.deleteSubscription(id);
        }
        
        
           
}
