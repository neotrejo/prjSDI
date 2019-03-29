/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.data;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author Diana
 */
public class Session {

    private String id;
    private String date;
    private String startTime;
    private String durationHrs;
    private String classRoom_id;
    private String subject_id;
    private String file;

    public Session() {
    }

    public Session(String id, String date, String startTime, String durationHrs, String classRoom_id, String subject_id, String file) {
        this.id = id;
        this.date = date;
        this.startTime = startTime;
        this.durationHrs = durationHrs;
        this.classRoom_id = classRoom_id;
        this.subject_id = subject_id;
        this.file = file;
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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getDurationHrs() {
        return durationHrs;
    }

    public void setDurationHrs(String durationHrs) {
        this.durationHrs = durationHrs;
    }

    public String getClassRoomId() {
        return classRoom_id;
    }

    public void setClassRoomId(String classRoom_id) {
        this.classRoom_id = classRoom_id;
    }

    public String getSubjectId() {
        return subject_id;
    }

    public void setSubjectId(String subject_id) {
        this.subject_id = subject_id;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

}
