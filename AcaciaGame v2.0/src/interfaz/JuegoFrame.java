// src/interfaz/JuegoFrame.java
package interfaz;

import javax.swing.BoxLayout;
import javax.swing.event.DocumentListener;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.stream.Collectors;

import modelo.Usuario;
import modelo.Acacia;
import modelo.Raton;
import modelo.Item;
import gestion.GestorJuego;
import gestion.GestorPartidas;
import gestion.GestorHistorialRuns;
import modelo.RunHistorial; // Nueva importación para gestionar partidas como administrador


public class JuegoFrame extends JFrame {
    private JList<Item> listaInventario;
    private DefaultListModel<Item> modeloListaInventario;
    private JLabel labelArmaEquipada;
    private JLabel labelArmaduraEquipada;
    private JButton botonEquiparUsarInventario;
    private GestorJuego gestorJuego;
    private GestorPartidas gestorPartidas;
    private GestorHistorialRuns gestorHistorialRuns;
    private JTextArea areaLog;
    private JLabel labelAcaciaVida;
    private JProgressBar progressBarAcaciaVida;
    private JLabel labelAcaciaAtaqueDefensa;
    private JLabel labelAcaciaOro;
    private JLabel labelAcaciaNivelExperiencia; // Agregado para mostrar Nivel y XP
    private JLabel labelEnemigoInfo;
    private JProgressBar progressBarEnemigoVida;
    private JButton botonAtacar;
    private JButton botonHuir;
    private JLabel labelFase;
    private final Usuario usuarioLogueado;
    private JLabel labelAcaciaImagen;
    private JLabel labelEnemigoImagen;
    
    

    public JuegoFrame(Acacia acacia, Usuario usuario) {
        super("Aventura de Acacia - Recupera la Tarjeta");
        this.usuarioLogueado = usuario;
        this.gestorJuego = new GestorJuego(acacia); // El constructor de GestorJuego ya inicia la primera batalla.
        this.gestorPartidas = new GestorPartidas();
        this.gestorHistorialRuns = new GestorHistorialRuns();
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1080, 720);
        setLocationRelativeTo(null);
        setResizable(false);

        // Panel Principal con un JSplitPane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.7);
        splitPane.setDividerLocation(0.7); // Ajustado para un mejor balance inicial

        // --- Panel de Juego (Anterior panelPrincipal, ahora es panelCentralJuego) ---
        JPanel panelCentralJuego = new JPanel(new BorderLayout(10, 10));
        panelCentralJuego.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- Panel Superior: Información del Juego (Acacia, Enemigo, Fase) ---
        JPanel panelSuperior = new JPanel(new BorderLayout(10, 10));
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JPanel panelInfoStats = new JPanel(new GridLayout(1, 2, 10, 0));

        // Panel de Acacia
        JPanel panelAcacia = new JPanel();
        panelAcacia.setLayout(new BoxLayout(panelAcacia, BoxLayout.Y_AXIS));
        panelAcacia.setBorder(BorderFactory.createTitledBorder("Acacia"));

        labelAcaciaImagen = new JLabel();
        labelAcaciaImagen.setIcon(new ImageIcon(getClass().getResource("/recursos/acacia_placeholder.png")));
        labelAcaciaImagen.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelAcacia.add(labelAcaciaImagen);

        labelAcaciaVida = new JLabel("Vida: " + acacia.getVidaActual() + "/" + acacia.getVidaMaxima());
        progressBarAcaciaVida = new JProgressBar(0, acacia.getVidaMaxima());
        progressBarAcaciaVida.setValue(acacia.getVidaActual());
        progressBarAcaciaVida.setStringPainted(true);
        progressBarAcaciaVida.setForeground(Color.GREEN);
        
        labelAcaciaVida.setAlignmentX(Component.LEFT_ALIGNMENT);
        progressBarAcaciaVida.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panelAcacia.add(labelAcaciaVida);
        panelAcacia.add(progressBarAcaciaVida);
        
        labelAcaciaAtaqueDefensa = new JLabel("Ataque: " + acacia.getAtaque() + " | Defensa: " + acacia.getDefensa());
        labelAcaciaOro = new JLabel("Oro: " + acacia.getOro());
        // Agregado label para Nivel y Experiencia
        labelAcaciaNivelExperiencia = new JLabel("Nivel: " + acacia.getNivel() + " | XP: " + acacia.getExperienciaActual() + "/" + acacia.getExperienciaParaSiguienteNivel());
        
        labelAcaciaAtaqueDefensa.setAlignmentX(Component.LEFT_ALIGNMENT);
        labelAcaciaOro.setAlignmentX(Component.LEFT_ALIGNMENT);
        labelAcaciaNivelExperiencia.setAlignmentX(Component.LEFT_ALIGNMENT); // Alineación
        
        panelAcacia.add(labelAcaciaAtaqueDefensa);
        panelAcacia.add(labelAcaciaOro);
        panelAcacia.add(labelAcaciaNivelExperiencia); // Añadir al panel

        // Panel del Enemigo
        JPanel panelEnemigo = new JPanel();
        panelEnemigo.setLayout(new BoxLayout(panelEnemigo, BoxLayout.Y_AXIS));
        panelEnemigo.setBorder(BorderFactory.createTitledBorder("Enemigo Actual"));

        labelEnemigoImagen = new JLabel();
        labelEnemigoImagen.setIcon(new ImageIcon(getClass().getResource("/recursos/raton_placeholder.png")));
        labelEnemigoImagen.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelEnemigo.add(labelEnemigoImagen);

        labelEnemigoInfo = new JLabel("Esperando enemigo...");
        progressBarEnemigoVida = new JProgressBar(0, 100);
        progressBarEnemigoVida.setStringPainted(true);
        progressBarEnemigoVida.setForeground(Color.RED);
        
        labelEnemigoInfo.setAlignmentX(Component.LEFT_ALIGNMENT);
        progressBarEnemigoVida.setAlignmentX(Component.LEFT_ALIGNMENT);

        panelEnemigo.add(labelEnemigoInfo);
        panelEnemigo.add(progressBarEnemigoVida);
        
        panelInfoStats.add(panelAcacia);
        panelInfoStats.add(panelEnemigo);

        panelSuperior.add(panelInfoStats, BorderLayout.CENTER);
        
        labelFase = new JLabel("Fase: " + gestorJuego.getFaseActual());
        labelFase.setFont(new Font("Arial", Font.BOLD, 16));
        labelFase.setHorizontalAlignment(SwingConstants.CENTER);
        panelSuperior.add(labelFase, BorderLayout.SOUTH);

        panelCentralJuego.add(panelSuperior, BorderLayout.NORTH);
        
        // --- Panel Central: Log de la Batalla ---
        areaLog = new JTextArea();
        areaLog.setEditable(false);
        areaLog.setLineWrap(true);
        areaLog.setWrapStyleWord(true);
        // Listener para que el scroll se mantenga abajo automáticamente
        areaLog.getDocument().addDocumentListener(new DocumentListener() {
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

        JPanel panelBotonesAccion = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        botonAtacar = new JButton("Atacar");
        botonHuir = new JButton("Huir (¡No recomendado!)");

        panelBotonesAccion.add(botonAtacar);
        panelBotonesAccion.add(botonHuir);
         JButton botonGuardar = new JButton("Guardar Partida");
        panelBotonesAccion.add(botonGuardar); // Añadir el nuevo botón

        botonGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Llama al método guardarPartida del gestorPartidas
                // Usa 'JuegoFrame.this.acacia' para referirte a la instancia de Acacia de este frame
                if (gestorPartidas.guardarPartida(JuegoFrame.this.gestorJuego.getAcacia())) {
                    JOptionPane.showMessageDialog(JuegoFrame.this,
                            "¡Partida guardada exitosamente!",
                            "Guardar Partida",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(JuegoFrame.this,
                            "Error al guardar la partida.",
                            "Guardar Partida",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        // --- FIN DE LA MODIFICACIÓN ---

        panelAcciones.add(panelBotonesAccion, BorderLayout.CENTER);
        panelCentralJuego.add(panelAcciones, BorderLayout.SOUTH);
        
        // --- Panel de Inventario ---
        JPanel panelInventario = new JPanel(new BorderLayout(5, 5));
        panelInventario.setBorder(BorderFactory.createTitledBorder("Inventario y Equipo"));

        modeloListaInventario = new DefaultListModel<>();
        listaInventario = new JList<>(modeloListaInventario);
        listaInventario.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollInventario = new JScrollPane(listaInventario);
        panelInventario.add(scrollInventario, BorderLayout.CENTER);

        JPanel panelEquipoActual = new JPanel(new GridLayout(2, 1));
        labelArmaEquipada = new JLabel("Arma: Ninguna");
        labelArmaduraEquipada = new JLabel("Armadura: Ninguna");
        panelEquipoActual.add(labelArmaEquipada);
        panelEquipoActual.add(labelArmaduraEquipada);
        panelInventario.add(panelEquipoActual, BorderLayout.NORTH);

        botonEquiparUsarInventario = new JButton("Usar / Equipar Seleccionado");
        panelInventario.add(botonEquiparUsarInventario, BorderLayout.SOUTH);

        splitPane.setLeftComponent(panelCentralJuego);
        splitPane.setRightComponent(panelInventario);

        add(splitPane);

        // --- Inicializar y Actualizar UI ---
        // Actualiza la UI para mostrar el estado inicial del juego (primer enemigo)
        actualizarUI();
        // Muestra el log inicial de la batalla (ej. "Enemigo salvaje aparece")
        if (gestorJuego.getBatallaActual() != null) {
            areaLog.setText(gestorJuego.getBatallaActual().getLogBatalla());
        }

        // --- Listeners de los Botones ---
        botonAtacar.addActionListener(e -> {
            if (gestorJuego.estaJuegoTerminado()) {
                manejarFinJuego();
                return;
            }

            if (gestorJuego.getEnemigoActual() != null && gestorJuego.getAcacia().estaViva()) {
                boolean ratonDerrotado = gestorJuego.getBatallaActual().turnoAcaciaAtaca();
                areaLog.setText(gestorJuego.getBatallaActual().getLogBatalla());
                actualizarUI();

                if (ratonDerrotado) {
                    // Dar experiencia y oro al derrotar al ratón
                    Raton ratonDerrotadoActual = gestorJuego.getEnemigoActual(); // Guardar referencia antes de avanzar
                    gestorJuego.getAcacia().anadirOro(ratonDerrotadoActual.getRecompensaOro());
                    boolean subioDeNivel = gestorJuego.getAcacia().ganarExperiencia(ratonDerrotadoActual.getRecompensaXP());
                    
                    JOptionPane.showMessageDialog(this, ratonDerrotadoActual.getNombre() + " ha sido derrotado! Obtuviste " + ratonDerrotadoActual.getRecompensaOro() + " oro y " + ratonDerrotadoActual.getRecompensaXP() + " XP.");
                    if (subioDeNivel) {
                        JOptionPane.showMessageDialog(this, gestorJuego.getAcacia().getNombre() + " ha subido al Nivel " + gestorJuego.getAcacia().getNivel() + "!");
                    }

                    boolean hayProximoEnemigo = gestorJuego.avanzarSiguienteEnemigo();

                    if (gestorJuego.estaJuegoTerminado()) {
                        manejarFinJuego();
                    } else if (hayProximoEnemigo) {
                        actualizarUI();
                        areaLog.setText(gestorJuego.getBatallaActual().getLogBatalla());
                    } else {
                        // Este else-if podría indicar un estado donde se completó una fase pero no la última.
                        // Según tu diseño de GestorJuego, debería avanzar a una nueva batalla o terminar.
                        // Si llegas aquí, revisa la lógica en GestorJuego.avanzarSiguienteEnemigo().
                        System.err.println("DEBUG: Estado inesperado después de derrotar enemigo y no hay próximo.");
                        actualizarUI(); // Refrescar por si acaso
                    }
                } else {
                    // Turno del ratón si no fue derrotado
                    boolean acaciaDerrotada = gestorJuego.getBatallaActual().turnoRatonAtaca();
                    areaLog.setText(gestorJuego.getBatallaActual().getLogBatalla());
                    actualizarUI();
                    if (acaciaDerrotada) {
                        manejarFinJuego();
                    }
                }
            } else {
                manejarFinJuego();
            }
        });

        botonEquiparUsarInventario.addActionListener(e -> {
            if (gestorJuego.estaJuegoTerminado()) {
                manejarFinJuego();
                return;
            }

            Item itemSeleccionado = listaInventario.getSelectedValue();
            if (itemSeleccionado != null) {
                String tipo = itemSeleccionado.getTipo();
                boolean accionExitosa = false;

                if (tipo.equals("PocionVida")) {
                    accionExitosa = gestorJuego.getBatallaActual().turnoAcaciaUsaItem(itemSeleccionado);
                }
                else if (tipo.equals("Arma") || tipo.equals("Armadura")) {
                    // Verificar si el ítem ya está equipado para permitir desequipar
                    if (itemSeleccionado.equals(gestorJuego.getAcacia().getArmaEquipada()) || 
                        itemSeleccionado.equals(gestorJuego.getAcacia().getArmaduraEquipada())) {
                        
                        // Si el ítem ya está equipado y está en el inventario, es porque
                        // el inventario no se actualizó correctamente al equipar,
                        // o lo estamos desequipando y volviendo a poner.
                        // La lógica de equipar/desequipar en Acacia ya maneja el añadir/remover del inventario.
                        // Aquí solo necesitamos asegurarnos de llamar al método correcto.
                        
                        // Si el item seleccionado es el arma equipada, desequípala.
                        if (itemSeleccionado.equals(gestorJuego.getAcacia().getArmaEquipada())) {
                            gestorJuego.getAcacia().desequiparItem(itemSeleccionado);
                            accionExitosa = true; // Desequipar es una acción exitosa
                            JOptionPane.showMessageDialog(this, itemSeleccionado.getNombre() + " desequipado.");
                        } 
                        // Si el item seleccionado es la armadura equipada, desequípala.
                        else if (itemSeleccionado.equals(gestorJuego.getAcacia().getArmaduraEquipada())) {
                            gestorJuego.getAcacia().desequiparItem(itemSeleccionado);
                            accionExitosa = true;
                            JOptionPane.showMessageDialog(this, itemSeleccionado.getNombre() + " desequipado.");
                        } else {
                            // Este caso no debería darse si el item está en el inventario y no es consumible
                            // significa que es un arma/armadura que no está equipada actualmente.
                            accionExitosa = gestorJuego.getAcacia().equiparItem(itemSeleccionado);
                            if (accionExitosa) {
                                JOptionPane.showMessageDialog(this, itemSeleccionado.getNombre() + " equipado.");
                            }
                        }

                    } else { // El ítem no está equipado, intentar equiparlo
                        accionExitosa = gestorJuego.getAcacia().equiparItem(itemSeleccionado);
                        if (accionExitosa) {
                            JOptionPane.showMessageDialog(this, itemSeleccionado.getNombre() + " equipado.");
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Este ítem no puede ser usado o equipado en este momento.", "Acción no Válida", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (accionExitosa) {
                    // Después de usar/equipar, el ratón ataca si aún está vivo y Acacia también
                    areaLog.setText(gestorJuego.getBatallaActual().getLogBatalla());
                    actualizarUI();
                    
                    if (gestorJuego.getAcacia().estaViva() && gestorJuego.getEnemigoActual() != null && gestorJuego.getEnemigoActual().estaViva()) {
                        boolean acaciaDerrotada = gestorJuego.getBatallaActual().turnoRatonAtaca();
                        areaLog.setText(gestorJuego.getBatallaActual().getLogBatalla());
                        actualizarUI();
                        if (acaciaDerrotada) {
                            manejarFinJuego();
                        }
                    } else if (gestorJuego.getEnemigoActual() != null && !gestorJuego.getEnemigoActual().estaViva()){
                        // Si por algún motivo usar un ítem derrota al ratón (ej. una bomba, aunque no la tienes implementada)
                        // entonces tendrías que llamar a la lógica de avance de enemigo aquí.
                        // Para tu diseño actual, usar un ítem no derrota al ratón, solo ayuda a Acacia.
                        // Por lo tanto, este else if es más bien un placeholder por si acaso.
                        System.err.println("DEBUG: El enemigo fue derrotado por un ítem. Esto no debería pasar con las pociones/equipos actuales.");
                        boolean hayProximoEnemigo = gestorJuego.avanzarSiguienteEnemigo();
                        if (gestorJuego.estaJuegoTerminado()) {
                            manejarFinJuego();
                        } else if (hayProximoEnemigo) {
                            actualizarUI();
                            areaLog.setText(gestorJuego.getBatallaActual().getLogBatalla());
                        }
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Por favor, selecciona un ítem del inventario para usar o equipar.", "Ningún Ítem Seleccionado", JOptionPane.WARNING_MESSAGE);
            }
        });

        botonHuir.addActionListener(e -> {
            if (gestorJuego.estaJuegoTerminado()) {
                manejarFinJuego();
                return;
            }

            JOptionPane.showMessageDialog(this, "¡Acacia intentó huir! Pero el ratón es muy rápido...", "No se puede huir", JOptionPane.WARNING_MESSAGE);
            // El ratón ataca de nuevo después de un intento fallido de huida
            if (gestorJuego.getEnemigoActual() != null && gestorJuego.getAcacia().estaViva()) {
                boolean acaciaDerrotada = gestorJuego.getBatallaActual().turnoRatonAtaca();
                areaLog.setText(gestorJuego.getBatallaActual().getLogBatalla());
                actualizarUI();
                if (acaciaDerrotada) {
                    manejarFinJuego();
                }
            } else {
                // Si el enemigo no existe o Acacia ya estaba muerta al intentar huir, simplemente maneja el fin.
                manejarFinJuego();
            }
        });
    }

    private void actualizarUI() {
        Acacia acacia = gestorJuego.getAcacia();
        Raton enemigo = gestorJuego.getEnemigoActual();
        
        // --- Actualizar el Inventario ---
        modeloListaInventario.clear();
        for (Item item : acacia.getInventario()) {
            modeloListaInventario.addElement(item);
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
        
        // BARRAS DE VIDA Y STATS DE ACACIA
        labelAcaciaVida.setText("Vida: " + acacia.getVidaActual() + "/" + acacia.getVidaMaxima());
        progressBarAcaciaVida.setMaximum(acacia.getVidaMaxima());
        progressBarAcaciaVida.setValue(acacia.getVidaActual());
        progressBarAcaciaVida.setString(acacia.getVidaActual() + "/" + acacia.getVidaMaxima());

        labelAcaciaAtaqueDefensa.setText("Ataque: " + acacia.getAtaque() + " | Defensa: " + acacia.getDefensa());
        labelAcaciaOro.setText("Oro: " + acacia.getOro());
        labelAcaciaNivelExperiencia.setText("Nivel: " + acacia.getNivel() + " | XP: " + acacia.getExperienciaActual() + "/" + acacia.getExperienciaParaSiguienteNivel());
        labelFase.setText("Fase: " + gestorJuego.getFaseActual());

        // INFO DEL ENEMIGO
        if (enemigo != null) {
            labelEnemigoInfo.setText(enemigo.getNombre());
            progressBarEnemigoVida.setMaximum(enemigo.getVidaMaxima());
            progressBarEnemigoVida.setValue(enemigo.getVidaActual());
            progressBarEnemigoVida.setString(enemigo.getVidaActual() + "/" + enemigo.getVidaMaxima());
            // --- ACTUALIZAR IMAGEN DEL ENEMIGO AQUÍ (ejemplo basado en nombre) ---
            if (enemigo.getNombre().contains("Común")) {
                labelEnemigoImagen.setIcon(new ImageIcon(getClass().getResource("/recursos/raton_comun.png")));
            } else if (enemigo.getNombre().contains("Robusto")) {
                labelEnemigoImagen.setIcon(new ImageIcon(getClass().getResource("/recursos/raton_robusto.png")));
            } else if (enemigo.getNombre().contains("Jefe")) {
                labelEnemigoImagen.setIcon(new ImageIcon(getClass().getResource("/recursos/raton_jefe.png")));
            } else {
                labelEnemigoImagen.setIcon(new ImageIcon(getClass().getResource("/recursos/raton_placeholder.png")));
            }
        } else {
            labelEnemigoInfo.setText("No hay enemigo actual.");
            progressBarEnemigoVida.setValue(0);
            progressBarEnemigoVida.setString("0/0");
            labelEnemigoImagen.setIcon(null); // O un placeholder de "no enemigo"
        }
        
        // Deshabilitar botones si el juego ha terminado o no hay enemigo activo
        boolean juegoActivo = !gestorJuego.estaJuegoTerminado() && gestorJuego.getEnemigoActual() != null;
        botonAtacar.setEnabled(juegoActivo);
        botonHuir.setEnabled(juegoActivo);
        botonEquiparUsarInventario.setEnabled(juegoActivo);

        // Si el juego ha terminado, asegura que los botones estén deshabilitados
        if (gestorJuego.estaJuegoTerminado()) {
            botonAtacar.setEnabled(false);
            botonHuir.setEnabled(false);
            botonEquiparUsarInventario.setEnabled(false);
        }
    }

    private void manejarFinJuego() {
        String mensaje;
        String titulo;
        int tipoMensaje;
        String resultadoRun;
        
        botonAtacar.setEnabled(false);
        botonHuir.setEnabled(false);
        botonEquiparUsarInventario.setEnabled(false);

        if (gestorJuego.estaPartidaGanada()) {
            mensaje = "¡Felicidades, has derrotado a todos los ratones y recuperado la tarjeta de crédito!\n" +
                      "Ganaste " + gestorJuego.getAcacia().getOro() + " de oro en esta run.\n" +
                      "Acacia alcanzó el Nivel " + gestorJuego.getAcacia().getNivel() + ".\n" +
                      "¿Deseas iniciar una nueva run con tu oro actual y tus stats de nivel?";
            titulo = "¡VICTORIA COMPLETA!";
            tipoMensaje = JOptionPane.INFORMATION_MESSAGE;
            resultadoRun = "Victoria";
        } else {
            mensaje = "¡Acacia ha sido derrotada!\n" +
                      "Acumulaste " + gestorJuego.getAcacia().getOro() + " de oro en esta run.\n" +
                      "Acacia alcanzó el Nivel " + gestorJuego.getAcacia().getNivel() + ".\n" +
                      "¿Deseas intentar una nueva run con tu oro actual y tus stats de nivel?";
            titulo = "Game Over";
            tipoMensaje = JOptionPane.ERROR_MESSAGE;
            resultadoRun = "Derrota";
        }
        // --- REGISTRAR LA RUN EN EL HISTORIAL ---
        RunHistorial nuevaRun = new RunHistorial(
        usuarioLogueado.getNombreUsuario(), // O el nombre del jugador si es diferente
        resultadoRun,
        gestorJuego.getAcacia().getNivel(),
        gestorJuego.getAcacia().getOro(), // Oro al finalizar la run
        gestorJuego.getTotalEnemigosDerrotados() // Necesitarás un getter para esto en GestorJuego
    );
    gestorHistorialRuns.añadirRun(nuevaRun);
    // ----------------------------------------

        int opcion = JOptionPane.showConfirmDialog(this,
            mensaje, titulo, JOptionPane.YES_NO_OPTION, tipoMensaje);

        if (opcion == JOptionPane.YES_OPTION) {
            // El usuario quiere jugar de nuevo (una nueva "run")
            // Reestablecer la vida de Acacia a su máximo (Acacia mantiene su nivel y oro acumulado)
            gestorJuego.getAcacia().restaurarVidaCompleta();
            
            // Abrir la tienda para que el jugador gaste su oro acumulado y se prepare
            abrirTienda();
            
            // La lógica de reiniciarProgresoBatalla() en GestorJuego
            // y la limpieza del log se ejecutarán cuando la TiendaFrame se cierre, en el WindowListener.
            
        } else {
            // El jugador no quiere jugar otra run, volver al menú principal
            JOptionPane.showMessageDialog(this, "Gracias por jugar. ¡Hasta la próxima aventura!", "Fin del Juego", JOptionPane.INFORMATION_MESSAGE);
            MenuPrincipalFrame menuPrincipal = new MenuPrincipalFrame(usuarioLogueado);
            menuPrincipal.setVisible(true);
            dispose(); // Cierra la ventana del juego
        }
    }

    private void abrirTienda() {
        this.setEnabled(false); // Deshabilita la ventana principal
        TiendaFrame tiendaFrame = new TiendaFrame(gestorJuego.getAcacia(), usuarioLogueado);
        tiendaFrame.setVisible(true);

        tiendaFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent windowEvent) {
                JuegoFrame.this.setEnabled(true); // Habilita de nuevo la ventana principal
                JuegoFrame.this.requestFocus(); // Devuelve el foco

                // Después de que la tienda se cierra, reiniciamos el progreso de la batalla
                // (esto incluye generar nuevos enemigos para la fase 1)
                gestorJuego.reiniciarProgresoBatalla();
                // Limpiar el log de batalla anterior y mostrar el nuevo mensaje del primer enemigo
                gestorJuego.getBatallaActual().limpiarLog();
                areaLog.setText(gestorJuego.getBatallaActual().getLogBatalla());
                
                // Actualizar todos los elementos de la UI para la nueva run
                actualizarUI();
            }
        });
    }
}