package server;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.User;
import views.Terminal;

/**
 *
 * @author cuong nguyen ngoc
 */
public class SyncController implements Runnable{
    
    private Terminal terminal;
    private Socket socket;
    private ObjectInputStream Ois;
    private ObjectOutputStream Oos;
    private User user;
    
    public SyncController(Socket socket, Terminal terminal, ObjectInputStream Ois, ObjectOutputStream Oos, User user) throws IOException{
        this.terminal = terminal;
        this.socket = socket;
        this.Ois = Ois;
        this.Oos = Oos;
        this.user = user;
//        Ois = new ObjectInputStream(socket.getInputStream()); // if you assign Ois again you will have error
//        Oos = new ObjectOutputStream(socket.getOutputStream()); // because outsite this class Ois, Oos have bean assign already.
        
        //Oos = new ObjectOutputStream(socket.getOutputStream());
    }
    
    @Override
    public void run() {
        try {
            sync();
        } catch (InterruptedException | ClassNotFoundException | IOException ex) {
            Logger.getLogger(SyncController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void sync() throws InterruptedException, ClassNotFoundException, IOException {
        
        SyncFromClientThread syncFromClient = new SyncFromClientThread(terminal, socket, Ois, Oos, user);
        SyncToClientThread syncToClient = new SyncToClientThread(terminal, Ois, Oos, user);
        
        while(socket.isConnected()){
            try {
                System.out.println("the fuck");
                /*
                fileEvent = (FileEvent) Ois.readObject();
                
                User user = fileEvent.getUser();
                // set path in which data are stored at client
                user.setStorage(fileEvent.getRootDirectoryPath());
                
                if(!fileEvent.getFilename().equals("empty")){
                
                (new SyncFromClientThread(fileEvent, terminal)).sync();
                
                }else{
                // Stop syncing from client to server
                System.out.println("Stop syncing from client to server");
                
                (new SyncToClientThread(terminal, Ois, Oos, user)).sync();
                //break;
                }
                */
                System.out.println("Begin to sync from client");
                syncFromClient.sync();
                //(new Thread(syncFromClient)).start();
                System.out.println("End to sync from client");
                
                System.out.println("Begin to sync to client");
                syncToClient.sync();
                //(new Thread(syncToClient)).start();
                System.out.println("End to sync to client");
                Thread.sleep(5000);
            } catch (IOException ex) {
                Logger.getLogger(SyncController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }

}
