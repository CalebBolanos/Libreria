/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package libreria;

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
 * FXML Controller class
 *
 * @author calebbolanos
 */
public class FXMLCrearCuentaController implements Initializable {

    conexion base;

    @FXML
    JFXTextField txtNombres, txtPaterno, txtMaterno, txtCorreo;
    @FXML
    JFXPasswordField pfContrasena, pfContrasena2;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        base = new conexion();
    }

    @FXML
    private void irAIniciarSesion(ActionEvent event) throws IOException {
        Stage stageActual = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loaderInicioSesion = new FXMLLoader(getClass().getResource("FXMLInicioSesion.fxml"));
        Scene sceneInicioSesion = new Scene(loaderInicioSesion.load());
        stageActual.setScene(sceneInicioSesion);
    }

    @FXML
    private void registrarCliente(ActionEvent event) throws IOException {
        String nombres = txtNombres.getText();
        String paterno = txtPaterno.getText();
        String materno = txtMaterno.getText();
        String correo = txtCorreo.getText();
        String contrasena = pfContrasena.getText();
        String contrasena2 = pfContrasena2.getText();
        if (Validaciones.StringsNoVacios(nombres, paterno, materno, correo, contrasena, contrasena2)) {
            if (contrasena.equals(contrasena2)) {
                try {
                    base.conectar();
                    ResultSet rs = base.ejecutaQuery("call spRegistrarCliente(\"" + nombres + "\", \"" + paterno + "\", \"" + materno + "\", \"" + correo + "\", \"" + contrasena + "\");");
                    if (rs.next()) {
                        if (rs.getString("msj").equals("ok")) {
                            DialogosFX.mostrarInfo("Crear Cuenta", "Cuenta creada exitosamente");
                        } else {
                            DialogosFX.mostrarWarning("Crear Cuenta", rs.getString("msj"));
                        }
                    }
                    base.cierraConexion();
                } catch (SQLException ex) {
                    DialogosFX.mostrarError("Crear Cuenta", "Hubo un error al crear la cuenta");
                    ex.printStackTrace();
                }
            }
            else{
                DialogosFX.mostrarWarning("Crear Cuenta", "Las contraseñas proporcionadas no coinciden. Vuelve a intentarlo");
            }

        } else {
            DialogosFX.mostrarWarning("Crear Cuenta", "Llena todos los campos vacios");
        }
    }


}
