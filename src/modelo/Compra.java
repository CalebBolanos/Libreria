/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.math.BigDecimal;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author calebbolanos
 */
public final class Compra {
    private SimpleStringProperty nombre = new SimpleStringProperty("");
    private SimpleStringProperty precio = new SimpleStringProperty("");
    private SimpleIntegerProperty cantidad = new SimpleIntegerProperty(0);
    private SimpleStringProperty total = new SimpleStringProperty("");
    private SimpleStringProperty fecha = new SimpleStringProperty("");

    public Compra() {
        this("", "", 0, "", "");
    }
    
    public Compra(String nombre, String precio, int cantidad, String total, String fecha) {
        setNombre(nombre);
        setPrecio(precio);
        setCantidad(cantidad);
        setTotal(total);
        setFecha(fecha);
    }

    public String getNombre() {
        return nombre.get();
    }

    public void setNombre(String nombre) {
        this.nombre.set(nombre);
    }

    public String getFecha() {
        return fecha.get();
    }

    public void setFecha(String fecha) {
        this.fecha.set(fecha);
    }

    public String getPrecio() {
        return precio.get();
    }

    public void setPrecio(String precio) {
        this.precio.set(precio);
    }

    public String getTotal() {
        return total.get();
    }

    public void setTotal(String total) {
        this.total.set(total);
    }

    public int getCantidad() {
        return cantidad.get();
    }

    public void setCantidad(int cantidad) {
        this.cantidad.set(cantidad);
    }
    
}
