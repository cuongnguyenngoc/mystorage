package server;

import views.Terminal;
import java.io.*;
import java.net.Socket;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.FileEvent;
import models.User;

/**
 *
 * @author cuong nguyen ngoc
 */
public class SyncFromClientThread implements Runnable{
    
    private FileEvent fileEvent;
    private Terminal terminal;
    private ObjectInputStream Ois;
    private ObjectOutputStream Oos;
    private Socket socket;
    private User user;
    
    public SyncFromClientThread(){
        
    }
    
    public SyncFromClientThread(Terminal terminal, Socket socket, ObjectInputStream ois, ObjectOutputStream oos, User user){
   
        this.terminal = terminal;
        this.socket = socket;
        this.Ois = ois;
        this.Oos = oos;
        this.user = user;
    }
    
    
    @Override
    public void run() {
        try {
            sync();
        } catch (IOException | ClassNotFoundException | InterruptedException ex) {
            Logger.getLogger(SyncFromClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void sync() throws FileNotFoundException, IOException, ClassNotFoundException, InterruptedException {
        
        Map<String,Long> metadata = user.getMetadata();
        
        while(socket.isConnected()){
                fileEvent = (FileEvent) Ois.readObject();
                fileEvent.setConn(user.getConn());
                String filename = fileEvent.getFilename();
                /* 
                    if fileEvent is empty obj, that means synced all of files or no file at client, it's time to exit loop
                    and then goto "syncToClient.sync();" outside this object: server sync files to client
                */
                if(filename.equals("empty")){
                    break;
                }else if(filename.equals("opening")){
                    System.out.println("Skiped");
                    continue;
                }
                if(fileEvent.getType().equals("deleted")){
                    // File at client was removed, so system need to remove file at server conrresponding
                    //removeFile(fileEvent);
                    System.out.println("Haha. I'm here too");
                    removeFile(fileEvent);
                    Thread.sleep(5000);
                    continue;
                }

                if(metadata.containsKey(filename)){
                    // file was modified
                    /* 
                        Check fileEvent's timestamp at client with fileEvent's timestamp at server
                        if bigger then update file, smaller or equal then skip
                    */
                    if(fileEvent.getTimestamp() > metadata.get(filename)){
                        fileEvent.updateTimestamp(fileEvent.getTimestamp());
                    }else{
                        System.out.println("Skiped by timestamp");
                        continue;
                    }
                }else {
                    // Save file's metadata ( filename, timestamp) to database
                    fileEvent.insertMetadata();
                }

                cloneFile(fileEvent); // make file at server
        }
    }
    
    private void cloneFile(FileEvent fileEvent) throws FileNotFoundException, IOException{
        
        String folderPath = getFolderPath(fileEvent);
        String outputFile = folderPath + fileEvent.getFilename();

        if (!new File(folderPath).exists()) {
            // it creates folderPath like this : D:/GuyDriveServer/1{user_id}/something
            new File(folderPath).mkdirs();
        }
        File file = new File(outputFile);

        FileOutputStream fos = new FileOutputStream(file);
        fos.write(fileEvent.getFileData());
        fos.flush();
        fos.close();

        System.out.println("Receive file : " + outputFile + " is successfully saved ");
    }
    
    public void removeFile(FileEvent fileEvent){
        String folderPath = getFolderPath(fileEvent);
        File file = new File(folderPath + fileEvent.getFilename());
        System.out.println(file.getPath());
        // Delete file at server
        if(file.delete()){
            System.out.println(file.getName() + " is deleted!");
        }
        // Delete file in database
        fileEvent.deleteFile();
    }
    
    private String getFolderPath(FileEvent fileEvent){
        int beginIndex = fileEvent.getFilePath().indexOf("GuyDrive") + 9; // to get path after GuyDrive folderPath  
                                                                         // GuyDrive/ has 9 letters so need to reflect them. 
        int endIndex = fileEvent.getFilePath().lastIndexOf("\\") + 1;
        String restOfPathFile = fileEvent.getFilePath().substring(beginIndex, endIndex);
        String folderPath = terminal.directoryPath + fileEvent.getUser().getUserId() + "\\" + restOfPathFile;
        
        return  folderPath;
    }

}
