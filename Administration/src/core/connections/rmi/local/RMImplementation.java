/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.connections.rmi.local;

import core.data.Config;
import core.connections.rmi.remote.ClientCallback;
import java.rmi.RemoteException;
import core.connections.rmi.remote.RemoteMachine;
import core.data.ServerFile;
import core.db.dao.DAOArchivo;
import core.utils.FileWalker;
import core.utils.GenericUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 *
 * @author SDI
 */
public class RMImplementation extends UnicastRemoteObject implements RemoteMachine{
    
    public static LinkedHashMap<String, String> users;
    
    public RMImplementation() throws RemoteException{
        
    }

    @Override
    public String conectar(String data) throws RemoteException {
        return "Hola";
    }

    @Override
    public void setListaUsuarios(LinkedHashMap<String, String> users)throws RemoteException {
        this.users = users;
    }

    @Override
    public LinkedHashMap<String, String> getListaUsuarios() throws RemoteException{
        return users;
    }

    @Override
    public ArrayList<ServerFile> list(String path)throws RemoteException {
        return new FileWalker().list(path);
    }

    @Override
    public ArrayList<ServerFile> list()throws RemoteException {
        return new FileWalker().list(Config.SHARED_FOLDER);
    }
    
    @Override
    public String existsFile(String filename, long size) throws RemoteException {
        DAOArchivo daoArchivo = new DAOArchivo();
        String sharedFolder = GenericUtils.normalize(Config.SHARED_FOLDER);
        String fileName = daoArchivo.findBySizeAndName(filename, size);
        return fileName.replace(sharedFolder,"");
    }
    
    @Override
    public boolean downloadFile(String name, ClientCallback client) throws RemoteException {

        	 try{
                     
                    if(!name.startsWith(Config.SHARED_FOLDER)){
                        name = Config.SHARED_FOLDER+name;
                    }
                    
                    System.out.println("Download "+name);
                    
                    File f1=new File(name);			 
                    FileInputStream in=new FileInputStream(f1);			 				 
                    byte [] mydata=new byte[1024*1024];						
                    int mylen=in.read(mydata);
                    while(mylen>0){
                            client.sendData(f1.getName(), mydata, mylen);	 
                            mylen=in.read(mydata);				 
                    }
		 }catch(Exception e){
			 e.printStackTrace();
			 
		 }
		
		return true;

    }

    
}
