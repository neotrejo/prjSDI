/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.db.dao;

import core.data.User;
import core.db.sqlite.SQLiteConnection;
import java.sql.ResultSet;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author Diana
 */
public class DAOUser {
    private SQLiteConnection connection;
    
    public DAOUser() {
        connection = SQLiteConnection.getInstance();
    }
    
    public void insertUser(String name, String username, String password, String email, String hostcomputer, String sharedfolder, String fingerprint) {
        
        Map<String, String> params = new LinkedHashMap<>();
        params.put("name", name);
        params.put("userName", username);
        params.put("password", password);
        params.put("email", email);
        params.put("hostComputer", hostcomputer);
        params.put("sharedFolder", sharedfolder);
        params.put("fingerPrint", fingerprint);

        connection.insert("User", params);
    }
    
    public User findByUserAndPass(String username, String pass){        
        try {
            String query = "SELECT * FROM User WHERE userName=\"" + username + "\" and password=\"" + pass + "\" LIMIT 1";
            ResultSet result = connection.select(query);
            User user = null;
            String file = "";
            if (result != null) {
                if(result.next()){
                    user = new User();
                    user.setId(result.getObject("id").toString());
                    user.setName(result.getObject("name").toString());
                    user.setUserName(result.getObject("userName").toString());
                    user.setPassword(result.getObject("password").toString());
                    user.setEmail(result.getObject("email").toString());
                    user.setHostComputer(result.getObject("hostComputer").toString());
                    user.setSharedFolder(result.getObject("sharedFolder").toString());
                }
            }            
            return user;
        } catch (Exception ex) {
            ex.printStackTrace();
        }        
        return null;
    }
    
    public void updateSharedFolder(String sharedFolder) {

        Map<String, String> params = new LinkedHashMap<>();

        params.put("sharedFolder",sharedFolder);

        connection.update("user", params, "id=\"" + 1 + "\"");
    }
    
    public void updateHostComputer(String hostComputer) {

        Map<String, String> params = new LinkedHashMap<>();

        params.put("hostComputer",hostComputer);

        connection.update("user", params, "id=\"" + 1 + "\"");
    }
    
}
