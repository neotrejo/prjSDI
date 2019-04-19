/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.db.dao;

import core.data.ActiveSession;
import core.db.sqlite.SQLiteConnection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author Diana
 */
public class DAOActiveSession {

    private SQLiteConnection connection;

    public DAOActiveSession() {
        try {
            connection = SQLiteConnection.getInstance();

            connection.conectar();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<ActiveSession> getBetweenDates(String user_id, String dateFirst, String dateLast) {
        String query = "SELECT * "
                + "FROM ActiveSession "
                + "WHERE (ActiveSession.user_id=\"" + user_id + "\"  or ActiveSession.user_id=" + user_id + ") and ActiveSession.date between \"" + dateFirst + "\" and  \"" + dateLast + "\"";

        return executeQueryList(query);
    }
    
    public ActiveSession findBySession(String session_id, String user_id) {
        String query = "SELECT * "
                + "FROM ActiveSession "
                + "WHERE (ActiveSession.user_id=\"" + user_id + "\"  or ActiveSession.user_id=" + user_id + ") and "+ "ActiveSession.session_id=\"" + session_id + "\"";
        return executeQuery(query);
    }
    
    private ActiveSession executeQuery(String query) {
        try {
            ResultSet result = connection.select(query);
            ActiveSession session = null;
            if (result != null) {
                if (result.next()) {
                    session = new ActiveSession();
                    session.setId(result.getObject("id").toString());
                    session.setUserId(result.getObject("user_id").toString());
                    session.setSessionId(result.getObject("session_id").toString());
                    session.setDate(result.getObject("date").toString());
                    session.setStartTime(result.getObject("startTime").toString());
                    session.setSubjectName(result.getObject("subjectName").toString());
                    session.setFileName(result.getObject("fileName").toString());
                    session.setPathFile(result.getObject("pathFile").toString());
                }
            }
            return session;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private ArrayList<ActiveSession> executeQueryList(String query) {
        try {
            ArrayList<ActiveSession> sessions = new ArrayList<>();
            ResultSet result = connection.select(query);
            ActiveSession session = null;
            if (result != null) {
                while (result.next()) {
                    session = new ActiveSession();
                    session.setUserId(result.getObject("user_id").toString());
                    session.setSessionId(result.getObject("session_id").toString());
                    session.setDate(result.getObject("date").toString());
                    session.setStartTime(result.getObject("startTime").toString());
                    session.setSubjectName(result.getObject("subjectName").toString());
                    session.setFileName(result.getObject("fileName").toString());
                    session.setPathFile(result.getObject("pathFile").toString());
                    sessions.add(session);
                }
            }
            return sessions;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public int insertSession(String session_id, String user_id, String date, String startTime, String subjectName, String fileName, String pathFile) {

        Map<String, String> params = new LinkedHashMap<>();
        params.put("session_id", session_id);
        params.put("user_id", user_id);
        params.put("date", date);
        params.put("startTime", startTime);
        params.put("subjectName", subjectName);
        params.put("fileName", fileName);
        params.put("pathFile", pathFile);

        return connection.insert("ActiveSession", params);
    }
    
    public void updateActiveSession(String id, String session_id, String user_id, String date, String startTime, String subjectName, String fileName, String pathFile) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("session_id", session_id);
        params.put("user_id", user_id);
        params.put("date", date);
        params.put("startTime", startTime);
        params.put("subjectName", subjectName);
        params.put("fileName", fileName);
        params.put("pathFile", pathFile);
        connection.update("ActiveSession", params, "id=\"" + id + "\"");
    }
    
     public void deleteActiveSession(String id){
        connection.execute("DELETE FROM ActiveSession WHERE id=\""+id+"\"");
    }
    
    

}
