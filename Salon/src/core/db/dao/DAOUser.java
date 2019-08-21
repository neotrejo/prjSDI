/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.db.dao;

import core.data.User;
import core.db.rqlite.RQLiteConnection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;

/**
 *
 * @author Diana
 */
public class DAOUser {

    private RQLiteConnection connection;

    public DAOUser() {
        connection = RQLiteConnection.getInstance();
    }

    public void insertUser(String name, String username, String password,
            String email, String hostcomputer, String sharedfolder, String port,String location ) {
        

        Map<String, String> params = new LinkedHashMap<>();
        params.put("name", name);
        params.put("userName", username);
        params.put("password", password);
        params.put("email", email);
        params.put("hostComputer", hostcomputer);
        params.put("sharedFolder", sharedfolder);
        params.put("port", port);
        params.put("location", location);
        connection.insert("User", params);

    }

    public void updateUser(String id, String name, String password, String email, String hostcomputer, String sharedfolder, String port, String location) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("name", name);
        params.put("password", password);
        params.put("email", email);
        params.put("hostComputer", hostcomputer);
        params.put("sharedFolder", sharedfolder);
        params.put("port", port);
        params.put("location", location);
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
            JSONArray resul = connection.select(query);
            if (resul != null) {
                User user = null;
                JSONArray cols = resul.getJSONObject(0).getJSONArray("columns");
                JSONArray values = resul.getJSONObject(0).getJSONArray("values");
                for (int i = 0; i < values.length(); i++) {
                    JSONArray reg = values.getJSONArray(i);
                    user = new User();
                    for (int j = 0; j < cols.length(); j++) {
                        switch (cols.getString(j)) {
                            case "id":
                                user.setId(reg.get(j).toString());
                                break;
                            case "name":
                                user.setName(reg.get(j).toString());
                                break;
                            case "userName":
                                user.setUsername(reg.get(j).toString());
                                break;
                            case "password":
                                user.setPassword(reg.get(j).toString());
                                break;
                            case "email":
                                user.setEmail(reg.get(j).toString());
                                break;
                            case "hostComputer":
                                user.setHostcomputer(reg.get(j).toString());
                                break;
                            case "sharedFolder":
                                user.setSharedfolder(reg.get(j).toString());
                                break;
                            case "port":
                                user.setPort(reg.get(j).toString());
                                break;
                        }
                    }
                    return user;
                }
            }
        } catch (JSONException ex) {
            System.out.println(ex.toString());
        }
        return null;
    }

    private List<User> executeQuerySet(String query) throws SQLException {
        ArrayList<User> usersList = new ArrayList<>();
        JSONArray resul = connection.select(query);
        if (resul != null) {
            User user = null;
            JSONArray cols = resul.getJSONObject(0).getJSONArray("columns");
            JSONArray values = resul.getJSONObject(0).getJSONArray("values");
            for (int i = 0; i < values.length(); i++) {
                JSONArray reg = values.getJSONArray(i);
                user = new User();
                for (int j = 0; j < cols.length(); j++) {
                    switch (cols.getString(j)) {
                        case "id":
                            user.setId(reg.get(j).toString());
                            break;
                        case "name":
                            user.setName(reg.get(j).toString());
                            break;
                        case "userName":
                            user.setUsername(reg.get(j).toString());
                            break;
                        case "password":
                            user.setPassword(reg.get(j).toString());
                            break;
                        case "email":
                            user.setEmail(reg.get(j).toString());
                            break;
                        case "hostComputer":
                            user.setHostcomputer(reg.get(j).toString());
                            break;
                        case "sharedFolder":
                            user.setSharedfolder(reg.get(j).toString());
                            break;
                        case "port":
                                user.setPort(reg.get(j).toString());
                                break;
                    }
                }
                usersList.add(user);
            }
        }
        return usersList;
    }
}
