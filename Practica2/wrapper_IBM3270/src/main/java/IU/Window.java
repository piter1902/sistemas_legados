package IU;

import Render.TextRenderer;
import Scrapper.Field;
import Scrapper.S3270;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static Scrapper.S3270.TerminalMode.MODE_80_24;
import static Scrapper.S3270.TerminalType.TYPE_3278;

public class Window extends JFrame {

    private final JButton generalTask;
    private final JButton specificTask;

    public Window() {

        // Main Container (top-level)
        Container cp = getContentPane();
        cp.setLayout(new GridLayout(2, 1, 0, 5));


        // General Task button
        generalTask = new JButton();
        generalTask.setText("General Task");
        generalTask.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("General Task");
                new GeneralTaskWindow().setVisible(true);
                setVisible(false);
                dispose();
            }
        });

        // Specific Task button
        specificTask = new JButton();
        specificTask.setText("Specific Task");
        specificTask.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("Specific Task");
                new SpecificTaskWindow().setVisible(true);
                setVisible(false);
            }
        });

        cp.add(generalTask);
        cp.add(specificTask);

        setSize(400, 400);
        setTitle("Tareas");
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static String getScreenText(S3270 s3270) {
        return new TextRenderer().render(s3270.getScreen());
    }

    public static void main(String[] args) {

        new Window();
        //Connection with S3270
        System.out.println("->Conexion con mainframe.");

        S3270 prueba = new S3270("s3270", "155.210.152.51", 101, TYPE_3278, MODE_80_24);
        System.out.println("->Conexion establecida.");

        prueba.enter();
        prueba.enter();


        System.out.println("*---------------*");
        prueba.updateScreen();


//        System.out.println("->HOLA");

        prueba.tab();
        prueba.type("Grupo_02");
        prueba.tab();
        prueba.type("secreto6");
//        prueba.enter();
        prueba.submitScreen();
        prueba.updateScreen();

        System.out.println("*---------------*");
        String out = getScreenText(prueba);
        System.out.println(out);
    }
}
