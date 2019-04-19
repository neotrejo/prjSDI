/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.db.dao;

import core.data.Subject;
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
public class DAOSubject {

    private SQLiteConnection connection;

    public DAOSubject() {
        connection = SQLiteConnection.getInstance();
    }

    public void insertSubject(String name, String password, String description, String sharedfolder, String user_id) {

        Map<String, String> params = new LinkedHashMap<>();
        params.put("subjectName", name);
        params.put("passKey", password);
        params.put("description", description);
        params.put("sharedFolder", sharedfolder);
        params.put("user_id", user_id);
        params.put("deleted", String.valueOf(false));

        connection.insert("Subject", params);
    }

    public void updateSubject(String id, String name, String password, String description, String sharedfolder) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("subjectName", name);
        params.put("passKey", password);
        params.put("description", description);
        params.put("sharedFolder", sharedfolder);
        connection.update("Subject", params, "id=\"" + id + "\"");
    }

    public void deleteSubject(int id) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("deleted", String.valueOf(true));
        connection.update("Subject", params, "id=\"" + id + "\"");
    }

    public Subject findByNameAndPass(String name, String pass) {
        String query = "SELECT * FROM Subject WHERE subjectName=\"" + name + "\" and passKey=\"" + pass + "\" and deleted=\"false\" LIMIT 1";
        return executeQuery(query);
    }

    public Subject findByName(String name) {
        String query = "SELECT * FROM Subject WHERE subjectName=\"" + name + "\" and deleted=\"false\" LIMIT 1";
        return executeQuery(query);
    }

    public Subject findById(String id) {
        String query = "SELECT * FROM Subject WHERE id=" + id + " LIMIT 1";
        return executeQuery(query);
    }

    public Subject findByNameNotId(String name, String id) {
        String query = "SELECT * FROM Subject WHERE subjectName=\"" + name + "\" and id<>" + id + " and deleted=\"false\" LIMIT 1";
        return executeQuery(query);
    }

    public Subject getByNameAndUserId(String name, String user_id) {
        String query = "SELECT * FROM Subject WHERE subjectName=\"" + name + "\" and (user_id= \"" + user_id + "\" or user_id=" + user_id + ") and deleted=\"false\"";
        return executeQuery(query);
    }

    public ArrayList<Subject> getByUserId(String user_id) {
        String query = "SELECT * FROM Subject WHERE (user_id=" + user_id + " or user_id=\"" + user_id + "\") and deleted=\"false\"";
        return executeQueryList(query);
    }

    public ArrayList<Subject> getByNotUserId(String user_id) {
        String query = "SELECT * FROM Subject WHERE (user_id <> " + user_id + " and user_id <> \"" + user_id + "\") and deleted=\"false\"";
        return executeQueryList(query);
    }

    public ArrayList<Subscriptor> findSubscriptors(String subject_id, String user_id) {
        try {
            String query = "SELECT Subscription.id subscription_id, User.name, User.email, Subject.subjectName FROM Subject\n"
                    + "INNER JOIN Subscription on Subscription.subject_id = Subject.id\n"
                    + "INNER JOIN User on User.id = Subscription.user_id\n"
                    + "WHERE Subject.user_id = \"" + user_id + "\"  and  Subject.id=" + subject_id;
            ResultSet result = connection.select(query);
            Subscriptor subscriptor = null;
            ArrayList<Subscriptor> subscriptors = new ArrayList<>();
            if (result != null) {
                while (result.next()) {
                    subscriptor = new Subscriptor();
                    subscriptor.setSubscriptionId(result.getObject("subscription_id").toString());
                    subscriptor.setName(result.getObject("name").toString());
                    subscriptor.setEmail(result.getObject("email").toString());
                    subscriptor.setSubject(result.getObject("subjectName").toString());
                    subscriptors.add(subscriptor);
                }
            }
            return subscriptors;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private Subject executeQuery(String query) {
        try {
            ResultSet result = connection.select(query);
            Subject subject = null;
            if (result != null) {
                if (result.next()) {
                    subject = new Subject();
                    subject.setId(result.getObject("id").toString());
                    subject.setName(result.getObject("subjectName").toString());
                    subject.setPassword(result.getObject("passKey").toString());
                    subject.setDescription(result.getObject("description").toString());
                    subject.setSharedFolder(result.getObject("sharedFolder").toString());
                    subject.setUserId(result.getObject("user_id").toString());
                }
            }
            return subject;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private ArrayList<Subject> executeQueryList(String query) {
        try {
            ArrayList<Subject> subjects = new ArrayList<>();
            ResultSet result = connection.select(query);
            Subject subject = null;
            if (result != null) {
                while (result.next()) {
                    subject = new Subject();
                    subject.setId(result.getObject("id").toString());
                    subject.setName(result.getObject("subjectName").toString());
                    subject.setPassword(result.getObject("passKey").toString());
                    subject.setDescription(result.getObject("description").toString());
                    subject.setSharedFolder(result.getObject("sharedFolder").toString());
                    subject.setUserId(result.getObject("user_id").toString());
                    subjects.add(subject);
                }
            }
            return subjects;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
