/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.data;

import java.util.HashMap;
import java.util.Map;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

/**
 *
 * @author Diana
 */
public class MessageACL implements JSONAware {
    
    
    
    private String performative;    //Type of communicative acts 
    private String sender;          //Participant in communication 
    private String receiver;        //Participant in communication
    private String replyTo;         //Participant in communication
    private String content;         //Content of message
    private String language;        //Description of Content 
    private String encoding;        //Description of Content 
    private String ontology;        //Description of Content 
    private String protocol;        //Control of conversation
    private String conversationId;  //Control of conversation
    private String replyWith;       //Control of conversation
    private String inReplyTo;       //Control of conversation
    private String replyBy;         //Control of conversation
    
    // TYPES OF PERFORMATIVES    
    public final String REQUEST ="REQUEST";
    public final String INFORM ="INFORM";
    public final String INFORM_IF ="INFORM_IF";
    public final String AGREE ="AGREE";
    public final String REFUSE ="REFUSE";
    
    // ONTOLOGIES    
    public final String ADD ="ADD";
    public final String UPDATE ="UPDATE";
    public final String DOWNLOAD ="DOWNLOAD";  
    
    
    public MessageACL(JSONObject obj){
       
       this.setContent((String) obj.get("content"));
       this.setConversationId((String) obj.get("conversationId"));
       this.setEncoding((String) obj.get("encoding"));
       this.setInReplyTo((String) obj.get("inReplyTo"));
       this.setLanguage((String) obj.get("language"));
       this.setOntology((String) obj.get("ontology"));
       this.setPerformative((String) obj.get("performative"));
       this.setProtocol((String) obj.get("protocol"));
       this.setReceiver((String) obj.get("receiver"));
       this.setReplyBy((String) obj.get("replyBy"));
       this.setReplyTo((String) obj.get("replyTo"));
       this.setReplyWith((String) obj.get("replyWith"));
       this.setSender((String) obj.get("sender"));
    }

    public MessageACL() {
        content="";
        conversationId="";
        encoding="";
        inReplyTo="";
        language="";
        ontology="";
        performative="";
        protocol="";
        receiver="";
        replyBy="";
        replyTo="";
        replyWith="";
        sender="";
    }
    
    @Override
    public String toJSONString() {
        Map<Object, Object> msgACL = new HashMap<Object, Object>();
        msgACL.put("content", getContent());
        msgACL.put("conversationId", getConversationId());
        msgACL.put("encoding", getEncoding());
        msgACL.put("inReplyTo", getInReplyTo());
        msgACL.put("language", getLanguage());
        msgACL.put("ontology", getOntology());
        msgACL.put("performative", getPerformative());
        msgACL.put("protocol", getProtocol());
        msgACL.put("receiver", getReceiver());
        msgACL.put("replyBy", getReplyBy());
        msgACL.put("replyTo", getReplyTo());
        msgACL.put("replyWith", getReplyWith());
        msgACL.put("sender", getSender());
        return JSONObject.toJSONString(msgACL);
    }

    public String getPerformative() {
        return performative;
    }

    public void setPerformative(String performative) {
        this.performative = performative;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getOntology() {
        return ontology;
    }

    public void setOntology(String ontology) {
        this.ontology = ontology;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getReplyWith() {
        return replyWith;
    }

    public void setReplyWith(String replyWith) {
        this.replyWith = replyWith;
    }

    public String getInReplyTo() {
        return inReplyTo;
    }

    public void setInReplyTo(String inReplyTo) {
        this.inReplyTo = inReplyTo;
    }

    public String getReplyBy() {
        return replyBy;
    }

    public void setReplyBy(String replyBy) {
        this.replyBy = replyBy;
    }
    
    
}
