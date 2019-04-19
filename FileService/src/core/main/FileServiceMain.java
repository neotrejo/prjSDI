/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.main;

import core.webservices.NotificationServerRpi;
import core.webservices.QueueConfig;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author luismartin
 */
public class FileServiceMain {

    /**
     * @param args the command line arguments
     */
    
    public static String[] readConfig(){
        try {
            BufferedReader br = new BufferedReader(new FileReader("Config.txt"));
            
            String path = br.readLine().split("=")[1].trim();
            String host = br.readLine().split("=")[1].trim();
            
            System.out.println(path);
            System.out.println(host);
            
            QueueConfig.SHARED_FOLDER = path;
            
            
            return new String[]{path,host};
            
        } catch (Exception ex) {
            Logger.getLogger(FileServiceMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
        
        //OpenFileService.openFile("/home/luismartin/Documentos/Proyectos_LaboratorioComputacion_2015.pptx");
        
        String conf[] = readConfig();
        new NotificationServerRpi(conf[1]).start();
        
        //new NotificationClient().start();
        //new RequestFile("C:/Users/Public/Documents/Logica/04-Semantic2-SF2.pdf","04-Semantic2-SF2.pdf").start();
    }
    
}
