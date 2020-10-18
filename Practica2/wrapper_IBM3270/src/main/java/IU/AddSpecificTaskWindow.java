package IU;

import Models.SpecificTask;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class AddSpecificTaskWindow extends JFrame {

    private final TextArea descriptionArea;
    private final TextField calendarField;
    private final TextField nameField;
    private final JButton acceptButton;

    public AddSpecificTaskWindow(List<SpecificTask> specificTasks, JTable table) {

        Container cp = getContentPane();
        cp.setLayout(new GridLayout(7, 1, 0, 0));

        // Initialize components
        calendarField = new TextField();

        nameField = new TextField();

        descriptionArea = new TextArea();

        acceptButton = new JButton("Save");
        acceptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("Saving new General Task.");
                synchronized (specificTasks) {
                    specificTasks.add(new SpecificTask(calendarField.getText(), nameField.getText(), descriptionArea.getText()));
                    table.updateUI();
                    // TODO: Edit this to save a new Specific Task in tareas.c
                }
                setVisible(false);
                dispose();
            }
        });

        // Add components to cp
        cp.add(new JLabel("Date (DDMM)"));
        cp.add(calendarField);

        cp.add(new JLabel("Name"));
        cp.add(nameField);

        cp.add(new JLabel(("Description")));
        cp.add(descriptionArea);

        cp.add(acceptButton);

        // Final operations
        setTitle("Add Specific Task");
        setSize(400, 400);
        // Close on EXIT
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);

    }
}
