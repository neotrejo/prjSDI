/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.utils;

/**
 *
 * @author SDI
 */
public class MyLogger {
    
    public static boolean LOG_ENABLED = true;
    
    public static void log(String text){
        if(LOG_ENABLED)
            System.out.println(text);
    }
    
}
