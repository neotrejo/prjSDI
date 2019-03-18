/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.utils;

import core.data.ServerFile;
import core.data.Usuario;
import java.util.ArrayList;
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
public class JSONUtils {
    
    
    public static ServerFile[] getFilesFromJSON(String jsonString){
        
        try {
            
            JSONParser parser = new JSONParser();
            Object obj=parser.parse(jsonString);
            JSONArray array=(JSONArray)obj;
            
            ServerFile files[] = new ServerFile[array.size()];
            
            for (int i = 0; i < array.size(); i++) {
                
                JSONObject job = (JSONObject)array.get(i);
                
                files[i] = new ServerFile();
                files[i].setIsFile((boolean) job.get("is_file"));
                files[i].setName((String) job.get("name"));
                files[i].setFullName((String) job.get("path"));
                
            }
            
            return files;
            
        } catch (ParseException ex) {
            Logger.getLogger(JSONUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    public static String getJoinJSON(String name){
        
        JSONObject main = new JSONObject();
        main.put("action", "join");
        main.put("name", name);
        
        return main.toJSONString();
    }
    
    public static String getDownloadJSON(String path,long start){
        
        JSONObject main = new JSONObject();
        main.put("action","download");
        main.put("path", path);
        main.put("start", start);
        return main.toJSONString();
    }
    
    public static String getRootJSON(){
        
        JSONObject main = new JSONObject();
        main.put("action","get_root");
        return main.toJSONString();
    }
    
     public static String getSubdirJSON(String path){
        
        JSONObject main = new JSONObject();
        main.put("action","get_files");
        main.put("path", path);
        return main.toJSONString();
    }
    
    public static String getJoinJSON(String name,int count){
        
        JSONObject main = new JSONObject();
        main.put("action", "join");
        main.put("name", name);
        main.put("retry",count);
        
        return main.toJSONString();
    }
    
    public static Usuario getUsuarioFromJson(String jsonString,String ip){
        try {
            
            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject)parser.parse(jsonString);
            
            Usuario usuario = new Usuario();
            usuario.setAction((String) obj.get("action"));
            usuario.setNombre((String) obj.get("name"));
            usuario.setAddress(ip);
            
            return usuario;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
    
    public static String getSharedFolderFromJson(String jsonString){
        try {
            
            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject)parser.parse(jsonString);

            return (String) obj.get("shared_folder");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    
    public static String getDirAsJSON(String path){
        
        FileWalker fw = new FileWalker();
        ServerFile[] list = fw.list(path);
        
        JSONArray files = new JSONArray();
        
        for (ServerFile info : list) {
            JSONObject file = new JSONObject();
            file.put("name", info.getName());
            file.put("path",info.getFullName());
            file.put("is_file",info.isIsFile());
            files.add(file);
        }
        
        return files.toJSONString();
    }
    
}
