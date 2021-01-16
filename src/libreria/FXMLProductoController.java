/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package libreria;


import com.jfoenix.controls.JFXRippler;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import modelo.Producto;
import interfaz.ProductoListener;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;

/**
 * FXML Controller class
 *
 * @author calebbolanos
 */
public class FXMLProductoController implements Initializable {

    @FXML
    private AnchorPane anchorPane;
    
    @FXML
    private Pane pane;
    
    @FXML
    private ImageView imgvPortada;

    @FXML
    private Label labelNombre, labelTipo, labelAutor, labelEditorial, labelAnio, labelPrecio;
    
    private Producto producto;
    private ProductoListener productoListener; 
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        JFXRippler ripple = new JFXRippler(pane);
        ripple.setRipplerFill(Paint.valueOf("#e74645"));
        anchorPane.getChildren().add(ripple);
    }
    
    @FXML
    void presionado(MouseEvent event) {
        productoListener.onClickListener(producto);
    }

    public void ponerDatos(Producto producto, ProductoListener productoListener){
        this.producto = producto;
        this.productoListener = productoListener;
        Image portada = new Image(producto.getLinkPortada());
        imgvPortada.setImage(portada);
        labelNombre.setText(producto.getNombre());
        labelTipo.setText(stringTipoProducto(producto.getTipoProducto()));
        labelAutor.setText(producto.getAutor());
        labelEditorial.setText(producto.getEditorial());
        labelAnio.setText(String.valueOf(producto.getAnio()));
        labelPrecio.setText("$"+producto.getPrecio().toString());
    }
    
    public static String stringTipoProducto(int tipoProducto){
        switch (tipoProducto){
            case Producto.LIBRO:
                return "Libro";
            case Producto.EBOOK:
                return "eBook";
            case Producto.AUDIOLIBRO:
                return "Audiolibro";
            default:
                return null;
        }
    }
    
}
