/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.fileserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author SDI
 */
public class FileServerHandler extends Thread implements Observer{
    
    private Socket socket;
    private JTextArea txtArea;
    public FileServerHandler(Socket socket, JTextArea txtArea){
        this.txtArea= txtArea;
        this.socket = socket;
        
        this.start();
    }
    
    @Override
    public void run(){
        try {
                        
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String input = in.readLine();
            

            if (input != null) {
                System.out.println("File requested :"+input);
                
                ///////////////////////////////////////////////
                    JSONParser parser = new JSONParser();
                    JSONObject obj = (JSONObject) parser.parse(input);

                    String performative = (String) obj.get("performative");
                    String sender = (String) obj.get("sender");
                    String receiver = (String) obj.get("receiver");
                    String reply_to = (String) obj.get("reply-to");
                    String content = (String) obj.get("content");
                    String language = (String) obj.get("language");
                    String encoding = (String) obj.get("encoding");
                    String ontology = (String) obj.get("ontology");
                    String protocol = (String) obj.get("protocol");
                    String conversation_id = (String) obj.get("conversation-id");
                    String reply_with = (String) obj.get("reply-with");
                    String in_reply_to = (String) obj.get("in-reply-to");
                    String reply_by = (String) obj.get("reply-by");
                    
                    setMessage(receiver + " <= " + sender + "   " + performative);
                    
                    //////////////////////////////////////////////////
                if(ontology.equals("DOWNLOAD")){
                OutputStream out = socket.getOutputStream();
                
                String path = input;
                
                RandomAccessFile raf = new RandomAccessFile(path, "r");
                int file_size = (int)raf.length();
                byte buffer[] = new byte[1024];
                
                int len = raf.read(buffer);
                
                while(len > 0){
                    out.write(buffer,0,len);
                    len = raf.read(buffer);
                }
                
                System.out.println("File sent");
                raf.close();
                out.close();
                
                in.close();
                socket.close();
                setMessage(receiver + " => " + sender + "   " + "CONFIRM");
                }
            }

                      
        }catch(Exception ex){
                Logger.getLogger(FileServerHandler.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            try {
                socket.close();
            } catch (IOException ex) {
                Logger.getLogger(FileServerHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {

        try {
            System.out.println("Nuevo evento"+arg.toString());
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void setMessage(String msg) {
        if (txtArea != null) {
            String aux = txtArea.getText();
            txtArea.setText(aux + "\n" + msg);
        }
    }
    
}
