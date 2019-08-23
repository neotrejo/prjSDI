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
public class Email {
    private  String Username;
    private  String PassWord;
    private String Mensage;
    private String To;
    private String Subject;

    public Email() {
    }

    public Email(String Username, String PassWord, String Mensage, String To, String Subject) {
        this.Username = Username;
        this.PassWord = PassWord;
        this.Mensage = Mensage;
        this.To = To;
        this.Subject = Subject;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String Username) {
        this.Username = Username;
    }

    public String getPassWord() {
        return PassWord;
    }

    public void setPassWord(String PassWord) {
        this.PassWord = PassWord;
    }

    public String getMensage() {
        return Mensage;
    }

    public void setMensage(String Mensage) {
        this.Mensage = Mensage;
    }

    public String getTo() {
        return To;
    }

    public void setTo(String To) {
        this.To = To;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String Subject) {
        this.Subject = Subject;
    }
    

    
    
    
}
