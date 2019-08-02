/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.db.dao;

import core.data.FileSession;
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
        params.put("fileId", file_id);
        params.put("sessionId", session_id);
        params.put("deleted", deleted);
        connection.insert("FileSession", params);
    }

    public void deleteFilesSession(int id) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("deleted", String.valueOf(true));
        connection.update("FileSession", params, "id=\"" + id + "\"");

    }

    public FileSession findBySession(String session_id) {
        try {
            String query = "SELECT * FROM FileSession WHERE sessionId=\"" + session_id + "\"";
            ResultSet result = connection.select(query);
            FileSession filesSession = null;
            String file = "";
            if (result != null) {
                if (result.next()) {
                    filesSession = new FileSession();
                    filesSession.setId(result.getObject("id").toString());
                    filesSession.setFileId(result.getObject("fileId").toString());
                    filesSession.setSessionId(result.getObject("sessionId").toString());
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
