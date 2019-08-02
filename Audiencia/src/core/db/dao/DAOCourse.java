/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.db.dao;

import core.data.Course;
import core.data.Subscriptor;
import core.db.sqlite.SQLiteConnection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author Diana
 */
public class DAOCourse {

    private SQLiteConnection connection;

    public DAOCourse() {
        connection = SQLiteConnection.getInstance();
    }

    public void insertSubject(String name, String password, String description, String sharedfolder, String user_id) {

        Map<String, String> params = new LinkedHashMap<>();
        params.put("name", name);
        params.put("passkey", password);
//        params.put("description", description);
        params.put("sharedFolder", sharedfolder);
        params.put("userId", user_id);
        params.put("deleted", String.valueOf(false));

        connection.insert("Course", params);
    }

    public void updateSubject(String id, String name, String password, String description, String sharedfolder) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("name", name);
        params.put("passkey", password);
//        params.put("description", description);
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
        try {
            String query = "SELECT Subscription.id subscriptionId, User.name, User.email, Course.name course FROM Course\n"
                    + "INNER JOIN Subscription on Subscription.courseId = Course.id\n"
                    + "INNER JOIN User on User.id = Subscription.userId\n"
                    + "WHERE Course.userId = \"" + user_id + "\"  and  Course.id=" + subject_id;
            ResultSet result = connection.select(query);
            Subscriptor subscriptor = null;
            ArrayList<Subscriptor> subscriptors = new ArrayList<>();
            if (result != null) {
                while (result.next()) {
                    subscriptor = new Subscriptor();
                    subscriptor.setSubscriptionId(result.getObject("subscriptionId").toString());
                    subscriptor.setName(result.getObject("name").toString());
                    subscriptor.setEmail(result.getObject("email").toString());
                    subscriptor.setCourse(result.getObject("course").toString());
                    subscriptors.add(subscriptor);
                }
            }
            return subscriptors;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private Course executeQuery(String query) {
        try {
            ResultSet result = connection.select(query);
            Course course = null;
            if (result != null) {
                if (result.next()) {
                    course = new Course();
                    course.setId(result.getObject("id").toString());
                    course.setName(result.getObject("name").toString());
                    course.setPasskey(result.getObject("passkey").toString());
//                    course.setDescription(result.getObject("description").toString());
                    course.setSharedFolder(result.getObject("sharedFolder").toString());
                    course.setUserId(result.getObject("userId").toString());
                }
            }
            return course;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private ArrayList<Course> executeQueryList(String query) {
        try {
            ArrayList<Course> subjects = new ArrayList<>();
            ResultSet result = connection.select(query);
            Course course = null;
            if (result != null) {
                while (result.next()) {
                    course = new Course();
                    course.setId(result.getObject("id").toString());
                    course.setName(result.getObject("name").toString());
                    course.setPasskey(result.getObject("passkey").toString());
//                    course.setDescription(result.getObject("description").toString());
                    course.setSharedFolder(result.getObject("sharedFolder").toString());
                    course.setUserId(result.getObject("userId").toString());
                    subjects.add(course);
                }
            }
            return subjects;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
