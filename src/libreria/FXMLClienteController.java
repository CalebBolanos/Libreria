/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package libreria;

import Sesion.Cliente;
import base.conexion;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import modelo.Compra;
import modelo.Producto;

/**
 * FXML Controller class
 *
 * @author calebbolanos
 */
public class FXMLClienteController implements Initializable {

    @FXML
    private Label labelNombre, labelCorreo;

    @FXML
    private TableView<Compra> tableEbook;

    @FXML
    private TableView<Compra> tableAudioLibro;

    conexion base;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        base = new conexion();
        String nombre = Cliente.getNombre() + " " + Cliente.getPaterno() + " " + Cliente.getMaterno();
        labelNombre.setText(nombre);
        labelCorreo.setText(Cliente.getCorreo());
        obtenerCompras();
    }

    @FXML
    void cerrarSesion(ActionEvent event) throws IOException {
        Cliente.limpiarDatos();
        Stage stageActual = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loaderInicioSesion = new FXMLLoader(getClass().getResource("FXMLInicioSesion.fxml"));
        Scene sceneInicioSesion = new Scene(loaderInicioSesion.load());
        stageActual.setScene(sceneInicioSesion);
    }

    @FXML
    void irAInicio(ActionEvent event) {
        try {
            Stage stageActual = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loaderInicio = new FXMLLoader(getClass().getResource("FXMLInicio.fxml"));
            Scene sceneInicio = new Scene(loaderInicio.load());
            stageActual.setScene(sceneInicio);
        } catch (IOException ex) {
            Logger.getLogger(FXMLVisualizarProductoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void obtenerCompras() {
        ObservableList<Compra> ebooksComprados = FXCollections.observableArrayList();
        ObservableList<Compra> audiolibrosComprados = FXCollections.observableArrayList();
        Compra comprax;
        try {
            base.conectar();
            ResultSet rsCompraEbook = base.ejecutaQuery("select * from vwCompraEbook where idCliente = " + Cliente.getIdCliente() + " order by 8 desc;");
            while (rsCompraEbook.next()) {
                System.out.println(rsCompraEbook.getString("nombre"));
                comprax = new Compra(
                        rsCompraEbook.getString("nombre"),
                        rsCompraEbook.getBigDecimal("precio").toString(),
                        rsCompraEbook.getInt("cantidad"),
                        rsCompraEbook.getBigDecimal("total").toString(),
                        rsCompraEbook.getTimestamp("fechaCompra").toString()
                );
                ebooksComprados.add(comprax);
            }
            tableEbook.setItems(ebooksComprados);

            ResultSet rsCompraAudiolibro = base.ejecutaQuery("select * from vwCompraAudioLibro where idCliente = " + Cliente.getIdCliente() + " order by 8 desc;");
            while (rsCompraAudiolibro.next()) {
                comprax = new Compra(
                        rsCompraAudiolibro.getString("nombre"),
                        rsCompraAudiolibro.getBigDecimal("precio").toString(),
                        rsCompraAudiolibro.getInt("cantidad"),
                        rsCompraAudiolibro.getBigDecimal("total").toString(),
                        rsCompraAudiolibro.getTimestamp("fechaCompra").toString()
                );
                audiolibrosComprados.add(comprax);
            }
            tableAudioLibro.setItems(audiolibrosComprados);
            base.cierraConexion();
        } catch (SQLException ex) {
            DialogosFX.mostrarError("Inicio", "Hubo un error al obtener los datos");
            ex.printStackTrace();
        }
    }

    @FXML
    void abrirCambioContrasena(ActionEvent event) {
        Dialog dialog = new Dialog();

        dialog.setTitle("Cambiar contraseña");
        dialog.setHeaderText("Llena el siguiente formulario");
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        PasswordField pswAntigua = new PasswordField();
        pswAntigua.setPromptText("Escribe tu contraseña");

        PasswordField pswNueva = new PasswordField();
        pswNueva.setPromptText("Escribe tu nueva contraseña");

        PasswordField pswConfirmar = new PasswordField();
        pswConfirmar.setPromptText("Vuelve a escribir tu nueva contraseña");

        dialogPane.setContent(new VBox(8, pswAntigua, pswNueva, pswConfirmar));
        Platform.runLater(pswAntigua::requestFocus);
        dialog.setResultConverter(botonPresionado -> {
            if (botonPresionado == ButtonType.OK) {
                if (pswNueva.getText().equals(pswConfirmar.getText())) {
                    cambiarContrasena(pswAntigua.getText(), pswNueva.getText());
                } else {
                    DialogosFX.mostrarError("Cambiar contraseña", "La nuevas contraseñas que ingresaste no coinciden");
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    @FXML
    void borrarCuenta(ActionEvent event) throws IOException {
        boolean clienteBorrado = false;
        if (DialogosFX.mostrarConfirmacion("¿Estas seguro de que quieres borrar tu cuenta?", "Se eliminaran todos tus datos y compras")) {
            try {
                System.out.println("borrar");
                base.conectar();
                ResultSet rs = base.ejecutaQuery("call spBr(" + Cliente.getIdCliente() + ");");
                if (rs.next()) {
                    clienteBorrado = rs.getString("msj").equals("Cliente eliminado");
                }
                base.cierraConexion();
            } catch (SQLException ex) {
                Logger.getLogger(FXMLClienteController.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (clienteBorrado) {
                DialogosFX.mostrarInfo("Cuenta dada de baja", "Los datos de tu cuenta se han borrado correctamente");
                cerrarSesion(event);

            } else {
                DialogosFX.mostrarError("Error", "No se pudo eliminar la cuenta");
            }
        }
    }

    private void cambiarContrasena(String antigua, String nueva) {
        boolean contrasenaCambiada = false;
        try {
            base.conectar();
            ResultSet rs = base.ejecutaQuery("call Cambiocon("+Cliente.getIdCliente()+",\""+antigua+"\",\""+nueva+"\");");
            if (rs.next()) {
                contrasenaCambiada = rs.getString("msj").equals("Cambio de contrasena correcto");
            }
            base.cierraConexion();
        } catch (SQLException ex) {
            Logger.getLogger(FXMLClienteController.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(contrasenaCambiada){
            DialogosFX.mostrarInfo("Contraseña cambiada", "Se ha actualizado tu contraseña correctamente");
        }
        else{
            DialogosFX.mostrarError("Error", "La contraseña que ingresaste es incorrecta. Intentalo de nuevo");
        }
    }

}
