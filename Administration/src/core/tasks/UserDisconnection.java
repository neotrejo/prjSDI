/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.tasks;


import core.connections.multicast.MulticastListener;
import core.data.ClientList;
import core.data.ClientModel;
import core.data.Config;
import core.data.Usuario;
import core.utils.MyLogger;
import java.util.ArrayList;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;


/**
 *
 * @author Luis
 */
public class UserDisconnection extends TimerTask{
    
    private Timer timer;
    private long delay = 1000*2;
    private ArrayList<MulticastListener> listener;
    private static UserDisconnection instance = null;
    
    private UserDisconnection(){
        timer = new Timer();
        listener = new ArrayList<>();
    }
    
    public static UserDisconnection getInstance(){
        if(instance == null){
            instance = new UserDisconnection();
        }
        return instance;
    }
    
    public void register(MulticastListener object){
        listener.add(object);
    }
    
    public void unregister(MulticastListener object){
        listener.remove(object);
    }
    
    public void start(){
        timer.scheduleAtFixedRate(this, 0,delay);
    }

    @Override
    public void run() {
        
        try{
        Set<String> keys = ClientList.clients.keySet();
        long ct = System.currentTimeMillis();
        for(String key: keys){
            ClientModel cm = ClientList.clients.get(key);
            if((ct-cm.getTimeStamp()) > Config.DISCONNECTION_TIME){    
               ClientList.remove(key);
               for(MulticastListener l:listener){
                   l.updateList();
                   l.disconnectedUser(cm);
               }
            }
        }
        }catch(Exception ex){MyLogger.log("Ups");}
        
    }
    
}
