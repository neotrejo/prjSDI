/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.connections.rmi.remote;

import core.connections.rmi.local.RMImplementation;
import core.data.Config;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import javax.swing.JOptionPane;

/**
 *
 * @author Luis
 */
public class RMIServer {
    
    public static final int PORT = 1099;
    
    
    public static void start(){
        
        System.setProperty("java.rmi.server.hostname",Config.YOUR_ADDR);

               
       if (System.getSecurityManager() == null) {
            System.out.println("Estableciendo Politicas de Seguridad");

            System.setProperty("java.security.policy","security.policy");
        }
       
       try {

            LocateRegistry.createRegistry(PORT);
           
            //RemoteMachine stub =(RemoteMachine)UnicastRemoteObject.exportObject(new RMImplementation(), 0);
            RMImplementation server = new RMImplementation();
            
            Naming.rebind("//localhost:"+PORT+"/FileServer",server);
            
            System.out.println("Servidor Listo...");
        } catch (Exception e) {
            e.printStackTrace();
        }       
    }
    
    
    public static RemoteMachine find(String hostname){

        RemoteMachine objRemoto = null;
        
        if (System.getSecurityManager() == null) {
            System.setProperty("java.security.policy","security.policy");
        }

        try {
            Registry registry = LocateRegistry.getRegistry(hostname);
            objRemoto = (RemoteMachine) Naming.lookup("//"+hostname+":"+PORT+"/FileServer");

        } catch (Exception ex) {
            System.out.println("Error:"+ex.getMessage());
            ex.printStackTrace();
        }
        return objRemoto;
    }
    
}
