import vista.VentanaEmpleados;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            VentanaEmpleados ventana = new VentanaEmpleados();
            ventana.setVisible(true);
        });
    }
}
