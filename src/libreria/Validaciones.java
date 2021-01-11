/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package libreria;

/**
 *
 * @author calebbolanos
 */
public class Validaciones {
    //metodo para verificar que ningun textfield este vacio
    static boolean StringsNoVacios(String... textfileds) {
        for (String contenido : textfileds) {
            if (contenido.equals("")) {
                return false;
            }
        }
        return true;
    }
}
