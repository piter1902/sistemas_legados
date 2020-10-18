package Scrapper;

import Models.GeneralTask;
import Models.SpecificTask;
import Render.TextRenderer;

import java.io.Closeable;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static Scrapper.S3270.TerminalMode.MODE_80_24;
import static Scrapper.S3270.TerminalType.TYPE_3278;

public class S3270Singleton implements Closeable {
    public static final int X_TEXT = 0;
    public static final int Y_TEXT = 22;
    private static S3270Singleton instance;
    private static S3270 s3270;

    private S3270Singleton() {
        s3270 = new S3270("s3270", "155.210.152.51", 101, TYPE_3278, MODE_80_24);

        s3270.enter();
        s3270.enter();

        s3270.updateScreen();

        s3270.type("grupo_02");
        s3270.typeAt("secreto6", 17, 4);
        enter2();
        s3270.type("tareas.c");
        enter2();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        s3270.updateScreen();

    }

    public static S3270Singleton getInstance() {
        if (instance == null) {
            instance = new S3270Singleton();
        }
        return instance;
    }

    private static String getScreenText() {
        return new TextRenderer().render(s3270.getScreen());
    }

    public void print() {
        String out = getScreenText();
        System.out.println(out);
    }

    private void enter2() {
        s3270.submitScreen();
        s3270.enter();
        s3270.enter();
        s3270.updateScreen();
    }

    private void enter1() {
        s3270.submitScreen();
        s3270.enter();
        s3270.updateScreen();
    }

    public void addGeneralTask(GeneralTask generalTask) {
        s3270.type("1");
        enter1();
        s3270.type("1");

        enter1();
        s3270.type(generalTask.getDate());
        enter1();
        s3270.type(generalTask.getDescription());
        enter1();
        s3270.type("3");
        enter2();
    }

    public void addSpecificTask(SpecificTask specificTask) {
        s3270.type("1");
        enter1();
        s3270.type("2");
        enter1();

        s3270.type(specificTask.getDate());
        enter1();
        s3270.type(specificTask.getName());
        enter1();
        s3270.type(specificTask.getDescription());
        enter1();
        s3270.type("3");
        enter1();
    }

    public void printGeneralTasks() {
        s3270.type("2");
        enter1();
        s3270.type("1");
        enter1();
        String out = getScreenText();

        String tasksPattern = "^TASK [0-9]+: GENERAL .*$";
        List<String> pantalla = Arrays.asList(out.split("\n"));
        for (String linea : pantalla) {
            if (linea.matches(tasksPattern)) {
                System.out.println(linea);
            }

        }
        s3270.type("3");
        enter2();
    }

    public void printSpecificTasks() {
        s3270.type("2");
        enter1();
        s3270.type("2");
        enter1();
        String out = getScreenText();

        String tasksPattern = "^TASK [0-9]+: SPECIFIC .*$";
        List<String> pantalla = Arrays.asList(out.split("\n"));
        for (String linea : pantalla) {
            if (linea.matches(tasksPattern)) {
                System.out.println(linea);
            }

        }
        s3270.type("3");
        enter2();
    }
    @Override
    public void close() throws IOException {
        s3270.disconnect();
    }
}
