/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.main;

/**
 *
 * @author Luis
 */
public interface DownloadListener {
    
    public void downloadStart(String file);
    public void downloadEnd(String file);
    
}
