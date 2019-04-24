/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.main.listener;

import core.connections.sockets.FileDownloader;

/**
 *
 * @author SDI
 */
public interface DownloadListener {
    
    public void downloadStart(int fId,String file,String path,String host,long size,String hostPath,FileDownloader fileDownloader);
    public void downloadEnd(int fId,String file);
    public void downloadFail(int fId,String file);
    public void updatePercent(int fId,String file,double percent,long bytes);
    public void hostOffline(String file,long size);
    
}
