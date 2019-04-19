/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.main;

import core.webservices.NotificationServerRpi;

/**
 *
 * @author luismartin
 */
public class RpiClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        //OpenFileService.openFile("/home/luismartin/Documentos/Proyectos_LaboratorioComputacion_2015.pptx");
        
        
        new NotificationServerRpi().start();
        
        //new NotificationClient().start();
        //new RequestFile("C:/Users/Public/Documents/Logica/04-Semantic2-SF2.pdf","04-Semantic2-SF2.pdf").start();
    }
    
}
