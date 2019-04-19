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
public class FilesSession {
    private String id;
    private String session_id;
    private String file_id;
    private String deleted;
    
     public FilesSession() {
    }
     
    public FilesSession(String id, String session_id, String file_id, String deleted) {
        this.id = id;
        this.session_id = session_id;
        this.file_id = file_id;
         this.deleted = deleted;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileId() {
        return file_id;
    }

    public void setFileId(String file_id) {
        this.file_id = file_id;
    }

    public String getSessionId() {
        return session_id;
    }

    public void setSessionId(String session_id) {
        this.session_id = session_id;
    }
    
    public String getDeleted() {
        return session_id;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }
    
}
