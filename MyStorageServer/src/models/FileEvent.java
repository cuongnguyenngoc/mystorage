package models;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cuong nguyen ngoc
 */
public class FileEvent extends Model implements Serializable{
    
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
    
    public FileEvent(String filename, String type){
        this.filename = filename;
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

    public void setRootDirectoryPath(String rootDirectoryPath) {
        this.rootDirectoryPath = rootDirectoryPath;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    public long getTimestampFromDatabase() {
        String sql = "SELECT timestamp FROM files WHERE filename = ?";
        this.pst = this.getPreparedStatement(sql);
        try {
            this.pst.setString(1, this.filename);
            ResultSet rs = this.pst.executeQuery();
            if(rs.next()){
                timestamp = rs.getLong("timestamp");
            }
        } catch (SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }finally {

            if (this.pst != null) {
                try {
                    this.pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Storage.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    
    public long getTimestamp(){
        return timestamp;
    }
    
    public void insertMetadata(){
        try {
            String sql = "INSERT INTO files(user_id,filename,timestamp) VALUES(?,?,?)";
            
            this.pst = this.getPreparedStatement(sql);
            this.pst.setInt(1, this.user.getUserId());
            this.pst.setString(2, this.filename);
            this.pst.setLong(3, this.timestamp);
            
            this.pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Storage.class.getName()).log(Level.SEVERE, null, ex);
        }finally {

            if (this.pst != null) {
                try {
                    this.pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Storage.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    public void updateTimestamp(long timestamp){
        try {
            String sql = "UPDATE files SET timestamp = ? WHERE filename = ?";
            this.pst = this.getPreparedStatement(sql);
            this.pst.setLong(1, timestamp);
            this.pst.setString(2, this.filename);
            
            this.pst.executeUpdate(); // this method to update data into database
            
        } catch (SQLException ex) {
            Logger.getLogger(Storage.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            if (this.pst != null) {
                try {
                    this.pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Storage.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    public void deleteFile(){
        try {
            String sql = "DELETE FROM files WHERE filename = ?";
            this.pst = this.getPreparedStatement(sql);
            this.pst.setString(1, this.filename);
            
            this.pst.executeUpdate(); // this method to update data into database
            
        } catch (SQLException ex) {
            Logger.getLogger(Storage.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            if (this.pst != null) {
                try {
                    this.pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Storage.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
