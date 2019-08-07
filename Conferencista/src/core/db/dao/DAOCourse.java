/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.db.dao;

import core.data.Course;
import core.data.Subscriptor;
import core.db.rqlite.RQLiteConnection;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import org.json.JSONArray;

/**
 *
 * @author Diana
 */
public class DAOCourse {

    private RQLiteConnection connection;

    public DAOCourse() {
        connection = RQLiteConnection.getInstance();
    }

    public void insertSubject(String name, String password, String description, String sharedfolder, String user_id) {

        Map<String, String> params = new LinkedHashMap<>();
        params.put("name", name);
        params.put("passkey", password);
        params.put("sharedFolder", sharedfolder);
        params.put("userId", user_id);
        params.put("deleted", String.valueOf(false));

        connection.insert("Course", params);
    }

    public void updateSubject(String id, String name, String password, String description, String sharedfolder) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("name", name);
        params.put("passkey", password);
        params.put("sharedFolder", sharedfolder);
        connection.update("Course", params, "id=\"" + id + "\"");
    }

    public void deleteSubject(int id) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("deleted", String.valueOf(true));
        connection.update("Course", params, "id=\"" + id + "\"");
    }

    public Course findByNameAndPass(String name, String pass) {
        String query = "SELECT * FROM Course WHERE name=\"" + name + "\" and passkey=\"" + pass + "\" and deleted=\"false\" LIMIT 1";
        return executeQuery(query);
    }

    public Course findByName(String name) {
        String query = "SELECT * FROM Course WHERE name=\"" + name + "\" and deleted=\"false\" LIMIT 1";
        return executeQuery(query);
    }

    public Course findById(String id) {
        String query = "SELECT * FROM Course WHERE id=" + id + " LIMIT 1";
        return executeQuery(query);
    }

    public Course findByNameNotId(String name, String id, String user_id) {
        String query = "SELECT * FROM Course WHERE name=\"" + name + "\" and id<>\"" + id + "\" and  userId=\"" + user_id + "\" and deleted=\"false\"  LIMIT 1";
        return executeQuery(query);
    }

    public Course getByNameAndUserId(String name, String user_id) {
        String query = "SELECT * FROM Course WHERE name=\"" + name + "\" and (userId= \"" + user_id + "\" or userId=" + user_id + ") and deleted=\"false\"";
        return executeQuery(query);
    }

    public ArrayList<Course> getByUserId(String user_id) {
        String query = "SELECT * FROM Course WHERE (userId=" + user_id + " or userId=\"" + user_id + "\") and deleted=\"false\"";
        return executeQueryList(query);
    }

    public ArrayList<Course> getByNotUserId(String user_id) {
        String query = "SELECT * FROM Course WHERE (userId <> " + user_id + " and userId <> \"" + user_id + "\") and deleted=\"false\"";
        return executeQueryList(query);
    }

    public ArrayList<Subscriptor> findSubscriptors(String subject_id, String user_id) {
        Subscriptor subscriptor = null;
        ArrayList<Subscriptor> subscriptors = new ArrayList<>();
        try {
            String query = "SELECT Subscription.id subscriptionId, User.name, User.email, Course.name course FROM Course\n"
                    + "INNER JOIN Subscription on Subscription.courseId = Course.id\n"
                    + "INNER JOIN User on User.id = Subscription.userId\n"
                    + "WHERE Course.userId = \"" + user_id + "\"  and  Course.id=" + subject_id;

            JSONArray resul = connection.select(query);

            if (!resul.getJSONObject(0).has("error")) {
                if (resul.getJSONObject(0).has("values")) {
                    JSONArray cols = resul.getJSONObject(0).getJSONArray("columns");
                    JSONArray values = resul.getJSONObject(0).getJSONArray("values");
                    for (int i = 0; i < values.length(); i++) {
                        JSONArray reg = values.getJSONArray(i);
                        subscriptor = new Subscriptor();
                        for (int j = 0; j < cols.length(); j++) {
                            switch (cols.getString(j)) {
                                case "subscriptionId":
                                    subscriptor.setSubscriptionId(reg.get(j).toString());
                                    break;
                                case "name":
                                    subscriptor.setName(reg.get(j).toString());
                                    break;
                                case "email":
                                    subscriptor.setEmail(reg.get(j).toString());
                                    break;
                                case "course":
                                    subscriptor.setCourse(reg.get(j).toString());
                                    break;
                            }
                        }
                        subscriptors.add(subscriptor);
                    }
                } else {
                    System.out.println("no hay datos");
                    return null;
                }
            } else {
                System.out.println(resul.getJSONObject(0).get("error"));
                return null;
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
        return subscriptors;
    }

    private Course executeQuery(String query) {
        try {
            Course course = null;
            JSONArray resul = connection.select(query);
            if (resul != null) {
                JSONArray cols = resul.getJSONObject(0).getJSONArray("columns");
                JSONArray values = resul.getJSONObject(0).getJSONArray("values");
                for (int i = 0; i < values.length(); i++) {
                    JSONArray reg = values.getJSONArray(i);
                    course = new Course();
                    for (int j = 0; j < cols.length(); j++) {
                        switch (cols.getString(j)) {
                            case "id":
                                course.setId(reg.get(j).toString());
                                break;
                            case "name":
                                course.setName(reg.get(j).toString());
                                break;
                            case "passkey":
                                course.setPasskey(reg.get(j).toString());
                                break;
                            case "sharedFolder":
                                course.setSharedFolder(reg.get(j).toString());
                                break;
                            case "userId":
                                course.setUserId(reg.get(j).toString());
                                break;
                        }
                    }
                    return course;
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
        return null;
    }

    private ArrayList<Course> executeQueryList(String query) {
        ArrayList<Course> courses = new ArrayList<>();
        try {
            Course course = null;
            JSONArray resul = connection.select(query);
            if (resul != null) {
                JSONArray cols = resul.getJSONObject(0).getJSONArray("columns");
                JSONArray values = resul.getJSONObject(0).getJSONArray("values");
                for (int i = 0; i < values.length(); i++) {
                    JSONArray reg = values.getJSONArray(i);
                    course = new Course();
                    for (int j = 0; j < cols.length(); j++) {
                        switch (cols.getString(j)) {
                            case "id":
                                course.setId(reg.get(j).toString());
                                break;
                            case "name":
                                course.setName(reg.get(j).toString());
                                break;
                            case "passkey":
                                course.setPasskey(reg.get(j).toString());
                                break;
                            case "sharedFolder":
                                course.setSharedFolder(reg.get(j).toString());
                                break;
                            case "userId":
                                course.setUserId(reg.get(j).toString());
                                break;
                        }
                    }
                    courses.add(course);
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
        return courses;
    }

}
