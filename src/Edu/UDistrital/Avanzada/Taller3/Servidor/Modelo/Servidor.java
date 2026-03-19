/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Edu.UDistrital.Avanzada.Taller3.Servidor.Modelo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

/**
 * Servidor Sumo 
 * Maneja el ServerSocket y carga configuración
 * @author nath
 */
public class Servidor {
    
    private ServerSocket serverSocket;
    private int puerto;
    private String direccion;
    private boolean activo = true;
    private Properties configuracion;
    private File archivoConfig;
    
    /**
     * Constructor que carga configuración desde archivo properties
     */
    public Servidor(File archivoConfig) throws IOException {
        this.archivoConfig = archivoConfig;
        cargarConfiguracion();
    }
    
    /**
     * Carga la configuración desde archivo properties
     */
    private void cargarConfiguracion() throws IOException {
        try (FileInputStream fis = new FileInputStream(archivoConfig)) {
            configuracion = new Properties();
            configuracion.load(fis);
            
            this.puerto = Integer.parseInt(
                configuracion.getProperty("servidor.puerto", "5000")
            );
            this.direccion = configuracion.getProperty(
                "servidor.direccion", "localhost"
            );
                        
        } catch (IOException e) {
            throw e;
        }
    }
    
    /**
     * Inicia el servidor en el puerto especificado
     */
    public void iniciar() throws IOException {
        serverSocket = new ServerSocket(puerto);
    }
    
    /**
     * Espera conexiones de clientes
     * @return Socket del cliente conectado
     */
    public Socket aceptarCliente() throws IOException {
        if (serverSocket == null) {
            throw new IOException("Servidor no iniciado. Llama a iniciar() primero.");
        }
        return serverSocket.accept();
    }
    
    /**
     * Detiene el servidor
     */
    public void detener() throws IOException {
        activo = false;
        if (serverSocket != null && !serverSocket.isClosed()) {
            serverSocket.close();
        }
    }
    
    /**
     * Verifica si el servidor está activo
     */
    public boolean isActivo() {
        return activo;
    }
    
    /**
     * Obtiene el puerto
     */
    public int getPuerto() {
        return puerto;
    }
    
    /**
     * Obtiene la dirección
     */
    public String getDireccion() {
        return direccion;
    }
    
    /**
     * Obtiene las propiedades cargadas
     */
    public Properties getConfiguracion() {
        return configuracion;
    }
    
    /**
     * Obtiene el archivo de configuración
     */
    public File getArchivoConfig() {
        return archivoConfig;
    }
}