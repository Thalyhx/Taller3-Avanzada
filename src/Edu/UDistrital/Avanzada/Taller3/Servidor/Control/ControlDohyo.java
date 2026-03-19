/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Edu.UDistrital.Avanzada.Taller3.Servidor.Control;

import Edu.UDistrital.Avanzada.Taller3.Servidor.Modelo.Luchador;
import Edu.UDistrital.Avanzada.Taller3.Servidor.Modelo.Kimarite;
import Edu.UDistrital.Avanzada.Taller3.Servidor.Modelo.Servidor;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Control del Dohyō (ring de sumo)
 * Orquesta el combate y gestiona los resultados
 * @author nath
 */


public class ControlDohyo {
    
    private ExecutorService executor;
    private ControlKimarite controlKimarite;
    private Servidor servidor;
    private Properties configuracion;
    
    // Estado del combate
    private int turnoActual = 1;
    private boolean combateActivo = true;
    private Luchador ganador;
    private List<Map<String, Object>> historialTurnos;
    private int numeroTurno = 0;
    
    
    //Contructor Vacio

    public ControlDohyo() {
    }
    
   /**
     * Constructor con servidor y control de kimarites
     */
    public ControlDohyo(Servidor servidor, ControlKimarite controlKimarite) {
        this.servidor = servidor;
        this.controlKimarite = controlKimarite;
        this.configuracion = servidor.getConfiguracion();
        this.historialTurnos = new ArrayList<>();
        
        // Cargar kimarites
        try {
            controlKimarite.cargarKimaritesDesdeArchivo(servidor.getArchivoConfig());
        } catch (Exception e) {
        }
    }
    /**
     * Recibe un luchador desde el cliente
     */
    public Luchador recibirLuchador(Socket cliente) throws IOException {
        DataInputStream entrada = new DataInputStream(cliente.getInputStream());
        DataOutputStream salida = new DataOutputStream(cliente.getOutputStream());
        
        //Recibe datos del luchador
        String nombre = entrada.readUTF();
        double peso = entrada.readDouble();
        int cantidadTecnicas = entrada.readInt();
        
        String[] nombresTecnicas = new String[cantidadTecnicas];
        for (int i = 0; i < cantidadTecnicas; i++) {
            nombresTecnicas[i] = entrada.readUTF();
        }
        
        //Crea array de Kimarite con probabilidades desde properties
        Kimarite[] tecnicas = new Kimarite[cantidadTecnicas];
        for (int i = 0; i < cantidadTecnicas; i++) {
            String nombre_tecnica = nombresTecnicas[i];
            String descripcion = configuracion.getProperty(
                nombre_tecnica, "Técnica sin descripción"
            );
            double probabilidad = Double.parseDouble(
                configuracion.getProperty(nombre_tecnica + ".probabilidad", "0.3")
            );
            tecnicas[i] = new Kimarite(nombre_tecnica, descripcion, probabilidad);
        }
        
        // Crea luchador
        Luchador luchador = new Luchador(nombre, (int) peso, tecnicas);
        
        // Envia confirmación
        salida.writeInt(1);
        salida.writeUTF("Luchador registrado correctamente");
        salida.flush();
        
        return luchador;
    }
    
    /**
     * Inicia un combate entre dos luchadores
     */
    public void iniciarCombate(Luchador l1, Luchador l2) {

        // Reinicializar estado del combate
        this.turnoActual = 1;
        this.combateActivo = true;
        this.ganador = null;
        this.numeroTurno = 0;
        this.historialTurnos = new ArrayList<>();
        
        // Crear executor con 2 hilos
        this.executor = Executors.newFixedThreadPool(2);
        
        // Crear controles para cada luchador
        ControlLuchador ctrl1 = new ControlLuchador(l1, this, 1);
        ControlLuchador ctrl2 = new ControlLuchador(l2, this, 2);
        
        // Ejecutar en hilos separados
        executor.execute(ctrl1);
        executor.execute(ctrl2);
        
    }
    
    /**
     * Ejecuta un turno del combate
     * Sincroniza para que solo un luchador ataque por turno
     */
    public synchronized void ejecutarTurno(Luchador atacante, int idLuchador) {
        
        // Esperar si no es el turno
        while (idLuchador != turnoActual && combateActivo) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Si el combate ya acabó, salir
        if (!combateActivo) {
            return;
        }

        // Luchador ejecuta su técnica
        Kimarite tecnica = atacante.obtenerKimariteAleatorio();
        
        //evalúa si sacó al oponente
        boolean sacoAlOponente = controlKimarite.evaluarExito(tecnica);
        
        // Registrar el turno en el historial
        numeroTurno++;
        Map<String, Object> turno = new HashMap<>();
        turno.put("numero", numeroTurno);
        turno.put("luchador", atacante.getNombre());
        turno.put("kimarite", tecnica.getNombre());
        turno.put("probabilidad", tecnica.getProbabilidadExito());
        turno.put("exito", sacoAlOponente);
        historialTurnos.add(turno);

        //Determinar resultado
        if (sacoAlOponente) {
            // Gana
            combateActivo = false;
            ganador = atacante;
        } else {
            // Pasar el turno al rival
            turnoActual = (idLuchador == 1) ? 2 : 1;
        }

        //Despertar al otro luchador
        notifyAll();
    }
    
    /**
     * Espera a que termine el combate, incrementa victorias y retorna el ganador
     */
    public Luchador esperarResultado() {
        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Incrementar victorias del ganador
        if (ganador != null) {
            ganador.incrementarVictorias();
        }
        
        return ganador;
    }
    
    /**
     * Verifica si el combate está activo
     */
    public boolean isCombateActivo() {
        return combateActivo;
    }
    
    /**
     * Obtiene el ganador del combate
     */
    public Luchador getGanador() {
        return ganador;
    }
    
    /**
     * Obtiene el historial de turnos
     */
    public List<Map<String, Object>> getHistorialTurnos() {
        return historialTurnos;
    }
}

