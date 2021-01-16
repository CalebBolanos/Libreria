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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

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
    
    @FXML
    private RadioButton radioTodo, radioLibro, radioEbook, radioAudiolibro;

    private ToggleGroup toggleGroupOpciones;
    private List<Producto> productos;
    private conexion base;
    private int producto = Producto.TODO;//0 todo, 1 libro, 2, ebook, 3 audiolibro
    
    public final static String TITULO = "Titulo";
    public final static String AUTOR = "Autor";
    public final static String EDITORIAL = "Editorial";
    public final static String ANIO = "Año";
    public final static String IDIOMA = "Idioma";
    public final static String PRECIO = "Precio";
    private String filtro = AUTOR;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        base = new conexion();

        productos = new ArrayList<>();
        productos.addAll(obtenerProductos(""));

        comboBoxFiltro.getItems().add(TITULO);
        comboBoxFiltro.getItems().add(AUTOR);
        comboBoxFiltro.getItems().add(EDITORIAL);
        comboBoxFiltro.getItems().add(ANIO);
        comboBoxFiltro.getItems().add(IDIOMA);
        comboBoxFiltro.getItems().add(PRECIO);
        
        comboBoxFiltro.getSelectionModel().selectFirst();
        
        toggleGroupOpciones= new ToggleGroup();
        radioTodo.setToggleGroup(toggleGroupOpciones);
        radioLibro.setToggleGroup(toggleGroupOpciones);
        radioEbook.setToggleGroup(toggleGroupOpciones);
        radioAudiolibro.setToggleGroup(toggleGroupOpciones);
        toggleGroupOpciones.selectedToggleProperty().addListener((observable, radioAnterior, radioNuevo) -> {
            if(radioNuevo.equals(radioTodo)){
                producto = Producto.TODO;
            }
            if(radioNuevo.equals(radioLibro)){
                producto = Producto.LIBRO;
            }
            if(radioNuevo.equals(radioEbook)){
                producto = Producto.EBOOK;
            }
            if(radioNuevo.equals(radioAudiolibro)){
                producto = Producto.AUDIOLIBRO;
            }
            System.out.println(producto);
        });
                
        linkUsuario.setText(Cliente.getNombre());

        mostrarProductos();

    }

    @FXML
    void buscarProducto(ActionEvent event) {
        productos.clear();
        productos.addAll(obtenerProductos(txtBuscar.getText()));
        gridPaneProductos.getChildren().clear();
        mostrarProductos();
    }

    @FXML
    void irAUsuario(ActionEvent event) {
        try {
            Stage stageActual = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loaderCliente = new FXMLLoader(getClass().getResource("FXMLCliente.fxml"));
            Scene sceneCliente = new Scene(loaderCliente.load());
            stageActual.setScene(sceneCliente);
        } catch (IOException ex) {
            Logger.getLogger(FXMLVisualizarProductoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private List<Producto> obtenerProductos(String busqueda) {
        List<Producto> productos = new ArrayList<>();
        Producto productox;
        try {
            base.conectar();
            ResultSet rs = base.ejecutaQuery("call spBusqueda("+producto+", \""+filtro+"\", \""+busqueda+"\");");
            while (rs.next()) {
                
                productox = new Producto(rs.getString("sku"),
                        rs.getString("Nombre"),
                        rs.getString("Autor"),
                        rs.getString("Portada"),
                        rs.getString("Editorial"),
                        rs.getInt("tipoProducto"),
                        rs.getBigDecimal("Precio"),
                        rs.getInt("Anio")
                );
                productos.add(productox);
            }
            base.cierraConexion();
        } catch (SQLException ex) {
            DialogosFX.mostrarError("Inicio", "Hubo un error al obtener los datos");
            ex.printStackTrace();
        }
        return productos;
    }

    @Override
    public void onClickListener(Producto producto) {
        try {
            System.out.println("Producto elegido:" + producto.getNombre());

            Stage stageActual = (Stage) ((Node) buttonBuscar).getScene().getWindow();
            FXMLLoader loaderVisualizarProducto = new FXMLLoader(getClass().getResource("FXMLVisualizarProducto.fxml"));
            Scene sceneInicio = new Scene(loaderVisualizarProducto.load());

            FXMLVisualizarProductoController visualizarProducto = loaderVisualizarProducto.<FXMLVisualizarProductoController>getController();
            visualizarProducto.setProducto(producto);

            stageActual.setScene(sceneInicio);
        } catch (IOException ex) {
            Logger.getLogger(FXMLInicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void filtroSeleccionado(ActionEvent event) {
        switch (comboBoxFiltro.getValue()){
            case TITULO:
                filtro = TITULO;
                break;
            case AUTOR:
                filtro = AUTOR;
                break;
            case EDITORIAL:
                filtro = EDITORIAL;
                break;
            case ANIO:
                filtro = ANIO;
                break;
            case IDIOMA:
                filtro = IDIOMA;
                break;
            case PRECIO:
                filtro = PRECIO;
                break;
        }
        System.out.println(filtro);
    }
    
    private void mostrarProductos() {
        int columnas = 0, filas = 1;
        try {
            for (Producto producto : productos) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("FXMLProducto.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();

                FXMLProductoController productoController = fxmlLoader.getController();
                productoController.ponerDatos(producto, this);

                if (columnas == 3) {
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
