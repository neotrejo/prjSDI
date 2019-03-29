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
public class Subject {

    private String id;
    private String name;
    private String password;
    private String description;
    private String sharedfolder;
    private String user_id;

    public Subject() {

    }

    public Subject(String id, String name, String password, String description, String sharedfolder, String user_id) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.description = description;
        this.sharedfolder = sharedfolder;
        this.user_id = user_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSharedFolder() {
        return sharedfolder;
    }

    public void setSharedFolder(String sharedfolder) {
        this.sharedfolder = sharedfolder;
    }
    
    public String getUserId() {
        return user_id;
    }

    public void setUserId(String user_id) {
        this.user_id = user_id;
    }
    

}
