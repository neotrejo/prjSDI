/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.webservices;

import core.db.dao.DAOActiveSession;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author Luis
 */
public class NotificationServerRpi extends Thread {

    private Socket socket;
    private String path = QueueConfig.SHARED_FOLDER;

    public NotificationServerRpi() {
        createServer();
    }

    private void createServer() {
        try {
            socket = new Socket("10.0.5.182",10001);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {

            while (true) {
                
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                String input = br.readLine();
            
            

                if (input != null) {
                    System.out.println("New event: "+input);
                    
                    JSONParser parser = new JSONParser();
                    JSONObject obj = (JSONObject)parser.parse(input);

                    String event = (String)obj.get("event");                    
                    String sessionID = (String)obj.get("session_id");                    
                    String userID = (String) obj.get("user_id");
                    String date = (String) obj.get("date");
                    String startTime = (String)obj.get("startTime");                    
                    String subjectName = (String) obj.get("subjectName");
                    String fileName = (String) obj.get("fileName");
                    String filePath = (String) obj.get("pathFile");
                    
                    fileName = fileName.replace(" ", "_");
                    subjectName = subjectName.replace(" ", "_");
                    
                    
                    FileUtils.createPath(path+userID+"/"+subjectName);
                    
                    System.out.println(event);
                    System.out.println(sessionID);
                    System.out.println(userID);
                    System.out.println(date);
                    System.out.println(startTime);
                    System.out.println(subjectName);
                    System.out.println(fileName);
                    System.out.println(filePath);
                    
                    DAOActiveSession daoAS = new DAOActiveSession();
                    
                    daoAS.insertSession(sessionID, userID, date, startTime, subjectName, fileName, path+userID+"/"+subjectName+"/"+fileName);
                    
                    new RequestFile(filePath,path+userID+"/"+subjectName+"/"+fileName).start();
                    
                    
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
