package server;

import views.Terminal;
import java.io.IOException;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cuong nguyen ngoc
 */
public class ThreadServer implements Runnable{
    
    private ServerSocket server;
    private boolean keepWaiting = true;
    private Socket socket;
    private Terminal terminal;
    
    
    public ThreadServer(Terminal terminal){
        
        this.terminal = terminal;
        try {
            server = new ServerSocket(Integer.parseInt(terminal.getTxtPort().getText()));
            
        } catch (IOException ex) {
            Logger.getLogger(ThreadServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        
        while(keepWaiting){
            try {
                socket = server.accept();
                
                new Thread(new ThreadSocket(socket, terminal)).start();
                /*
                UserManagement userManagement = new UserManagement(terminal, socket);
                userManagement.checkLogin();
                if(userManagement.getIsLogined()){
                    Thread syncFromClientThread = new Thread(new SyncFromClientThread(socket,terminal));
                    syncFromClientThread.start();

//                    Thread syncToClientThread = new Thread(new SyncToClientThread(socket, terminal));
//                    syncToClientThread.start();
                }
                */
            } catch (IOException ex) {
                Logger.getLogger(ThreadServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
