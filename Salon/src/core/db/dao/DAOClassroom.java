/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.db.dao;

import core.data.Classroom;
import core.db.rqlite.RQLiteConnection;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import org.json.JSONArray;

/**
 *
 * @author Diana
 */
public class DAOClassroom {

    private RQLiteConnection connection;

    public DAOClassroom() {
        connection = RQLiteConnection.getInstance();
    }

    public void insertClassroom(String name, String location, String hostname, String rootFolder, String port) {

        Map<String, String> params = new LinkedHashMap<>();
        params.put("name", name);
        params.put("location", location);
        params.put("hostname", hostname);
        params.put("rootFolder", rootFolder);
        params.put("port", port);
        connection.insert("Classroom", params);
    }

    public ArrayList<Classroom> getClassrooms() {
        try {
            ArrayList<Classroom> classrooms = new ArrayList<>();
            String query = "SELECT * FROM Classroom";
            JSONArray resul = connection.select(query);
            if (resul != null) {
                Classroom classroom = null;
                JSONArray cols = resul.getJSONObject(0).getJSONArray("columns");
                JSONArray values = resul.getJSONObject(0).getJSONArray("values");
                for (int i = 0; i < values.length(); i++) {
                    JSONArray reg = values.getJSONArray(i);
                    classroom = new Classroom();
                    for (int j = 0; j < cols.length(); j++) {
                        switch (cols.getString(j)) {
                            case "id":
                                classroom.setId(reg.get(j).toString());
                                break;
                            case "name":
                                classroom.setName(reg.get(j).toString());
                                break;
                            case "location":
                                classroom.setLocation(reg.get(j).toString());
                                break;
                            case "hostname":
                                classroom.setHostname(reg.get(j).toString());
                                break;
                            case "rootFolder":
                                classroom.setRootFolder(reg.get(j).toString());
                                break;
                            case "port":
                                classroom.setPort(reg.get(j).toString());
                                break;
                        }                        
                    }
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
        String query = "SELECT * FROM Classroom WHERE name=\"" + name + "\"";
        return executeQuery(query);
    }

    public Classroom getByHostName(String hostName) {
        String query = "SELECT * FROM Classroom WHERE hostname=\"" + hostName + "\" LIMIT 1";
        return executeQuery(query);
    }

    public Classroom getById(String id) {
        String query = "SELECT * FROM Classroom WHERE id=" + id;
        return executeQuery(query);
    }

    private Classroom executeQuery(String query) {
        try {
            JSONArray resul = connection.select(query);
            if (resul != null) {
                Classroom classroom = null;
                JSONArray cols = resul.getJSONObject(0).getJSONArray("columns");
                JSONArray values = resul.getJSONObject(0).getJSONArray("values");
                for (int i = 0; i < values.length(); i++) {
                    JSONArray reg = values.getJSONArray(i);
                    classroom = new Classroom();
                    for (int j = 0; j < cols.length(); j++) {
                        switch (cols.getString(j)) {
                            case "id":
                                classroom.setId(reg.get(j).toString());
                                break;
                            case "name":
                                classroom.setName(reg.get(j).toString());
                                break;
                            case "location":
                                classroom.setLocation(reg.get(j).toString());
                                break;
                            case "hostname":
                                classroom.setHostname(reg.get(j).toString());
                                break;
                            case "rootFolder":
                                classroom.setRootFolder(reg.get(j).toString());
                                break;
                            case "port":
                                classroom.setPort(reg.get(j).toString());
                                break;
                        }                       
                    }
                     return classroom;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
