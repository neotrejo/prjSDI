/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template subscription, choose Tools | Templates
 * and open the template in the editor.
 */
package core.db.dao;

import core.data.Subscription;
import core.db.rqlite.RQLiteConnection;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import org.json.JSONArray;

/**
 *
 * @author Diana
 */
public class DAOSubscription {

    private RQLiteConnection connection;

    public DAOSubscription() {
        connection = RQLiteConnection.getInstance();
    }

    public int insertSubscription(String subject_id, String user_id, String deleted) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("courseId", subject_id);
        params.put("userId", user_id);
        params.put("deleted", deleted);
        return connection.insert("Subscription", params);
    }

    public void deleteSubscription(int id) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("deleted", String.valueOf(true));
        connection.update("Subscription", params, "id=\"" + id + "\"");
    }

    public Subscription findById(String id) {
        String query = "SELECT * FROM Subscription WHERE id=" + id;
        return executeQuery(query);
    }

    public Subscription findBySubjectUser(String subject_id, String user_id) {
        String query = "SELECT * FROM Subscription WHERE courseId=\"" + subject_id + "\" and userId=\"" + user_id + "\" and deleted=\"false\"";
        return executeQuery(query);
    }

    private Subscription executeQuery(String query) {
        try {
            JSONArray resul = connection.select(query);
            Subscription subscription = null;
            if (resul != null) {
                JSONArray cols = resul.getJSONObject(0).getJSONArray("columns");
                JSONArray values = resul.getJSONObject(0).getJSONArray("values");
                for (int i = 0; i < values.length(); i++) {
                    JSONArray reg = values.getJSONArray(i);
                    subscription = new Subscription();
                    for (int j = 0; j < cols.length(); j++) {
                        switch (cols.getString(j)) {
                            case "id":
                                subscription.setId(reg.get(j).toString());
                                break;
                            case "courseId":
                                subscription.setCourseId(reg.get(j).toString());
                                break;
                            case "userId":
                                subscription.setUserId(reg.get(j).toString());
                                break;
                        }
                    }
                    return subscription;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public ArrayList<Subscription> findByUserId(String user_id) {
        try {
            ArrayList<Subscription> subscriptions = new ArrayList<>();
            String query = "SELECT Subscription.*, Course.name course, User.name professor, User.location hostProfesor, User.port portHost\n"
                    + "FROM Subscription\n"
                    + "INNER JOIN Course on Course.id = Subscription.courseId\n"
                    + "INNER JOIN User on User.id= Course.userId WHERE Subscription.userId = \"" + user_id + "\" and Subscription.deleted=\"false\"";
            JSONArray resul = connection.select(query);
            Subscription subscription = null;
            if (resul != null) {
                JSONArray cols = resul.getJSONObject(0).getJSONArray("columns");
                JSONArray values = resul.getJSONObject(0).getJSONArray("values");
                for (int i = 0; i < values.length(); i++) {
                    JSONArray reg = values.getJSONArray(i);
                    subscription = new Subscription();
                    for (int j = 0; j < cols.length(); j++) {
                        switch (cols.getString(j)) {
                            case "id":
                                subscription.setId(reg.get(j).toString());
                                break;
                            case "courseId":
                                subscription.setCourseId(reg.get(j).toString());
                                break;
                            case "userId":
                                subscription.setUserId(reg.get(j).toString());
                                break;
                            case "course":
                                subscription.setCourseName(reg.get(j).toString());
                                break;
                            case "professor":
                                subscription.setProfessorName(reg.get(j).toString());
                                break;
                            case "hostProfesor":
                                subscription.setHostProfesor(reg.get(j).toString());
                                break;
                            case "portHost":
                                subscription.setPortHostProf(reg.get(j).toString());
                                break;
                        }
                    }
                    subscriptions.add(subscription);
                }
            }
            return subscriptions;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
