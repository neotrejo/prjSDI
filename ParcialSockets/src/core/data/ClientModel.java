/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.data;

/**
 *
 * @author Luis
 */
public class ClientModel {
    
    private String address;
    private String name;
    private boolean isLeader;
    private String index;
    private String action;
    
    public ClientModel(){
        
    }
    
    public ClientModel(String label,boolean isLeader){
        this.name = label;
        this.isLeader = isLeader;
    }
    
    public ClientModel(String label,String address,boolean isLeader,String index){
        this.name = label;
        this.isLeader = isLeader;
        this.address = address;
        this.index = index;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param label the name to set
     */
    public void setName(String label) {
        this.name = label;
    }

    /**
     * @return the isLeader
     */
    public boolean isIsLeader() {
        return isLeader;
    }

    /**
     * @param isLeader the isLeader to set
     */
    public void setIsLeader(boolean isLeader) {
        this.isLeader = isLeader;
    }

    @Override
    public String toString() {       
        return name+" : "+address;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the index
     */
    public String getIndex() {
        return index;
    }

    /**
     * @param index the index to set
     */
    public void setIndex(String index) {
        this.index = index;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
    
    
}
