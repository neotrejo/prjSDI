/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.db.dao;

import core.data.FileSession;
import core.db.rqlite.RQLiteConnection;
import java.util.LinkedHashMap;
import java.util.Map;
import org.json.JSONArray;

/**
 *
 * @author Diana
 */
public class DAOFilesSession {

//    private SQLiteConnection connection;
    private RQLiteConnection connection;

    public DAOFilesSession() {
//        connection = SQLiteConnection.getInstance();
        connection = RQLiteConnection.getInstance();
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
            JSONArray resul = connection.select(query);
            FileSession filesSession = null;
            String file = "";
            if (resul != null) {
                JSONArray cols = resul.getJSONObject(0).getJSONArray("columns");
                JSONArray values = resul.getJSONObject(0).getJSONArray("values");
                for (int i = 0; i < values.length(); i++) {
                    JSONArray reg = values.getJSONArray(i);
                    filesSession = new FileSession();
                    for (int j = 0; j < cols.length(); j++) {
                        switch (cols.getString(j)) {
                            case "id":
                                filesSession.setId(reg.get(j).toString());
                                break;
                            case "fileId":
                                filesSession.setFileId(reg.get(j).toString());
                                break;
                            case "sessionId":
                                filesSession.setSessionId(reg.get(j).toString());
                                break;
                            case "deleted":
                                filesSession.setDeleted(reg.get(j).toString());
                                break;
                        }                        
                    }
                    return filesSession;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
