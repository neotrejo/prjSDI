/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.db.dao;

import core.data.FileD;
import core.db.sqlite.SQLiteConnection;
import java.sql.ResultSet;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author Diana
 */
public class DAOFile {
    private SQLiteConnection connection;
    
    public DAOFile() {
        connection = SQLiteConnection.getInstance();
    }
    public int  insertFile(String fileName, String currentPage, String path) {
        
        Map<String, String> params = new LinkedHashMap<>();
        params.put("fileName", fileName);
        params.put("currentPage", currentPage);
        params.put("path", path);
        return connection.insert("FileD", params);
    }
    
    public void updateFile(String id,String fileName, String currentPage, String path){
        Map<String, String> params = new LinkedHashMap<>();
        params.put("fileName", fileName);
        params.put("currentPage", currentPage);
        params.put("path", path);
        connection.update("FileD", params, "id=\"" + id + "\"");
    }
    public FileD findById(String id){        
        try {
            String query = "SELECT * FROM FileD WHERE id="+ id;
            ResultSet result = connection.select(query);
            FileD file = null;
            if (result != null) {
                if(result.next()){
                    file = new FileD();
                    file.setId(result.getObject("id").toString());
                    file.setFileName(result.getObject("fileName").toString());
                    file.setCurrentPage(result.getObject("currentPage").toString());
                    file.setPath(result.getObject("path").toString());                    
                }
            }           
            return file;
        } catch (Exception ex) {
            ex.printStackTrace();
        }        
        return null;
    }
    
}
