package models;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cuong nguyen ngoc
 */
public class Storage extends Model implements Serializable{
    
    private static final long serialVersionUID = 3L;
    
    private int id;
    private String name;
    private String path;
    private int userId;
    
    public Storage(int userId){
        this.userId = userId;
        
    }
    
    public void setId(int id){
        this.id = id;
    }
    
    public int getInt(){
        return id;
    }
    
    public void setUserId(int id){
        this.userId = id;
    }
    
    public int getUserId(){
        return userId;
    }
    
    public void setName(String name){
        this.name = name;
    }    
    
    public String getName(){
        return name;
    }
    
    public void setPath(String path){
        this.path = path;
    }
    
    public String getPath(){
        return path;
    }
    
    public boolean checkStorageExisted(){
        
        try {
            String sql = "SELECT id FROM storages WHERE user_id = ?";
            this.pst = this.getPreparedStatement(sql);
            this.pst.setInt(1, this.userId);
            ResultSet rs = this.pst.executeQuery();
            
            if(rs.next()){
                return true;
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
        return false;
    }
    
    public void setProperties(){
        try {
            String sql = "SELECT id, user_id, path FROM storages WHERE user_id = ?";
            this.pst = this.getPreparedStatement(sql);
            this.pst.setInt(1, this.userId);
            ResultSet rs = this.pst.executeQuery();
            if(rs.next()){
                this.id = rs.getInt("id");
                this.path = rs.getString("path");
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
    }
    
    public void insertPath(String path){
        try {
            this.path = path;
            String sql = "INSERT INTO storages(user_id,name,path) VALUES(?,?,?)";
            
            this.pst = this.getPreparedStatement(sql);
            this.pst.setInt(1,this.userId);
            this.pst.setString(2, "");
            this.pst.setString(3, path);
            
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
    
    public void updatePath(String path){
        try {
            this.path = path;
            String sql = "UPDATE storages SET path = ? WHERE user_id = ?";
            this.pst = this.getPreparedStatement(sql);
            this.pst.setString(1, path);
            this.pst.setInt(2, this.userId);
            
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
