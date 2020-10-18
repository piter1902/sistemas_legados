package IU;

import Models.GeneralTask;
import Render.TextRenderer;
import Scrapper.Field;
import Scrapper.S3270;
import Scrapper.S3270Singleton;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

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


    public static void main(String[] args) {

        //new Window();
        S3270Singleton s3270Singleton = S3270Singleton.getInstance();

        s3270Singleton.addGeneralTask(new GeneralTask("1902", "PRUEBA123456"));
        s3270Singleton.printGeneralTasks();

        s3270Singleton.addGeneralTask(new GeneralTask("2009", "POLLA1"));
        //s3270Singleton.print();
        s3270Singleton.printGeneralTasks();
        s3270Singleton.addGeneralTask(new GeneralTask("2209", "POLLA2"));
        s3270Singleton.printGeneralTasks();

        try {
            s3270Singleton.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
