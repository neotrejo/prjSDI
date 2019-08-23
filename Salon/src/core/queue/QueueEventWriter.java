/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.queue;


import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author SDI Martin
 */
public class QueueEventWriter {
    
     private String address;
    private int port;

    public QueueEventWriter(String address, int port) {
        this.address = address;
        this.port=port;
    }

    public void writeToQueue(String data) {
        try {
            Socket socket = new Socket(this.address, this.port);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(data);
            out.close();
            socket.close();
        } catch (Exception ex) {
            Logger.getLogger(QueueEventWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
