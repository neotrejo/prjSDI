/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.db.dao;

import core.data.Session;
import core.db.rqlite.RQLiteConnection;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import org.json.JSONArray;

/**
 *
 * @author Diana
 */
public class DAOSession {

    private RQLiteConnection connection;

    public DAOSession() {
        connection = RQLiteConnection.getInstance();
    }

    public int insertSession(String date, String startTime, String durationHrs, String classRoom_id, String subject_id) {

        Map<String, String> params = new LinkedHashMap<>();
        params.put("date", date);
        params.put("startTime", startTime);
        params.put("duration", durationHrs);
        params.put("classroomId", classRoom_id);
        params.put("courseId", subject_id);

        return connection.insert("Session", params);
    }

    public void updateSession(String id, String date, String startTime, String durationHrs, String classRoom_id, String subject_id) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("date", date);
        params.put("startTime", startTime);
        params.put("duration", durationHrs);
        params.put("classroomId", classRoom_id);
        params.put("courseId", subject_id);
        connection.update("Session", params, "id=\"" + id + "\"");
    }

    public Session findBySession(String user_id) {
        String query = "SELECT Session.date, Session.startTime, Session.duration, Course.name subject, Classroom.name classroom, FileD.fileName file, Session.id "
                + "FROM Session "
                + "INNER JOIN Course on Course.id = Session.courseId "
                + "INNER JOIN Classroom on Classroom.id = Session.classroomId "
                + "INNER JOIN FileSession on FileSession.sessionId = Session.id "
                + "INNER JOIN FileD on FileD.id = FileSession.fileId "
                + "WHERE (Course.userId=\"" + user_id + "\"  or Course.userId=" + user_id + ") and FileSession.deleted = \"false\"";
        return executeQuery(query);
    }

    public ArrayList<Session> getByUserId(String user_id) {
        String query = "SELECT Session.date, Session.startTime, Session.duration, Course.name subject, Classroom.name classroom, FileD.fileName file, Session.id "
                + "FROM Session "
                + "INNER JOIN Course on Course.id = Session.courseId "
                + "INNER JOIN Classroom on Classroom.id = Session.classroomId "
                + "INNER JOIN FileSession on FileSession.sessionId = Session.id "
                + "INNER JOIN FileD on FileD.id = FileSession.fileId "
                + "WHERE (Course.userId=\"" + user_id + "\"  or Course.userId=" + user_id + ") and FileSession.deleted = \"false\"";
        return executeQueryList(query);
    }

    public ArrayList<Session> getByUserIdSubscription(String user_id, String dateToday) {
        String query = "SELECT Session.date, Session.startTime, Session.duration, Course.name subject, Classroom.name classroom, FileD.fileName file, Session.id "
                + "FROM Session "
                + "INNER JOIN Course on Course.id = Session.courseId "
                + "INNER JOIN Subscription on Subscription.courseId = Course.id "
                + "INNER JOIN Classroom on Classroom.id = Session.classroomId "
                + "INNER JOIN FileSession on FileSession.sessionId = Session.id "
                + "INNER JOIN FileD on FileD.id = FileSession.fileId "
                + "WHERE (Subscription.userId=\"" + user_id + "\"  or Subscription.userId=" + user_id + ") and Session.date>=\"" + dateToday + "\" and FileSession.deleted = \"false\"";
        return executeQueryList(query);
    }

    public ArrayList<Session> getBetweenDates(String user_id, String dateFirst, String dateLast) {
        String query = "SELECT Session.date, Session.startTime, Session.duration, Course.name subject, Classroom.name classroom, FileD.fileName file, Session.id "
                + "FROM Session "
                + "INNER JOIN Course on Course.id = Session.courseId "
                + "INNER JOIN Classroom on Classroom.id = Session.classroomId "
                + "INNER JOIN FileSession on FileSession.sessionId = Session.id "
                + "INNER JOIN FileD on FileD.id = FileSession.fileId "
                + "WHERE (Course.userId=\"" + user_id + "\"  or Course.userId=" + user_id + ") and FileSession.deleted = \"false\" and  Session.date between \"" + dateFirst + "\" and  \"" + dateLast + "\"";
        return executeQueryList(query);
    }

    public Session findById(String id) {
        try {
            String query = "SELECT * FROM Session WHERE id=" + id;
            JSONArray resul = connection.select(query);
            Session session = null;
            if (resul != null) {
                JSONArray cols = resul.getJSONObject(0).getJSONArray("columns");
                JSONArray values = resul.getJSONObject(0).getJSONArray("values");
                for (int i = 0; i < values.length(); i++) {
                    JSONArray reg = values.getJSONArray(i);
                    session = new Session();
                    for (int j = 0; j < cols.length(); j++) {
                        switch (cols.getString(j)) {
                            case "id":
                                session.setId(reg.get(j).toString());
                                break;
                            case "date":
                                session.setDate(reg.get(j).toString());
                                break;
                            case "startTime":
                                session.setStartTime(reg.get(j).toString());
                                break;
                            case "duration":
                                session.setDuration(reg.get(j).toString());
                                break;
                            case "courseId":
                                session.setCourseId(reg.get(j).toString());
                                break;
                            case "classroomId":
                                session.setClassroomId(reg.get(j).toString());
                                break;
                        }
                    }
                    return session;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private Session executeQuery(String query) {
        try {
            JSONArray resul = connection.select(query);
            Session session = null;
            if (resul != null) {
                JSONArray cols = resul.getJSONObject(0).getJSONArray("columns");
                JSONArray values = resul.getJSONObject(0).getJSONArray("values");
                for (int i = 0; i < values.length(); i++) {
                    JSONArray reg = values.getJSONArray(i);
                    session = new Session();
                    for (int j = 0; j < cols.length(); j++) {
                        switch (cols.getString(j)) {
                            case "id":
                                session.setId(reg.get(j).toString());
                                break;
                            case "date":
                                session.setDate(reg.get(j).toString());
                                break;
                            case "startTime":
                                session.setStartTime(reg.get(j).toString());
                                break;
                            case "duration":
                                session.setDuration(reg.get(j).toString());
                                break;
                            case "subject":
                                session.setCourseId(reg.get(j).toString());
                                break;
                            case "classroom":
                                session.setClassroomId(reg.get(j).toString());
                                break;
                            case "file":
                                session.setFile(reg.get(j).toString());
                                break;
                        }
                    }
                    return session;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private ArrayList<Session> executeQueryList(String query) {
        try {
            ArrayList<Session> sessions = new ArrayList<>();
            JSONArray resul = connection.select(query);
            Session session = null;
            if (resul != null) {
                JSONArray cols = resul.getJSONObject(0).getJSONArray("columns");
                JSONArray values = resul.getJSONObject(0).getJSONArray("values");
                for (int i = 0; i < values.length(); i++) {
                    JSONArray reg = values.getJSONArray(i);
                    session = new Session();
                    for (int j = 0; j < cols.length(); j++) {
                        switch (cols.getString(j)) {
                            case "id":
                                session.setId(reg.get(j).toString());
                                break;
                            case "date":
                                session.setDate(reg.get(j).toString());
                                break;
                            case "startTime":
                                session.setStartTime(reg.get(j).toString());
                                break;
                            case "duration":
                                session.setDuration(reg.get(j).toString());
                                break;
                            case "subject":
                                session.setCourseId(reg.get(j).toString());
                                break;
                            case "classroom":
                                session.setClassroomId(reg.get(j).toString());
                                break;
                            case "file":
                                session.setFile(reg.get(j).toString());
                                break;
                        }
                    }
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
