/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Edu.UDistrital.Avanzada.Taller3.Servidor.Modelo;

/**
 *
 * @author nath
 */
public class Kimarite {
    private String nombre;
    private String descripcion;
    private double probabilidadExito;
    
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
        return nombre + " (" + (probabilidadExito * 100) + "% éxito)";
    }
}   