package models;

import java.net.*;
import java.util.logging.*;

/**
 *
 * @author Cuong Nguyen Ngoc
 */
public class Machine {
    
    public static String getMacAddress(){
        StringBuilder sb = null;
        try {
            InetAddress ip = InetAddress.getLocalHost();
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            
            byte[] mac = network.getHardwareAddress();
            
            sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {		
                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
            }
        } catch (UnknownHostException | SocketException ex) {
            Logger.getLogger(Machine.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sb.toString();
    }
}
