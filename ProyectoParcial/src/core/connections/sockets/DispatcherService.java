/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.connections.sockets;

import core.data.Archivo;
import core.data.Config;
import core.main.Buscar;
import core.utils.GenericUtils;
import core.utils.JSONUtils;
import core.utils.MyLogger;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Luis
 */
public class DispatcherService {
    
    public static String dispatchService(String jsonString,String address){
        try {
            
            String response = null;
            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject)parser.parse(jsonString);           
            String action = (String) obj.get("action");
            
            switch(action){
                case "search_results":
                    ArrayList<Archivo> results = JSONUtils.getArchivosFromJSON(jsonString);
                    Buscar.getInstance().addResults(results, address);
                    break;
                case "get_root":
                    response = JSONUtils.getDirAsJSON(Config.SHARED_FOLDER);
                    break;
                case "get_files":
                    String subdir = (String) obj.get("path");
                    response = JSONUtils.getDirAsJSON(GenericUtils.normalize(Config.SHARED_FOLDER)+subdir);
                    break;
                case "download":
                    response = "download";
                    break;
            }
                        
            return response;
            
        } catch (ParseException ex) {
            Logger.getLogger(DispatcherService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
}
