package models;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cuong nguyen ngoc
 */
public class Model {
    
    private Connection conn = null;
    protected PreparedStatement pst = null;
    
    public Model(){
        //conn = Connector.getConnector();
    }
    
    public PreparedStatement getPreparedStatement(String sql){
        try {
            pst = conn.prepareStatement(sql);
        } catch (SQLException ex) {
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return pst;
    }

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    
}
