/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.connections.multicast;

import core.crypt.CryptCipher;
import core.data.ClientList;
import core.data.ClientModel;
import core.data.Config;
import core.data.JSONAssembler;
import core.services.SearchService;
import core.utils.JSONUtils;
import core.utils.MyLogger;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JCheckBox;

/**
 *
 * @author SDI
 */
public class MulticastServer extends Thread {

    private int RETRY_TIME = 1000;
    public static final int BUFFER_SIZE = 1024;
    private String myAddress = Config.YOUR_ADDR;
    private String groupId = Config.GROUP_IP;
    private int port = Config.GROUP_PORT;
    private MulticastListener listener;
    private SearchService searchService;

    public MulticastServer() {
        searchService = new SearchService();
    }

    public void setMulticastListener(MulticastListener listener) {
        this.listener = listener;
    }

    public void sendMulticast(String data) {
        sendToGroup(groupId, port, data);
    }

    public void sendToGroup(String groupId, int port, String rawData) {
        
        

        int reintentos = 1;

        while (reintentos > 0) {

            try {
                String data = CryptCipher.encrypt(rawData);
                InetAddress addr = InetAddress.getByName(groupId);

                try (DatagramSocket serverSocket = new DatagramSocket()) {

                    DatagramPacket msgPacket = new DatagramPacket(data.getBytes(), data.getBytes().length, addr, port);
                    serverSocket.send(msgPacket);
                    System.out.println("Multicast enviado");

                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                //Thread.sleep(RETRY_TIME);
            } catch (Exception ex) {
                Logger.getLogger(MulticastServer.class.getName()).log(Level.SEVERE, null, ex);
            }

            reintentos--;
        }

    }

    @Override
    public void run() {

        try {

            InetAddress address = InetAddress.getByName(getGroupId());
            byte[] buf = new byte[BUFFER_SIZE];

            try (MulticastSocket clientSocket = new MulticastSocket(getPort())) {

                clientSocket.joinGroup(address);

                MyLogger.log("Escuchando Multicast...");
                while (true) {
                    buf = new byte[BUFFER_SIZE];
                    DatagramPacket msgPacket = new DatagramPacket(buf, buf.length);
                    clientSocket.receive(msgPacket);

                    String packetIp = msgPacket.getAddress().getHostAddress();

                    if (!packetIp.equals(myAddress)) { //El mensaje debe ser de un host diferente

                        String data = new String(buf, 0, buf.length);
                        data = data.trim();
                        
                        try{
                                data = CryptCipher.decrypt(data);
                                ClientModel usuario = JSONAssembler.getClientFromJSON(data, packetIp);

                                if (usuario.getAction().equals("join")) {

                                    if (ClientList.add(usuario.getAddress(), usuario)) {

                                        if (listener != null) {
                                            listener.updateList();
                                            listener.connectedUser(usuario);
                                        }
                                    }

                                } else if (usuario.getAction().equals("search")) {
                                    String keyword = usuario.getName();
                                    boolean flag = usuario.getFlag();
                                    boolean pdf = usuario.getPdf();
                                    boolean gif = usuario.getGif();
                                    boolean jpeg = usuario.getJpeg();
                                    String results = searchService.search(keyword, flag, pdf, gif, jpeg);
                                    String crypResults = CryptCipher.encrypt(results);
                                    searchService.sendSearchResults(packetIp, crypResults);
                                }
                                
                                
                        }catch(Exception ex){

                        }
                        
                        
                    }

                }

            } catch (SocketTimeoutException ex) {
                MyLogger.log("No recibio respuesta, soy el primero en la red");
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        } catch (UnknownHostException ex) {
            Logger.getLogger(MulticastServer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /*
    * SETTER AND GETTER
     */
    /**
     * @return the groupId
     */
    public String getGroupId() {
        return groupId;
    }

    /**
     * @param groupId the groupId to set
     */
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    /**
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * @param port the port to set
     */
    public void setPort(int port) {
        this.port = port;
    }

}
