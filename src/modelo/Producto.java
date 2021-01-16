/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.math.BigDecimal;

/**
 *
 * @author calebbolanos
 */
public class Producto {
    public final static int TODO = 0;
    public final static int LIBRO = 1;
    public final static int EBOOK = 2;
    public final static int AUDIOLIBRO = 3;
    
    private String sku, nombre, autor, linkPortada, editorial;
    private int tipoProducto, anio;
    private BigDecimal precio;

    public Producto(String sku, String nombre, String autor, String linkPortada, String editorial, int tipoProducto, BigDecimal precio, int anio) {
        this.sku = sku;
        this.nombre = nombre;
        this.autor = autor;
        this.linkPortada = linkPortada;
        this.editorial = editorial;
        this.tipoProducto = tipoProducto;
        this.anio = anio;
        this.precio = precio;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getLinkPortada() {
        return linkPortada;
    }

    public void setLinkPortada(String linkPortada) {
        this.linkPortada = linkPortada;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public int getTipoProducto() {
        return tipoProducto;
    }

    public void setTipoProducto(int tipoProducto) {
        this.tipoProducto = tipoProducto;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }
    
    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }
}
