/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Edu.UDistrital.Avanzada.Taller3.Cliente.Control;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;

/**
 * Cargador de técnicas (Kimarites) 
 * Contiene la definición de Kimarite y la lógica para cargar desde properties
 * @author nath
 */
public class KimariteLoader {

    private static final Random random = new Random();
    private static final double PROBABILIDAD_MIN = 0.15; // 15%
    private static final double PROBABILIDAD_MAX = 0.50; // 50%

    /**
     * Clase interna: Kimarite (Técnica de Sumo)
     */
    public static class Kimarite {
        private String nombre;
        private String descripcion;
        private double probabilidadExito;
        
        /**
         * Constructor de Kimarite
         */
        public Kimarite(String nombre, String descripcion, double probabilidadExito) {
            this.nombre = nombre;
            this.descripcion = descripcion;
            this.probabilidadExito = probabilidadExito;
        }
        
        public String getNombre() {
            return nombre;
        }
        
        public String getDescripcion() {
            return descripcion;
        }
        
        public double getProbabilidadExito() {
            return probabilidadExito;
        }
        
        public void setProbabilidadExito(double probabilidad) {
            this.probabilidadExito = probabilidad;
        }
        
        @Override
        public String toString() {
            return nombre + " (" + String.format("%.0f", probabilidadExito * 100) + "% éxito)";
        }
    }

    /**
     * Carga las técnicas desde el archivo properties
     * Retorna array de objetos Kimarite con nombre, descripción y probabilidad
     * 
     * @param archivo archivo properties con las técnicas
     * @return array de Kimarite cargados
     */
    public static Kimarite[] cargarTecnicas(File archivo) {
        ArrayList<Kimarite> lista = new ArrayList<>();

        try {
            Properties propiedades = new Properties();
            propiedades.load(new FileInputStream(archivo));

            // Cargar técnicas
            for (Object key : propiedades.keySet()) {
                String keyStr = (String) key;
                
                // Filtrar: solo técnicas 
                if (!keyStr.contains(".") && !keyStr.toLowerCase().startsWith("servidor")) {
                    String nombreTecnica = keyStr;
                    String descripcion = propiedades.getProperty(nombreTecnica);
                    
                    // Intentar obtener probabilidad desde properties
                    double probabilidad = obtenerProbabilidad(propiedades, nombreTecnica);
                    
                    // Crear Kimarite
                    Kimarite kimarite = new Kimarite(nombreTecnica, descripcion, probabilidad);
                    lista.add(kimarite);
                }
            }

        } catch (Exception e) {

        }

        return lista.toArray(new Kimarite[0]);
    }
    
    /**
     * Obtiene la probabilidad de una técnica
     * Si existe en properties, usa ese valor
     * Si no existe, genera un valor aleatorio entre PROBABILIDAD_MIN y PROBABILIDAD_MAX
     * 
     * @param propiedades properties del archivo
     * @param nombreTecnica nombre de la técnica
     * @return probabilidad de éxito (0.0 a 1.0)
     */
    private static double obtenerProbabilidad(Properties propiedades, String nombreTecnica) {
        String probabilidadStr = propiedades.getProperty(nombreTecnica + ".probabilidad");
        
        // Si existe en properties, usar ese valor
        if (probabilidadStr != null) {
            try {
                double prob = Double.parseDouble(probabilidadStr);
                // Validar que esté entre 0 y 1
                if (prob >= 0.0 && prob <= 1.0) {
                    return prob;
                }
            } catch (NumberFormatException e) {
                // Ignorar y usar aleatorio
            }
        }
        
        // Si no existe o es inválido, generar probabilidad aleatoria
        return PROBABILIDAD_MIN + (PROBABILIDAD_MAX - PROBABILIDAD_MIN) * random.nextDouble();
    }
}