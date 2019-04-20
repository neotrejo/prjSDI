/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.db.dao;

import core.data.Archivo;
import core.data.Descarga;
import core.db.sqlite.SQLiteConnection;
import core.utils.MyLogger;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author luismartin
 */
public class DAODescarga {

    private SQLiteConnection connection;

    public DAODescarga() {
        connection = SQLiteConnection.getInstance();
    }

    public int insertarDescarga(String name, String path, String host, double percent, String status, long lastByte, long size,String hostPath) {

        Map<String, String> params = new LinkedHashMap<>();
        params.put("name", name);
        params.put("path", path);
        params.put("host", host);
        params.put("host_path", hostPath);
        params.put("percent", String.valueOf(percent));
        params.put("status", status);
        params.put("last_byte", String.valueOf(lastByte));
        params.put("size", String.valueOf(size));

        return connection.insert("downloads", params);
    }

    public void updateDescarga(int id, double percent, String status, long lastByte) {

        Map<String, String> params = new LinkedHashMap<>();

        params.put("percent", String.valueOf(percent));
        params.put("status", status);
        params.put("last_byte", String.valueOf(lastByte));

        connection.update("downloads", params, "id=\"" + id + "\"");
    }
    
    public void updateDescarga(int id, double percent, long lastByte) {

        Map<String, String> params = new LinkedHashMap<>();

        params.put("percent", String.valueOf(percent));
        params.put("last_byte", String.valueOf(lastByte));

        connection.update("downloads", params, "id=\"" + id + "\"");
    }
    
    public void borrarDescarga(String name, String path){
        connection.execute("DELETE FROM downloads WHERE name=\""+name+"\" and path=\""+path+"\"");
    }
    
    public void borrarDescarga(int id){
        connection.execute("DELETE FROM downloads WHERE id=\""+id+"\"");
    }

    public void borrarDescargas(){
        connection.execute("DELETE FROM downloads");
    }
        
    public ArrayList<Descarga> getDescargas() {

        try {
            ArrayList<Descarga> descargas = new ArrayList<>();
            String query = "SELECT * FROM downloads";
            ResultSet result = connection.select(query);

            if (result != null) {
                while (result.next()) {

                    Descarga descarga = new Descarga();
                    
                    descarga.setId(result.getInt("id"));
                    descarga.setName(result.getObject("name").toString());
                    descarga.setPath(result.getObject("path").toString());
                    descarga.setHostPath(result.getObject("host_path").toString());                    
                    descarga.setHost(result.getObject("host").toString());
                    descarga.setStatus(result.getObject("status").toString());
                    descarga.setPercent(result.getInt("percent"));
                    descarga.setLastByte(result.getLong("last_byte"));
                    descarga.setSize(result.getLong("size"));

                    descargas.add(descarga);
                }
            }

            return descargas;

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }
}
