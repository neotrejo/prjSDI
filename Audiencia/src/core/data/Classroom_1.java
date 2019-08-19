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
public class Classroom_1 {
    private String id;
    private String name;
    private String location;
    private String hostname;
    private String rootFolder;
    private String port;

    public Classroom_1() {

    }

    public Classroom_1(String id, String name, String location, String hostname, String rootFolder) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.hostname = hostname;
        this.rootFolder = rootFolder;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }
    
    public String getRootFolder() {
        return rootFolder;
    }

    public void setRootFolder(String rootFolder) {
        this.rootFolder = rootFolder;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
    
    
    
}
