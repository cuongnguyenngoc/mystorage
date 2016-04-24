package client;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import models.User;
import views.Gui;
import views.LoginForm;

/**
 *
 * @author cuong nguyen ngoc
 */
public class LoginSocketThread implements Runnable{

    private User user;
    private Socket socket;
    private LoginForm loginForm;
    private ObjectInputStream Ois = null;
    private ObjectOutputStream Oos = null;
    
    public LoginSocketThread(Socket socket, User user, LoginForm loginForm) throws IOException{
        
        this.socket = socket;
        this.user = user;
        this.loginForm = loginForm;
        this.Oos = new ObjectOutputStream(socket.getOutputStream());
        this.Ois = new ObjectInputStream(socket.getInputStream());
    }
    
    @Override
    public void run() {

        try {
            /*
             I dont need to integrated user object into SocketContainer because i found new solution that is using instance of
             to know Ois.readObject() is which object.
             So I dont need to setSocketType to distinguish two sockets ( socket login and socket synchronize)
            
//            SocketContainer container = new SocketContainer();
//            container.setSocketType("CHECK_LOGIN");
//            container.setUser(user);
            */
            // send object user to check whether this user exist in database or not
            Oos.writeObject(user);

            // Receive user from server after server finish checking
            user = (User) Ois.readObject();
            if(user.getUserId() > 0){

                // close socket check login when loginning successfully
                Oos.close();
                Ois.close();
                socket.close();

                Gui gui = new Gui(user);
                gui.setVisible(true);

                loginForm.setVisible(false);
                
            }else{
                JOptionPane.showMessageDialog(null, "Something went wrong. Please type email or password again", "Notification", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(LoginSocketThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
