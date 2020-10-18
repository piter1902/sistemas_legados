package IU;

import Models.GeneralTask;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class AddGeneralTaskWindow extends JFrame {

    private final TextArea descriptionArea;
    private final TextField calendarField;
    private final JButton acceptButton;

    public AddGeneralTaskWindow(List<GeneralTask> generalTasks, JTable table) {

        Container cp = getContentPane();
        cp.setLayout(new GridLayout(5, 1, 0, 0));

        // Initialize components
        calendarField = new TextField();

        descriptionArea = new TextArea();

        acceptButton = new JButton("Save");
        acceptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("Saving new General Task.");
                synchronized (generalTasks) {
                    generalTasks.add(new GeneralTask(calendarField.getText(), descriptionArea.getText()));
                    table.updateUI();
                    // TODO: Edit this to save a new General Task in tareas.c
                }
                setVisible(false);
                dispose();
            }
        });

        // Add components to cp
        cp.add(new JLabel("Date (DDMM)"));
        cp.add(calendarField);

        cp.add(new JLabel(("Description")));
        cp.add(descriptionArea);

        cp.add(acceptButton);

        // Final operations
        setTitle("Add General Task");
        setSize(400, 400);
        // Close on EXIT
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);

    }
}
