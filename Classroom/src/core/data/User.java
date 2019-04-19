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
    private String fingerprint1;
    private String fingerprint2;
    private String fingerprintImage1;
    private String fingerprintImage2;
    
    public User(){
        
    }

    public User (String id,String name, String username, String password, 
            String email,
            String hostcomputer, 
            String sharedfolder, 
            String fingerprint, 
            String fingerprintImage1,
            String fingerprintImage2) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.email = email;
        this.hostcomputer = hostcomputer;
        this.sharedfolder = sharedfolder;
        this.fingerprint1 = fingerprint1;
        this.fingerprint2 = fingerprint2;
        this.fingerprintImage1 = fingerprintImage1;
        this.fingerprintImage1 = fingerprintImage1;
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
    
    public String getFingerPrint1() {
        return fingerprint1;
    }

    public String getFingerPrint2() {
        return fingerprint2;
    }
    
    public void setFingerPrint1(String fingerprint1) {
        this.fingerprint1 = fingerprint1;
    }
    
    public void setFingerPrint2(String fingerprint2) {
        this.fingerprint2 = fingerprint2;
    }
    
    public String getFingerPrintImage1() {
        return this.fingerprintImage1;
    }   
    public String getFingerPrintImage2() {
        return this.fingerprintImage2;
    }
    
    public void setFingerPrintImage1(String fingerprintImage1) {
        this.fingerprintImage1 = fingerprintImage1;
    }   
    
    public void setFingerPrintImage2(String fingerprintImage2) {
        this.fingerprintImage2 = fingerprintImage2;
    }  
}
