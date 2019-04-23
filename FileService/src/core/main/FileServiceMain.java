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
import java.util.ArrayList;
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
    public static ArrayList<String> readConfig() {
        String line;
        ArrayList<String> hosts = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader("ConfigSources.txt"));

            String path = br.readLine().split("=")[1].trim();
            System.out.println(path);
            line = br.readLine();
            while (line != null) {
                hosts.add(line.split("=")[1].trim());
                System.out.println(line);
                line = br.readLine();
            }
            //String host = br.readLine().split("=")[1].trim();

            
            //System.out.println(host);

            QueueConfig.SHARED_FOLDER = path;

            return hosts;

        } catch (Exception ex) {
            Logger.getLogger(FileServiceMain.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public static void main(String[] args) {
        // TODO code application logic here

        //OpenFileService.openFile("/home/luismartin/Documentos/Proyectos_LaboratorioComputacion_2015.pptx");
        int port= 10001;
        ArrayList<String> conf = readConfig();
        for(String host: conf){
            new NotificationServerRpi(host,port).start();
            port++;
        }
        //new NotificationServerRpi(conf[1]).start();

        //new NotificationClient().start();
        //new RequestFile("C:/Users/Public/Documents/Logica/04-Semantic2-SF2.pdf","04-Semantic2-SF2.pdf").start();
    }

}
