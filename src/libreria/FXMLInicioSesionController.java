/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package libreria;

import Sesion.Cliente;
import base.conexion;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author calebbolanos
 */
public class FXMLInicioSesionController implements Initializable {
    
    conexion base;
    @FXML
    JFXTextField txtUsuario;
    @FXML
    JFXPasswordField pfContrasena;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        base = new conexion();
    }
    
    
    @FXML
    private void iniciarSesion(ActionEvent event) throws IOException {
        String usuario = txtUsuario.getText();
        String contrasena = pfContrasena.getText();
        
        if(Validaciones.StringsNoVacios(usuario, contrasena)){
            try {
                    base.conectar();
                    ResultSet rs = base.ejecutaQuery("call spIniciarSesion(\""+usuario+"\", \""+contrasena+"\");");
                    if (rs.next()) {
                        if (rs.getString("msj").equals("ok")) {
                            int idCliente = rs.getInt("idCli");
                            String nombres = rs.getString("nom");
                            String paterno = rs.getString("pat");
                            String materno = rs.getString("mat");
                            String correo = rs.getString("corr");
                            Cliente.almacenarDatos(idCliente, nombres, paterno, materno, correo);

                            Stage stageActual = (Stage) ((Node) event.getSource()).getScene().getWindow();
                            FXMLLoader loaderInicio = new FXMLLoader(getClass().getResource("FXMLInicio.fxml"));
                            Scene sceneInicio = new Scene(loaderInicio.load());
                            stageActual.setScene(sceneInicio);
                        } else {
                            DialogosFX.mostrarWarning("Inicar Sesion", rs.getString("msj"));
                        }
                    }
                    base.cierraConexion();
                } catch (SQLException ex) {
                    DialogosFX.mostrarError("Inicar Sesion", "Hubo un error al intentar iniciar sesion");
                    ex.printStackTrace();
                }
        }
        else{
            DialogosFX.mostrarWarning("Iniciar Sesión", "Llena todos los campos vacios");
        }
        
    }  
    
    @FXML
    private void irACrearCuenta(ActionEvent event) throws IOException {
        Stage stageActual = (Stage)((Node)event.getSource()).getScene().getWindow();
        FXMLLoader loaderCrearCuenta = new FXMLLoader(getClass().getResource("FXMLCrearCuenta.fxml"));
        Scene sceneCrearCuenta = new Scene(loaderCrearCuenta.load());
        stageActual.setScene(sceneCrearCuenta);
    }  
    
}
