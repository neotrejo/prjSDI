/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.data;

/**
 *
 * @author SDI
 */
public class FileModel {
    
    private String path;
    private boolean isFile;
    private String fullPath;
    private String host;
    
    public FileModel(String path,boolean isFile,String fullPath,String host){
        this.path = path;
        this.isFile = isFile;
        this.fullPath = fullPath;
        this.host = host;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean getIsFile() {
        return isFile;
    }

    public void setIsFile(boolean isFile) {
        this.isFile = isFile;
    }

    @Override
    public String toString() {
        return path;
    }

    public String getFullPath() {
        return fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
    
    
    
}
