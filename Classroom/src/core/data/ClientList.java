/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.data;

import core.utils.MyLogger;
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
public class ClientList {
    
        public static LinkedHashMap<String,ClientModel> clients = new LinkedHashMap<>();  
        
        public static boolean add(String key,ClientModel client){
            
            if(!clients.containsKey(key)){
                client.setTimeStamp(System.currentTimeMillis());
                clients.put(key, client);
                return true;
            }else{
                client.setTimeStamp(System.currentTimeMillis());
                clients.put(key, client);
                return false;
            }
        }
        
        public static void remove(String key){
            clients.remove(key);
        }
        
        public static String nextKey(){
            return clients.entrySet().iterator().next().getKey();       
        }
        
        public static void update(String jsonString){
            
            try {
                JSONParser parser = new JSONParser();
                JSONObject obj = (JSONObject)parser.parse(jsonString);
                JSONArray array = (JSONArray) obj.get("table");
                String label = obj.get("label").toString();
                
                clients.clear();
                
                clients.put(Config.LEADER_ADDR,new ClientModel(label,Config.LEADER_ADDR,true,""));
                
                for (int i = 0; i < array.size(); i++) {
                    JSONObject o = (JSONObject) array.get(i);
                    ClientModel cm = new ClientModel((String)o.get("label"),(String)o.get("address"),(Boolean)o.get("isLeader"),(String)o.get("index"));
                    clients.put(cm.getAddress(), cm);
                }
                
                print();
                
            } catch (ParseException ex) {
                Logger.getLogger(ClientList.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        public static void print(){
            
            Set<String> keys = clients.keySet();

            for(String key: keys){
                 ClientModel cm = clients.get(key);
                 MyLogger.log("["+key+"]: "+cm.getName());
            }
        }
        
}
