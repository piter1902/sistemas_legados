package IU;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {

    public Window() {
        setLayout(new FlowLayout());
        add(new Label("Hola mundo"));
        setSize(400, 400);
        setTitle("Prueba");
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        new Window();
    }
}
