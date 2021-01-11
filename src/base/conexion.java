package base;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class conexion {
    private String usrBD;
    private String passBD;
    private String urlBD;
    private String driverClassName;
    private Connection conn = null;
    private Statement sentencia;

    public conexion() {
        usrBD = "root";
        passBD = "n0m3l0s3";
        urlBD = "jdbc:mysql://localhost:3306/libreria";
        driverClassName = "com.mysql.jdbc.Driver";
    }
 
    public conexion(String usrBD, String passBD, String urlBD, String driverClassName) {
        this.usrBD = usrBD;
        this.passBD = passBD;
        this.urlBD = urlBD;
        this.driverClassName = driverClassName;
    }
    
    public void conectar() throws SQLException {
        try {
            Class.forName(driverClassName).newInstance();
            conn = DriverManager.getConnection(urlBD, usrBD, passBD);
 
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | SQLException err) {
            System.out.println("Error " + err.getMessage());
        }
    }
    
    public void cierraConexion() throws SQLException {
        conn.close();
    }
    
    public boolean ejecuta(String consulta) throws SQLException {
        sentencia = (Statement) conn.createStatement();
        return sentencia.execute(consulta);
    }
    
    public ResultSet ejecutaQuery(String consulta) throws SQLException {
        sentencia = (Statement) conn.createStatement();
        return sentencia.executeQuery(consulta);
    }
    
    public int ejecutaUpdate(String consulta) throws SQLException {
        sentencia = (Statement) conn.createStatement();
        return sentencia.executeUpdate(consulta);
    }
    
    public Connection getConexion(){
        return conn;
    }
}

