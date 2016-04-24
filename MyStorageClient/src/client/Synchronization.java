package client;

import java.io.*;
import java.net.Socket;
import java.util.logging.*;
import views.Gui;

/**
 *
 * @author cuong nguyen ngoc
 */
public class Synchronization implements Runnable{
    
    private Socket socketSync;
    private ObjectInputStream Ois = null;
    private ObjectOutputStream Oos = null;
    private Gui gui;

    public Synchronization(Gui gui, Socket socketSync) throws IOException{

        this.gui = gui;
        this.socketSync = socketSync;
        /*
        Ois = new ObjectInputStream(socket.getInputStream()); 
        it's not working if you assign ObjectInputStream first.
        */
        this.Oos = new ObjectOutputStream(socketSync.getOutputStream());
        this.Ois = new ObjectInputStream(socketSync.getInputStream());
        /*
        ObjectInputStream have to be assign after assigning ObjectOutputStream. 
        I dont know why but it working. 
        */
    }
    
    @Override
    public void run() {
            
        try {
            SyncToServer syncToServer = new SyncToServer(gui, Oos, Ois); 
            SyncFromServer syncFromServer = new SyncFromServer(gui,socketSync,Oos,Ois);
            
            // watch directory change such as file was removed, modified or created
            WatcherAllDirsThread watcherThread = new WatcherAllDirsThread(socketSync, gui.getTxtPath().getText(), Oos, gui.getUser());
            /*  
                Tạo 1 thread riêng để xem thư mục thay đổi, nó diễn ra cùng lúc với việc đồng bộ file. Rất tuyệt.
                Chỉ cần xóa, chỉnh sửa hoặc tạo mới sẽ có thông báo ngay. awesome. Chứ không phải đợi đồng bộ 1 lượt rồi 
                mới biết được có sự thay đổi.
            */
            (new Thread(watcherThread)).start();
            
            while(socketSync.isConnected()){
                
                // Send file to server
                syncToServer.sync();
                //(new Thread(syncToServer)).start();
                // Receive file from server
                syncFromServer.sync(gui.getUser().getMetaData(gui.getTxtPath().getText()));
                //syncFromServer.setMetadata(gui.getUser().getMetaData(gui.getTxtPath().getText()));
                //(new Thread(syncFromServer)).start();
                
                Thread.sleep(5000);
            }
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Synchronization.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | InterruptedException | ClassNotFoundException ex) {
            Logger.getLogger(Synchronization.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
