/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.fileserver;

/**
 *
 * @author Diana
 */
public class Host {
    
    private String address;
    private int port;
    
    public Host(){
        
    }
    
    public Host(String address, int port){
        this.address=address;
        this.port = port;
    }

    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPort(int port) {
        this.port = port;
    }
    
    
}
