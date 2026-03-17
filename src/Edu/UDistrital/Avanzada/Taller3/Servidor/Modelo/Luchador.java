/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Edu.UDistrital.Avanzada.Taller3.Servidor.Modelo;

/**
 *
 * @author nath
 */

public class Luchador {
    private String nombre;
    private int peso;
    private int victorias = 0;
    private Kimarite[] tecnicas;
    
    public Luchador(String nombre, int peso, Kimarite[] tecnicas) {
        
        this.nombre = nombre;
        this.peso = peso;
        this.tecnicas = tecnicas;

    }
    
    public Kimarite obtenerKimariteAleatorio() {
        int indice = (int)(Math.random() * tecnicas.length);
        return tecnicas[indice];
    }
    
    public void incrementarVictorias() {
        this.victorias++;
    }
    
    // Getters
    public String getNombre() 
    { return nombre; }
    
    public int getPeso() 
    { return peso; }
    
    public int getVictorias() 
    { return victorias; }
    
}