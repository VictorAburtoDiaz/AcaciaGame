package interfaz;

import modelo.RunHistorial;
import gestion.GestorHistorialRuns;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class HistorialRunsFrame extends JFrame {

    private GestorHistorialRuns gestorHistorialRuns;
    private JTable tablaHistorial;
    private DefaultTableModel tableModel;
    private JButton botonEliminarSeleccion; // Correcto: Declarado a nivel de clase
    private JButton botonLimpiarTodo;       // Correcto: Declarado a nivel de clase


    // Constructor que acepta un booleano para indicar si es administrador
    public HistorialRunsFrame(boolean esAdministrador) {
        super("Historial de Partidas Completadas (Runs)");
        this.gestorHistorialRuns = new GestorHistorialRuns();

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        // ¡Esto ya estaba bien! Llama a initComponents() antes de usar los botones
        initComponents(); 
        
        // Ahora sí, botonEliminarSeleccion y botonLimpiarTodo YA NO SON NULL
        botonEliminarSeleccion.setEnabled(esAdministrador);
        botonLimpiarTodo.setEnabled(esAdministrador);
        
        cargarHistorialEnTabla();
    }
    
    // Constructor sin parámetros para compatibilidad
    public HistorialRunsFrame() {
        this(false); 
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        
        JLabel tituloLabel = new JLabel("Historial de Aventuras de Acacia", SwingConstants.CENTER);
        tituloLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(tituloLabel, BorderLayout.NORTH);

        String[] columnNames = {"Fecha y Hora", "Jugador", "Resultado", "Nivel", "Oro Final", "Enemigos Derrotados"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaHistorial = new JTable(tableModel);
        tablaHistorial.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(tablaHistorial);
        add(scrollPane, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));

        // *** ¡¡¡EL CAMBIO ESTÁ EN ESTAS DOS LÍNEAS!!! ***
        // Antes: JButton botonEliminarSeleccion = new JButton("Eliminar Seleccionado");
        // Ahora: Simplemente asigna al campo de la clase:
        botonEliminarSeleccion = new JButton("Eliminar Seleccionado"); 
        botonEliminarSeleccion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tablaHistorial.getSelectedRow();
                if (selectedRow != -1) {
                    int confirm = JOptionPane.showConfirmDialog(HistorialRunsFrame.this,
                            "¿Estás seguro de que quieres eliminar esta entrada del historial?",
                            "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        gestorHistorialRuns.eliminarRun(selectedRow);
                        cargarHistorialEnTabla();
                    }
                } else {
                    JOptionPane.showMessageDialog(HistorialRunsFrame.this,
                            "Por favor, selecciona una fila para eliminar.",
                            "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        panelBotones.add(botonEliminarSeleccion);

        // *** ¡¡¡EL CAMBIO TAMBIÉN ESTÁ AQUÍ!!! ***
        // Antes: JButton botonLimpiarTodo = new JButton("Limpiar Historial Completo");
        // Ahora: Simplemente asigna al campo de la clase:
        botonLimpiarTodo = new JButton("Limpiar Historial Completo"); 
        botonLimpiarTodo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(HistorialRunsFrame.this,
                        "¡ADVERTENCIA! ¿Estás seguro de que quieres eliminar TODO el historial de partidas?",
                        "Confirmar Limpieza Total", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (confirm == JOptionPane.YES_OPTION) {
                    gestorHistorialRuns.limpiarHistorialCompleto();
                    cargarHistorialEnTabla();
                    JOptionPane.showMessageDialog(HistorialRunsFrame.this,
                            "El historial ha sido limpiado completamente.",
                            "Historial Limpiado", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        panelBotones.add(botonLimpiarTodo);

        JButton botonCerrar = new JButton("Cerrar");
        botonCerrar.addActionListener(e -> dispose());
        panelBotones.add(botonCerrar);

        add(panelBotones, BorderLayout.SOUTH);
    }

    private void cargarHistorialEnTabla() {
        tableModel.setRowCount(0);
        List<RunHistorial> historial = gestorHistorialRuns.getHistorial();
        
        historial.sort((r1, r2) -> r2.getFechaHora().compareTo(r1.getFechaHora()));

        for (RunHistorial run : historial) {
            Object[] rowData = {
                run.getFechaHoraFormateada(),
                run.getNombreUsuario(),
                run.getResultado(),
                run.getNivelAlcanzado(),
                run.getOroGanado(),
                run.getEnemigosDerrotados()
            };
            tableModel.addRow(rowData);
        }
    }

}