package IU;

import Models.GeneralTask;
import Scrapper.S3270Singleton;

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
                    GeneralTask task = new GeneralTask(calendarField.getText(), descriptionArea.getText());
                    generalTasks.add(task);
                    table.updateUI();
                    // TODO: HAY QUE PROBARLO.
                    S3270Singleton s3270Singleton = S3270Singleton.getInstance();
                    s3270Singleton.addGeneralTask(task);
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
