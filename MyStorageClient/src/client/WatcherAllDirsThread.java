/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.*;
import java.net.Socket;
import java.nio.file.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.User;

/**
 *
 * @author Cuong Nguyen Ngoc
 */
public class WatcherAllDirsThread implements Runnable{
    
    private WatchService watcher;
    private Socket socket;
    private String dirPath;
    private ObjectOutputStream Oos;
    private User user;
    
    public WatcherAllDirsThread(Socket socket, String dirPath, ObjectOutputStream Oos, User user) throws IOException{
        this.socket = socket;
        this.dirPath = dirPath;
        this.Oos = Oos;
        this.user = user;
        watcher = FileSystems.getDefault().newWatchService();
        
    }
    
    @Override
    public void run() {
        try {
            registerWatcher(new File(dirPath));
        } catch (IOException ex) {
            Logger.getLogger(WatcherAllDirsThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void registerWatcher(File file) throws IOException, FileNotFoundException{
        
        File[] files = file.listFiles();
        for(File fuzzyFile : files){
            if(fuzzyFile.isFile()){
                Path dir = Paths.get(fuzzyFile.getParent());
                System.out.println("dir was registered "+dir);
                (new Thread(new WatcherEachDirThread(socket, dir, Oos, user, watcher))).start();
            }else if(fuzzyFile.isDirectory()){
                registerWatcher(fuzzyFile);
            }
        }
    }
    
}
