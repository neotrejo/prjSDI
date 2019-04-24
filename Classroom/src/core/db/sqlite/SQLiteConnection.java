/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.db.sqlite;

import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author SDI
 */
public class SQLiteConnection {

    private Connection conexion;
    private Statement statement;
    private String ruta;

    private static SQLiteConnection instance;
    
    private SQLiteConnection() {
        ruta = "wsappbridge/parciales.bd";
    }
    
    public static SQLiteConnection getInstance(){
        if(instance == null){
            instance = new SQLiteConnection();
        }
        return instance;
    }

    public void conectar() throws ClassNotFoundException {
        try {
            
            Class.forName("org.sqlite.JDBC");

            conexion = DriverManager.getConnection("jdbc:sqlite:" + ruta);
            statement = conexion.createStatement();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Sentencias
     */
    
    private String createParamString(Map<String,String> params){
        StringBuilder fields = new StringBuilder();
        StringBuilder values = new StringBuilder();
        
        fields.append("(");
        values.append("(");
        
        for (Map.Entry<String,String> param : params.entrySet()) {
            fields.append(param.getKey()).append(",");
            values.append("\"").append(param.getValue()).append("\"").append(",");
        }
        
        if(fields.toString().endsWith(",")){
            fields.deleteCharAt(fields.length()-1);
        }
        
        if(values.toString().endsWith(",")){
            values.deleteCharAt(values.length()-1);
        }
        
        fields.append(")");
        values.append(")");
        
        return fields.append(" VALUES ").append(values).toString();
    }
    
    private String createUpdateParamString(Map<String,String> params){
            
            StringBuilder fields = new StringBuilder();
            
            for (Map.Entry<String,String> param : params.entrySet()) {
                
                fields.append(param.getKey()).append("=").append("\"").append(param.getValue()).append("\"").append(",");
 
            }

            if(fields.toString().endsWith(",")){
                fields.deleteCharAt(fields.length()-1);
            }
            
        
        return fields.toString();
    }
        
    public int insert(String table,Map<String,String> params){
        
        int response = 0, key = -1;
        ResultSet rsId = null;
        Statement stm = null,stm2=null;
        
        try {
            
            if(conexion == null){
                System.out.println("no mams");
            }
            conexion.setAutoCommit(false);
            
            String query = "INSERT INTO "+table+" "+createParamString(params);
            System.out.println(query);
            stm = conexion.createStatement();
            stm2 = conexion.createStatement();
            
            response = stm.executeUpdate(query);
            
            rsId = stm2.executeQuery("SELECT last_insert_rowid() as last_id from "+table);
            
            conexion.commit();
            
            rsId.next();  
            key = rsId.getInt("last_id");
          
            return key;
            
        } catch (SQLException ex) {
            
            try {
                ex.printStackTrace();
                conexion.rollback();
            } catch (SQLException ex1) {
                ex1.printStackTrace();
            }
            
        }finally{
            try {
                
                conexion.setAutoCommit(true);
            } catch (SQLException ex) {
                Logger.getLogger(SQLiteConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return response;
    }
    
    
    public int update(String table,Map<String,String> params,String where){
        
        int response = 0;
        
        try {
            String query = "UPDATE "+table+" SET "+createUpdateParamString(params)+" WHERE "+where;
            System.out.println(query);         
            response = statement.executeUpdate(query);
            
            return response;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return response;
    }
    
    public int execute(String query){
        
        int response = 0;
        
        try {
            response = conexion.createStatement().executeUpdate(query);
            return response;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return response;
    }
   
   
   public ResultSet select(String query){
       
       ResultSet result = null;
       System.out.println(query);
        try {
            result = conexion.createStatement().executeQuery(query);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        //System.out.println(query);
        return result;
   }
   
}
