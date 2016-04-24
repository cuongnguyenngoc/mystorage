package models;

import java.io.Serializable;

/**
 *
 * @author cuong nguyen ngoc
 */
public class SocketContainer implements Serializable{
    
    private static final long serialVersionUID = 4L;
    private String socketType;
    private User user;
    private FileEvent fileEvent;

    public String getSocketType() {
        return socketType;
    }

    public void setSocketType(String socketType) {
        this.socketType = socketType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public FileEvent getFileEvent() {
        return fileEvent;
    }

    public void setFileEvent(FileEvent fileEvent) {
        this.fileEvent = fileEvent;
    }
    
    
}