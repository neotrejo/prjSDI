/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.connections.rmi.local;

import core.connections.rmi.remote.ClientCallback;
import core.data.Config;
import core.db.dao.DAOArchivo;
import java.io.File;
import java.io.FileOutputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author SDI
 */
public class RMClientImplementation extends UnicastRemoteObject implements ClientCallback{
    
    public RMClientImplementation() throws RemoteException{
        
    }

    @Override
    public boolean sendData(String filename, byte[] data, int len) throws RemoteException {
      
        try{

            File f=new File(filename);
            f.createNewFile();
            FileOutputStream out=new FileOutputStream(f,true);
            out.write(data,0,len);
            out.flush();
            out.close();
            
            System.out.println("Done writing data...");
                
        }catch(Exception e){
        	e.printStackTrace();
        }
		return true;
    }
    
    
}
