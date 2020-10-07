package IU;

import javax.swing.*;
import java.awt.*;

public class Ventana extends JFrame {

    public Ventana() {
        setLayout(new FlowLayout());
        add(new Label("Hola mundo"));
        setSize(400, 400);
        setTitle("Prueba");
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        new Ventana();
    }
}
