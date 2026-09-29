package vista;

import controlador.EmpleadoControlador;
import modelo.EmpleadoAdministrativo;
import modelo.EmpleadoBase;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VentanaEmpleados extends JFrame {

    private final EmpleadoControlador controlador;

    private JTextField txtCedula;
    private JTextField txtNombre;
    private JTextField txtSalario;
    private JTextField txtBonificacion;

    private JComboBox<String> cmbTipo;

    private JTable tabla;
    private DefaultTableModel modeloTabla;

    public VentanaEmpleados() {

        controlador = new EmpleadoControlador();

        setTitle("Gestión de Talento Humano");
        setSize(900, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        crearInterfaz();
    }

    private void crearInterfaz() {

        setLayout(new BorderLayout(10, 10));

        // =========================
        // PANEL DE DATOS
        // =========================

        JPanel panelDatos = new JPanel(new GridLayout(5, 2, 5, 5));

        panelDatos.add(new JLabel("Cédula:"));
        txtCedula = new JTextField();
        panelDatos.add(txtCedula);

        panelDatos.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        panelDatos.add(txtNombre);

        panelDatos.add(new JLabel("Salario base:"));
        txtSalario = new JTextField();
        panelDatos.add(txtSalario);

        panelDatos.add(new JLabel("Bonificación:"));
        txtBonificacion = new JTextField();
        panelDatos.add(txtBonificacion);

        panelDatos.add(new JLabel("Tipo:"));
        cmbTipo = new JComboBox<>(EmpleadoControlador.TIPOS_EMPLEADO);
        panelDatos.add(cmbTipo);

        // =========================
        // BOTONES
        // =========================

        JButton btnAgregar = new JButton("Agregar");
        JButton btnBuscar = new JButton("Buscar");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnListar = new JButton("Listar");
        JButton btnHistorial = new JButton("Historial");
        JButton btnNomina = new JButton("Total Nómina");

        JPanel panelBotones = new JPanel();

        panelBotones.add(btnAgregar);
        panelBotones.add(btnBuscar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnListar);
        panelBotones.add(btnHistorial);
        panelBotones.add(btnNomina);

        // =========================
        // TABLA
        // =========================

        modeloTabla = new DefaultTableModel(
                new String[]{"Cédula", "Nombre", "Tipo", "Salario Total"},
                0
        ) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tabla = new JTable(modeloTabla);

        tabla.setRowHeight(28);
        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tabla.setFillsViewportHeight(true);

        // Encabezado de la tabla
        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.getTableHeader().setResizingAllowed(true);

        // Alinear los datos de la tabla
        DefaultTableCellRenderer centrado = new DefaultTableCellRenderer();
        centrado.setHorizontalAlignment(SwingConstants.CENTER);

        tabla.getColumnModel().getColumn(0).setCellRenderer(centrado);
        tabla.getColumnModel().getColumn(2).setCellRenderer(centrado);
        tabla.getColumnModel().getColumn(3).setCellRenderer(centrado);

        // Anchos iniciales
        tabla.getColumnModel().getColumn(0).setPreferredWidth(100);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(250);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(150);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(180);

        // =========================
        // PANEL SUPERIOR
        // =========================

        JPanel panelSuperior = new JPanel(new BorderLayout(5, 5));

        panelSuperior.add(panelDatos, BorderLayout.NORTH);
        panelSuperior.add(panelBotones, BorderLayout.SOUTH);

        // =========================
        // AGREGAR COMPONENTES
        // =========================

        add(panelSuperior, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // =========================
        // EVENTOS DE LOS BOTONES
        // =========================

        btnAgregar.addActionListener(e -> agregar());
        btnBuscar.addActionListener(e -> buscar());
        btnActualizar.addActionListener(e -> actualizar());
        btnEliminar.addActionListener(e -> eliminar());
        btnListar.addActionListener(e -> listar());
        btnHistorial.addActionListener(e -> mostrarHistorial());
        btnNomina.addActionListener(e -> mostrarNomina());

        // Mostrar los empleados iniciales
        listar();
    }

    // =========================
    // AGREGAR
    // =========================

    private void agregar() {

        String cedula = txtCedula.getText();
        String nombre = txtNombre.getText();
        String salario = txtSalario.getText();
        String tipo = (String) cmbTipo.getSelectedItem();
        String bonificacion = txtBonificacion.getText();

        String mensaje = controlador.agregarEmpleado(
                cedula,
                nombre,
                salario,
                tipo,
                bonificacion
        );

        JOptionPane.showMessageDialog(this, mensaje);

        listar();
    }

    // =========================
    // BUSCAR
    // =========================

    private void buscar() {

        String cedula = txtCedula.getText();

        EmpleadoBase empleado = controlador.buscarEmpleado(cedula);

        if (empleado == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Empleado no encontrado."
            );
            return;
        }

        txtNombre.setText(empleado.getNombre());
        txtSalario.setText(String.valueOf(empleado.getSalarioBase()));
        cmbTipo.setSelectedItem(empleado.getTipo());

        if (empleado instanceof EmpleadoAdministrativo administrativo) {

            txtBonificacion.setText(
                    String.valueOf(administrativo.getBonificacion())
            );

        } else {

            txtBonificacion.setText("");
        }

        JOptionPane.showMessageDialog(
                this,
                "Empleado encontrado: " + empleado.getTipo()
        );
    }

    // =========================
    // ACTUALIZAR
    // =========================

    private void actualizar() {

        String cedula = txtCedula.getText();
        String nombre = txtNombre.getText();
        String salario = txtSalario.getText();
        String tipo = (String) cmbTipo.getSelectedItem();
        String bonificacion = txtBonificacion.getText();

        String mensaje = controlador.actualizarEmpleado(
                cedula,
                nombre,
                salario,
                tipo,
                bonificacion
        );

        JOptionPane.showMessageDialog(this, mensaje);

        listar();
    }

    // =========================
    // ELIMINAR
    // =========================

    private void eliminar() {

        String cedula = txtCedula.getText();

        String mensaje = controlador.eliminarEmpleado(cedula);

        JOptionPane.showMessageDialog(this, mensaje);

        listar();
    }

    // =========================
    // LISTAR
    // =========================

    private void listar() {

        modeloTabla.setRowCount(0);

        for (EmpleadoBase empleado : controlador.obtenerEmpleados()) {

            modeloTabla.addRow(new Object[]{
                    empleado.getCedula(),
                    empleado.getNombre(),
                    empleado.getTipo(),
                    empleado.calcularSalarioTotal()
            });
        }
    }

    // =========================
    // HISTORIAL
    // =========================

    private void mostrarHistorial() {

        StringBuilder texto = new StringBuilder();

        int i = 0;

        while (i < controlador.obtenerHistorial().size()) {

            texto.append(
                    controlador.obtenerHistorial().get(i)
            ).append("\n");

            i++;
        }

        if (texto.length() == 0) {
            texto.append("No hay registros en el historial.");
        }

        JOptionPane.showMessageDialog(
                this,
                texto.toString(),
                "Historial",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // =========================
    // TOTAL NÓMINA
    // =========================

    private void mostrarNomina() {

        double total = controlador.calcularTotalNomina();

        JOptionPane.showMessageDialog(
                this,
                "Total de nómina: $" + total,
                "Total Nómina",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}