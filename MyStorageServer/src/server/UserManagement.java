package server;

import java.io.IOException;
import models.User;
import views.Terminal;

/**
 *
 * @author cuong nguyen ngoc
 */
public class UserManagement{

    private Terminal terminal;
    private User user;
    
    public UserManagement(Terminal terminal, User user) throws IOException{
        
        this.terminal = terminal;
        this.user = user;
    }
    
    public User login() throws IOException, ClassNotFoundException{
        
        // server check this user who was sent from client, if it is eligible,
        // server set all properties for this user and send full user back to client
        if(user.checkLogin(user.getEmail(), user.getPassword())){
            //  if it is eligible, server set all properties for this user and send full user back to client
            // of course this user has userId
            user.setProperties(user.getEmail());
        } 

        // if it is not eligible, Server also send this user back client but this user hasn't userId
        // we can use userId to either check login successfully or not
        return user;
    }
    
}
