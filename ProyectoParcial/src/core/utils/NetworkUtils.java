/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.utils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 *
 * @author Luis
 */
public class NetworkUtils {
    
        public static String getIpAddress() { 
            try {
                for (Enumeration en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                    NetworkInterface intf = (NetworkInterface) en.nextElement();
                    for (Enumeration enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                        InetAddress inetAddress = (InetAddress) enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()&&inetAddress instanceof Inet4Address) {
                            String ipAddress=inetAddress.getHostAddress().toString();
                            return ipAddress;
                        }
                    }
                }
            } catch (Exception ex) {
                MyLogger.log("Socket exception in GetIP Address of Utilities"+ ex.toString());
            }
            return null; 
    }
}
