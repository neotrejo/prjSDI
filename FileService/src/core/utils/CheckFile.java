/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Diana
 */

public class CheckFile {

    private File file;
    private String path;
    private String nameFile;
    private long lastModify;
    private long size;

    public CheckFile() {

    }

    public CheckFile(String path) {
        this.file = new File(path);
        this.path = path;
        this.nameFile = file.getName();
        this.lastModify = file.lastModified();
        this.size = file.length();
    }

    public void createFile() {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(CheckFile.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public Boolean existsFile() {
        return file.exists();
    }
/**crea archivo sino existe antes de escribir*/
    public void addLine(String line) { 
        FileWriter fw = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            
            fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(line);
            bw.close();

        } catch (IOException ex) {
            Logger.getLogger(CheckFile.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fw.close();
            } catch (IOException ex) {
                Logger.getLogger(CheckFile.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getNameFile() {
        return nameFile;
    }

    public void setNameFile(String nameFile) {
        this.nameFile = nameFile;
    }

    public long getLastModify() {
        return lastModify;
    }

    public void setLastModify(long lastModify) {
        this.lastModify = lastModify;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

}

