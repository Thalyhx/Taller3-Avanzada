/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Edu.UDistrital.Avanzada.Taller3.Cliente.Vista;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

import Edu.UDistrital.Avanzada.Taller3.Cliente.Control.ControlSocket;
import Edu.UDistrital.Avanzada.Taller3.Cliente.Control.KimariteLoader;
import Edu.UDistrital.Avanzada.Taller3.Cliente.Control.ThreadCliente;
import Edu.UDistrital.Avanzada.Taller3.Cliente.Modelo.ConfigConexion;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

/**
 * Interfaz gráfica del cliente.
 */
public class VentanaCliente extends JFrame implements ActionListener {

    private JTextField txtNombre;
    private JTextField txtPeso;
    private JTextArea areaMensajes;
    private JButton btnCargar;
    private JButton btnEnviar;

    private String[] tecnicas;
    private ControlSocket control;

    public VentanaCliente() {

        setTitle("Cliente - Combate de Sumo");

        txtNombre = new JTextField(10);
        txtPeso = new JTextField(5);

        btnCargar = new JButton("Cargar Tecnicas");
        btnEnviar = new JButton("Enviar");

        areaMensajes = new JTextArea();
        areaMensajes.setEditable(false);

        btnCargar.addActionListener(e -> cargarTecnicas());
        btnEnviar.addActionListener(this);

        JPanel panel = new JPanel();
        panel.add(new JLabel("Nombre"));
        panel.add(txtNombre);
        panel.add(new JLabel("Peso"));
        panel.add(txtPeso);
        panel.add(btnCargar);
        panel.add(btnEnviar);

        add(panel, BorderLayout.NORTH);
        add(new JScrollPane(areaMensajes), BorderLayout.CENTER);

        ConfigConexion config = new ConfigConexion("localhost", 8081);
        control = new ControlSocket(config);

        setSize(500, 350);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void cargarTecnicas() {

        JFileChooser selector = new JFileChooser();

        if (selector.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {

            File archivo = selector.getSelectedFile();
            tecnicas = KimariteLoader.cargarTecnicas(archivo);

            if (tecnicas.length > 0) {
                mostrarMensaje("Tecnicas cargadas correctamente");
            } else {
                mostrarMensaje("Archivo sin tecnicas validas");
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (tecnicas == null) {
            mostrarMensaje("Debe cargar las tecnicas primero");
            return;
        }

        try {
            control.conectar();

            String nombre = txtNombre.getText();
            double peso = Double.parseDouble(txtPeso.getText());

            control.enviarLuchador(nombre, peso, tecnicas);

            ThreadCliente hilo = new ThreadCliente(control.getEntrada(), this);
            hilo.start();

            mostrarMensaje("Datos enviados al servidor");

        } catch (Exception ex) {
            mostrarMensaje("Error en la conexion");
        }
    }

    public void mostrarMensaje(String mensaje) {
        areaMensajes.append(mensaje + "\n");
    }

    public static void main(String[] args) {
        new VentanaCliente();
    }
}