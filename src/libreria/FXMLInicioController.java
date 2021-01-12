/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package libreria;

import Sesion.Cliente;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import modelo.Producto;
import interfaz.ProductoListener;

/**
 * FXML Controller class
 *
 * @author calebbolanos
 */
public class FXMLInicioController implements Initializable, ProductoListener {

    @FXML
    private ScrollPane scrollPaneProductos;

    @FXML
    private GridPane gridPaneProductos;

    @FXML
    private JFXTextField txtBuscar;

    @FXML
    private JFXComboBox<String> comboBoxFiltro;

    @FXML
    private JFXButton buttonBuscar;

    @FXML
    private Hyperlink linkUsuario;

    private List<Producto> productos;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        productos = new ArrayList<>();
        productos.addAll(obtenerProductos(10));
        

        comboBoxFiltro.getItems().add("Autor");
        comboBoxFiltro.getItems().add("Editorial");
        comboBoxFiltro.getItems().add("Año");
        linkUsuario.setText(Cliente.getNombre());
        
        mostrarProductos();

        

    }

    @FXML
    void buscarProducto(ActionEvent event) {
        productos.clear();
        productos.addAll(obtenerProductos(5));
        gridPaneProductos.getChildren().clear();
        mostrarProductos();
    }

    @FXML
    void irAUsuario(ActionEvent event) {

    }

    private List<Producto> obtenerProductos(int tamano) {
        List<Producto> productos = new ArrayList<>();
        Producto productox;
        for (int i = 0; i < tamano; i++) {
            productox = new Producto("1", "Las batallas en el desierto" + i,
                    "Jose Emilio Pacheco", "https://cdn.gandhi.com.mx/media/catalog/product/cache/1/image/370x/9df78eab33525d08d6e5fb8d27136e95/i/m/image_1165_1_98488.jpg",
                    "Ediciones era", 1, BigDecimal.valueOf(103));
            productos.add(productox);
        }
        return productos;
    }

    @Override
    public void onClickListener(Producto producto) {
        System.out.println("Producto elegido:"+ producto.getNombre());
    }

    
    private void mostrarProductos(){
        int columnas = 0, filas = 1;
        try {
            for (Producto producto : productos) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("FXMLProducto.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();
                
                
                FXMLProductoController productoController = fxmlLoader.getController();
                productoController.ponerDatos(producto, this);

                if(columnas == 3 ){
                    columnas = 0;
                    filas++;
                }
                
                gridPaneProductos.setMinWidth(Region.USE_COMPUTED_SIZE);
                gridPaneProductos.setPrefWidth(Region.USE_COMPUTED_SIZE);
                gridPaneProductos.setMaxWidth(Region.USE_PREF_SIZE);

                gridPaneProductos.setMinHeight(Region.USE_COMPUTED_SIZE);
                gridPaneProductos.setPrefHeight(Region.USE_COMPUTED_SIZE);
                gridPaneProductos.setMaxHeight(Region.USE_PREF_SIZE);
                
                gridPaneProductos.add(anchorPane, columnas++, filas);
                GridPane.setMargin(anchorPane, new Insets(20, 37, 0, 37));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
