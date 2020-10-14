package IU;

import Models.GeneralTask;
import Models.SpecificTask;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

public class SpecificTaskWindow extends JFrame {

    private final Container leftContainer;
    private final Container rightContainer;

    /**
     * Public Constructor
     */
    public SpecificTaskWindow() {

        // TODO: Do some stuff to get DATA

        // Top-level container
        Container cp = getContentPane();

        // Left / right divided
//        cp.setLayout(new GridLayout(1, 2, 20, 0));
        cp.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        leftContainer = initLeftContainer();
        rightContainer = initRightContainer();

        cp.add(leftContainer);
        cp.add(rightContainer);


        // Final operations
        setTitle("General Tasks");
        setSize(800, 800);
        // Close on EXIT
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    /**
     * Method that initializes left container.
     *
     * @return a container initialized.
     */
    private Container initLeftContainer() {
        // Return container
        Container c = new Container();
        c.setLayout(new FlowLayout());
        // Data Model for the table
        TableModel dataModel = new AbstractTableModel() {
            @Override
            public String getColumnName(int column) {
                switch (column) {
                    case 0:
                        return "Date";
                    case 1:
                        return "Name";
                    case 2:
                        return "Description";
                    default:
                        return "NULL";
                }
            }

            @Override
            public int getRowCount() {
                return 100;
            }

            @Override
            public int getColumnCount() {
                return 3;
            }

            @Override
            public Object getValueAt(int i, int i1) {
                // TODO: Remove this Mock object
                SpecificTask mock = new SpecificTask(new Date(), "Name", "Mock Object");
                if (i1 == 0) {
                    return mock.getDate();
                } else if (i1 == 1) {
                    return mock.getName();
                } else {
                    return mock.getDescription();
                }
            }
        };
        JTable table = new JTable(dataModel);
        JScrollPane scrollPane = new JScrollPane(table);
        c.add(scrollPane);
        return c;
    }

    /**
     * Method that initializes the right container.
     *
     * @return a container initialized.
     */
    private Container initRightContainer() {
        // Return container
        Container c = new Container();
        c.setLayout(new FlowLayout());
        // Add Task Button
        JButton addTaskButton = new JButton("Add Task");
        addTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("Add Specific Task");
            }
        });

        // Exit button -> Go to menu screen
        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("Exit");
                new Window().setVisible(true);
                setVisible(false);
            }
        });

        c.add(addTaskButton);
        c.add(exitButton);
        return c;
    }

}
