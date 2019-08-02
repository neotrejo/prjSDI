/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.db.dao;

import core.data.User;
import core.db.sqlite.SQLiteConnection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
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

    public void insertUser(String name, String username, String password,
            String email, String hostcomputer, String sharedfolder) {

        Map<String, String> params = new LinkedHashMap<>();
        params.put("name", name);
        params.put("userName", username);
        params.put("password", password);
        params.put("email", email);
        params.put("hostComputer", hostcomputer);
        params.put("sharedFolder", sharedfolder);
        connection.insert("User", params);                  
        
    }

    public void updateUser(String id, String name, String password, String email, String hostcomputer, String sharedfolder) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("name", name);
        params.put("password", password);
        params.put("email", email);
        params.put("hostComputer", hostcomputer);
        params.put("sharedFolder", sharedfolder);
        connection.update("User", params, "id=\"" + id + "\"");
    }

    public User findById(String id) {
            String query = "SELECT * FROM User WHERE id=\"" + id + "\"";
            return executeQuery(query);
    }

    public User findByUserAndPass(String username, String pass) {
            String query = "SELECT * FROM User WHERE userName=\"" + username + "\" and password=\"" + pass + "\" LIMIT 1";
            return executeQuery(query);
    }
    public User findByUserName(String username) {
            String query = "SELECT * FROM User WHERE userName=\"" + username + "\" LIMIT 1";
            return executeQuery(query);
    }
    
    public List<User> getAllUsers() throws SQLException {
            String query = "SELECT * FROM User";
            return executeQuerySet(query);
    }

    private User executeQuery(String query) {
        try {
            ResultSet result = connection.select(query);
            User user = null;
            if (result != null) {
                if (result.next()) {
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
    
    private List<User> executeQuerySet(String query) throws SQLException {
        ArrayList<User> usersList = new ArrayList<>();
        
        
        ResultSet result = connection.select(query);
        
        if(result!=null){
            try {
                while (result.next()) {
                    User user = new User();
                    user.setId(result.getObject("id").toString());
                    user.setName(result.getObject("name").toString());
                    user.setUserName(result.getObject("userName").toString());
                    user.setPassword(result.getObject("password").toString());
                    user.setEmail(result.getObject("email").toString());
                    user.setHostComputer(result.getObject("hostComputer").toString());
                    user.setSharedFolder(result.getObject("sharedFolder").toString());
                    boolean add = usersList.add(user);
                }
                            
            } catch(SQLException e) {
                System.out.println(e.toString());
            }          
        }else{
            System.out.println("Select * From User, query did not return results.");
        }
        return usersList;
    }
}
