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
public class Subscriptor {
    private String subscription_id;
    private String name;
    private String email;
    private String course;
    
    public Subscriptor(){
    }
    
    public Subscriptor(String subscription_id, String name,String email, String course  ){
        this.subscription_id=subscription_id;
        this.name =name;
        this.email = email;
        this.course = course;
    }
    
    public String getSubscriptionId(){
        return subscription_id;
    }
    
    public void setSubscriptionId(String subscription_id ){
        this.subscription_id=subscription_id;
    }
    
    public String getName(){
        return name;
    }
    
    public void setName(String name ){
        this.name=name;
    }
    public String getEmail(){
        return email;
    }
    
    public void setEmail(String email ){
        this.email=email;
    }
    public String getCourse(){
        return course;
    }
    
    public void setCourse(String course ){
        this.course=course;
    }
    
    
}
