package server;

import views.Terminal;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.FileEvent;
import models.User;

/**
 *
 * @author cuong nguyen ngoc
 */
public class SyncToClientThread implements Runnable{

    private Terminal terminal;
    private ObjectInputStream Ois;
    private ObjectOutputStream Oos;
    private User user;
    private FileEvent fileEvent;
    
    public SyncToClientThread(Terminal terminal, ObjectInputStream ois, ObjectOutputStream oos, User user){
        this.terminal = terminal;
        this.Oos = oos;
        this.Ois = ois;
        this.user = user;
    }
    @Override
    public void run() {
        try {
            sync();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SyncToClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void sync() throws ClassNotFoundException {
        try {
            
            File directory = new File(terminal.directoryPath + user.getUserId());
            System.out.println(directory.getAbsoluteFile());
            
            transferDirectory(directory);
            
            fileEvent = new FileEvent("empty","notdeleted");
       
            Oos.writeObject(fileEvent);
            System.out.println("File was sent..!");
            
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(SyncToClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void transferDirectory(File file) throws IOException, FileNotFoundException, InterruptedException, ClassNotFoundException{
        
        File[] files = file.listFiles();
        for(File fuzzyFile : files){
            if(fuzzyFile.isFile()){
                transferFile(fuzzyFile);
            }else if(fuzzyFile.isDirectory()){
                transferDirectory(fuzzyFile);
            }
        }
    }
    
    private void transferFile(File file) throws FileNotFoundException, IOException, InterruptedException, ClassNotFoundException{
        
        fileEvent = new FileEvent();
        fileEvent.setConn(user.getConn());
        fileEvent.setFilename(file.getName());
        fileEvent.setFilePath(file.getAbsolutePath());
        fileEvent.setUser(user);
        fileEvent.setType("notdeleted");
        byte[] fileDatas = new byte[(int)file.length()]; // file size must be maximum 64 MB = 2^16 ( kiểu int trong java la 2 byte ), if not, exception out of heap.
        
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        
        bis.read(fileDatas);

        fileEvent.setFileData(fileDatas);
        fileEvent.setTimestamp(fileEvent.getTimestampFromDatabase());
        System.out.println("Sending " + file.getAbsolutePath());
        // send object from client to server
        Oos.writeObject(fileEvent);
        Thread.sleep(1000);
        bis.close();
    } 

    
}
