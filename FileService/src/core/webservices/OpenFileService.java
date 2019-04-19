/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.webservices;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author luismartin
 */
public class OpenFileService {
    
    public static String getFileExtension(String fileName) {
        String extension =  fileName.substring(fileName.lastIndexOf("."));
        return extension;
    }
    
    public static void openFile(String file){
        try {
            String ext = getFileExtension(file);
            
            switch(ext){
                case ".pdf":
                    //Runtime.getRuntime().exec("/usr/bin/atril "+file);
                    break;
                case ".ppt":
                case ".pptx":
                    System.out.println(file);
                    Runtime.getRuntime().exec("/usr/bin/libreoffice --show "+file);
                    
                    break;
            }
        } catch (IOException ex) {
            Logger.getLogger(OpenFileService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
