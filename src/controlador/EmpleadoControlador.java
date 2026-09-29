package controlador;

import modelo.EmpleadoAdministrativo;
import modelo.EmpleadoBase;
import modelo.RepositorioEmpleados;

import java.util.ArrayList;

public class EmpleadoControlador {

    public static final String[] TIPOS_EMPLEADO = {
            "Operativo", "Administrativo"
    };

    private final RepositorioEmpleados repositorio;
    private final ArrayList<String> historial;

    public EmpleadoControlador() {
        repositorio = new RepositorioEmpleados();
        historial = new ArrayList<>();

        cargarDatosPrueba();
    }

    private boolean validarCedula(String cedula) {

        if (cedula == null || cedula.isBlank()) {
            return false;
        }

        for (int i = 0; i < cedula.length(); i++) {

            char caracter = cedula.charAt(i);

            if (!Character.isDigit(caracter)) {
                return false;
            }
        }

        return true;
    }

    private boolean validarNombre(String nombre) {

        if (nombre == null || nombre.isBlank()) {
            return false;
        }

        for (int i = 0; i < nombre.length(); i++) {

            char caracter = nombre.charAt(i);

            if (!Character.isLetter(caracter) && caracter != ' ') {
                return false;
            }
        }

        return true;
    }

    private boolean esNumeroValido(String texto) {

        if (texto == null || texto.isEmpty() || texto.equals(".")) {
            return false;
        }

        int puntos = 0;

        for (int i = 0; i < texto.length(); i++) {

            char caracter = texto.charAt(i);

            if (caracter == '.') {
                puntos++;
            } else if (!Character.isDigit(caracter)) {
                return false;
            }
        }

        return puntos <= 1;
    }

    private String validar(String cedula, String nombre, String salario,
                           String tipo, String bonificacion) {

        if (!validarCedula(cedula)) {
            return "La cédula solo debe contener números.";
        }

        if (!validarNombre(nombre)) {
            return "El nombre solo debe contener letras y espacios.";
        }

        if (!esNumeroValido(salario)) {
            return "El salario debe ser un número válido.";
        }

        if (tipo.equals("Administrativo") && !esNumeroValido(bonificacion)) {
            return "La bonificación debe ser un número válido.";
        }

        return null;
    }

    private EmpleadoBase construirEmpleado(String cedula, String nombre,
                                           String salario, String tipo,
                                           String bonificacion) {

        double salarioBase = Double.parseDouble(salario);

        if (tipo.equals("Administrativo")) {

            double bono = Double.parseDouble(bonificacion);

            return new EmpleadoAdministrativo(
                    cedula,
                    nombre,
                    salarioBase,
                    bono
            );
        }

        return new EmpleadoBase(
                cedula,
                nombre,
                salarioBase
        );
    }

    public void cargarDatosPrueba() {

        String[] cedulas = {
                "1001", "1002", "1003", "1004"
        };

        String[] nombres = {
                "Ana", "Carlos", "Laura", "Pedro"
        };

        double[] salarios = {
                1500000, 1800000, 1600000, 2000000
        };

        double[] bonificaciones = {
                100000, 150000, 120000, 200000
        };

        for (int i = 0; i < cedulas.length; i++) {

            EmpleadoBase empleado;

            if (i % 2 == 0) {
                empleado = new EmpleadoBase(
                        cedulas[i],
                        nombres[i],
                        salarios[i]
                );
            } else {
                empleado = new EmpleadoAdministrativo(
                        cedulas[i],
                        nombres[i],
                        salarios[i],
                        bonificaciones[i]
                );
            }

            repositorio.agregar(empleado);
            historial.add("Agregado: " + cedulas[i]);
        }
    }


    public String agregarAdministrativo(String cedula, String nombre,
                                        double salarioBase, double bonificacion) {

        if (!validarCedula(cedula)) {
            return "La cédula solo debe contener números.";
        }

        if (!validarNombre(nombre)) {
            return "El nombre solo debe contener letras y espacios.";
        }

        if (salarioBase < 0) {
            return "El salario no puede ser negativo.";
        }

        if (bonificacion < 0) {
            return "La bonificación no puede ser negativa.";
        }

        EmpleadoAdministrativo empleado =
                new EmpleadoAdministrativo(cedula, nombre, salarioBase, bonificacion);

        if (!repositorio.agregar(empleado)) {
            return "Ya existe un empleado con esa cédula.";
        }

        historial.add("Agregado: " + cedula);

        return "Empleado agregado correctamente.";
    }

    public EmpleadoBase buscar(String cedula) {
        return repositorio.buscar(cedula);
    }

    public String actualizarAdministrativo(String cedula, String nombre,
                                           double salarioBase, double bonificacion) {

        if (!validarCedula(cedula)) {
            return "La cédula solo debe contener números.";
        }

        if (!validarNombre(nombre)) {
            return "El nombre solo debe contener letras y espacios.";
        }

        if (salarioBase < 0) {
            return "El salario no puede ser negativo.";
        }

        if (bonificacion < 0) {
            return "La bonificación no puede ser negativa.";
        }

        EmpleadoAdministrativo empleado =
                new EmpleadoAdministrativo(cedula, nombre, salarioBase, bonificacion);

        if (!repositorio.actualizar(empleado)) {
            return "No existe un empleado con esa cédula.";
        }

        historial.add("Actualizado: " + cedula);

        return "Empleado actualizado correctamente.";
    }

    public String eliminar(String cedula) {
        if (cedula == null || cedula.isBlank()) {
            return "La cédula es obligatoria.";
        }

        if (!repositorio.eliminar(cedula)) {
            return "No existe un empleado con esa cédula.";
        }

        historial.add("Eliminado: " + cedula);

        return "Empleado eliminado correctamente.";
    }

    public ArrayList<EmpleadoBase> listarTodos() {
        return repositorio.listarTodos();
    }

    public ArrayList<String> obtenerHistorial() {
        return historial;
    }

    public double calcularTotalNomina() {

        double total = 0;

        for (EmpleadoBase empleado : repositorio.listarTodos()) {
            total += empleado.calcularSalarioTotal();
        }

        return total;
    }

    public String agregarEmpleado(String cedula, String nombre, String salario,
                                  String tipo, String bonificacion) {

        String error = validar(cedula, nombre, salario, tipo, bonificacion);

        if (error != null) {
            return error;
        }

        EmpleadoBase nuevo =
                construirEmpleado(cedula, nombre, salario, tipo, bonificacion);

        if (repositorio.agregar(nuevo)) {
            historial.add("AGREGADO: " + cedula + " - " + nombre);
            return "Empleado agregado correctamente.";
        }

        return "Ya existe un empleado con la cédula " + cedula + ".";
    }

    public EmpleadoBase buscarEmpleado(String cedula) {

        historial.add("BÚSQUEDA: " + cedula);

        return repositorio.buscar(cedula);
    }

    public String actualizarEmpleado(String cedula, String nombre, String salario,
                                     String tipo, String bonificacion) {

        String error = validar(cedula, nombre, salario, tipo, bonificacion);

        if (error != null) {
            return error;
        }

        EmpleadoBase actualizado =
                construirEmpleado(cedula, nombre, salario, tipo, bonificacion);

        if (repositorio.actualizar(actualizado)) {
            historial.add("ACTUALIZADO: " + cedula + " - " + nombre);
            return "Empleado actualizado correctamente.";
        }

        return "No existe ningún empleado con la cédula " + cedula + ".";
    }

    public String eliminarEmpleado(String cedula) {

        if (repositorio.eliminar(cedula)) {
            historial.add("ELIMINADO: " + cedula);
            return "Empleado eliminado correctamente.";
        }

        return "No existe ningún empleado con la cédula " + cedula + ".";
    }

    public ArrayList<EmpleadoBase> obtenerEmpleados() {
        return repositorio.listarTodos();
    }
}
