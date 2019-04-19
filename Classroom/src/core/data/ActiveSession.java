/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.data;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Diana
 */
public class ActiveSession {
    private String session_id;
    private String user_id;
    private String date;
    private String startTime;    
    private String subjectName;
    private String fileName;
    private String pathFile;
    private String id;
    
    public ActiveSession(){
        
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public ActiveSession(String session_id, String user_id, String date, String startTime, String subjectName, String fileName, String pathFile) {
        
        this.session_id = session_id;
        this.user_id = user_id;
        this.date = date;
        this.startTime = startTime;
        this.subjectName = subjectName;
        this.fileName = fileName;
        this.pathFile = pathFile;
    }
    
    public String getSessionId() {
        return session_id;
    }

    public void setSessionId(String session_id) {
        this.session_id = session_id;
    }
    public String getUserId() {
        return user_id;
    }

    public void setUserId(String user_id) {
        this.user_id = user_id;
    }
    
    public String getDate() throws ParseException {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateD = (Date) formatter.parse(date);
        SimpleDateFormat newFormat = new SimpleDateFormat("dd-MM-yyyy");
        return newFormat.format(dateD);
    }

    public void setDate(String date) {
        this.date = date;
    }
    
     public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getStartTime() throws ParseException {
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        Date hour = (Date) formatter.parse(this.startTime);
        SimpleDateFormat newFormat = new SimpleDateFormat("HH:mm");
        return newFormat.format(hour);
    }

    public void setStartTime(String startTime) {
        
        this.startTime = startTime;
    }
    
     public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    public String getPathFile() {
        return pathFile;
    }

    public void setPathFile(String pathFile) {
        this.pathFile = pathFile;
    }
    
    
}
