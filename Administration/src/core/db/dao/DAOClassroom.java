/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.db.dao;

import core.data.Classroom;
import core.db.sqlite.SQLiteConnection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author Diana
 */
public class DAOClassroom {

    private SQLiteConnection connection;

    public DAOClassroom() {
        connection = SQLiteConnection.getInstance();
    }

    public void insertClassroom(String name, String location, String hostname, String rootFolder) {

        Map<String, String> params = new LinkedHashMap<>();
        params.put("name", name);
        params.put("location", location);
        params.put("hostName", hostname);
        params.put("root", rootFolder );

        connection.insert("ClassRoom", params);
    }

    public ArrayList<Classroom> getClassrooms() {
        try {
            ArrayList<Classroom> classrooms = new ArrayList<>();
            String query = "SELECT * FROM ClassRoom";
            ResultSet result = connection.select(query);
            Classroom classroom = null;
            String file = "";
            if (result != null) {
                while (result.next()) {
                    classroom = new Classroom();
                    classroom.setId(result.getObject("id").toString());
                    classroom.setName(result.getObject("name").toString());
                    classroom.setLocation(result.getObject("location").toString());
                    classroom.setHostname(result.getObject("hostName").toString());
                    classroom.setRootFolder(result.getObject("rootFolder").toString());
                    classrooms.add(classroom);
                }
            }
            return classrooms;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public Classroom getByName(String name) {
        String query = "SELECT * FROM ClassRoom WHERE name=\"" + name + "\"";
        return executeQuery(query);
    }
    
    public Classroom getByHostName(String hostName) {
        String query = "SELECT * FROM ClassRoom WHERE hostName=\"" + hostName + "\" LIMIT 1";
        return executeQuery(query);
    }    

    public Classroom getById(String id) {
        String query = "SELECT * FROM ClassRoom WHERE id=" + id;
        return executeQuery(query);
    }

    private Classroom executeQuery(String query) {
        try {
            ResultSet result = connection.select(query);
            Classroom classroom = null;
            if (result != null) {
                if (result.next()) {
                    classroom = new Classroom();
                    classroom.setId(result.getObject("id").toString());
                    classroom.setName(result.getObject("name").toString());
                    classroom.setLocation(result.getObject("location").toString());
                    classroom.setHostname(result.getObject("hostName").toString());
                    classroom.setRootFolder(result.getObject("rootFolder").toString());
                }
            }
            return classroom;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
