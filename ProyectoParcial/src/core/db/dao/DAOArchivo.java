/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.db.dao;

import core.data.Archivo;
import core.db.sqlite.SQLiteConnection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author Luis
 */
public class DAOArchivo {

    private SQLiteConnection connection;

    public DAOArchivo() {
        connection = SQLiteConnection.getInstance();
    }

    public void insertArchivo(String nombre, String fullName, String path,long size) {

        Map<String, String> params = new LinkedHashMap<>();
        params.put("name", nombre);
        params.put("full_name", fullName);
        params.put("path", path);
        params.put("size", size+"");

        connection.insert("files", params);
    }
    
    public String findBySizeAndName(String name, long size){
        
        try {
            ArrayList<Archivo> archivos = new ArrayList<>();
            String query = "SELECT * FROM files WHERE name=\"" + name + "\" and size="+size+" LIMIT 1";
            ResultSet result = connection.select(query);
            Archivo arch = null;
            String file = "";
            if (result != null) {
                if(result.next()){
                    arch = new Archivo();
                    arch.setname(result.getObject("name").toString());
                    arch.setpath(result.getObject("full_name").toString());
                    archivos.add(arch);
                    file = arch.getpath();
                }
            }
            
            return file;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        return "";
    }

    public ArrayList<Archivo> findSimilarFiles(String name, Boolean pdf, Boolean zip, Boolean jpeg) {
        String ext="";
        if(name.isEmpty()){
            ext="SELECT * FROM files WHERE name ='%" + name + "%'";
        }
        else{
            ext = "SELECT * FROM files WHERE name LIKE'%" + name + "%'";
        }
        try {
            ArrayList<Archivo> archivos = new ArrayList<>();
            String query1;
            String query=ext;
//            String query = "SELECT * FROM files WHERE name LIKE'%" + name + "%'";
            ResultSet result = connection.select(query);

            if (result != null) {
                while (result.next()) {
                    
                    Archivo arch = new Archivo();
                    arch.setname(result.getObject("name").toString());
                    arch.setpath(result.getObject("full_name").toString());
                    archivos.add(arch);
                }
            }
            
            return archivos;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        return null;
    }

    public ArrayList<Archivo> findEqualFiles(String name) {
        //System.out.println("\n\n\n"+name+", "+""+""+""+"\n\n\n");
        try {
            ArrayList<Archivo> archivos = new ArrayList<>();
            String query = "SELECT * FROM files WHERE name =\"" + name +".pdf"+ "\"";
            ResultSet result = connection.select(query);
            if (result != null) {
                while (result.next()) {
                    Archivo arch = new Archivo();
                    arch.setname(result.getObject("name").toString());
                    arch.setpath(result.getObject("full_name").toString());
                    archivos.add(arch);
                }
            }
            return archivos;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
    
    public ArrayList<Archivo> findEqualFiles(String name, Boolean pdf, Boolean zip, Boolean jpeg) {
        String ext="";
        if(pdf){
            ext=name+".pdf"+ "\"";
            if(zip){
                ext=ext+" OR name="+ "\""+name+".zip"+ "\"";
                if(jpeg){
                    ext=ext+" OR name="+ "\""+name+".jpg"+ "\"";
                }
            }
            else if(jpeg){
                ext=ext+" OR name="+ "\""+name+".jpg"+ "\"";
            }
        }
        else{
            if(zip){
                ext=name+".zip"+ "\"";
                if(jpeg){
                    ext=ext+" OR name="+ "\""+name+".jpg"+ "\"";
                }
            }
            else if(jpeg){
                ext=ext+name+".jpg"+ "\"";
            }
            else{
                ext=ext+"\"";
            }
        }
        try {
            ArrayList<Archivo> archivos = new ArrayList<>();
            String query = "SELECT * FROM files WHERE name =\"" + ext;
            ResultSet result = connection.select(query);
            if (result != null) {
                while (result.next()) {
                    
                    Archivo arch = new Archivo();
                    arch.setname(result.getObject("name").toString());
                    arch.setpath(result.getObject("full_name").toString());
                    archivos.add(arch);
                }
            }
            
            return archivos;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public void updateFileSize(String name, String fullName, long size) {

        Map<String, String> params = new LinkedHashMap<>();

        params.put("size", String.valueOf(size));

        connection.update("files", params, "name=\"" + name + "\" and full_name=\"" + fullName + "\"");
    }
    
    public void updateSharedFolder(String sharedFolder) {

        Map<String, String> params = new LinkedHashMap<>();

        params.put("shared_folder",sharedFolder);

        connection.update("configuration", params, "id=\"" + 1 + "\"");
    }
        
    public void deleteArchivo(String nombre, String fullName) {
        String query = "DELETE FROM files WHERE name=\"" + nombre + "\" AND full_name=\"" + fullName + "\"";
        connection.execute(query);
    }

    public void deleteDirectory(String path) {
        String query = "DELETE FROM files WHERE path=\"" + path + "\"";
        connection.execute(query);
    }

    public void deleteAll() {
        String query = "DELETE FROM files";
        connection.execute(query);
    }

}
