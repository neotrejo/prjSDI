/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.db.dao;

import core.data.FilesSession;
import core.db.sqlite.SQLiteConnection;
import java.sql.ResultSet;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author Diana
 */
public class DAOFilesSession {

    private SQLiteConnection connection;

    public DAOFilesSession() {
        connection = SQLiteConnection.getInstance();
    }

    public void insertFilesSession(String file_id, String session_id, String deleted) {

        Map<String, String> params = new LinkedHashMap<>();
        params.put("file_id", file_id);
        params.put("session_id", session_id);
        params.put("deleted", deleted);
        connection.insert("FilesSession", params);
    }

    public void deleteFilesSession(int id) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("deleted", String.valueOf(true));
        connection.update("FilesSession", params, "id=\"" + id + "\"");

    }

    public FilesSession findBySession(String session_id) {
        try {
            String query = "SELECT * FROM FilesSession WHERE session_id=\"" + session_id + "\"";
            ResultSet result = connection.select(query);
            FilesSession filesSession = null;
            String file = "";
            if (result != null) {
                if (result.next()) {
                    filesSession = new FilesSession();
                    filesSession.setId(result.getObject("id").toString());
                    filesSession.setFileId(result.getObject("file_id").toString());
                    filesSession.setSessionId(result.getObject("session_id").toString());
                    filesSession.setDeleted(result.getObject("deleted").toString());
                }
            }
            return filesSession;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
