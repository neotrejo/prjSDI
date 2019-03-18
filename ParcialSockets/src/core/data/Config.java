/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.data;

/**
 *
 * @author Luis
 */
public class Config {
    public static String LEADER_ADDR = "";
    public static String YOUR_ADDR = "";
    public static String USER_ID = "";
    public static String SHARED_FOLDER = "";
    public static boolean IS_LEADER = true;
    public static boolean YOUR_IP_ASSIGNED = false;
    public static String  GROUP_IP           = "230.1.1.1";
    public static int     GROUP_PORT         = 8888;
    public static int     SERVER_PORT        = 2509;
    
    public static String  PROTOCOL = "http://";
    public static String  WS_GET_SHAREDF  = "/fileserver/fileserver/get_shared_folder";
    public static String  WS_SET_SHAREDF  = "/fileserver/fileserver/set_shared_folder";
    public static String  WS_ROOT  = "/fileserver/fileserver/get_root";
    public static String  WS_LIST  = "/fileserver/fileserver/get_files";
    public static String  WS_DOWNLOAD  = "/fileserver/fileserver/download_file";
    
}
