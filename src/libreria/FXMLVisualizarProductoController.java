/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package libreria;

import Sesion.Cliente;
import base.conexion;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import modelo.Producto;

/**
 * FXML Controller class
 *
 * @author calebbolanos
 */
public class FXMLVisualizarProductoController implements Initializable {

    @FXML
    private ImageView imvPortada;

    @FXML
    private Label labelNombre, labelAutor, labelEditorial, labelInfoAdicional, labelPrecio, labelTotal, 
            labelProducto, labelCantidadText, labelTotalText;

    @FXML
    private JFXTextField txtCantidad;

    @FXML
    private JFXButton buttonInicio, buttonComprar;

    @FXML
    private Hyperlink linkUsuario;

    private Producto producto;
    private conexion base;

    public void setProducto(Producto producto) {
        this.producto = producto;
        System.out.println(producto.getNombre());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        base = new conexion();
        linkUsuario.setText(Cliente.getNombre());

        UnaryOperator<Change> filtroNumeros = cambiar -> {
            String texto = cambiar.getText();

            if (texto.matches("[0-9]*")) {
                return cambiar;
            }

            return null;
        };
        TextFormatter<String> textFormatterNumeros = new TextFormatter<>(filtroNumeros);
        txtCantidad.setText("1");
        txtCantidad.setTextFormatter(textFormatterNumeros);
        txtCantidad.textProperty().addListener((observable, viejoValor, nuevoValor) -> {
            String cantidad = nuevoValor.equals("") ? viejoValor : nuevoValor;
            System.out.println("textfield changed from " + viejoValor + " to " + nuevoValor);
            BigDecimal total = (producto.getPrecio().multiply(new BigDecimal(cantidad)));
            labelTotal.setText("$" + total.toString());
        });
        txtCantidad.focusedProperty().addListener((obs, viejoValor, nuevoValor) -> {
            if (!nuevoValor) {//textfield desenfocado
                if (txtCantidad.getText().equals("") || txtCantidad.getText().equals("0")) {
                    txtCantidad.setText("1");
                }
            }
        });

        Platform.runLater(() -> {
            labelProducto.setText(FXMLProductoController.stringTipoProducto(producto.getTipoProducto()));
            labelNombre.setText(producto.getNombre());
            Image portada = new Image(producto.getLinkPortada());
            imvPortada.setImage(portada);
            labelAutor.setText("Autor: " + producto.getAutor());
            labelEditorial.setText("Editorial: " + producto.getEditorial());
            labelPrecio.setText("Precio: $" + producto.getPrecio().toString());
            labelTotal.setText("$" + producto.getPrecio().toString());
            obtenerInfoAdicional();
        });
    }

    @FXML
    void comprarProducto(ActionEvent event) {
        int cantidad = Integer.parseInt(txtCantidad.getText());
        boolean compraExitosa = false;
        ResultSet rs;
        try {
            base.conectar();
            switch (producto.getTipoProducto()) {
                case Producto.EBOOK:
                    rs = base.ejecutaQuery("call spComprarEbook("+Cliente.getIdCliente()+", \""+producto.getSku()+"\", "+cantidad+");");
                    if (rs.next()) {
                        compraExitosa = rs.getString("msj").equals("Compra exitosa");
                        DialogosFX.mostrarInfo("Compra", rs.getString("msj"));
                    }
                    break;
                case Producto.AUDIOLIBRO:
                    rs = base.ejecutaQuery("call spComprarAudiolibro("+Cliente.getIdCliente()+", \""+producto.getSku()+"\", "+cantidad+");");
                    if (rs.next()) {
                        compraExitosa = rs.getString("msj").equals("Compra exitosa");
                        DialogosFX.mostrarInfo("Compra", rs.getString("msj"));
                    }
                    break;
            }
            base.cierraConexion();
            if(compraExitosa){
                irAInicio(event);
            }
        } catch (SQLException ex) {
            DialogosFX.mostrarError("Inicio", "Hubo un error al obtener los datos");
            ex.printStackTrace();
        }
    }

    @FXML
    void irACliente(ActionEvent event) {
        try {
            Stage stageActual = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loaderCliente = new FXMLLoader(getClass().getResource("FXMLCliente.fxml"));
            Scene sceneCliente = new Scene(loaderCliente.load());
            stageActual.setScene(sceneCliente);
        } catch (IOException ex) {
            Logger.getLogger(FXMLVisualizarProductoController.class.getName()).log(Level.SEVERE, null, ex);
        }

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

    private void obtenerInfoAdicional() {
        String contenidoLabel = "";
        ResultSet rs;
        try {
            base.conectar();
            switch (producto.getTipoProducto()) {
                case Producto.LIBRO:
                    habilitarSeccionCompra(false);
                    rs = base.ejecutaQuery("select * from vwLibros where sku = \""+producto.getSku()+"\";");
                    if (rs.next()) {
                        contenidoLabel = "Año: "+ String.valueOf(producto.getAnio()) +"\n"
                                + "Edición: "+ rs.getString("Edicion")+"\n"
                                + "Páginas: "+ rs.getInt("Paginas")+"\n"
                                + "Idioma: "+ rs.getString("Idioma")+"\n"
                                + "Disponibilidad: "+ rs.getString("Disponibilidad");
                    }
                    break;
                case Producto.EBOOK:
                    rs = base.ejecutaQuery("select * from vwebook where sku = \""+producto.getSku()+"\";");
                    if (rs.next()) {
                        contenidoLabel = "Año: "+ String.valueOf(producto.getAnio())+"\n"
                                + "Año de digitalización: "+ rs.getString("AnioDigitalizacion")+"\n"
                                + "Idioma: "+ rs.getString("Idioma")+"\n"
                                + "Formato: "+ rs.getString("Formato");
                    }
                    break;
                case Producto.AUDIOLIBRO:
                    rs = base.ejecutaQuery("select * from vwAudiolibro where sku = \""+producto.getSku()+"\";");
                    if (rs.next()) {
                        contenidoLabel = "Año: "+ String.valueOf(producto.getAnio())+"\n"
                                + "Idioma: "+ rs.getString("Idioma")+"\n"
                                + "Duración (en minutos): "+ rs.getInt("Duracion")+"\n"
                                + "Narrador: "+ rs.getString("Narrador");
                    }
                    break;
            }
            base.cierraConexion();
            labelInfoAdicional.setText(contenidoLabel);
        } catch (SQLException ex) {
            DialogosFX.mostrarError("Inicio", "Hubo un error al obtener los datos");
            ex.printStackTrace();
        }
    }
    private void habilitarSeccionCompra(boolean eleccion){
        labelCantidadText.setVisible(eleccion);
        txtCantidad.setVisible(eleccion);
        labelTotal.setVisible(eleccion);
        labelTotalText.setVisible(eleccion);
        buttonComprar.setVisible(eleccion);
    }
}
