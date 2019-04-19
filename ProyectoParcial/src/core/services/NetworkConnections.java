/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.services;

import core.connections.multicast.MulticastListener;
import core.connections.multicast.MulticastPolling;
import core.connections.multicast.MulticastServer;
import core.connections.rmi.remote.RMIServer;
import core.connections.sockets.FileServer2;

/**
 *
 * @author Luis
 */
public class NetworkConnections {
    
    private static NetworkConnections instance;
    
    private MulticastServer multicastServer;
    private MulticastPolling multicastPolling;
    private FileServer2 fileServer;
    private DirectoryObserver directoryObserver;
    private DirectoryIndexCreator directoryIndexCreator;
    
    private boolean running;
    
    private NetworkConnections(){
        
    }
    
    public void start(MulticastListener listener){
        
        if(!isRunning()){
            
            RMIServer.start();
        
            fileServer = new FileServer2();
            fileServer.start();

            multicastServer = new MulticastServer();
            multicastServer.setMulticastListener(listener);
            multicastServer.start();

            multicastPolling = new MulticastPolling();        
            multicastPolling.start();

            directoryIndexCreator = new DirectoryIndexCreator();
            directoryIndexCreator.start();

            directoryObserver = new DirectoryObserver();
            directoryObserver.start();
            
            running = true;
        }
    }
    
    public static NetworkConnections getInstance(){
        if(instance == null){
            instance = new NetworkConnections();
        }
        return instance;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
    
    
}
