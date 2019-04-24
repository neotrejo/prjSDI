/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.connections.multicast;

import core.crypt.CryptCipher;
import core.data.Config;
import core.utils.JSONUtils;
import core.utils.MyLogger;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author SDI
 */
public class MulticastPolling extends TimerTask{
    
    private boolean isOnline = true;
    private Timer timer;
    
    private int pollingTime = 1000*5;
    private int timeout = 5000;
    private static boolean isRunning = false;
    private int count = 0;
    
    public MulticastPolling(){
        timer = new Timer();
    }
    
    public void start(){
        if(!isRunning){
            isRunning = true;
            timer.scheduleAtFixedRate(this, 0, pollingTime);
        }
    }
    
    public void sendToGroup(String groupId, int port, String data){
        
        int reintentos = 1;
        
        while(reintentos > 0){
        
                try {

                    InetAddress addr = InetAddress.getByName(groupId);

                    try (DatagramSocket serverSocket = new DatagramSocket()) {

                            DatagramPacket msgPacket = new DatagramPacket(data.getBytes(),data.getBytes().length, addr, port);
                            
                            serverSocket.send(msgPacket);              

                    } catch (IOException ex) {
                            ex.printStackTrace();
                    }

                } catch (Exception ex) {
                    Logger.getLogger(MulticastServer.class.getName()).log(Level.SEVERE, null, ex);
                }
               
            reintentos--;
        }
        
    }
    
    @Override
    public void run(){    
        try{
            String jsonEncrypted = CryptCipher.encrypt(JSONUtils.getJoinJSON(Config.USER_ID));
            sendToGroup(Config.GROUP_IP, Config.GROUP_PORT,jsonEncrypted);
        }catch(Exception ex){
            ex.printStackTrace();
        }
//        sendToGroup(Config.GROUP_IP, Config.GROUP_PORT, JSONUtils.getJoinJSON(Config.USER_ID));
         
    }
    
}
