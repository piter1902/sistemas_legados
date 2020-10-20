package Scrapper;

import Models.GeneralTask;
import Models.SpecificTask;
import Render.TextRenderer;

import java.io.Closeable;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static Scrapper.S3270.TerminalMode.MODE_80_24;
import static Scrapper.S3270.TerminalType.TYPE_3278;

public class S3270Singleton implements Closeable {
    public static final int X_TEXT = 0;
    public static final int Y_TEXT = 22;
    private static S3270Singleton instance;
    private static S3270 s3270;
    private static final int SCREEN_WIDTH_IN_CHARS = 80;

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
        if (s3270.isEOF()) {
            s3270.enter();
        }
        s3270.submitScreen();
        s3270.enter();
        try {
            Thread.sleep(150);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

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
        enter1();
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
//        temporal();
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
        enter1();
    }

    public List<GeneralTask> getGeneralTasks() {
        s3270.type("2");
        enter1();
        s3270.type("1");
        enter1();
        String out = getScreenText();

        String tasksPattern = "^TASK [0-9]+: GENERAL .*$";
        List<String> pantalla = Arrays.asList(out.split("\n"));
        List<GeneralTask> result = new ArrayList<>();
        for (String linea : pantalla) {
            if (linea.matches(tasksPattern)) {
                String[] lineaStrings = linea.split(" ");
                result.add(new GeneralTask(lineaStrings[3], lineaStrings[5]));
            }

        }
        s3270.type("3");
        enter1();
        return result;
    }

    //TODO: Borra esto por dios
    public void temporal() {
        for (Field f : s3270.getScreen().getFields()) {
            if (f.getText().contains("More")) {
                for (int i = 1; i < f.getText().length(); i++) {
                    char temp = s3270.getScreen().charAt(i, 23);
                    System.out.println(i + "-----" + temp);
                }
//                System.out.println(f.getStartX() + " --- " + f.getStartY());
//                System.out.println(f.getEndX() + " --- " + f.getEndY());
            }

        }
    }

    public void printSpecificTasks() {
        boolean end = true;
        s3270.type("2");
        enter1();
        s3270.type("2");
        enter1();

        while (end) {
            String out = getScreenText();

            String tasksPattern = "^TASK [0-9]+: SPECIFIC .*$";
            List<String> pantalla = Arrays.asList(out.split("\n"));
            for (String linea : pantalla) {
                if (linea.matches(tasksPattern)) {
                    System.out.println(linea);
                }
                if (end = linea.contains("More")) {
                    enter1();
                }
            }
        }
        s3270.type("3");
        enter1();
    }

    public List<SpecificTask> getSpecificTasks() {
        boolean end = true;
        s3270.type("2");
        enter1();
        s3270.type("2");
        enter1();
        List<SpecificTask> result = new ArrayList<>();
        while (end) {
            String out = getScreenText();

            String tasksPattern = "^TASK [0-9]+: SPECIFIC .*$";
            List<String> pantalla = Arrays.asList(out.split("\n"));
            for (String linea : pantalla) {
                if (linea.matches(tasksPattern)) {
                    String[] lineaStrings = linea.split(" ");
                    result.add(new SpecificTask(lineaStrings[3], lineaStrings[4], lineaStrings[5]));
                }
                if (end = linea.contains("More")) {
                    enter1();
                }
            }
        }
        s3270.type("3");
        enter1();
        return result;
    }

    private static final String SCREEN_SEPARATOR = "+--------------------------------------------------------------------------------+";

    public void printScreen() {
        printScreen(System.out);
    }

    public void printScreen(PrintStream stream) {

        final String[] lines = getScreenText().split("\n");
        final String blanks = "                                                                                ";
        stream.println(SCREEN_SEPARATOR);
        for (String line : lines) {
            final String fixedLine = (line + blanks).substring(0, SCREEN_WIDTH_IN_CHARS);
            stream.println(String.format("|%s|", fixedLine));

        }
        stream.println(SCREEN_SEPARATOR);
    }

    public void printScreen2(PrintStream stream) {

        String[] lines = getScreenText().split("\n");
        final String blanks = "                                                                                ";
        boolean end = false;
        stream.println(SCREEN_SEPARATOR);

        for (String line : lines) {
            end = line.equals("TOTAL TASK");
            final String fixedLine = (line + blanks).substring(0, SCREEN_WIDTH_IN_CHARS);
            stream.println(String.format("|%s|", fixedLine));
        }
        if (!end) {
            System.out.println("*...................*");
            s3270.enter();
            lines = getScreenText().split("\n");
            for (String line : lines) {
                end = line.equals("TOTAL TASK");
                final String fixedLine = (line + blanks).substring(0, SCREEN_WIDTH_IN_CHARS);
                stream.println(String.format("|%s|", fixedLine));
            }
        }
        stream.println(SCREEN_SEPARATOR);
    }

    @Override
    public void close() throws IOException {
        s3270.disconnect();
    }
}
