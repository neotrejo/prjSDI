/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.services;

import core.data.Config;
import core.db.dao.DAOArchivo;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author SDI
 */

/*
* Si se agrega un archivo, eliminarlo
* Si se elimina, borrarlo
* Si cambia, No es necesario, ya que se invoca a crear y eliminar
* 
 */
public class DirectoryObserver extends Thread {

    private WatchService watcher;
    private Path dir;
    private DAOArchivo daoArchivo;

    public DirectoryObserver() {
        try {
            daoArchivo = new DAOArchivo();
            watcher = FileSystems.getDefault().newWatchService();
            dir = Paths.get(Config.SHARED_FOLDER);
            registerAll(dir);
            //dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
        } catch (IOException ex) {
            Logger.getLogger(DirectoryObserver.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

        
    private void registerAll(final Path start) throws IOException {
        Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
                    throws IOException {
                dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE);
                return FileVisitResult.CONTINUE;
            }
        }
        );
    }

    @Override
    public void run() {
        while (true) {

            // wait for key to be signaled
            WatchKey key = null;
            try {

                key = watcher.take();

                for (WatchEvent<?> event : key.pollEvents()) {
                    // get event type
                    WatchEvent.Kind<?> kind = event.kind();

                    // get file name
                    @SuppressWarnings("unchecked")
                    WatchEvent<Path> ev = (WatchEvent<Path>) event;
                    Path fileName = ev.context();
                    Path fullPath = dir.resolve((Path) key.watchable());
                    String filePath = fullPath + File.separator + fileName;
                    File mFile = new File(filePath);

                    System.out.println(filePath + " - " + mFile.isDirectory());

                    if (kind == OVERFLOW) {
                        System.out.println("Overflow");
                        continue;
                    } else if (kind == ENTRY_CREATE) {

                        if (mFile.isDirectory()) {                            
                            Path nPath = Paths.get(filePath);
                            nPath.register(watcher, ENTRY_CREATE, ENTRY_DELETE);
                            
                            /*
                            * FALTA REGISTRAR CUANDO SE PEGA UNA CARPETA (sus subcarpetas y archivos)
                            */
                            
                            System.out.println("Registrar: " + fileName);
                        } else {
                            System.out.println("Agregar a la base de datos");
                            //daoArchivo.insertArchivo(mFile.getName(), filePath,fullPath.toString(),mFile.length());
                        }
                        // process create event

                    } else if (kind == ENTRY_DELETE) {
                       
                        if (!mFile.isDirectory()) {  
                            System.out.println("Borrar de la base de datos");
                            //daoArchivo.deleteArchivo(mFile.getName(), filePath);
                        }else{
                            //daoArchivo.deleteDirectory(filePath);
                        }
                        // process delete event
                    } else if (kind == ENTRY_MODIFY) {

                    }

                }

                // Reset the key -- this step is critical if you want to
                // receive further watch events.  If the key is no longer valid,
                // the directory is inaccessible so exit the loop.
                boolean valid = key.reset();
                if (!valid) {
                    System.out.println("invalid");
                }

            } catch (Exception x) {
                x.printStackTrace();
            }

        }
    }

}
