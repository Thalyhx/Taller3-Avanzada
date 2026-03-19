/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Edu.UDistrital.Avanzada.Taller3.Cliente.Control;

import Edu.UDistrital.Avanzada.Taller3.Cliente.Vista.VentanaCliente;

/**
 * Control de Vista del Cliente
 * Intermediario entre Vista y ControlPrincipal
 * @author nath
 */
public class ControlVista {
    
    private VentanaCliente vista;
    private ControlPrincipal controlPrincipal;
    
    /**
     * Constructor que recibe ControlPrincipal e instancia la Vista
     */
    public ControlVista(ControlPrincipal controlPrincipal) {
        this.controlPrincipal = controlPrincipal;
        // Crear vista pasando this (ControlVista)
        this.vista = new VentanaCliente(this);
    }
    
    /**
     * Obtiene la vista
     */
    public VentanaCliente getVista() {
        return vista;
    }
    
    /**
     * Maneja el evento: seleccionar archivo
     */
    public void onSeleccionarArchivo(java.io.File archivo) {
        controlPrincipal.cargarArchivoTecnicas(archivo);
    }
    
    /**
     * Maneja el evento: agregar técnica
     */
    public void onAgregarTecnica(int indice) {
        controlPrincipal.agregarTecnica(indice);
    }
    
    /**
     * Maneja el evento: eliminar técnica
     */
    public void onEliminarTecnica(int indice) {
        controlPrincipal.eliminarTecnica(indice);
    }
    
    /**
     * Maneja el evento: conectar al servidor
     */
    public void onConectarAlServidor(String nombre, String peso) {
        controlPrincipal.conectarAlServidor(nombre, peso);
    }
}