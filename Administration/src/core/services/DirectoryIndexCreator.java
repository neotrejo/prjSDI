/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.services;

import core.data.Config;
import core.db.dao.DAOArchivo;
import java.io.File;

/**
 *
 * @author Luis
 */
public class DirectoryIndexCreator extends Thread{
    
    private DAOArchivo connection;
        
    public DirectoryIndexCreator(){
        
        connection = new DAOArchivo();
        
    }
    
    @Override
    public void run(){
        connection.deleteAll();
        registerAllFilesInDB();
    }
        
    private void registerAllFilesInDB(){
        System.out.println(Config.SHARED_FOLDER);
        walk(Config.SHARED_FOLDER);
    }

    public void walk( String path ) {

        File root = new File( path );
        File[] list = root.listFiles();

        if (list == null) return;

        for ( File f : list ) {
            if ( f.isDirectory() ) {
                walk( f.getAbsolutePath() );
            }
            else {
                connection.insertArchivo(f.getName(),f.getAbsolutePath(),path,f.length());
                //System.out.println( "File:" + f.getAbsoluteFile() );
            }
        }
    }
    
}
