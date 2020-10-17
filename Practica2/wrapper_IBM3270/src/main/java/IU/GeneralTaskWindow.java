package IU;

import Models.GeneralTask;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class GeneralTaskWindow extends JFrame {

    public static final int SLEEP_TIME = 5000;

    private final List<GeneralTask> generalTasks;
    private final Thread updater;

    private final Container leftContainer;
    private JTable table;

    private final Container rightContainer;

    /**
     * Public Constructor
     */
    public GeneralTaskWindow() {

        // TODO: Do some stuff to get DATA

        generalTasks = new ArrayList<>();

        updater = new Thread(() -> {
            for (; ; ) {
                synchronized (generalTasks){
                    System.out.println("Estoy vivo. Size del vector = " + generalTasks.size());
                    generalTasks.add(new GeneralTask(String.valueOf(new Random().nextInt(9999)), "Description"));
                    table.updateUI();
                }
                try {
                    Thread.sleep(SLEEP_TIME);
                } catch (InterruptedException e) {
                    System.out.println("Fin del thread updater");
                    break;
                }
            }
        });

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

        // Start updater Thread
        updater.start();
    }

    @Override
    public void dispose() {
        updater.interrupt();
        super.dispose();
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
                        return "Description";
                    default:
                        return "NULL";
                }
            }

            @Override
            public int getRowCount() {
                return generalTasks.size();
            }

            @Override
            public int getColumnCount() {
                return 2;
            }

            @Override
            public Object getValueAt(int i, int i1) {
                if (i1 == 0) {
                    return generalTasks.get(i).getDate();
                } else {
                    return generalTasks.get(i).getDescription();
                }
            }
        };
        table = new JTable(dataModel);
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
                new AddGeneralTaskWindow().setVisible(true);
                System.out.println("Add General Task");
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
                dispose();
            }
        });

        c.add(addTaskButton);
        c.add(exitButton);
        return c;
    }

}
