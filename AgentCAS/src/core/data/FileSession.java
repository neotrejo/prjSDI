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
public class FileSession {
    private String id;
    private String sessionId;
    private String fileId;
    private String deleted;
    
     public FileSession() {
    }

    public FileSession(String id, String sessionId, String fileId, String deleted) {
        this.id = id;
        this.sessionId = sessionId;
        this.fileId = fileId;
        this.deleted = deleted;
    }
     

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String file_id) {
        this.fileId = file_id;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String session_id) {
        this.sessionId = session_id;
    }
    
    public String getDeleted() {
        return sessionId;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }
    
}
