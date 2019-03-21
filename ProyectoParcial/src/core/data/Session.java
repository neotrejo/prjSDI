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
public class Session {
    private String id;
    private String name;
    private String username;
    private String password;
    private String email;
    private String hostcomputer;
    private String sharedfolder;
    private String fingerprint;
    
    public Session(){
        
    }

    public Session (String id,String name, String username, String password, String email, String hostcomputer, String sharedfolder, String fingerprint) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.email = email;
        this.hostcomputer = hostcomputer;
        this.sharedfolder = sharedfolder;
        this.fingerprint = fingerprint;
    }
    
}
