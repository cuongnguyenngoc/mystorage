/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.*;
import java.net.Socket;
import java.nio.file.Path;
import static java.nio.file.StandardWatchEventKinds.*;
import java.nio.file.*;
import java.util.logging.*;
import models.FileEvent;
import models.User;

/**
 *
 * @author Cuong Nguyen Ngoc
 */
public class WatcherEachDirThread implements Runnable{

    private WatchService watcher;
    private Socket socket;
    private Path dir;
    private ObjectOutputStream Oos;
    private User user;
    
    public WatcherEachDirThread(Socket socket, Path dir, ObjectOutputStream Oos, User user, WatchService watcher) throws IOException{
        this.socket = socket;
        this.dir = dir;
        this.Oos = Oos;
        this.user = user;
        this.watcher = watcher;
    }
    @Override
    public void run() {
        try {
            dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
            WatchKey key;
            do {
                System.out.println("Checked");
                
                try{
                    key = watcher.take();
                }catch (InterruptedException ex) {
                    return;
                }

                for (WatchEvent<?> event : key.pollEvents()) {
                    System.out.println("Hello guy");
                    WatchEvent.Kind<?> kind = event.kind();

                    @SuppressWarnings("unchecked")
                            WatchEvent<Path> ev = (WatchEvent<Path>) event;
                    Path parent = (Path)key.watchable();
                    Path fileName = parent.resolve(ev.context());

                    System.out.println(kind.name() + ": " + fileName);
                    if(kind == ENTRY_DELETE){
                        try {
                            FileEvent fileEvent = new FileEvent(
                                    fileName.getFileName().toString(),
                                    fileName.toString(),null,user,null,0,"deleted"
                            );
                            System.out.println(kind.name() + ": " + fileName.getFileName().toString() +
                                    " file path: "+fileName.toString() + " time: "+fileName.toFile().lastModified());
                            Oos.writeObject(fileEvent);
                            Thread.sleep(5000);
                        } catch (IOException ex) {
                            Logger.getLogger(WatcherAllDirsThread.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(WatcherEachDirThread.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                Thread.sleep(5000);
            } while(socket.isConnected() && key.reset());
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(WatcherEachDirThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
