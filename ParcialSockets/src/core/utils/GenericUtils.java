/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.utils;

import core.data.ConfigModel;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Luis
 */
public class GenericUtils {
    
    
    public static String getHostname(){
        try {
            InetAddress addr;
            addr = InetAddress.getLocalHost();
            String hostname = addr.getHostName();
            return hostname;
        } catch (UnknownHostException ex) {
            System.out.println("Hostname can not be resolved");
            return "Default";
        }
    }
    
    public static String getMyIP(){
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException ex) {
            Logger.getLogger(GenericUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    public static void writeConfig(ConfigModel cm){
        try{
            FileOutputStream fileOut = new FileOutputStream(".config");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(cm);
            out.close();
            fileOut.close();
         }catch(Exception ex){ex.printStackTrace();}   
    }
    
    public static ConfigModel readData() {
        
       try {
         
        FileInputStream fileIn = new FileInputStream(".config");
        ObjectInputStream in = new ObjectInputStream(fileIn);
        ConfigModel config = (ConfigModel) in.readObject();
        in.close();
        fileIn.close();
        
        return config;
         
      }catch(FileNotFoundException i) {
          
         ConfigModel cm = null;
          
          try{
            cm = new ConfigModel();
            FileOutputStream fileOut = new FileOutputStream(".config");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(cm);
            out.close();
            fileOut.close();
          }catch(Exception ex){ex.printStackTrace();}         
         
          return cm;
         
      }catch(Exception c) {
         c.printStackTrace();
         return null;
      }

    }
    
    
    public static String normalize(String path){
        if(path.charAt(path.length()-1)!=File.separatorChar){
            path += File.separator;
        }
        return path;
    }


}
