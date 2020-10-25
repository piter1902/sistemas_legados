package Scrapper;

import Models.GeneralTask;
import Models.SpecificTask;
import Render.TextRenderer;
import org.apache.log4j.Logger;

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
    private static final Logger logger = Logger.getLogger(S3270Singleton.class);

    private S3270Singleton() {
        s3270 = new S3270("s3270", "155.210.152.51", 101, TYPE_3278, MODE_80_24);
        sleep(2000);
        s3270.updateScreen();
        enter1();
        s3270.type("grupo_02");
        s3270.typeAt("secreto6", 17, 4);
        enter1();
        enter1();
        s3270.type("tareas.c");
        enter1();
        sleep(2000);
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

    private void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void enter2() {
        s3270.submitScreen();
        sleep(100);
        s3270.enter();
        sleep(100);
        s3270.enter();
        s3270.updateScreen();
    }

    private void enter1() {
        //Comprobamos que no se ha llegado a fin de pantalla
        if (s3270.isEOF() || s3270.isEmpty()) {
            logger.info("Double enter!");
            s3270.enter();
        }
        s3270.submitScreen();
        sleep(100);
        s3270.enter();
        sleep(150);
        //Esta segunda comprobacion arregla otros casos de fin de pantalla
        if (s3270.isEOF()) {
            logger.info("Double enter version 2!");
            s3270.enter();
            sleep(100);
        }
        s3270.updateScreen();
    }

    public void addGeneralTask(GeneralTask generalTask) {
        s3270.type("1");
        enter1();
        s3270.type("1");
        enter1();

        if (screenMatches("ASSIGN TASKS")) {
            logger.info("Acceso a agnadir tareas generales correcto...");
        } else {
            logger.info("Acceso a agnadir tareas generales FALLIDO!!.");
            printScreen();
        }
        s3270.type(generalTask.getDate());
        enter1();
        s3270.type(generalTask.getDescription());
        enter1();
        s3270.type("3");
        enter1();
        if (screenMatches("MENU PRINCIPAL")) {
            logger.info("Acceso a menu principal correcto ... ");
        } else {
            logger.info("Acceso a menu principal FALLIDO!!.");
            printScreen();
        }
    }

    public void addSpecificTask(SpecificTask specificTask) {
        s3270.type("1");
        enter1();
        s3270.type("2");
        enter1();
        if (screenMatches("ASSIGN TASKS")) {
            logger.info("Acceso a agnadir tareas especificas correcto...");
        } else {
            logger.info("Acceso a agnadir tareas especificas FALLIDO!!.");
            printScreen();
        }
        s3270.type(specificTask.getDate());
        enter1();
        s3270.type(specificTask.getName());
        enter1();
        s3270.type(specificTask.getDescription());
        enter1();
        s3270.type("3");
        enter1();
        if (screenMatches("MENU PRINCIPAL")) {
            logger.info("Acceso a menu principal correcto ... ");
        } else {
            logger.info("Acceso a menu principal FALLIDO!!.");
            printScreen();
        }
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
        if (screenMatches("VIEW TASKS")) {
            logger.info("Acceso a mostrar tareas generales correcto...");
        } else {
            logger.info("Acceso a mostrar tareas generales FALLIDO!!.");
            printScreen();
        }
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
        if (screenMatches("MENU PRINCIPAL")) {
            logger.info("Acceso a menu principal correcto ... ");
        } else {
            logger.info("Acceso a menu principal FALLIDO!!.");
            printScreen();
        }
        return result;
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
        if (screenMatches("VIEW TASKS")) {
            logger.info("Acceso a mostrar tareas especificas correcto...");
        } else {
            logger.info("Acceso a mostrar tareas especificas FALLIDO!!.");
            printScreen();
        }
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
        if (screenMatches("MENU PRINCIPAL")) {
            logger.info("Acceso a menu principal correcto ... ");
        } else {
            logger.info("Acceso a menu principal FALLIDO!!.");
            printScreen();
        }
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

    //TODO: Borra esto por dios
    public void temporal() {
        for (Field f : s3270.getScreen().getFields()) {
            if (f.getText().trim().length() == 0) {
                System.out.println("VACIA");
            }
            if (getScreenText().trim().length() == 0)
                System.out.println("VACIAAAA");

            System.out.println(f.getText());
            System.out.println(f.getStartX() + " --- " + f.getStartY());
            System.out.println(f.getEndX() + " --- " + f.getEndY());
        }
    }

    public boolean screenMatches(String match) {
        final String[] lines = getScreenText().split("\n");
        for (String line : lines) {
            if (line.contains(match)) {
                logger.info("screen MATCHES with " + match);
                return true;
            }
        }
        return false;
    }

    @Override
    public void close() throws IOException {
        s3270.disconnect();
    }
}
