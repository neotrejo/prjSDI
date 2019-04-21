/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.controller;

import core.data.ActiveSession;
import java.sql.SQLException;
import java.util.List;
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
import core.db.dao.DAOActiveSession;
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
import core.utils.JSONUtils;
import core.utils.OpenFile;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 
 */
public class MainController {
    
        private static DAODescarga daoDescarga = new DAODescarga();
        private static DAOTransferencias daoTransferencias = new DAOTransferencias();
        private static DAOUser daoUser = new DAOUser();
        private static DAOSubject daoSubject = new DAOSubject();
        private static DAOSession daoSession = new DAOSession();
        private static DAOClassroom daoClassroom = new DAOClassroom();
        private static DAOFile daoFile = new DAOFile();
        private static DAOFilesSession daoFileSession = new DAOFilesSession();
        private static DAOSubscription daoSubscription = new DAOSubscription();
        private static DAOActiveSession daoActiveSession = new DAOActiveSession();
        private static OpenFile openFile =  openFile = new OpenFile();;
        
        public MainController(){
           
        }
        
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
        public static User addUser(String name, String username, String pass,
                String email, 
                String hostcomputer, 
                String sharedfolder,
                String fingerprint1, 
                String fingerprint2, 
                String fingerprintimage1,
                String fingerprintimage2){
                daoUser.insertUser(name, username, pass, email, hostcomputer, sharedfolder, fingerprint1, fingerprint2, fingerprintimage1, fingerprintimage2);
                
                User user= new User();
                user.setName(name);
                user.setUserName(username);
                user.setPassword(pass);
                user.setEmail(email);
                user.setHostComputer(hostcomputer);
                user.setSharedFolder(sharedfolder);
                user.setFingerPrint1(fingerprint1);
                user.setFingerPrint1(fingerprint2);
                user.setFingerPrintImage1(fingerprintimage1);
                user.setFingerPrintImage2(fingerprintimage2);
                
                return user;
        } 
        
        public static void updateUser(String id, String name, String pass, String email, String hostcomputer, String sharedfolder){
                daoUser.updateUser(id, name, pass, email, hostcomputer, sharedfolder);
            
        }
        
        public static User existUser(String username, String password){         
            return daoUser.findByUserAndPass(username, password);
        }
        
        public static User existUserName(String username){         
            return daoUser.findByUserName(username);
        }
        
        public static User getUserId(String id){         
            return daoUser.findById(id);
        }
        
        public static List<User> getAllUsers() throws SQLException{         
            return daoUser.getAllUsers();
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
        
        public static Subject existSubjectNameNotId (String name, String id, String user_id){
            return daoSubject.findByNameNotId(name, user_id);
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
        
        public static ArrayList<Session> getSessionsDates(String user_id, String dateFirst, String dateLast){
            return daoSession.getBetweenDates(user_id, dateFirst, dateLast);
        }        
        
        public static ArrayList<ActiveSession> getActiveSessionsDates(String user_id, String dateFirst, String dateLast){
            return daoActiveSession.getBetweenDates(user_id, dateFirst, dateLast);
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
        
        public static Classroom existClassroomHostName(String hostName){
            return daoClassroom.getByHostName(hostName);
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
        
        //-----------------------------------------------------------------------------------------
        
        public static void getFileToOpen(String name, String path ){
            openFile.setNameFile(name);
            openFile.setPathFile(path);
            openFile.openFile();
        }
        
        
           
}
