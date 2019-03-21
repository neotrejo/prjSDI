/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.connections.sockets;

import core.crypt.CryptCipher;
import core.data.Config;
import core.db.dao.DAOArchivo;
import core.db.dao.DAODescarga;
import core.main.listener.DownloadListener;
import core.main.listener.GenericListener;
import core.utils.GenericUtils;
import core.utils.JSONUtils;
import core.utils.MyLogger;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Luis
 */
public class FileDownloader extends Thread {

    private BufferedReader in;
    private PrintWriter out;
    private DownloadListener listener;
    private GenericListener gListener;
    private int BUFFER_SIZE = 8192;
    private DAODescarga daoDescarga;
    private String address;
    private String path;
    private String data;
    private long startByte;
    private boolean paused;
    private final Object lock;
    private boolean isNew;
    private int fId;
    
    public FileDownloader(DownloadListener listener, GenericListener gListener, DAODescarga daoDescarga, PrintWriter out, String address, String path, String data, long startByte,boolean isNew,int fId) {
        this.listener = listener;
        this.gListener = gListener;
        this.daoDescarga = daoDescarga;
        this.out = out;
        this.address = address;
        this.path = path;
        this.data = data;
        this.startByte = startByte;
        this.lock = new Object();
        this.isNew = isNew;
        this.fId = fId;
    }

    public synchronized void pauseOrContinue() {
        if (paused) {
            paused = false;
            synchronized (lock) {
                lock.notify();
            }
        } else {
            paused = true;
        }

    }

    @Override
    public void run() {

        try {
            
            DAOArchivo daoArchivo = new DAOArchivo();
            // Verifica que el archivo no exista, si existe le cambia el nombre
            File f = new File(path);
            String fname = f.getName();

            String downloadPath = GenericUtils.normalize(Config.SHARED_FOLDER) + "downloads/" + fname;

            File destFile = new File(downloadPath);

            if (!destFile.exists() && startByte == 0) {

            }else if(!isNew){
            
            } else {

                String fext = GenericUtils.getFileExtension(f);
                String dot = fext.isEmpty() ? "" : ".";
                String nName = f.getName().replace(fext, "") + "(" + System.currentTimeMillis() + ")" + dot + fext;
                fname = nName;

                downloadPath = GenericUtils.normalize(Config.SHARED_FOLDER) + "downloads/" + fname;

            }

            FileOutputStream fout = new FileOutputStream(downloadPath, true);

            //------------------------ 
            // Solicita el tam del archivo
            Socket socket = new Socket();
            
            socket.connect(new InetSocketAddress(address, Config.SERVER_PORT),5000);

            out = new PrintWriter(socket.getOutputStream(), true);
            InputStream in = socket.getInputStream();

            String jsonEnc = CryptCipher.encrypt(JSONUtils.getDownloadJSON(path, startByte));
            out.println(jsonEnc);

            InputStreamReader ir = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(ir);
            String size = br.readLine();

            System.out.println("size: " + size);

            //--------------------------
            byte[] bytes = new byte[BUFFER_SIZE];

            int count;
            long remain = 0; //Long.parseLong(size);
            int percent = 0;

            int fId = this.fId;
            
            if(isNew){
                fId = daoDescarga.insertarDescarga(fname, downloadPath, address, 0, "Descargando", 0, Long.parseLong(size),path);
                listener.downloadStart(fId,fname, downloadPath, address, Long.parseLong(size), path,this);
            }else{
                remain = startByte;
                percent = (int) (100*(remain*1.0f/Long.parseLong(size)*1.0f));
            }
            
            gListener.startEvent(this, "", 0);

            while ((count = in.read(bytes)) > 0) {
                fout.write(bytes, 0, count);
                remain += count;
                percent = (int) (100*(remain*1.0f/Long.parseLong(size)*1.0f));
                listener.updatePercent(fId,fname, percent,remain);
                daoDescarga.updateDescarga(fId, percent, remain);
                synchronized (lock) {
                    while(paused){
                        lock.wait();
                    }
                        
                }
           
            }

            String status = "Incompleta";
            
            if(remain >= Long.parseLong(size)){
                status = "Completada";
            }

            daoDescarga.updateDescarga(fId, percent, status, remain);  
            
            if(remain >= Long.parseLong(size)){
                daoArchivo.updateFileSize(fname, downloadPath, Long.parseLong(size));
                listener.downloadEnd(fId,fname);
            }else{
                listener.downloadFail(fId, size);
            }
                        
            
            
            
            gListener.endEvent(this, "", 0);

            

            fout.flush();
            fout.close();

            ir.close();
            br.close();

            out.close();
            in.close();
            socket.close();
            
            System.out.println(Long.parseLong(size) + "," + remain);

        }catch(SocketTimeoutException ex){
            listener.hostOffline("",0);
        }catch(ConnectException ex){
            listener.hostOffline("",0);
        }catch (Exception ex) {
            System.out.println(ex);
            Logger.getLogger(RequestSocket.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
