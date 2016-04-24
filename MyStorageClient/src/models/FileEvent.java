package models;

import java.io.Serializable;

/**
 *
 * @author cuong nguyen ngoc
 */
public class FileEvent implements Serializable{
    
    private static final long serialVersionUID = 1L;
     
    private String filename;
    private String filePath;
    private byte[] fileData;
    private long timestamp;
    private int remainder;
    private User user;
    private String rootDirectoryPath;
    private String type;
    
    public FileEvent(){
        
    }

    public FileEvent(String filename, String filePath, byte[] fileData, User user, String rootDirectoryPath, long timestamp, String type) {
        this.filename = filename;
        this.filePath = filePath;
        this.fileData = fileData;
        this.user = user;
        this.rootDirectoryPath = rootDirectoryPath;
        this.timestamp = timestamp;
        this.type = type;
    }
    
    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }

    public int getRemainder() {
        return remainder;
    }

    public void setRemainder(int remainder) {
        this.remainder = remainder;
    }
    
    public void setUser(User user){
        this.user = user;
    }
    
    public User getUser(){
        return user;
    }
    
    public String getRootDirectoryPath() {
        return rootDirectoryPath;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    
    public void setRootDirectoryPath(String rootDirectoryPath) {
        this.rootDirectoryPath = rootDirectoryPath;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
