/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.connections.rmi.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author SDI
 */
public interface ClientCallback extends Remote{
    
    public boolean sendData(String filename, byte[] data, int len) throws RemoteException;
}
