/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.data;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

/**
 *
 * @author Diana
 */
public class ACLMessage extends GenericJson  {
    public static final int ACCEPT_PROPOSAL = 0;
    public static final int AGREE = 1;
    public static final int CANCEL = 2;
    public static final int CFP = 3;
    public static final int CONFIRM = 4;
    public static final int DISCONFIRM = 5;
    public static final int FAILURE = 6;
    public static final int INFORM = 7;
    public static final int INFORM_IF = 8;
    public static final int INFORM_REF = 9;
    public static final int NOT_UNDERSTOOD = 10;
    public static final int PROPOSE = 11;
    public static final int QUERY_IF = 12;
    public static final int QUERY_REF = 13;
    public static final int REFUSE = 14;
    public static final int REJECT_PROPOSAL = 15;
    public static final int REQUEST = 16;
    public static final int REQUEST_WHEN = 17;
    public static final int REQUEST_WHENEVER = 18;
    public static final int SUBSCRIBE = 19;
    public static final int PROXY = 20;
    public static final int PROPAGATE = 21;
    public static final int UNKNOWN = -1;
    
    @Key ("performative")
    public int performative;
    @Key("sender")
    public String sender;
    @Key ("receiver")
    public String receiver;
    @Key ("reply_to")
    public String reply_to;
    @Key ("content")
    public String content;
    @Key ("language")
    public String language;
    @Key ("encoding")
    public String encoding;
    @Key ("encode")
    public String encode;
    @Key ("ontology")
    public String ontology;
    @Key ("protocol")
    public String protocol;
    @Key ("conversation_id")
    public String conversation_id;
    @Key ("reply_with")
    public String reply_with;
    @Key ("in_reply_to")
    public String in_reply_to;
    @Key ("reply_by")
    public String reply_by ;  
}
