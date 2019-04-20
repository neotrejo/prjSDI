/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fileserviceclient;

import core.fileserver.FileServer;
import core.queue.EventQueueNotificationServer;
import core.queue.EventQueueServer;

/**
 *
 * @author Diana
 */
public class FileServiceClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        EventQueueServer server = new EventQueueServer();
        EventQueueNotificationServer nserver = new EventQueueNotificationServer();
       FileServer fileServer = new FileServer();
        
        server.start();
        nserver.start();
        fileServer.start();
    }
    
}
