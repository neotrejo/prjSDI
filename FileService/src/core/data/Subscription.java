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
public class Subscription {
    private String id;
    private String deleted;
    private String userId;
    private String courseId;
    private String professorName;
    private String courseName;
    private String hostProfesor;
    private String portHostProf;

    
    public Subscription(){
        
    }

    public Subscription(String id, String deleted, String userId, String courseId) {
        this.id = id;
        this.deleted = deleted;
        this.userId = userId;
        this.courseId = courseId;
    }
    

    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getProfessorName() {
        return professorName;
    }

    public void setProfessorName(String professorName) {
        this.professorName = professorName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getHostProfesor() {
        return hostProfesor;
    }

    public void setHostProfesor(String hostProfesor) {
        this.hostProfesor = hostProfesor;
    }

    public String getPortHostProf() {
        return portHostProf;
    }

    public void setPortHostProf(String portHostProf) {
        this.portHostProf = portHostProf;
    }
    
    
}
