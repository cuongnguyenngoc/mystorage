package server;

import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.util.logging.*;
import models.Connector;
import models.FileEvent;
import models.User;
import views.Terminal;

/**
 *
 * @author cuong nguyen ngoc
 */
public class ThreadSocket implements Runnable{

    private Socket socket;
    private ObjectInputStream Ois = null;
    private ObjectOutputStream Oos = null;
    private Terminal terminal;
    private Connection conn;
//    private User user; // a socket conrespondings with a user 
    
    public ThreadSocket(Socket socket, Terminal terminal) throws IOException{
        conn = Connector.getConnector(); // it's just creating only a connection to db, avoid too many connection to database
        this.terminal = terminal;
        this.socket = socket;
        Oos = new ObjectOutputStream(socket.getOutputStream());
        Ois = new ObjectInputStream(socket.getInputStream());
    }
    
    @Override
    public void run() {
        try {
            /*
            The first time, I use SocketContainer set SocketType to distinguish 2 sockets
            I have coded like this but it was working
            So i use instanceof instead of to know which object is. it's nice choice.
            String socketType = container.getSocketType();
            System.out.println(socketType);
            System.out.println(container.getUser());
            switch(socketType){
                case "CHECK_LOGIN": 
                    User user = (new UserManagement(terminal, container.getUser())).login();
                    // server set all properties for this user and send full user back to client
                    Oos.writeObject(user);

                    break;
                default: (new Thread(new SyncFromClientThread(socket,terminal))).start();
                    break;
            }
            */
            Object object = Ois.readObject();
            //if(object instanceof SocketContainer){
            if(object instanceof User){
                /*
                SocketContainer container = (SocketContainer) object;
                String socketType = container.getSocketType();
                System.out.println(socketType);
                System.out.println(container.getUser());
                */
                User user = (User) object;
                user.setConn(conn);
                user = (new UserManagement(terminal, user)).login();
                Oos.writeObject(user);
                
                
            }else if(object instanceof FileEvent){
                // dong bo theo tuan tu, dong bo tu client truoc sau do dong bo tu server
//                FileEvent file = (FileEvent) object;
//                if(!file.getFilename().equals("empty")){
                    FileEvent opening = (FileEvent) object;
                    User user = opening.getUser();
                    user.setConn(conn);
                    user.setStorage(opening.getRootDirectoryPath());
                    
                    // watch directory change such as file was removed, modified or created
                    WatcherAllDirsThread watcherThread = new WatcherAllDirsThread(socket, terminal.directoryPath, Oos, user);
                    /*  
                        Tạo 1 thread riêng để xem thư mục thay đổi, nó diễn ra cùng lúc với việc đồng bộ file. Rất tuyệt.
                        Chỉ cần xóa, chỉnh sửa hoặc tạo mới sẽ có thông báo ngay. awesome. Chứ không phải đợi đồng bộ 1 lượt rồi 
                        mới biết được có sự thay đổi.
                    */
                    (new Thread(watcherThread)).start();
                    
                    (new Thread(new SyncController(socket, terminal, Ois, Oos, user))).start();
                //}else{
                    System.out.println("Hello dude");
                    //(new SyncToClientThread(socket, terminal, Ois, Oos)).sync();
                //}
                    
            }
            
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(ThreadSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
