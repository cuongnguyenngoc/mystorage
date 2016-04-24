package models;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cuong nguyen ngoc
 */
public class User extends Model implements Serializable{
    
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
    
    public void setProperties(String email){
        String sql = "SELECT id, fullname, password FROM users WHERE email = ?";
        this.pst = this.getPreparedStatement(sql);
        try {
            this.pst.setString(1, email);
            ResultSet rs = this.pst.executeQuery();
            if(rs.next()){
                userId = rs.getInt("id");
                fullname = rs.getString("fullname");
                this.email = email;
                password = rs.getString("password");
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
    }
    
    public boolean checkLogin(String email, String password){
        
        try {
            String sql = "SELECT id FROM users WHERE email = ? AND password = ?";
            this.pst = this.getPreparedStatement(sql);
            this.pst.setString(1, email);
            this.pst.setString(2, password);
            ResultSet rs = this.pst.executeQuery();
            if(rs.next())
                return true;
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
        
        return false;
    }
    
    public String getFullname(String email){
        String sql = "SELECT fullname FROM users WHERE email = ?";
        this.pst = this.getPreparedStatement(sql);
        try {
            this.pst.setString(1, email);
            ResultSet rs = this.pst.executeQuery();
            if(rs.next()){
                fullname = rs.getString("fullname");
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
        return fullname;
    }
    
    public void setStorage(String path){
        storage = new Storage(userId);
        storage.setConn(this.getConn());
        if(storage.checkStorageExisted()){
            storage.setProperties();
        }else{
            storage.insertPath(path);
        }
    }
    
    
    public Map<String,Long> getMetadata(){
        
        try {
            metadata = new HashMap<>();
            String sql = "SELECT filename,timestamp FROM files WHERE user_id = ?";
            this.pst = this.getPreparedStatement(sql);
            this.pst.setInt(1, this.userId);
            ResultSet rs = this.pst.executeQuery();
            while(rs.next()){
                metadata.put(rs.getString("filename"), rs.getLong("timestamp"));
            }
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
        System.out.println("hello. i'm metadata");
        return metadata;
    }
}
