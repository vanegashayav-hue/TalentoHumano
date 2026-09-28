package vista;

import controlador.EmpleadoControlador;
import modelo.EmpleadoBase;
import modelo.RepositorioEmpleados;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VentanaEmpleados extends JFrame {

    private final EmpleadoControlador controlador;

    private JTextField txtCedula;
    private JTextField txtNombre;
    private JTextField txtSalario;
    private JTextField txtBonificacion;

    private JTable tabla;
    private DefaultTableModel modeloTabla;

    public VentanaEmpleados() {

        controlador = new EmpleadoControlador(new RepositorioEmpleados());

        setTitle("Gestión de Talento Humano");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        crearInterfaz();
    }

    private void crearInterfaz() {

        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelDatos = new JPanel(new GridLayout(4, 2, 5, 5));

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

        JButton btnAgregar = new JButton("Agregar");
        JButton btnBuscar = new JButton("Buscar");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnListar = new JButton("Listar");

        JPanel panelBotones = new JPanel();

        panelBotones.add(btnAgregar);
        panelBotones.add(btnBuscar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnListar);

        JPanel panelSuperior = new JPanel(new BorderLayout(10, 10));

        panelSuperior.add(panelDatos, BorderLayout.CENTER);
        panelSuperior.add(panelBotones, BorderLayout.SOUTH);

        modeloTabla = new DefaultTableModel(
                new String[]{"Cédula", "Nombre", "Tipo", "Salario Total"},
                0
        );

        tabla = new JTable(modeloTabla);

        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);
        panelPrincipal.add(new JScrollPane(tabla), BorderLayout.CENTER);

        add(panelPrincipal);

        btnAgregar.addActionListener(e -> agregar());
        btnBuscar.addActionListener(e -> buscar());
        btnActualizar.addActionListener(e -> actualizar());
        btnEliminar.addActionListener(e -> eliminar());
        btnListar.addActionListener(e -> listar());
    }

    private void agregar() {

        try {

            String cedula = txtCedula.getText();
            String nombre = txtNombre.getText();
            double salario = Double.parseDouble(txtSalario.getText());
            double bonificacion =
                    Double.parseDouble(txtBonificacion.getText());

            String mensaje = controlador.agregarAdministrativo(
                    cedula,
                    nombre,
                    salario,
                    bonificacion
            );

            JOptionPane.showMessageDialog(this, mensaje);

            listar();

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Salario y bonificación deben ser números."
            );
        }
    }

    private void buscar() {

        String cedula = txtCedula.getText();

        EmpleadoBase empleado = controlador.buscar(cedula);

        if (empleado == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Empleado no encontrado."
            );

            return;
        }

        txtNombre.setText(empleado.getNombre());
        txtSalario.setText(
                String.valueOf(empleado.getSalarioBase())
        );

        JOptionPane.showMessageDialog(
                this,
                "Empleado encontrado: " + empleado.getTipo()
        );
    }

    private void actualizar() {

        try {

            String cedula = txtCedula.getText();
            String nombre = txtNombre.getText();
            double salario = Double.parseDouble(txtSalario.getText());
            double bonificacion =
                    Double.parseDouble(txtBonificacion.getText());

            String mensaje = controlador.actualizarAdministrativo(
                    cedula,
                    nombre,
                    salario,
                    bonificacion
            );

            JOptionPane.showMessageDialog(this, mensaje);

            listar();

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Salario y bonificación deben ser números."
            );
        }
    }

    private void eliminar() {

        String cedula = txtCedula.getText();

        String mensaje = controlador.eliminar(cedula);

        JOptionPane.showMessageDialog(this, mensaje);

        listar();
    }

    private void listar() {

        modeloTabla.setRowCount(0);

        for (EmpleadoBase empleado : controlador.listarTodos()) {

            modeloTabla.addRow(new Object[]{
                    empleado.getCedula(),
                    empleado.getNombre(),
                    empleado.getTipo(),
                    empleado.calcularSalarioTotal()
            });
        }
    }
}