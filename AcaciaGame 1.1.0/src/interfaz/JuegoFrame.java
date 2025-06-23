// src/interfaz/JuegoFrame.java
package interfaz;

import javax.swing.BoxLayout;
import javax.swing.event.DocumentListener;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.stream.Collectors; // Para usar streams para filtrar ítems

import modelo.Usuario;
import modelo.Acacia;
import modelo.Raton;
import modelo.Item;
import gestion.GestorJuego;


public class JuegoFrame extends JFrame {
    private GestorJuego gestorJuego;
    private JTextArea areaLog; // Para mostrar el log de la batalla
    private JLabel labelAcaciaVida;
    private JProgressBar progressBarAcaciaVida; // NOTA: se agrega una barra de vida visible !!
    private JLabel labelAcaciaAtaqueDefensa;
    private JLabel labelAcaciaOro;
    private JLabel labelEnemigoInfo;
    private JProgressBar progressBarEnemigoVida; // LA MISMA BARRA PERO PARA EL ENEMIGO IGUAL!!
    private JButton botonAtacar;
    private JButton botonUsarItem;
    private JButton botonHuir; // Opcional: Huir de la batalla
    private JLabel labelFase;
    private final Usuario usuarioLogueado;
    private JLabel labelAcaciaImagen; // <--- NUEVA VARIABLE para futuras imagenes.
    private JLabel labelEnemigoImagen; // <--- NUEVA VARIABLE para futuras imagenes.

    public JuegoFrame(Acacia acacia, Usuario usuario) {
        super("Aventura de Acacia - Recupera la Tarjeta");
        this.gestorJuego = new GestorJuego(acacia); // Creamos el gestor de juego con la Acacia recibida
        this.usuarioLogueado = usuario;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        // Panel Principal con BorderLayout
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- Panel Superior: Información del Juego (Acacia, Enemigo, Fase) ---
        JPanel panelInfo = new JPanel(new GridLayout(1, 2, 10, 0)); // Dividido para info de Acacia y Enemigo

        
        // Panel de Acacia <---- Nuevo panel con barras de vida!
       
        JPanel panelAcacia = new JPanel(); 
        panelAcacia.setLayout(new BoxLayout(panelAcacia, BoxLayout.Y_AXIS)); // Usamos BoxLayout para organizar verticalmente
        panelAcacia.setBorder(BorderFactory.createTitledBorder("Acacia"));
        //se agrega opcion para poner una imagen
        labelAcaciaImagen = new JLabel(); // Aquí podrías cargar un ImageIcon
        labelAcaciaImagen.setIcon(new ImageIcon(getClass().getResource("/recursos/acacia_placeholder.png"))); // NOTA: AQUI VA LA IMAGEN, AJUSTAR RUTA.
        labelAcaciaImagen.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelAcacia.add(labelAcaciaImagen); // <--- AÑADE LA IMAGEN
        //vida de la acacia en barra
        labelAcaciaVida = new JLabel("Vida: " + acacia.getVidaActual() + "/" + acacia.getVidaMaxima());
        progressBarAcaciaVida = new JProgressBar(0, acacia.getVidaMaxima()); // Rango de 0 a vida máxima
        progressBarAcaciaVida.setValue(acacia.getVidaActual()); // Valor actual
        progressBarAcaciaVida.setStringPainted(true); // Muestra el texto del porcentaje/valor
        progressBarAcaciaVida.setForeground(Color.GREEN); // Color de la barra

        // Alineamos los componentes a la izquierda dentro del BoxLayout
        labelAcaciaVida.setAlignmentX(Component.LEFT_ALIGNMENT);
        progressBarAcaciaVida.setAlignmentX(Component.LEFT_ALIGNMENT);

        panelAcacia.add(labelAcaciaVida);
        panelAcacia.add(progressBarAcaciaVida); // <--- Se añade la barra de vida

        labelAcaciaAtaqueDefensa = new JLabel("Ataque: " + acacia.getAtaque() + " | Defensa: " + acacia.getDefensa());
        labelAcaciaOro = new JLabel("Oro: " + acacia.getOro());

        labelAcaciaAtaqueDefensa.setAlignmentX(Component.LEFT_ALIGNMENT);
        labelAcaciaOro.setAlignmentX(Component.LEFT_ALIGNMENT);

        panelAcacia.add(labelAcaciaAtaqueDefensa);
        panelAcacia.add(labelAcaciaOro);

        // Panel del Enemigo, tambien con barra de vida nueva!
        
        JPanel panelEnemigo = new JPanel();
        panelEnemigo.setLayout(new BoxLayout(panelEnemigo, BoxLayout.Y_AXIS)); // Usamos BoxLayout
        panelEnemigo.setBorder(BorderFactory.createTitledBorder("Enemigo Actual"));
        //TAMBIEN SE AGREGA OPCION PARA CARGAR IMAGEN
        labelEnemigoImagen = new JLabel(); // Aquí podrías cargar un ImageIcon
        labelEnemigoImagen.setIcon(new ImageIcon(getClass().getResource("/recursos/raton_placeholder.png"))); // Asegúrate de tener esta imagen o ajusta la ruta
        labelEnemigoImagen.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelEnemigo.add(labelEnemigoImagen); // <--- AÑADE LA IMAGEN

        labelEnemigoInfo = new JLabel("Esperando enemigo...");
        progressBarEnemigoVida = new JProgressBar(0, 100); // Se actualizará el max y valor al cargar enemigo
        progressBarEnemigoVida.setStringPainted(true);
        progressBarEnemigoVida.setForeground(Color.RED); // Color de la barra

        labelEnemigoInfo.setAlignmentX(Component.LEFT_ALIGNMENT);
        progressBarEnemigoVida.setAlignmentX(Component.LEFT_ALIGNMENT);

        panelEnemigo.add(labelEnemigoInfo); // <--- AÑADE EL LABEL DE INFO (nombre del ratón)
        panelEnemigo.add(progressBarEnemigoVida); // <--- AÑADE LA BARRA DE VIDA

        panelInfo.add(panelAcacia);
        panelInfo.add(panelEnemigo);
        panelPrincipal.add(panelInfo, BorderLayout.NORTH);

        // --- Panel Central: Log de la Batalla ---
        areaLog = new JTextArea();
        areaLog.setEditable(false);
        areaLog.setLineWrap(true);
        areaLog.setWrapStyleWord(true);
        areaLog.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                SwingUtilities.invokeLater(() -> {
                    areaLog.setCaretPosition(areaLog.getDocument().getLength());
                });
            }
            public void removeUpdate(javax.swing.event.DocumentEvent e) {}
            public void changedUpdate(javax.swing.event.DocumentEvent e) {}
        });
        JScrollPane scrollLog = new JScrollPane(areaLog);
        scrollLog.setBorder(BorderFactory.createTitledBorder("Registro de Batalla"));
        panelPrincipal.add(scrollLog, BorderLayout.CENTER);

        // --- Panel Inferior: Acciones y Fase ---
        JPanel panelAcciones = new JPanel(new BorderLayout());

        // Botones de acción
        JPanel panelBotonesAccion = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        botonAtacar = new JButton("Atacar");
        botonUsarItem = new JButton("Usar Objeto");
        botonHuir = new JButton("Huir (¡No recomendado!)"); // Por ahora, solo un placeholder

        panelBotonesAccion.add(botonAtacar);
        panelBotonesAccion.add(botonUsarItem);
        panelBotonesAccion.add(botonHuir);

        // Etiqueta de Fase
        labelFase = new JLabel("Fase: " + gestorJuego.getFaseActual());
        labelFase.setFont(new Font("Arial", Font.BOLD, 16));
        labelFase.setHorizontalAlignment(SwingConstants.CENTER);

        panelAcciones.add(panelBotonesAccion, BorderLayout.CENTER);
        panelAcciones.add(labelFase, BorderLayout.NORTH); // Puedes mover esto si prefieres
        panelPrincipal.add(panelAcciones, BorderLayout.SOUTH);

        add(panelPrincipal);

        // --- Inicializar y Actualizar UI ---
        actualizarUI(); // Muestra el primer enemigo y stats
        gestorJuego.getBatallaActual().getLogBatalla().lines().forEach(line -> areaLog.append(line + "\n")); // Mostrar log inicial de la batalla

        // --- Listeners de los Botones ---
        botonAtacar.addActionListener(e -> {
            if (gestorJuego.getEnemigoActual() != null && gestorJuego.getAcacia().estaViva()) {
                boolean ratonDerrotado = gestorJuego.getBatallaActual().turnoAcaciaAtaca();
                areaLog.setText(gestorJuego.getBatallaActual().getLogBatalla()); // Actualizar log

                if (ratonDerrotado) {
                    JOptionPane.showMessageDialog(this, gestorJuego.getEnemigoActual().getNombre() + " ha sido derrotado!");
                    if (!gestorJuego.avanzarSiguienteEnemigo()) { // Intenta avanzar al siguiente enemigo o fase
                        manejarFinJuego(); // Si no hay más enemigos/fases
                    } else {
                        actualizarUI();
                    }
                } else {
                    // Turno del ratón si no fue derrotado
                    boolean acaciaDerrotada = gestorJuego.getBatallaActual().turnoRatonAtaca();
                    areaLog.setText(gestorJuego.getBatallaActual().getLogBatalla()); // Actualizar log
                    actualizarUI();
                    if (acaciaDerrotada) {
                        manejarFinJuego();
                    }
                }
            } else {
                manejarFinJuego(); // Ya terminó el juego (Acacia muerta o todos los enemigos derrotados)
            }
        });

        botonUsarItem.addActionListener(e -> {
            // Mostrar los ítems disponibles para usar (solo pociones de vida por ahora)
            List<Item> pociones = acacia.getInventario().stream()
                                    .filter(item -> item.getTipo().equals("PocionVida"))
                                    .collect(Collectors.toList());

            if (pociones.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No tienes pociones de vida para usar.", "Inventario Vacío", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Crear un array de nombres para el JComboBox
            String[] nombresPociones = pociones.stream().map(Item::getNombre).toArray(String[]::new);

            JComboBox<String> comboPociones = new JComboBox<>(nombresPociones);
            int opcion = JOptionPane.showConfirmDialog(this, comboPociones, "Selecciona una poción para usar:", JOptionPane.OK_CANCEL_OPTION);

            if (opcion == JOptionPane.OK_OPTION && comboPociones.getSelectedItem() != null) {
                String nombreSeleccionado = (String) comboPociones.getSelectedItem();
                // Encontrar el objeto Item original
                Item pocionSeleccionada = pociones.stream()
                                                .filter(item -> item.getNombre().equals(nombreSeleccionado))
                                                .findFirst()
                                                .orElse(null);

                if (pocionSeleccionada != null) {
                    // Lógica para usar el ítem (asumiendo que Batalla.turnoAcaciaUsaItem maneja el tipo)
                    boolean itemUsado = gestorJuego.getBatallaActual().turnoAcaciaUsaItem(pocionSeleccionada);
                    if (itemUsado) {
                        actualizarUI();
                        // Turno del ratón después de usar el ítem
                        boolean acaciaDerrotada = gestorJuego.getBatallaActual().turnoRatonAtaca();
                        areaLog.setText(gestorJuego.getBatallaActual().getLogBatalla());
                        actualizarUI();
                        if (acaciaDerrotada) {
                            manejarFinJuego();
                        }
                    }
                }
            }
        });

        botonHuir.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "¡Acacia intentó huir! Pero el ratón es muy rápido...", "No se puede huir", JOptionPane.WARNING_MESSAGE);
            // Podrías implementar una lógica de huida con probabilidad o castigo
            // Por ahora, solo es un mensaje y el ratón ataca de nuevo.
            boolean acaciaDerrotada = gestorJuego.getBatallaActual().turnoRatonAtaca();
            areaLog.setText(gestorJuego.getBatallaActual().getLogBatalla());
            actualizarUI();
            if (acaciaDerrotada) {
                manejarFinJuego();
            }
        });
    }

    private void actualizarUI() {
    Acacia acacia = gestorJuego.getAcacia();
    Raton enemigo = gestorJuego.getEnemigoActual();

    labelAcaciaVida.setText("Vida: " + acacia.getVidaActual() + "/" + acacia.getVidaMaxima());
    progressBarAcaciaVida.setMaximum(acacia.getVidaMaxima()); // Asegurar que el máximo es correcto
    progressBarAcaciaVida.setValue(acacia.getVidaActual()); // Actualizar valor de la barra
    progressBarAcaciaVida.setString(acacia.getVidaActual() + "/" + acacia.getVidaMaxima()); // Actualizar texto de la barra

    labelAcaciaAtaqueDefensa.setText("Ataque: " + acacia.getAtaque() + " | Defensa: " + acacia.getDefensa());
    labelAcaciaOro.setText("Oro: " + acacia.getOro());
    labelFase.setText("Fase: " + gestorJuego.getFaseActual());

    if (enemigo != null) {
        labelEnemigoInfo.setText(enemigo.getNombre()); // Solo el nombre, la vida va en la barra
        progressBarEnemigoVida.setMaximum(enemigo.getVidaMaxima());
        progressBarEnemigoVida.setValue(enemigo.getVidaActual());
        progressBarEnemigoVida.setString(enemigo.getVidaActual() + "/" + enemigo.getVidaMaxima());
         // *** ACTUALIZAR IMAGEN DEL ENEMIGO AQUÍ ***
        // Por ejemplo, si tienes imágenes para diferentes ratones:
        // if (enemigo.getNombre().contains("Común")) {
        //    labelEnemigoImagen.setIcon(new ImageIcon(getClass().getResource("/recursos/raton_comun.png")));
        // } else if (enemigo.getNombre().contains("Jefe")) {
        //    labelEnemigoImagen.setIcon(new ImageIcon(getClass().getResource("/recursos/raton_jefe.png")));
        // } else {
             labelEnemigoImagen.setIcon(new ImageIcon(getClass().getResource("/recursos/raton_placeholder.png")));
        // }
    } else {
        labelEnemigoInfo.setText("No hay enemigo actual.");
        progressBarEnemigoVida.setValue(0); // Reiniciar la barra si no hay enemigo
        progressBarEnemigoVida.setString("0/0");
    }

        
        // Deshabilitar botones si el juego ha terminado
        boolean juegoActivo = !gestorJuego.estaJuegoTerminado() && gestorJuego.getEnemigoActual() != null;
        botonAtacar.setEnabled(juegoActivo);
        botonUsarItem.setEnabled(juegoActivo);
        botonHuir.setEnabled(juegoActivo);
    }

    private void manejarFinJuego() {
        if (!gestorJuego.getAcacia().estaViva()) {
            JOptionPane.showMessageDialog(this, "Acacia ha sido derrotada... ¡La tarjeta de crédito sigue en manos de los ratones!", "¡Derrota!", JOptionPane.ERROR_MESSAGE);
        } else if (gestorJuego.estaPartidaGanada()) {
             JOptionPane.showMessageDialog(this, "¡Felicidades! ¡Has derrotado al Gran Jefe Ratón y recuperado la tarjeta de crédito!", "¡Victoria!", JOptionPane.INFORMATION_MESSAGE);
        }
        
        // Volver al menú principal
            MenuPrincipalFrame menuPrincipal = new MenuPrincipalFrame(usuarioLogueado); // <--- MODIFICADO
            menuPrincipal.setVisible(true);
        dispose(); // Cierra la ventana del juego
    }
}