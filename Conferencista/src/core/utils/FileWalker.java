/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.utils;

import core.data.Config;
import core.data.ServerFile;
import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author SDI
 */
public class FileWalker {
 
    public ArrayList<ServerFile> list(String path){
        
        ArrayList<ServerFile> dirs = new ArrayList<ServerFile>();
        
        String sharedFolder = GenericUtils.normalize(Config.SHARED_FOLDER);
        path = GenericUtils.normalize(path);
        
        if(!path.startsWith(sharedFolder)){
            path = sharedFolder+path;
        }
        
        File root = new File( path );
        File[] list = root.listFiles();

        if (list == null) return null;

        for ( File f : list ) {
            
            if(f.getName().endsWith("downloads"))continue;
            
            ServerFile fileInfo = new ServerFile();
            
            fileInfo.setIsFile(f.isFile());
            fileInfo.setName(f.getName());
            fileInfo.setFullName(f.getAbsolutePath().replace(sharedFolder,""));
            fileInfo.setHost(Config.YOUR_ADDR);
            dirs.add(fileInfo);
            
        }
        
        
//        ServerFile files [] = new ServerFile[dirs.size()];
//        files = dirs.toArray(files);
        
        
        return dirs;
    }
    
    public void walk( String path ) {

        File root = new File( path );
        File[] list = root.listFiles();

        if (list == null) return;

        for ( File f : list ) {
            if ( f.isDirectory() ) {
                walk( f.getAbsolutePath() );
                System.out.println( "Dir:" + f.getAbsoluteFile() );
            }
            else {
                System.out.println( "File:" + f.getAbsoluteFile() );
            }
        }
    }
      
}
