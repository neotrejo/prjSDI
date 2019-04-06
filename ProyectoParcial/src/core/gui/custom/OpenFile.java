/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.gui.custom;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author Diana
 */
public class OpenFile {

    private String nameFile;
    private String pathFile;

    public OpenFile() {
    }

    public OpenFile(String nameFile, String pathFile) {
        this.nameFile = nameFile;
        this.pathFile = pathFile;
    }

    public void setNameFile(String nameFile) {
        this.nameFile = nameFile;
    }

    public String getNameFile() {
        return nameFile;
    }

    public void setPathFile(String pathFile) {
        this.pathFile = pathFile;
    }

    public String getPathFile() {
        return pathFile;
    }

    public void openFile() {
        if (Desktop.isDesktopSupported()) {
            try {
                File myFile = new File(pathFile);
                Desktop.getDesktop().open(myFile);
            } catch (IOException ex) {
                System.out.println((ex));
            }

        }
    }

}
