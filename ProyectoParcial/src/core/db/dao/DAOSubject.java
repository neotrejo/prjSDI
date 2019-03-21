/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.db.dao;

import core.data.Subject;
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
        params.put("user_id",user_id );

        connection.insert("Subject", params);
    }
    
    public Subject findByNameAndPass(String name, String pass){        
        try {
            String query = "SELECT * FROM Subject WHERE subjectName=\"" + name + "\" and passKey=\"" + pass + "\" LIMIT 1";
            ResultSet result = connection.select(query);
            Subject subject = null;
            String file = "";
            if (result != null) {
                if(result.next()){
                    subject = new Subject();
                    subject.setId(result.getObject("id").toString());
                    subject.setName(result.getObject("subjetcName").toString());
                    subject.setPassword(result.getObject("passKey").toString());
                    subject.setDescription(result.getObject("description").toString());
                    subject.setSharedFolder(result.getObject("sharedFolder").toString());
                }
            }            
            return subject;
        } catch (Exception ex) {
            ex.printStackTrace();
        }        
        return null;
    }
    
    public ArrayList<Subject> getByUserId(String id){        
        try {
            ArrayList<Subject> subjects = new ArrayList<>();
            String query = "SELECT * FROM Subject WHERE user_id=" + id + " or user_id=\"" + id + "\"";
            ResultSet result = connection.select(query);
            Subject subject = null;
            String file = "";
            if (result != null) {
                while(result.next()){
                    subject = new Subject();
                    subject.setId(result.getObject("id").toString());
                    subject.setName(result.getObject("subjectName").toString());
                    subject.setPassword(result.getObject("passKey").toString());
                    subject.setDescription(result.getObject("description").toString());
                    subject.setSharedFolder(result.getObject("sharedFolder").toString());
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
