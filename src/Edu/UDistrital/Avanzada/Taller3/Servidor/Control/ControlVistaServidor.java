/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.UDistrital.Avanzada.Taller3.Servidor.Control;

/**
 *
 * @author nath
 */

import Edu.UDistrital.Avanzada.Taller3.Servidor.Control.ControlDohyo;
import Edu.UDistrital.Avanzada.Taller3.Servidor.Control.ControlKimarite;
import java.awt.Component;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ControlVistaServidor {
    private ControlKimarite controlKimarite;
    private ControlDohyo controlDohyo;
    
    public ControlVistaServidor() {
        this.controlKimarite = new ControlKimarite();
      //  this.controlDohyo = new ControlDohyo(controlKimarite);
    }
    
    /**
     * Abre un JFileChooser para seleccionar el archivo kimarites.properties
     * 
     * @param parentComponent componente padre Frame/
     */
    
    public void seleccionarArchivoKimarites(Component parentComponent) {
        JFileChooser fileChooser = new JFileChooser();

        // Configurar el filtro de archivos
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "Archivos Properties (*.properties)", "properties"
        );
        fileChooser.setFileFilter(filter);
        fileChooser.setDialogTitle("Seleccionar archivo kimarites.properties");
        fileChooser.setAcceptAllFileFilterUsed(false);

        // Mostrar el diálogo
        int resultado = fileChooser.showOpenDialog(parentComponent);

        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivoSeleccionado = fileChooser.getSelectedFile();

            // Intentar cargar el archivo
            boolean cargado = controlKimarite.cargarKimaritesDesdeArchivo(archivoSeleccionado);

            if (cargado) {
                JOptionPane.showMessageDialog(
                    parentComponent,
                    "Kimarites cargados exitosamente desde:\n" + archivoSeleccionado.getAbsolutePath(),
                    "Exito",
                    JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                JOptionPane.showMessageDialog(
                    parentComponent,
                    "Error al cargar el archivo. Verifica que sea un archivo properties valido.",
                    "Error",
                    JOptionPane.WARNING_MESSAGE
                );
            }
        }
    }
    
    /**
     * Verifica si los kimarites están cargados
     */

    public ControlKimarite getControlKimarite() {
        return controlKimarite;
    }
    
    public ControlDohyo getControlDohyo() {
        return controlDohyo;
    }
}