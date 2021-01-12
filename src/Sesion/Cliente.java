/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sesion;

/**
 *
 * @author calebbolanos
 */
public class Cliente {
    private static int idCliente;
    private static String nombre, paterno, materno, correo;

    public static void almacenarDatos(int idCliente, String nombre, String paterno, String materno,String correo) {
        Cliente.idCliente = idCliente;
        Cliente.nombre = nombre;
        Cliente.paterno = paterno;
        Cliente.materno = materno;
        Cliente.correo = correo;
    }
    
    public static int getIdCliente() {
        return idCliente;
    }

    public static void setIdCliente(int idCliente) {
        Cliente.idCliente = idCliente;
    }

    public static String getNombre() {
        return nombre;
    }

    public static void setNombre(String nombre) {
        Cliente.nombre = nombre;
    }

    public static String getPaterno() {
        return paterno;
    }

    public static void setPaterno(String paterno) {
        Cliente.paterno = paterno;
    }

    public static String getMaterno() {
        return materno;
    }

    public static void setMaterno(String materno) {
        Cliente.materno = materno;
    }

    public static String getCorreo() {
        return correo;
    }

    public static void setCorreo(String correo) {
        Cliente.correo = correo;
    }

    
    
}
