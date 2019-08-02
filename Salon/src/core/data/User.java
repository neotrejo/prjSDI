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
public class User {
    private String id;
    private String name;
    private String username;
    private String password;
    private String email;
    private String hostcomputer;
    private String sharedfolder;
    
    public User(){
        
    }

    public User(String id, String name, String username, String password, String email, String hostcomputer, String sharedfolder) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.email = email;
        this.hostcomputer = hostcomputer;
        this.sharedfolder = sharedfolder;
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

    public String getUserName() {
        return username;
    }

    public void setUserName(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }  
    
     public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }   
    
    public String getHostComputer() {
        return hostcomputer;
    }

    public void setHostComputer(String hostcomputer) {
        this.hostcomputer = hostcomputer;
    }
    
    public String getSharedFolder() {
        return sharedfolder;
    }

    public void setSharedFolder(String sharedfolder) {
        this.sharedfolder = sharedfolder;
    }
}
