/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.db.dao;

import core.data.Session;
import core.db.sqlite.SQLiteConnection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author Diana
 */
public class DAOSession {

    private SQLiteConnection connection;

    public DAOSession() {
        connection = SQLiteConnection.getInstance();
    }

    public int insertSession(String date, String startTime, String durationHrs, String classRoom_id, String subject_id) {

        Map<String, String> params = new LinkedHashMap<>();
        params.put("date", date);
        params.put("startTime", startTime);
        params.put("durationHrs", durationHrs);
        params.put("classRoom_id", classRoom_id);
        params.put("subject_id", subject_id);

        return connection.insert("Session", params);
    }

    public void updateSession(String id, String date, String startTime, String durationHrs, String classRoom_id, String subject_id) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("date", date);
        params.put("startTime", startTime);
        params.put("durationHrs", durationHrs);
        params.put("classRoom_id", classRoom_id);
        params.put("subject_id", subject_id);
        connection.update("Session", params, "id=\"" + id + "\"");
    }

    public Session findBySession(String user_id) {
        String query = "SELECT Session.date, Session.startTime, Session.durationHrs, Subject.subjectName subject, ClassRoom.name classroom, FileD.fileName file, Session.id "
                + "FROM Session "
                + "INNER JOIN Subject on Subject.id = Session.subject_id "
                + "INNER JOIN ClassRoom on ClassRoom.id = Session.classRoom_id "
                + "INNER JOIN FilesSession on FilesSession.session_id = Session.id "
                + "INNER JOIN FileD on FileD.id = FilesSession.file_id "
                + "WHERE (Subject.user_id=\"" + user_id + "\"  or Subject.user_id=" + user_id + ") and FilesSession.deleted = \"false\"";
        return executeQuery(query);
    }

    public ArrayList<Session> getByUserId(String user_id) {
        String query = "SELECT Session.date, Session.startTime, Session.durationHrs, Subject.subjectName subject, ClassRoom.name classroom, FileD.fileName file, Session.id "
                + "FROM Session "
                + "INNER JOIN Subject on Subject.id = Session.subject_id "
                + "INNER JOIN ClassRoom on ClassRoom.id = Session.classRoom_id "
                + "INNER JOIN FilesSession on FilesSession.session_id = Session.id "
                + "INNER JOIN FileD on FileD.id = FilesSession.file_id "
                + "WHERE (Subject.user_id=\"" + user_id + "\"  or Subject.user_id=" + user_id + ") and FilesSession.deleted = \"false\"";
        return executeQueryList(query);
    }
    
    public ArrayList<Session> getBetweenDates(String user_id, String dateFirst, String dateLast){
         String query = "SELECT Session.date, Session.startTime, Session.durationHrs, Subject.subjectName subject, ClassRoom.name classroom, FileD.fileName file, Session.id "
                + "FROM Session "
                + "INNER JOIN Subject on Subject.id = Session.subject_id "
                + "INNER JOIN ClassRoom on ClassRoom.id = Session.classRoom_id "
                + "INNER JOIN FilesSession on FilesSession.session_id = Session.id "
                + "INNER JOIN FileD on FileD.id = FilesSession.file_id "
                + "WHERE (Subject.user_id=\"" + user_id + "\"  or Subject.user_id=" + user_id + ") and FilesSession.deleted = \"false\" and  Session.date between \""+dateFirst+"\" and  \""+dateLast+"\"";
        return executeQueryList(query);
    } 

    public Session findById(String id) {
        try {
            String query = "SELECT * FROM Session WHERE id=" + id;
            ResultSet result = connection.select(query);
            Session session = null;
            if (result != null) {
                if (result.next()) {
                    session = new Session();
                    session.setId(result.getObject("id").toString());
                    session.setDate(result.getObject("date").toString());
                    session.setStartTime(result.getObject("startTime").toString());
                    session.setDurationHrs(result.getObject("durationHrs").toString());
                    session.setSubjectId(result.getObject("subject_id").toString());
                    session.setClassRoomId(result.getObject("classRoom_id").toString());
                }
            }
            return session;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private Session executeQuery(String query) {
        try {
            ResultSet result = connection.select(query);
            Session session = null;
            if (result != null) {
                if (result.next()) {
                    session = new Session();
                    session.setId(result.getObject("id").toString());
                    session.setDate(result.getObject("date").toString());
                    session.setStartTime(result.getObject("startTime").toString());
                    session.setDurationHrs(result.getObject("durationHrs").toString());
                    session.setSubjectId(result.getObject("subject").toString());
                    session.setClassRoomId(result.getObject("classroom").toString());
                    session.setFile(result.getObject("file").toString());
                }
            }
            return session;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private ArrayList<Session> executeQueryList(String query) {
        try {
            ArrayList<Session> sessions = new ArrayList<>();
            ResultSet result = connection.select(query);
            Session session = null;
            if (result != null) {
                while (result.next()) {
                    session = new Session();
                    session.setId(result.getObject("id").toString());
                    session.setDate(result.getObject("date").toString());
                    session.setStartTime(result.getObject("startTime").toString());
                    session.setDurationHrs(result.getObject("durationHrs").toString());
                    session.setSubjectId(result.getObject("subject").toString());
                    session.setClassRoomId(result.getObject("classroom").toString());
                    session.setFile(result.getObject("file").toString());
                    sessions.add(session);
                }
            }
            return sessions;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
