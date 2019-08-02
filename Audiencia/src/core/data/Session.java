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
public class Session {

    private String id;
    private String date;
    private String startTime;
    private String duration;
    private String classroomId;
    private String courseId;
    private String file; // archivo asociado a la sesión

  

    public Session() {
    }

    public Session(String id, String date, String startTime, String duration, String classRoomId, String courseId) {
        this.id = id;
        this.date = date;
        this.startTime = startTime;
        this.duration = duration;
        this.classroomId = classRoomId;
        this.courseId = courseId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getStartTime() throws ParseException {
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        Date hour = (Date) formatter.parse(this.startTime);
        SimpleDateFormat newFormat = new SimpleDateFormat("HH:mm");
        return newFormat.format(hour);
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

      public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getClassroomId() {
        return classroomId;
    }

    public void setClassroomId(String classroomId) {
        this.classroomId = classroomId;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
