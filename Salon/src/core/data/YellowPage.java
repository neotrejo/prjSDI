/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.data;

/**
 *
 * @author Diana
 */
public class YellowPage {
    private String id;
    private String hostname;
    private String name;
    private String typeServiceId;
    
    public YellowPage(){
        
    }

    public YellowPage(String id, String hostname, String name, String typeServiceId) {
        this.id = id;
        this.hostname = hostname;
        this.name = name;
        this.typeServiceId = typeServiceId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTypeServiceId() {
        return typeServiceId;
    }

    public void setTypeServiceId(String typeServiceId) {
        this.typeServiceId = typeServiceId;
    }
    
    
    
}
