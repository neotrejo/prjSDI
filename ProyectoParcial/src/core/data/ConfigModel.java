/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.data;

import java.io.Serializable;

/**
 *
 * @author Luis
 */
public class ConfigModel implements Serializable{
    
    private String hostname;
    private String sharedFolder;
    private String downloadFolder;
    
    public ConfigModel(){
        
    }

    /**
     * @return the hostname
     */
    public String getHostname() {
        return hostname;
    }

    /**
     * @param hostname the hostname to set
     */
    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    /**
     * @return the sharedFolder
     */
    public String getSharedFolder() {
        return sharedFolder;
    }

    /**
     * @param sharedFolder the sharedFolder to set
     */
    public void setSharedFolder(String sharedFolder) {
        this.sharedFolder = sharedFolder;
    }

    /**
     * @return the downloadFolder
     */
    public String getDownloadFolder() {
        return downloadFolder;
    }

    /**
     * @param downloadFolder the downloadFolder to set
     */
    public void setDownloadFolder(String downloadFolder) {
        this.downloadFolder = downloadFolder;
    }
    
}
