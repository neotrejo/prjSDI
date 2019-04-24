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
 * @author SDI
 */
public class DAOTransferencias {

    private SQLiteConnection connection;

    public DAOTransferencias() {
        connection = SQLiteConnection.getInstance();
    }

    public int insertarTransferencia(String name, String path, String host, double percent, String status, long lastByte, long size) {

        Map<String, String> params = new LinkedHashMap<>();
        params.put("name", name);
        params.put("path", path);
        params.put("host", host);
        params.put("percent", String.valueOf(percent));
        params.put("status", status);
        params.put("last_byte", String.valueOf(lastByte));
        params.put("size", String.valueOf(size));

        return connection.insert("transfers", params);
    }

    public void updateTransferencia(int id, double percent, String status, long lastByte) {

        Map<String, String> params = new LinkedHashMap<>();

        params.put("percent", String.valueOf(percent));
        params.put("status", status);
        params.put("last_byte", String.valueOf(lastByte));

        connection.update("transfers", params, "id="+id);
    }
    
    public void borrarTransferencia(String name, String path){
        connection.execute("DELETE FROM transfers WHERE name=\""+name+"\" and path=\""+path+"\"");
    }

    public void borrarTransferencia(){
        connection.execute("DELETE FROM transfers");
    }
        
    public ArrayList<Descarga> getTransferencias() {

        try {
            ArrayList<Descarga> descargas = new ArrayList<>();
            String query = "SELECT * FROM transfers";
            ResultSet result = connection.select(query);

            if (result != null) {
                while (result.next()) {

                    Descarga descarga = new Descarga();
                    
                    descarga.setName(result.getObject("name").toString());
                    descarga.setPath(result.getObject("path").toString());
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
