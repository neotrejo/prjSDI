/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.data;

/**
 *
 * @author Diana
 */
public class Subscription {
    private String id;
    private String subject_id;
    private String user_id;
    private String deleted;
    private String professor;
    private String subject;
    public Subscription(){
        
    }
    
    public Subscription(String id, String subject_id, String user_id, String deleted){
        this.id = id;
        this.subject_id = subject_id;
        this.user_id = user_id;
        this.deleted = deleted;
    }
    
    public String getId(){
        return this.id;
    }
    
    public void setId(String id){
        this.id = id;
    }
    
    public String getUserId(){
        return this.user_id;
    }
    
    public void setUserId(String user_id){
        this.user_id = user_id;
    }
    
    public String getSubjectId(){
        return this.subject_id;
    }
    
    public void setSubjectId(String subject_id){
        this.subject_id = subject_id;
    }
    public String getDeleted(){
        return this.deleted;
    }
    
    public void setDeleted(String deleted){
        this.deleted = deleted;
    }
    public String getSubject(){
        return this.subject;
    }
    
    public void setSubject(String subject){
        this.subject = subject;
    }
    
    public String getprofessor(){
        return this.professor;
    } 
    
    public void setProfessor(String professor){
        this.professor = professor;
    }
    
}
