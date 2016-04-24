package client;

import java.io.*;
import java.net.Socket;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.FileEvent;
import models.User;
import views.Gui;

/**
 *
 * @author cuong nguyen ngoc
 */
public class SyncFromServer implements Runnable{
    
    private FileEvent fileEvent;
    private Gui gui;
    private Socket socket;
    private ObjectOutputStream Oos;
    private ObjectInputStream Ois;
    private Map<String,Long> metadata;
    
    public SyncFromServer(Gui gui, Socket socket, ObjectOutputStream Oos, ObjectInputStream Ois){
   
        this.gui = gui;
        this.socket = socket;
        this.Oos = Oos;
        this.Ois = Ois;
    }
    
    @Override
    public void run() {
        while(socket.isConnected()){
            
            FileOutputStream fos = null;
            try {
                fileEvent = (FileEvent) Ois.readObject();
                String filename = fileEvent.getFilename();
                if(filename.equals("empty")){
                    break;
                }   if(metadata.containsKey(filename)){
                    // file was modified
                    /*
                    Check fileEvent's timestamp at client with fileEvent's timestamp at server
                    if bigger then update file, smaller or equal then skip
                    */
                    if(fileEvent.getTimestamp() > metadata.get(filename)){
                        // metadata.put(filename, fileEvent.getTimestamp());
                        // To write to file metadata
                        //(new MetadataCreator(gui.getTxtPath().getText())).setMetaData(metadata);
                    }else{
                        System.out.println("Skiped by timestamp");
                        continue;
                    }
                }   
                User user = fileEvent.getUser();
                /*
                PATH like this: D:/GuyDriveServer/{userId}/restOfPathFile
                to get path after GuyDriveServer folderPath
                GuyDrive/ has 15 letters so need to reflect them.
                */
                int beginIndex = fileEvent.getFilePath().indexOf(String.valueOf(user.getUserId())) + String.valueOf(user.getUserId()).length() + 1;
                int endIndex = fileEvent.getFilePath().lastIndexOf("\\") + 1;
                String restOfPathFile = fileEvent.getFilePath().substring(beginIndex, endIndex);
                String folderPath = gui.getTxtPath().getText() + "\\" + restOfPathFile;
                String outputFile = folderPath + fileEvent.getFilename();
                if (!new File(folderPath).exists()) {
                    // it creates folderPath like this : D:/GuyDriveServer/1{user_id}/something
                    new File(folderPath).mkdirs();
                }   File file = new File(outputFile);
                fos = new FileOutputStream(file);
                fos.write(fileEvent.getFileData());
                fos.flush();
                fos.close();
                // You have to set lastmodified of file after file is closed. if not, it is inefficient
                // Set lastmodified of file to same with file at server
                file.setLastModified(fileEvent.getTimestamp());
                System.out.println("Receive file : " + outputFile + " is successfully saved ");
            } catch (FileNotFoundException ex) {
                Logger.getLogger(SyncFromServer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(SyncFromServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void setMetadata(Map<String, Long> metadata) {
        this.metadata = metadata;
    }
    
    public void sync(Map<String,Long> metadata) throws FileNotFoundException, IOException, ClassNotFoundException, InterruptedException {
        
        while(socket.isConnected()){
            
            fileEvent = (FileEvent) Ois.readObject();
            String filename = fileEvent.getFilename();
            if(filename.equals("empty")){
                break;
            }
            
            if(fileEvent.getType().equals("deleted")){
                // File at client was removed, so system need to remove file at server conrresponding
                System.out.println("Haha. I'm here too");
                removeFile(fileEvent);
                // File at client was removed, so system need to remove file at server conrresponding
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
                    // metadata.put(filename, fileEvent.getTimestamp());
                    // To write to file metadata 
                    //(new MetadataCreator(gui.getTxtPath().getText())).setMetaData(metadata);
                }else{
                    System.out.println("Skiped by timestamp");
                    continue;
                }
            }
            
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
            
            // You have to set lastmodified of file after file is closed. if not, it is inefficient
            // Set lastmodified of file to same with file at server
            file.setLastModified(fileEvent.getTimestamp());
            System.out.println("Receive file : " + outputFile + " is successfully saved ");
        }
        
    }
    
    public void removeFile(FileEvent fileEvent){
        String folderPath = getFolderPath(fileEvent);
        File file = new File(folderPath + fileEvent.getFilename());
        System.out.println(file.getPath());
        // Delete file at server
        if(file.delete()){
            System.out.println(file.getName() + " is deleted!");
        }
    }
    
    private String getFolderPath(FileEvent fileEvent){
        /*
            PATH like this: D:/GuyDriveServer/{userId}/restOfPathFile
            to get path after GuyDriveServer folderPath  
            GuyDrive/ has 15 letters so need to reflect them. 
        */
        int beginIndex = fileEvent.getFilePath().indexOf(String.valueOf(fileEvent.getUser().getUserId())) + String.valueOf(fileEvent.getUser().getUserId()).length() + 1;
        int endIndex = fileEvent.getFilePath().lastIndexOf("\\") + 1;
        String restOfPathFile = fileEvent.getFilePath().substring(beginIndex, endIndex);
        String folderPath = gui.getTxtPath().getText() + "\\" + restOfPathFile;
        
        return  folderPath;
    }

}
