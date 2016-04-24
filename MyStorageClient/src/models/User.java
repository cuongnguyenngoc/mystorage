package models;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author cuong nguyen ngoc
 */
public class User implements Serializable{
    
    private static final long serialVersionUID = 2L;
    
    private int userId;
    private Storage storage;
    private String email;
    private String password;
    private String fullname;
    private Map<String,Long> metadata;
    
    public User(){
        
    }
    
    public void setUserId(int userId){
        this.userId = userId;
    }
    
    public int getUserId(){
        return userId;
    }
    
    public Storage getStorage(){
        return storage;
    }
    
    public void setEmail(String email){
        this.email = email;
    }
    
    public String getEmail(){
        return email;
    }
    
    public void setPassword(String password){
        this.password = password;
    }
    
    public String getPassword(){
        return password;
    }
    
    public Map<String,Long> getMetadata(String dirPath){
        
        metadata = new HashMap<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(dirPath+"\\metadata"))) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(":");
                metadata.put(data[0], Long.parseLong(data[1]));
            }
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }
        System.out.println("hello. i'm metadata");
        return metadata;
    }
    
    public Map<String,Long> getMetaData(String dirPath) throws IOException{
        metadata = new HashMap<>();
        addMetadata(metadata, new File(dirPath));
        
        return metadata;
    }
    
    private void addMetadata(Map<String,Long> metadata, File file) throws IOException, FileNotFoundException{
        
        File[] files = file.listFiles();
        for(File fuzzyFile : files){
            if(fuzzyFile.isFile()){
                metadata.put(fuzzyFile.getName(), fuzzyFile.lastModified());
            }else if(fuzzyFile.isDirectory()){
                addMetadata(metadata,fuzzyFile);
            }
        }
    }
}
