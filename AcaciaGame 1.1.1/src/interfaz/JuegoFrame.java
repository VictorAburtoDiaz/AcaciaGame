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

//actualizacion de codigo para uso de items (nueva version 1.1.1).
public class JuegoFrame extends JFrame {
    private JList<Item> listaInventario; // Para mostrar el inventario
    private DefaultListModel<Item> modeloListaInventario;
    private JLabel labelArmaEquipada; // Para mostrar el arma equipada
    private JLabel labelArmaduraEquipada; // Para mostrar la armadura equipada
    private JButton botonEquiparUsarInventario; // Un botón para ambas acciones
    private GestorJuego gestorJuego;
    private JTextArea areaLog; // Para mostrar el log de la batalla
    private JLabel labelAcaciaVida;
    private JProgressBar progressBarAcaciaVida; // NOTA: se agrega una barra de vida visible !!
    private JLabel labelAcaciaAtaqueDefensa;
    private JLabel labelAcaciaOro;
    private JLabel labelEnemigoInfo;
    private JProgressBar progressBarEnemigoVida; // LA MISMA BARRA PERO PARA EL ENEMIGO IGUAL!!
    private JButton botonAtacar;
    private JButton botonUsarItem; // Se mantiene por ahora, aunque su lógica de uso se centraliza en botonEquiparUsarInventario
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

        // Panel Principal con un JSplitPane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.7); // 70% para el área de juego, 30% para inventario
        splitPane.setDividerLocation(0.7); // Posición inicial del divisor

        // --- Panel de Juego (Anterior panelPrincipal, ahora es panelCentralJuego) ---
        JPanel panelCentralJuego = new JPanel(new BorderLayout(10, 10));
        panelCentralJuego.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Mantiene el padding

        // --- Panel Superior: Información del Juego (Acacia, Enemigo, Fase) ---
        // Este panel Superior ahora contendrá un panel para las estadísticas y el label de la fase
        JPanel panelSuperior = new JPanel(new BorderLayout(10, 10));
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0)); // Padding

        // Un panel para agrupar las stats de Acacia y el Enemigo, para que estén lado a lado
        JPanel panelInfoStats = new JPanel(new GridLayout(1, 2, 10, 0));

        // Panel de Acacia
        JPanel panelAcacia = new JPanel();
        panelAcacia.setLayout(new BoxLayout(panelAcacia, BoxLayout.Y_AXIS)); // Usamos BoxLayout para organizar verticalmente
        panelAcacia.setBorder(BorderFactory.createTitledBorder("Acacia"));

        labelAcaciaImagen = new JLabel(); // Aquí podrías cargar un ImageIcon
        labelAcaciaImagen.setIcon(new ImageIcon(getClass().getResource("/recursos/acacia_placeholder.png"))); // Asegúrate de tener esta imagen o ajusta la ruta
        labelAcaciaImagen.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelAcacia.add(labelAcaciaImagen); // <--- AÑADE LA IMAGEN

        labelAcaciaVida = new JLabel("Vida: " + acacia.getVidaActual() + "/" + acacia.getVidaMaxima());
        progressBarAcaciaVida = new JProgressBar(0, acacia.getVidaMaxima()); // Rango de 0 a vida máxima
        progressBarAcaciaVida.setValue(acacia.getVidaActual()); // Valor actual
        progressBarAcaciaVida.setStringPainted(true); // Muestra el texto del porcentaje/valor
        progressBarAcaciaVida.setForeground(Color.GREEN); // Color de la barra
        
        // Alineamos los componentes a la izquierda dentro del BoxLayout
        labelAcaciaVida.setAlignmentX(Component.LEFT_ALIGNMENT);
        progressBarAcaciaVida.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panelAcacia.add(labelAcaciaVida);
        panelAcacia.add(progressBarAcaciaVida); // <--- AÑADE LA BARRA DE VIDA
        
        labelAcaciaAtaqueDefensa = new JLabel("Ataque: " + acacia.getAtaque() + " | Defensa: " + acacia.getDefensa());
        labelAcaciaOro = new JLabel("Oro: " + acacia.getOro());
        
        labelAcaciaAtaqueDefensa.setAlignmentX(Component.LEFT_ALIGNMENT);
        labelAcaciaOro.setAlignmentX(Component.LEFT_ALIGNMENT);

        panelAcacia.add(labelAcaciaAtaqueDefensa);
        panelAcacia.add(labelAcaciaOro);

        // Panel del Enemigo
        JPanel panelEnemigo = new JPanel();
        panelEnemigo.setLayout(new BoxLayout(panelEnemigo, BoxLayout.Y_AXIS)); // Usamos BoxLayout
        panelEnemigo.setBorder(BorderFactory.createTitledBorder("Enemigo Actual"));

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
        
        // Añadir los paneles de stats (Acacia y Enemigo) al panelInfoStats
        panelInfoStats.add(panelAcacia);
        panelInfoStats.add(panelEnemigo);

        // Añadir panelInfoStats al panelSuperior, en la región central de su BorderLayout
        panelSuperior.add(panelInfoStats, BorderLayout.CENTER);
        
        // Añadir el label de fase en la parte inferior del panelSuperior
        labelFase = new JLabel("Fase: " + gestorJuego.getFaseActual());
        labelFase.setFont(new Font("Arial", Font.BOLD, 16));
        labelFase.setHorizontalAlignment(SwingConstants.CENTER);
        panelSuperior.add(labelFase, BorderLayout.SOUTH); // Mover el labelFase aquí

        // Añadir el panelSuperior (que ahora contiene todo lo de arriba) al panelCentralJuego
        panelCentralJuego.add(panelSuperior, BorderLayout.NORTH); // El panelSuperior va al Norte del panelCentralJuego
        
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
        panelCentralJuego.add(scrollLog, BorderLayout.CENTER);

        // --- Panel Inferior: Acciones ---
        JPanel panelAcciones = new JPanel(new BorderLayout());

        // Botones de acción
        JPanel panelBotonesAccion = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        botonAtacar = new JButton("Atacar");
        // botonUsarItem = new JButton("Usar Objeto"); // Puedes comentar o eliminar esta línea si no lo necesitas más
        botonHuir = new JButton("Huir (¡No recomendado!)"); // Por ahora, solo un placeholder

        panelBotonesAccion.add(botonAtacar);
        // panelBotonesAccion.add(botonUsarItem); // Si el botonUsarItem se eliminó, esta línea también debe irse
        panelBotonesAccion.add(botonHuir);

        panelAcciones.add(panelBotonesAccion, BorderLayout.CENTER);
        panelCentralJuego.add(panelAcciones, BorderLayout.SOUTH); // Este panel va al Sur del panelCentralJuego
        
        // --- Panel de Inventario ---
        JPanel panelInventario = new JPanel(new BorderLayout(5, 5));
        panelInventario.setBorder(BorderFactory.createTitledBorder("Inventario y Equipo"));

        // Lista de inventario
        modeloListaInventario = new DefaultListModel<>();
        listaInventario = new JList<>(modeloListaInventario);
        listaInventario.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollInventario = new JScrollPane(listaInventario);
        panelInventario.add(scrollInventario, BorderLayout.CENTER);

        // Panel de equipo actual
        JPanel panelEquipoActual = new JPanel(new GridLayout(2, 1));
        labelArmaEquipada = new JLabel("Arma: Ninguna");
        labelArmaduraEquipada = new JLabel("Armadura: Ninguna");
        panelEquipoActual.add(labelArmaEquipada);
        panelEquipoActual.add(labelArmaduraEquipada);
        panelInventario.add(panelEquipoActual, BorderLayout.NORTH);

        // Botón para usar/equipar
        botonEquiparUsarInventario = new JButton("Usar / Equipar Seleccionado");
        panelInventario.add(botonEquiparUsarInventario, BorderLayout.SOUTH);

        // Añadir los paneles al JSplitPane
        splitPane.setLeftComponent(panelCentralJuego);
        splitPane.setRightComponent(panelInventario);

        add(splitPane); // Añadir el JSplitPane a la ventana principal

        // --- Inicializar y Actualizar UI ---
        actualizarUI(); // Muestra el primer enemigo y stats
        // Esto es para asegurar que el log de la primera batalla ya se vea
        if (gestorJuego.getBatallaActual() != null && gestorJuego.getBatallaActual().getLogBatalla() != null) {
            gestorJuego.getBatallaActual().getLogBatalla().lines().forEach(line -> areaLog.append(line + "\n"));
        }

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

        // Listener para el nuevo botón de inventario (Usar/Equipar)
        botonEquiparUsarInventario.addActionListener(e -> {
            Item itemSeleccionado = listaInventario.getSelectedValue();
            if (itemSeleccionado != null) {
                String tipo = itemSeleccionado.getTipo();
                boolean accionExitosa = false;

                if (tipo.equals("PocionVida")) {
                    accionExitosa = gestorJuego.getBatallaActual().turnoAcaciaUsaItem(itemSeleccionado);
                } else if (tipo.equals("Arma") || tipo.equals("Armadura")) {
                    accionExitosa = gestorJuego.getAcacia().equiparItem(itemSeleccionado);
                    // Si se equipa, el ítem es removido del inventario por Acacia.equiparItem()
                    // Solo necesitamos actualizar la UI.
                } else {
                    JOptionPane.showMessageDialog(this, "Este ítem no puede ser usado o equipado en este momento.", "Acción no Válida", JOptionPane.WARNING_MESSAGE);
                    return; // Salir si no es un tipo manejable
                }

                if (accionExitosa) {
                    // Después de usar/equipar, el ratón ataca
                    areaLog.setText(gestorJuego.getBatallaActual().getLogBatalla()); // Actualizar log con lo que pasó
                    actualizarUI(); // Actualizar vida, stats, y la lista de inventario
                    
                    if (gestorJuego.getEnemigoActual() != null && gestorJuego.getEnemigoActual().estaVivo()) { // Si el ratón sigue vivo
                        boolean acaciaDerrotada = gestorJuego.getBatallaActual().turnoRatonAtaca();
                        areaLog.setText(gestorJuego.getBatallaActual().getLogBatalla());
                        actualizarUI();
                        if (acaciaDerrotada) {
                            manejarFinJuego();
                        }
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Por favor, selecciona un ítem del inventario para usar o equipar.", "Ningún Ítem Seleccionado", JOptionPane.WARNING_MESSAGE);
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
        
        // --- Actualizar el Inventario ---
        modeloListaInventario.clear(); // Limpiar la lista actual
        for (Item item : acacia.getInventario()) {
            modeloListaInventario.addElement(item); // Añadir los ítems actuales de Acacia
        }

        // --- Actualizar el Equipo ---
        if (acacia.getArmaEquipada() != null) {
            labelArmaEquipada.setText("Arma: " + acacia.getArmaEquipada().getNombre() + " (+" + acacia.getArmaEquipada().getValorEfecto() + " Atk)");
        } else {
            labelArmaEquipada.setText("Arma: Ninguna");
        }

        if (acacia.getArmaduraEquipada() != null) {
            labelArmaduraEquipada.setText("Armadura: " + acacia.getArmaduraEquipada().getNombre() + " (+" + acacia.getArmaduraEquipada().getValorEfecto() + " Def)");
        } else {
            labelArmaduraEquipada.setText("Armadura: Ninguna");
        }
        
        //BARRAS DE VIDA
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
        // Si no usas botonUsarItem directamente, asegúrate de que no se use aquí
        // botonUsarItem.setEnabled(juegoActivo); 
        botonHuir.setEnabled(juegoActivo);
        botonEquiparUsarInventario.setEnabled(juegoActivo); // También el botón de inventario
    }

    private void manejarFinJuego() {
        if (!gestorJuego.getAcacia().estaViva()) {
            JOptionPane.showMessageDialog(this, "Acacia ha sido derrotada... ¡La tarjeta de crédito sigue en manos de los ratones!", "¡Derrota!", JOptionPane.ERROR_MESSAGE);
        } else if (gestorJuego.estaPartidaGanada()) {
            JOptionPane.showMessageDialog(this, "¡Felicidades! ¡Has derrotado al Gran Jefe Ratón y recuperado la tarjeta de crédito!", "¡Victoria!", JOptionPane.INFORMATION_MESSAGE);
        }
        
        // Volver al menú principal
        MenuPrincipalFrame menuPrincipal = new MenuPrincipalFrame(usuarioLogueado);
        menuPrincipal.setVisible(true);
        dispose(); // Cierra la ventana del juego
    }
}