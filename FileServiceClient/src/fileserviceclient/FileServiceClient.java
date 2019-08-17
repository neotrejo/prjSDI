/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fileserviceclient;

import core.fileserver.FileServer;
import core.fileserver.Host;
import core.queue.EventQueueNotificationServer;
import core.queue.EventQueueServer;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Diana
 */
public class FileServiceClient {

    /**
     * @param args the command line arguments
     */
    public static Host readConfig() {
        String line;
        Host host = new Host();
        try {
            BufferedReader br = new BufferedReader(new FileReader("ConfigHost.txt"));
            line = br.readLine();
            host.setAddress(line.split("=")[1].trim());
            host.setPort(Integer.parseInt(line.split("=")[2].trim()));
            System.out.println(line);
            return host;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileServiceClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FileServiceClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(FileServiceClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static void main(String[] args) {
        // TODO code application logic here
        Host host = readConfig();
        try{
        EventQueueServer server = new EventQueueServer();
        EventQueueNotificationServer nserver = new EventQueueNotificationServer(host.getPort());
        FileServer fileServer = new FileServer();

        server.start();
//        nserver.start();
//        fileServer.start();
        }catch(Exception ex){
            System.out.println(ex.getLocalizedMessage());
        }
    }

}
