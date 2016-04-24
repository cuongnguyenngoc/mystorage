package models;

import java.io.Serializable;

/**
 *
 * @author cuong nguyen ngoc
 */
public class Storage implements Serializable{
    
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
}
