/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.connections.rmi.remote;

import core.data.ServerFile;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 *
 * @author SDI
 */
public interface RemoteMachine extends Remote{
    
        public String conectar(String data)throws RemoteException;
        public void setListaUsuarios(LinkedHashMap<String,String> clients)throws RemoteException;
        public LinkedHashMap<String,String> getListaUsuarios()throws RemoteException;
        public ArrayList<ServerFile> list(String path)throws RemoteException;
        public ArrayList<ServerFile> list()throws RemoteException;
        public String existsFile(String filename,long size) throws RemoteException;

        public boolean downloadFile(String name,ClientCallback client) throws RemoteException;
    
}
