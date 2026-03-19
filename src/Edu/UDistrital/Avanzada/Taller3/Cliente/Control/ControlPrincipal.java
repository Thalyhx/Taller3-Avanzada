/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Edu.UDistrital.Avanzada.Taller3.Cliente.Control;

import Edu.UDistrital.Avanzada.Taller3.Cliente.Vista.VentanaCliente;
import Edu.UDistrital.Avanzada.Taller3.Cliente.Modelo.ConfigConexion;
import javax.swing.SwingUtilities;
import java.io.File;
import java.util.ArrayList;

/**
 * Control Principal del Cliente
 * Recibe eventos de la vista y coordina la comunicación
 * @author nath
 */
public class ControlPrincipal {
    
    private VentanaCliente vista;
    private ControlVista controlVista;
    private ControlSocket controlSocket;
    private KimariteLoader.Kimarite[] todasLasTecnicas;
    private ArrayList<KimariteLoader.Kimarite> tecnicasSeleccionadas;
    
    /**
     * Constructor - Instancia ControlVista que instancia la Vista
     */
    public ControlPrincipal() {
        this.tecnicasSeleccionadas = new ArrayList<>();
        
        // Instanciar en EDT (Event Dispatch Thread)
        SwingUtilities.invokeLater(() -> {
            // Crear ControlVista pasando this (ControlPrincipal)
            this.controlVista = new ControlVista(this);
            // ControlVista ya crea la vista internamente
            this.vista = controlVista.getVista();
            this.vista.setVisible(true);
        });
    }
    
    /**
     * Obtiene la vista
     */
    public VentanaCliente getVista() {
        return vista;
    }
    
    /**
     * Carga el archivo de técnicas
     */
    public void cargarArchivoTecnicas(File archivo) {
        try {
            todasLasTecnicas = KimariteLoader.cargarTecnicas(archivo);
            
            if (todasLasTecnicas.length > 0) {
                vista.habilitarComboBoxTecnicas();
                vista.mostrarMensaje("✓ " + todasLasTecnicas.length + " técnicas cargadas");
                vista.actualizarComboBoxTecnicas(todasLasTecnicas);
            } else {
                vista.mostrarMensaje("❌ No se cargaron técnicas");
            }
        } catch (Exception e) {
            vista.mostrarMensaje("❌ Error cargando técnicas: " + e.getMessage());
        }
    }
    
    /**
     * Agrega una técnica a la lista de seleccionadas
     */
    public void agregarTecnica(int indice) {
        if (indice >= 0 && todasLasTecnicas != null && indice < todasLasTecnicas.length) {
            KimariteLoader.Kimarite kimarite = todasLasTecnicas[indice];
            
            // Verificar que no esté duplicada
            for (KimariteLoader.Kimarite k : tecnicasSeleccionadas) {
                if (k.getNombre().equals(kimarite.getNombre())) {
                    vista.mostrarMensaje("⚠ Ya está agregada: " + kimarite.getNombre());
                    return;
                }
            }
            
            // Agregar a la lista
            tecnicasSeleccionadas.add(kimarite);
            vista.agregarTecnicaALista(kimarite.toString());
            vista.habilitarBotonConectar();
            vista.mostrarMensaje("✓ Agregada: " + kimarite.getNombre());
        }
    }
    
    /**
     * Elimina una técnica de la lista
     */
    public void eliminarTecnica(int indice) {
        if (indice >= 0 && indice < tecnicasSeleccionadas.size()) {
            KimariteLoader.Kimarite eliminada = tecnicasSeleccionadas.get(indice);
            tecnicasSeleccionadas.remove(indice);
            vista.eliminarTecnicaDeLista(indice);
            vista.mostrarMensaje("✓ Eliminada: " + eliminada.getNombre());
            
            if (tecnicasSeleccionadas.isEmpty()) {
                vista.deshabilitarBotonConectar();
            }
        }
    }
    
    /**
     * Conecta con el servidor y envía los datos del luchador
     */
    public void conectarAlServidor(String nombre, String pesoStr) {
        try {
            // Validar datos
            if (nombre == null || nombre.isEmpty()) {
                vista.mostrarMensaje("❌ Ingresa el nombre del luchador");
                return;
            }
            
            if (pesoStr == null || pesoStr.isEmpty()) {
                vista.mostrarMensaje("❌ Ingresa el peso del luchador");
                return;
            }
            
            if (tecnicasSeleccionadas.isEmpty()) {
                vista.mostrarMensaje("❌ Selecciona al menos una técnica");
                return;
            }
            
            double peso = Double.parseDouble(pesoStr);
            KimariteLoader.Kimarite[] kimarites = tecnicasSeleccionadas.toArray(
                new KimariteLoader.Kimarite[0]
            );
            
            // Crear configuración
            ConfigConexion config = new ConfigConexion("localhost", 5000);
            controlSocket = new ControlSocket(config);
            
            vista.mostrarMensaje("═══════════════════════════════════════");
            vista.mostrarMensaje("Conectando con servidor...");
            vista.actualizarEstado("Estado: Conectando...");
            vista.deshabilitarBotonConectar();
            
            // Conectar
            controlSocket.conectar();
            vista.mostrarMensaje("✓ Conectado al servidor");
            
            // Enviar datos
            controlSocket.enviarLuchador(nombre, peso, kimarites);
            vista.mostrarMensaje("✓ Datos enviados:");
            vista.mostrarMensaje("  • Luchador: " + nombre);
            vista.mostrarMensaje("  • Peso: " + peso + " kg");
            vista.mostrarMensaje("  • Técnicas: " + kimarites.length);
            
            for (KimariteLoader.Kimarite k : kimarites) {
                vista.mostrarMensaje("    - " + k.toString());
            }
            
            // Iniciar thread para escuchar respuestas
            ThreadCliente threadCliente = new ThreadCliente(controlSocket.getEntrada(), vista);
            threadCliente.start();
            
            vista.actualizarEstado("Estado: Conectado - Esperando combate...");
            vista.mostrarMensaje("═══════════════════════════════════════");
            
        } catch (NumberFormatException e) {
            vista.mostrarMensaje("❌ El peso debe ser un número");
            vista.habilitarBotonConectar();
        } catch (Exception e) {
            vista.mostrarMensaje("❌ Error de conexión: " + e.getMessage());
            vista.habilitarBotonConectar();
            vista.actualizarEstado("Estado: Error");
        }
    }
}