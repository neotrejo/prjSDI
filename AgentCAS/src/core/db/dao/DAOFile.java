/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.db.dao;

import core.data.FileD;
import core.db.rqlite.RQLiteConnection;
import java.util.LinkedHashMap;
import java.util.Map;
import org.json.JSONArray;

/**
 *
 * @author Diana
 */
public class DAOFile {

    private RQLiteConnection connection;

    public DAOFile() {
        connection = RQLiteConnection.getInstance();
    }

    public int insertFile(String fileName, String currentPage, String path) {

        Map<String, String> params = new LinkedHashMap<>();
        params.put("fileName", fileName);
        params.put("currentPage", currentPage);
        params.put("path", path);
        return connection.insert("FileD", params);
    }

    public void updateFile(String id, String fileName, String currentPage, String path) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("fileName", fileName);
        params.put("currentPage", currentPage);
        params.put("path", path);
        connection.update("FileD", params, "id=\"" + id + "\"");
    }

    public FileD findById(String id) {
        try {
            String query = "SELECT * FROM FileD WHERE id=" + id;
            JSONArray resul = connection.select(query);
            FileD file = null;
            if (resul != null) {
                JSONArray cols = resul.getJSONObject(0).getJSONArray("columns");
                JSONArray values = resul.getJSONObject(0).getJSONArray("values");
                for (int i = 0; i < values.length(); i++) {
                    JSONArray reg = values.getJSONArray(i);
                    file = new FileD();
                    for (int j = 0; j < cols.length(); j++) {
                        switch (cols.getString(j)) {
                            case "id":
                                file.setId(reg.get(j).toString());
                                break;
                            case "fileName":
                                file.setFileName(reg.get(j).toString());
                                break;
                            case "currentPage":
                                file.setCurrentPage(reg.get(j).toString());
                                break;
                            case "path":
                                file.setPath(reg.get(j).toString());
                                break;
                        }                       
                    }
                     return file;
                }
            }
            return file;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
