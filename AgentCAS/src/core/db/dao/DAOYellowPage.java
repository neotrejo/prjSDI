/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.db.dao;

import core.data.YellowPage;
import core.db.rqlite.RQLiteConnection;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import org.json.JSONArray;

/**
 *
 * @author Diana
 */
public class DAOYellowPage {

    private RQLiteConnection connection;

    public DAOYellowPage() {
        connection = RQLiteConnection.getInstance();
    }

    public void insertYellowPage(String name, String hostname, String typeService,String port) {

        Map<String, String> params = new LinkedHashMap<>();
        params.put("name", name);
        params.put("hostname", hostname);
        params.put("typeServiceId", typeService);
        params.put("port", port);

        connection.insert("YellowPage", params);
    }

    public void deleteSubject(int id) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("deleted", String.valueOf(true));
        connection.update("YellowPage", params, "id=\"" + id + "\"");
    }

    public YellowPage findByNameAndType(String name, String typeServiceId) {
        String query = "SELECT * FROM YellowPage "
                + "WHERE name=\"" + name + "\" and typeServiceId=\"" + typeServiceId + "\"";
        return executeQuery(query);
    }

    public YellowPage findByHostname(String hostname, String typeService) {
        String query = "SELECT * FROM YellowPage INNER JOIN TypeService on TypeService.id= YellowPage.typeServiceId  "
                + "WHERE YellowPage.hostname=\"" + hostname + "\" and typeServiceId=\"" + typeService + "\"";
        return executeQuery(query);
    }
    
    public ArrayList<YellowPage> getAllButNot(String TypeService) {
        String query = "SELECT * FROM YellowPage INNER JOIN TypeService on TypeService.id= YellowPage.typeServiceId  "
                + "WHERE YellowPage.typeServiceId!=\"" + TypeService + "\"";
        return executeQueryList(query);
    }

    private YellowPage executeQuery(String query) {
        try {
            JSONArray resul = connection.select(query);
            YellowPage yellowPage = null;
            if (resul != null) {
                JSONArray cols = resul.getJSONObject(0).getJSONArray("columns");
                JSONArray values = resul.getJSONObject(0).getJSONArray("values");
                for (int i = 0; i < values.length(); i++) {
                    JSONArray reg = values.getJSONArray(i);
                    yellowPage = new YellowPage();
                    for (int j = 0; j < cols.length(); j++) {
                        switch (cols.getString(j)) {
                            case "id":
                                yellowPage.setId(reg.get(j).toString());
                                break;
                            case "hostname":
                                yellowPage.setHostname(reg.get(j).toString());
                                break;
                            case "name":
                                yellowPage.setName(reg.get(j).toString());
                                break;
                            case "typeServiceId":
                                yellowPage.setTypeServiceId(reg.get(j).toString());
                                break;
                            case "port":
                                yellowPage.setPort(reg.get(j).toString());
                                break;

                        }
                    }
                    return yellowPage;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private ArrayList<YellowPage> executeQueryList(String query) {
        try {
            ArrayList<YellowPage> yellosPages = new ArrayList<>();
            JSONArray resul = connection.select(query);
            YellowPage yellowPage = null;
            if (resul != null) {
                JSONArray cols = resul.getJSONObject(0).getJSONArray("columns");
                JSONArray values = resul.getJSONObject(0).getJSONArray("values");
                for (int i = 0; i < values.length(); i++) {
                    JSONArray reg = values.getJSONArray(i);
                    yellowPage = new YellowPage();
                    for (int j = 0; j < cols.length(); j++) {
                        switch (cols.getString(j)) {
                            case "id":
                                yellowPage.setId(reg.get(j).toString());
                                break;
                            case "hostname":
                                yellowPage.setHostname(reg.get(j).toString());
                                break;
                            case "name":
                                yellowPage.setName(reg.get(j).toString());
                                break;
                            case "typeServiceId":
                                yellowPage.setTypeServiceId(reg.get(j).toString());
                                break;
                            case "port":
                                yellowPage.setPort(reg.get(j).toString());
                                break;

                        }
                    }
                    yellosPages.add(yellowPage);
                }
            }
            return yellosPages;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
