/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.controller;

import java.sql.SQLException;
import java.util.List;
import core.data.Classroom;
import core.data.FileD;
import core.data.FileSession;
import core.data.Session;
import core.data.User;
import core.data.Course;
import core.data.Subscription;
import core.data.Subscriptor;
import core.db.dao.DAOClassroom;
import core.db.dao.DAOFile;
import core.db.dao.DAOFilesSession;
import core.db.dao.DAOSession;
import core.db.dao.DAOCourse;
import core.db.dao.DAOSubscription;
import core.db.dao.DAOUser;
import java.util.ArrayList;

/**
 *
 * @author
 */
public class MainController {

    //private static MulticastServer multicastServer = new MulticastServer();  
    private static DAOUser daoUser = new DAOUser();
    private static DAOCourse daoCourse = new DAOCourse();
    private static DAOSession daoSession = new DAOSession();
    private static DAOClassroom daoClassroom = new DAOClassroom();
    private static DAOFile daoFile = new DAOFile();
    private static DAOFilesSession daoFileSession = new DAOFilesSession();
    private static DAOSubscription daoSubscription = new DAOSubscription();

    public MainController() {

    }

    //---------------------------------------------------
    public static User addUser(String name, String username, String pass,
            String email,
            String hostcomputer,
            String sharedfolder, String port) {
        daoUser.insertUser(name, username, pass, email, hostcomputer, sharedfolder, port);

        User user = new User();
        user.setName(name);
        user.setUsername(username);
        user.setPassword(pass);
        user.setEmail(email);
        user.setHostcomputer(hostcomputer);
        user.setSharedfolder(sharedfolder);
        user.setPort(port);
        return user;
    }

    public static void updateUser(String id, String name, String pass, String email, String hostcomputer, String sharedfolder, String port) {
        daoUser.updateUser(id, name, pass, email, hostcomputer, sharedfolder, port);

    }

    public static User existUser(String username, String password) {
        return daoUser.findByUserAndPass(username, password);
    }

    public static User existUserName(String username) {
        return daoUser.findByUserName(username);
    }

    public static User getUserId(String id) {
        return daoUser.findById(id);
    }

    public static List<User> getAllUsers() throws SQLException {
        return daoUser.getAllUsers();
    }

    public static void addSubject(String name, String pass, String description, String sharedfolder, String user_id) {
        daoCourse.insertSubject(name, pass, description, sharedfolder, user_id);
    }

    public static void updateSubject(String id, String name, String password, String description, String sharedfolder) {
        daoCourse.updateSubject(id, name, password, description, sharedfolder);
    }

    public static ArrayList<Subscriptor> getSubscriptorToSubject(String user_id, String subject_id) {
        return daoCourse.findSubscriptors(subject_id, user_id);
    }

    public static void deleteSubject(int id) {
        daoCourse.deleteSubject(id);
    }

    public static Course existSubjectNamePass(String name, String password) {
        return daoCourse.findByNameAndPass(name, password);
    }

    public static Course existSubjectName(String name) {
        return daoCourse.findByName(name);
    }

    public static ArrayList<Course> getSubjectsUser(String user_id) {
        return daoCourse.getByUserId(user_id);
    }

    public static Course getSubjectNameUserId(String name, String user_id) {
        return daoCourse.getByNameAndUserId(name, user_id);
    }

    public static Course existSubjectNameNotId(String name, String id, String user_id) {
        return daoCourse.findByNameNotId(name, id, user_id);
    }

    public static Course getSubjectId(String id) {
        return daoCourse.findById(id);
    }

    public static ArrayList<Course> getSubjectsNotUser(String user_id) {
        return daoCourse.getByNotUserId(user_id);
    }

    public static int addSession(String date, String startTime, String durationHrs, String classRoom_id, String subject_id) {
        return daoSession.insertSession(date, startTime, durationHrs, classRoom_id, subject_id);
    }

    public static void updateSession(String id, String date, String startTime, String durationHrs, String classRoom_id, String subject_id) {
        daoSession.updateSession(id, date, startTime, durationHrs, classRoom_id, subject_id);
    }

    public static ArrayList<Session> getSessions(String user_id) {
        return daoSession.getByUserId(user_id);
    }
    public static ArrayList<Session> getSessionsActives(String user_id, String dateToday) {
            return daoSession.getByUserIdSubscription(user_id,dateToday);
        }    

    public static Session getSession(String id) {
        return daoSession.findById(id);
    }

    public static ArrayList<Session> getSessionsDates(String user_id, String dateFirst, String dateLast) {
        return daoSession.getBetweenDates(user_id, dateFirst, dateLast);
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

    public static Classroom existClassroomHostName(String hostName) {
        return daoClassroom.getByHostName(hostName);
    }

    public static int addFile(String name, String currentPage, String path) {
        return daoFile.insertFile(name, currentPage, path);
    }

    public static void updateFile(String id, String name, String currentPage, String path) {
        daoFile.updateFile(id, name, currentPage, path);
    }

    public static FileD getFile(String id) {
        return daoFile.findById(id);
    }

    public static void addFileSession(String session_id, String file_id, String deleted) {
        daoFileSession.insertFilesSession(file_id, session_id, deleted);
    }

    public static FileSession getFilesSession(String session_id) {
        return daoFileSession.findBySession(session_id);
    }

    public static void deleteFilesSession(int id) {
        daoFileSession.deleteFilesSession(id);
    }

    public static void addSubscription(String subject_id, String user_id, String deleted) {
        daoSubscription.insertSubscription(subject_id, user_id, deleted);
    }

    public static Subscription getSubscriptionId(String id) {
        return daoSubscription.findById(id);
    }

    public static Subscription getSubscriptionSubjectUser(String subject_id, String user_id) {
        return daoSubscription.findBySubjectUser(subject_id, user_id);
    }

    public static ArrayList<Subscription> getSubscriptionUserId(String user_id) {
        return daoSubscription.findByUserId(user_id);
    }

    public static void deleteSubscription(int id) {
        daoSubscription.deleteSubscription(id);
    }

    //-----------------------------------------------------------------------------------------
}
