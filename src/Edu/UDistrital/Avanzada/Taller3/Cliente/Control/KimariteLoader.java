/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Edu.UDistrital.Avanzada.Taller3.Cliente.Control;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Clase encargada de cargar las técnicas desde archivo properties.
 */
public class KimariteLoader {

    public static String[] cargarTecnicas(File archivo) {

        ArrayList<String> lista = new ArrayList<>();

        try {
            Properties propiedades = new Properties();
            propiedades.load(new FileInputStream(archivo));

            for (Object key : propiedades.keySet()) {
                lista.add(propiedades.getProperty((String) key));
            }

        } catch (Exception e) {
            System.out.println("Error cargando archivo de propiedades");
        }

        return lista.toArray(new String[0]);
    }
}