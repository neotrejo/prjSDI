/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.main.listener;

/**
 *
 * @author luismartin
 */
public interface GenericListener {
    
    public void startEvent(Object source,String descripcion,int eventId);
    public void endEvent(Object source,String descripcion,int eventId);
    
}
