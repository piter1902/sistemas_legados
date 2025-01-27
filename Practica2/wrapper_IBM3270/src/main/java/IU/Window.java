package IU;

import Scrapper.S3270Singleton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
        //comenzamos singleton para que aparezcan menos retrasos
        S3270Singleton.getInstance();
        new Window();
    }
}
