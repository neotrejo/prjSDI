/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.data;

import core.utils.FileWalker;
import core.utils.MyLogger;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Luis
 */
public class JSONAssembler {
    
    
    public static ClientModel getClientFromJSON(String jsonString,String ip){
        try {
            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject)parser.parse(jsonString);
            
            ClientModel usuario = new ClientModel();
            usuario.setAction((String) obj.get("action"));
            usuario.setName((String) obj.get("name"));
            usuario.setAddress(ip);
            
            return usuario;
            
        } catch (ParseException ex) {
            Logger.getLogger(JSONAssembler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    public static String getJSONDownloadRequest(String path){
        JSONObject main = new JSONObject();
        main.put("action", "download");
        main.put("file",path);
        return main.toJSONString();
    }
    
    public static String getJSONSubDirRequest(String path){
        JSONObject main = new JSONObject();
        main.put("action", "get_resource");
        main.put("path",path);
        return main.toJSONString();
    }
        
    public static String getJSONRootDirRequest(){
        JSONObject main = new JSONObject();
        main.put("action", "get_root_content");
        return main.toJSONString();
    }
    
    public static String getJSONAnnounce(String label){
        
        JSONObject main = new JSONObject();
        main.put("action", "join");
        main.put("label", label);
        return main.toJSONString();
    }
    
    public static String getJSONAnnounceLeader(){
        
        JSONObject main = new JSONObject();
        main.put("action", "set_leader");
        return main.toJSONString();
    }
    
    public static String getJSONLeave(){
        
        JSONObject main = new JSONObject();
        main.put("action", "leave");
        return main.toJSONString();
    }
    
    public static String getDirAsJSON(String path,String label,boolean isRoot){
        
        FileWalker fw = new FileWalker();
//        ArrayList<String[]> list = fw.list(path);
        
        JSONObject main = new JSONObject();
        JSONArray dirs = new JSONArray();
        JSONArray type = new JSONArray();
        
//        for (String info[] : list) {
//            dirs.add(info[0]);
//            type.add(info[1]);
//        }
        
        main.put("files", dirs);
        main.put("types", type);
        main.put("label", label);
        main.put("isRoot", isRoot);
        
        return main.toJSONString();
    }
    
    public static String getJSONString(LinkedHashMap<String,ClientModel> clients,String clientAddr){
        
        JSONObject main = new JSONObject();
        JSONArray table = new JSONArray();
        
        Set<String> keys = clients.keySet();
        
        for(String key: keys){
            
            JSONObject cl = new JSONObject();
            ClientModel clm = clients.get(key);
            cl.put("address", key);
            cl.put("isLeader",clm.isIsLeader());
            cl.put("label", clm.getName());
            cl.put("index", clm.getIndex());
            table.add(cl);
            
        }
        
        main.put("action", "set_table");
        main.put("table", table);
        main.put("label", Config.USER_ID);
        main.put("your_ip",clientAddr);
        
        MyLogger.log(main.toJSONString());
        
        return main.toJSONString();
    }
    
}
