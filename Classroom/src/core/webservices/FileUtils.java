/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.webservices;

import java.io.File;

/**
 *
 * @author SDI
 */
public class FileUtils {

    public static String getFileExtension(String fileName) {
        String extension =  fileName.substring(fileName.lastIndexOf("."));
        return extension;
    }
    
    public static void createPath(String path){
        File files = new File(path);
        if (!files.exists()) {
            if (files.mkdirs()) {
                System.out.println("Multiple directories are created! -" + path);
            } else {
                System.out.println("Failed to create multiple directories!");
            }
        }
    }
}
