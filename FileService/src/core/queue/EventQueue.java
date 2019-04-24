/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.queue;

import java.util.Observable;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 *
 * @author SDI Martin
 */
public class EventQueue extends Observable{
    
    private static BlockingQueue<String> queue = new ArrayBlockingQueue(10);
    private static EventQueue instance = null;
    
    private EventQueue(){}
    
    public static EventQueue getInstance(){
        if(instance == null){
            instance = new EventQueue();
        }
        
        return instance;
    }
    
    
    
    public void addEvent(String event){
        
        queue.add(event);
        
        setChanged();
        notifyObservers(event);
    }
    
}
