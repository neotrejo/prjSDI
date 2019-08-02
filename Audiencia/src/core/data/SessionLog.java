/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.data;

/**
 *
 * @author Diana
 */
public class SessionLog {
    private String id;
    private String date;
    private String startTime;
    private String duration;
    private String sessionId;
    
    public SessionLog(){
        
    }

    public SessionLog(String id, String date, String startTime, String duration, String sessionId) {
        this.id = id;
        this.date = date;
        this.startTime = startTime;
        this.duration = duration;
        this.sessionId = sessionId;
    }

    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
