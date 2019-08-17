/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.db.rqlite;

import core.db.rqlite.dto.ExecuteResults;
import core.db.rqlite.dto.QueryResults;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Diana
 */
public class RQLiteConnection {

    private Rqlite rqlite;
    private String ip;
    private int port;
    private ExecuteResults executeResul = null;
    private QueryResults queryResul = null;

    private static RQLiteConnection instance;

    private RQLiteConnection() {
        ip = "10.0.5.181";
        port = 4001;
    }

    public static RQLiteConnection getInstance() {
        if (instance == null) {
            instance = new RQLiteConnection();
        }
        return instance;
    }

    public void conectar() throws ClassNotFoundException {
        rqlite = RqliteFactory.connect("http", ip, port);
    }

    /**
     * Sentencias
     */
    private String createParamString(Map<String, String> params) {
        StringBuilder fields = new StringBuilder();
        StringBuilder values = new StringBuilder();

        fields.append("(");
        values.append("(");

        for (Map.Entry<String, String> param : params.entrySet()) {
            fields.append(param.getKey()).append(",");
            values.append("\"").append(param.getValue()).append("\"").append(",");
        }

        if (fields.toString().endsWith(",")) {
            fields.deleteCharAt(fields.length() - 1);
        }

        if (values.toString().endsWith(",")) {
            values.deleteCharAt(values.length() - 1);
        }

        fields.append(")");
        values.append(")");

        return fields.append(" VALUES ").append(values).toString();
    }

    private String createUpdateParamString(Map<String, String> params) {

        StringBuilder fields = new StringBuilder();
        for (Map.Entry<String, String> param : params.entrySet()) {
            fields.append(param.getKey()).append("=").append("\"").append(param.getValue()).append("\"").append(",");
        }

        if (fields.toString().endsWith(",")) {
            fields.deleteCharAt(fields.length() - 1);
        }

        return fields.toString();
    }

    public int insert(String table, Map<String, String> params) {

        int response = 0, key = -1;

        try {

            String query = "INSERT INTO " + table + " " + createParamString(params);
            System.out.println(query);

            executeResul = rqlite.Execute(query);
            JSONObject resulQuery = new JSONObject(executeResul);
            JSONObject resul = resulQuery.getJSONArray("results").getJSONObject(0);
            key = resul.getInt("last_insert_id");
            return key;

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return response;
    }

    public int update(String table, Map<String, String> params, String where) {
        int response = 0;
        String query = "UPDATE " + table + " SET " + createUpdateParamString(params) + " WHERE " + where;
        System.out.println(query);
        executeResul = rqlite.Execute(query);
        JSONObject resulQuery = new JSONObject(executeResul);
        JSONObject resul = resulQuery.getJSONArray("results").getJSONObject(0);
        response = resul.getInt("rows_affected");
        return response;
    }

    public int execute(String query) {
        int response = 0;
        executeResul = rqlite.Execute(query);
        JSONObject resulQuery = new JSONObject(executeResul);
        JSONObject resul = resulQuery.getJSONArray("results").getJSONObject(0);
        response = resul.getInt("rows_affected");
        return response;
    }

    public JSONArray select(String query) {
        QueryResults result = null;
        System.out.println(query);
        result = rqlite.Query(query, Rqlite.ReadConsistencyLevel.WEAK);
        JSONObject resulQuery = new JSONObject(result);
        JSONArray resuls = resulQuery.getJSONArray("results");
        if (!resuls.getJSONObject(0).has("error")) {
            if (resuls.getJSONObject(0).has("values")) {
                return resuls;
            } else {
                System.out.println("no hay datos");
            }
        } else {
            System.out.println(resuls.getJSONObject(0).get("error"));
        }
        return null;
    }

}
