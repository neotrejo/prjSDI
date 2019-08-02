/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template subscription, choose Tools | Templates
 * and open the template in the editor.
 */
package core.db.dao;

import core.data.Subscription;
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
public class DAOSubscription {
    private SQLiteConnection connection;
    
    public DAOSubscription() {
        connection = SQLiteConnection.getInstance();
    }
    
    public int  insertSubscription(String subject_id, String user_id,String deleted) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("courseId", subject_id);
        params.put("userId", user_id);
        params.put("deleted", deleted);
        return connection.insert("Subscription", params);
    }
    
    public void deleteSubscription (int id){
        Map<String, String> params = new LinkedHashMap<>();
        params.put("deleted",String.valueOf(true));
        connection.update("Subscription", params, "id=\"" + id + "\"");    
    }
    
    public Subscription findById(String id){        
            String query = "SELECT * FROM Subscription WHERE id="+ id;
            return executeQuery(query);
    }
    public Subscription findBySubjectUser(String subject_id, String user_id){     
            String query = "SELECT * FROM Subscription WHERE courseId=\""+ subject_id+"\" and userId=\""+user_id+"\" and deleted=\"false\"" ;
            return executeQuery(query);
    }
    
    private Subscription executeQuery(String query){
         try {
            ResultSet result = connection.select(query);
            Subscription subscription = null;
            if (result != null) {
                if(result.next()){
                    subscription = new Subscription();
                    subscription.setId(result.getObject("id").toString());
                    subscription.setCourseId(result.getObject("courseId").toString());
                    subscription.setUserId(result.getObject("userId").toString());                 
                }
            }           
            return subscription;
        } catch (Exception ex) {
            ex.printStackTrace();
        }        
        return null;
    }
    
     public ArrayList<Subscription> findByUserId(String user_id){        
        try {
            ArrayList<Subscription> subscriptions = new ArrayList<>();
            String query = "SELECT Subscription.*, Course.name course, User.name professor\n" +
                            "FROM Subscription\n" +
                            "INNER JOIN Course on Course.id = Subscription.courseId\n" +
                            "INNER JOIN User on User.id= Course.userId WHERE Subscription.userId = \""+ user_id+"\" and Subscription.deleted=\"false\"";
            ResultSet result = connection.select(query);
            Subscription subscription = null;
            if (result != null) {
                while(result.next()){
                    subscription = new Subscription();
                    subscription.setId(result.getObject("id").toString());
                    subscription.setCourseId(result.getObject("courseId").toString());
                    subscription.setUserId(result.getObject("userId").toString()); 
                    subscription.setCourseName(result.getObject("course").toString());
                    subscription.setProfessorName(result.getObject("professor").toString()); 
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
