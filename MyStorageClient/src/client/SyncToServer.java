package client;

import java.io.File;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.FileEvent;
import views.Gui;

/**
 *
 * @author cuong nguyen ngoc
 */
public class SyncToServer implements Runnable{
    
    private FileEvent fileEvent;
    private Gui gui;
    private ObjectOutputStream Oos;
    private ObjectInputStream Ois;
    
    public SyncToServer(Gui gui, ObjectOutputStream Oos, ObjectInputStream Ois){

        this.gui = gui;
        this.Oos = Oos;
        this.Ois = Ois;
    }
    
    @Override
    public void run() {
        try {
            sync();
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(SyncToServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void sync() throws IOException, InterruptedException{
        // new approach for coding
        File directory = new File(gui.getTxtPath().getText());
        System.out.println(directory.getAbsoluteFile());       
        /*
        SocketContainer container = new SocketContainer();
        container.setSocketType("SYNC");
        Oos.writeObject(container);
        */
        /*
            You have to send a fileEvent Object with "opening" name or other something
            This object has the only purpose that is server can check object is User or FileEvent
            if You delete 2 commands below, the first file will not be received by server. try to see
        */
        fileEvent = new FileEvent("opening", null, null, gui.getUser(), directory.getAbsolutePath(),0,"notdeleted"); // mở màn cho đồng bộ
        Oos.writeObject(fileEvent);
        Oos.reset();
        if(!checkEmptyDirectory(directory)){
            transferDirectory(directory);// sync from client to server
        }
        /*
        else{ // sync from server to client
            Oos.writeObject("Empty");
        }
        */
        /* 
        Vì khi đồng bộ hết file và đồng bộ khi không có file nào đều giống nhau, đều gửi 1 fileEvent trống
        chứa user object nên ta viết chung không dùng else như trên.
        */
        fileEvent = new FileEvent("empty", null, null, gui.getUser(), null, 0,"notdeleted");
        Oos.writeObject(fileEvent);
        Oos.reset();
        System.out.println("File was sent..!");
    }
    
    private void transferDirectory(File file) throws IOException, InterruptedException{
        
        File[] files = file.listFiles();
        for(File fuzzyFile : files){
            if(fuzzyFile.isFile() && !fuzzyFile.getName().equals("metadata")){
                transferFile(fuzzyFile);
            }else if(fuzzyFile.isDirectory()){
                transferDirectory(fuzzyFile);
            }
        }
    }
    
    private void transferFile(File file) throws IOException, InterruptedException{
        
        byte[] fileDatas = new byte[(int)file.length()]; // file size must be maximum 64 MB = 2^16 ( kiểu int trong java la 2 byte ), if not, exception out of heap.
        
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        /*
        int read = 0;
        int numRead = 0;
        while (read < fileDatas.length &&(numRead = bis.read(fileDatas, read, fileDatas.length - read)) >= 0){
            read = read + numRead;
        }
        */
        bis.read(fileDatas);
        
        fileEvent = new FileEvent(
                file.getName(), 
                file.getAbsolutePath(), 
                fileDatas, 
                gui.getUser(), null, 
                file.lastModified(),
                "notdeleted"
        );

        /* 
            I pass user = null, because i just use user for the first time on server, but I sent a fileEvent obj named 
            "openning" above and I passed user object along this fileEvent obj
        */
        System.out.println("Sending " + file.getAbsolutePath());
        
        // send object from client to server
        Oos.writeObject(fileEvent);
        Oos.reset();
        Thread.sleep(1000);
        bis.close();
    } 
    
    // Check directory at client empty or not
    public boolean checkEmptyDirectory(File dir){
        File[] files = dir.listFiles();
        for(File fuzzyFile : files){
            if(fuzzyFile.isFile()){
                return false;
            }else if(fuzzyFile.isDirectory()){
                return checkEmptyDirectory(fuzzyFile);
            }
        }
        return true;
    }

}
