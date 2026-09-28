package controlador;

import modelo.EmpleadoAdministrativo;
import modelo.EmpleadoBase;
import modelo.RepositorioEmpleados;

import java.util.ArrayList;

public class EmpleadoControlador {

    private final RepositorioEmpleados repositorio;

    public EmpleadoControlador(RepositorioEmpleados repositorio) {
        this.repositorio = repositorio;
    }

    public String agregarAdministrativo(String cedula, String nombre,
                                        double salarioBase, double bonificacion) {

        if (cedula == null || cedula.isBlank()) {
            return "La cédula es obligatoria.";
        }

        if (nombre == null || nombre.isBlank()) {
            return "El nombre es obligatorio.";
        }

        if (salarioBase < 0) {
            return "El salario no puede ser negativo.";
        }

        EmpleadoAdministrativo empleado =
                new EmpleadoAdministrativo(cedula, nombre, salarioBase, bonificacion);

        if (!repositorio.agregar(empleado)) {
            return "Ya existe un empleado con esa cédula.";
        }

        return "Empleado agregado correctamente.";
    }

    public EmpleadoBase buscar(String cedula) {
        return repositorio.buscar(cedula);
    }

    public String actualizarAdministrativo(String cedula, String nombre,
                                           double salarioBase, double bonificacion) {

        if (cedula == null || cedula.isBlank()) {
            return "La cédula es obligatoria.";
        }

        if (nombre == null || nombre.isBlank()) {
            return "El nombre es obligatorio.";
        }

        if (salarioBase < 0) {
            return "El salario no puede ser negativo.";
        }

        EmpleadoAdministrativo empleado =
                new EmpleadoAdministrativo(cedula, nombre, salarioBase, bonificacion);

        if (!repositorio.actualizar(empleado)) {
            return "No existe un empleado con esa cédula.";
        }

        return "Empleado actualizado correctamente.";
    }

    public String eliminar(String cedula) {
        if (cedula == null || cedula.isBlank()) {
            return "La cédula es obligatoria.";
        }

        if (!repositorio.eliminar(cedula)) {
            return "No existe un empleado con esa cédula.";
        }

        return "Empleado eliminado correctamente.";
    }

    public ArrayList<EmpleadoBase> listarTodos() {
        return repositorio.listarTodos();
    }
}
