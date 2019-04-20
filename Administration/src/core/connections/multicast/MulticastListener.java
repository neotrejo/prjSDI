/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.connections.multicast;

import core.data.ClientModel;

/**
 *
 * @author Luis
 */
public interface MulticastListener {
    public void updateList();
    public void connectedUser(ClientModel usuario);
    public void disconnectedUser(ClientModel usuario);
}
