/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.data;

import core.controller.MainController;

/**
 *
 * @author Diana
 */
public class ModelAgent {

    private String type; // CONFERENCISTA, SALON, AUDIENCIA
    private String pathRootFolder;
    private String name;
    private String ip;
    private String port;
    private Classroom classroom;
    private User user;
    private YellowPage yPage;

    public ModelAgent() {
    }

    public ModelAgent(String type, String pathRootFolder, String name, String ip, String port) {
        this.type = type;
        this.pathRootFolder = pathRootFolder;
        this.name = name;
        this.ip = ip;
        this.port = port;
    }

    public void setConfigAgent(String agentType, String agentname) {
        
        switch (agentType) {
            case "CONFERENCISTA":
                type = YellowPage.CONFERENCISTA;
                break;
            case "AUDIENCIA":
                type = YellowPage.AUDIENCIA;
                break;
            default:
                type = YellowPage.SALON;
                break;
        }
        
        if (type.equals(YellowPage.CONFERENCISTA) || type.equals(YellowPage.AUDIENCIA)) {
            user = MainController.existUserName(agentname);
            pathRootFolder = user.getSharedfolder();
            name = user.getUsername();
            ip = user.getLocation();
            port = user.getPort();
        } else {
            classroom = MainController.existClassroomHostName(agentname);
            pathRootFolder = classroom.getRootFolder();
            name = classroom.getHostname();
            ip = classroom.getLocation();
            port = classroom.getPort();
        }
        yPage = MainController.existsServiceYP(agentname, type);
    }

    public Classroom getClassroom() {
        return classroom;
    }

    public void setClassroom(Classroom classroom) {
        this.classroom = classroom;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public YellowPage getyPage() {
        return yPage;
    }

    public void setyPage(YellowPage yPage) {
        this.yPage = yPage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPathRootFolder() {
        return pathRootFolder;
    }

    public void setPathRootFolder(String pathRootFolder) {
        this.pathRootFolder = pathRootFolder;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

}
