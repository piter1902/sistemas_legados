package es.sistemaslegados2.Wrapper_MSDOS.Repository;

import com.google.gson.Gson;
import es.sistemaslegados2.Wrapper_MSDOS.Models.Program;
import net.sourceforge.tess4j.Tesseract1;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.stereotype.Repository;

import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.util.List;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

@Repository
public class MSDOSWrapper {

    // Final Attributes
    private static final String DOSBOX_PATH = "/usr/bin/dosbox";
    private static final String WMCTRL_PATH = "/usr/bin/wmctrl";
    private static final String TRAINED_DATA = "./tessdata/spa";
    public static final int INITIAL_SLEEP = 5000;

    // Attributes
    private Robot robot;
    // Dosbox proccess
    private Process dosbox;
    private int window_x;
    private int window_y;


    public MSDOSWrapper() {
        try {
            this.robot = new Robot();
        } catch (AWTException e) {
            System.err.println("Error al inicializar el robot.");
            e.printStackTrace();
        }
    }

    public List<Program> getProgramByTape(String tape) {
        // Ejecutar DosBox
        ProcessBuilder dosbox_builder = new ProcessBuilder(DOSBOX_PATH);
        try {
            dosbox = dosbox_builder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Program> ret = new ArrayList<>();
        if (dosbox != null) {
            sleep(INITIAL_SLEEP);
            // Para buscar el programa por nombre -> option 7 y N
            RobotAdapter.type(robot, "6");
            sleep(300);
            RobotAdapter.type(robot, "\n");
            sleep(100);
            // Obtencion de las coordenadas de la aplicacion legada
            updateWindowsConst();
            //saveImage(image);
            // Escaneo OCR de la captura
            Tesseract1 ocr = new Tesseract1();
            ocr.setLanguage(TRAINED_DATA);
            String result = null;
            do {
                BufferedImage image = robot.createScreenCapture(new Rectangle(window_x, window_y, 600, 400));
                try {
                    result = ocr.doOCR(image);
                } catch (TesseractException e) {
                    e.printStackTrace();
                }
                if (result != null) {
                    for (String line : result.split("\n")) {
                        //Nº NOMBRE TIPO CINTA REGISTRO
                        line = line.replaceAll("[\t ]+", " ").trim();
                        if (line.matches("^[0-9]+ .*$")) {
//                            System.err.println(line);
                            //Es una linea de registro
                            String[] separados = line.split(" ");
                            if (separados.length >= 5) {
                                System.err.println(new Gson().toJson(separados));
                                if (separados[separados.length - 2].equals(tape)) {
                                    System.err.println(new Gson().toJson(separados));
                                    ret.add(new Program(separados[1], separados[2], separados[3], separados[4]));
                                }
                            }
                        }
                    }
                }

                RobotAdapter.type(robot, " ");
                sleep(100);
                //Se comprueba si aparece "MENU" tras dar al espacio
                image = robot.createScreenCapture(new Rectangle(window_x, window_y, 600, 400));
//                saveImage(image, "imagen_menu_" + new Random().nextInt() + ".jpg");
                try {
                    result = ocr.doOCR(image);
                } catch (TesseractException e) {
                    e.printStackTrace();
                }
            } while (!result.trim().matches("M *E *N *U") && !result.trim().contains("ACABAR"));
            System.err.println("---> Salida del bucle");
            exitDosBox();
        }
        return ret;
    }

    public List<Program> getProgramByName(String name) {
        // Ejecutar DosBox
        ProcessBuilder dosbox_builder = new ProcessBuilder(DOSBOX_PATH);
        try {
            dosbox = dosbox_builder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (dosbox != null) {
            sleep(INITIAL_SLEEP);
            // Para buscar el programa por nombre -> option 7 y N
            RobotAdapter.type(robot, "7");
            sleep(300);
            RobotAdapter.type(robot, "N\n");
            sleep(100);
            RobotAdapter.type(robot, name + "\n");
            sleep(1000);
            // Obtencion de las coordenadas de la aplicacion legada
            updateWindowsConst();
            List<Program> ret = new ArrayList<>();
            //saveImage(image);
            // Escaneo OCR de la captura
            Tesseract1 ocr = new Tesseract1();
            ocr.setLanguage(TRAINED_DATA);
            String result = null;
            // contador de maximo de intentos
            int count = 0;
            // condicion de fin
            boolean fin = false;
            // ultimo id leido
            int last_id = -1;
            do {
                // Captura de pantalla
                // TODO: cambiar las restricciones width y height
                BufferedImage image = robot.createScreenCapture(new Rectangle(window_x, window_y, 600, 400));
                try {
                    result = ocr.doOCR(image);
                    // Comprueba que en el pantallazo hay un registro. Si no lo hay -> Terminacion
                    boolean hay_programa = false;
                    for (String l : result.split("\n")) {
                        System.err.println(l);
                        l = l.trim();
                        if (l.matches("^[0-9]+ .*$")) {
                            // Salida mostrada {nR - name utilidad cinta:}
                            // La salida es un programa
                            // Parseamos la salida
                            l.replaceAll("[\t ]+", " ");
                            String[] linea = l.split(" ");
                            // Minima longitud de la lista.
                            if (linea.length >= 5) {
                                if (last_id == Integer.parseInt(linea[0])) {
                                    // Ha leido dos veces el mismo programa. Salida
                                    fin = true;
                                    break;
                                } else {
                                    ret.add(new Program(linea[2], linea[3], linea[4], linea[0]));
                                    hay_programa = true;
                                    last_id = Integer.parseInt(linea[0]);
                                }
                            }
                        } else if (l.contains("NINGUN")) {
                            fin = true;
                            break;
                        }
                    }
                    if (!hay_programa) {
                        System.err.println("NO HAY PROGRAMA");
                        fin = true;
                        break;
                    }
                } catch (TesseractException e) {
                    e.printStackTrace();
                }
                RobotAdapter.type(robot, "N\n");
                sleep(2 * 1000);
                count++;
            } while (!fin && count < 20);
            RobotAdapter.type(robot, "\n");
            sleep(100);
            RobotAdapter.type(robot, "N\n");
            sleep(100);
            exitDosBox();
            return ret;
        }
        throw new RuntimeException("Error al iniciar la aplicacion legada");
    }

    private static void sleep(int milis) {
        try {
            Thread.sleep(milis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public int getRecordNumber() {
        // Ejecutar DosBox
        ProcessBuilder dosbox_builder = new ProcessBuilder(DOSBOX_PATH);
        try {
            dosbox = dosbox_builder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (dosbox != null) {
            sleep(INITIAL_SLEEP);
            // Para obtener el nº de registros -> Option 4 y arriba a la derecha
            RobotAdapter.type(robot, "4");
            sleep(300);
            // Obtencion de las coordenadas de la aplicacion legada
            updateWindowsConst();
            // Captura de pantalla
            BufferedImage image = robot.createScreenCapture(new Rectangle(window_x, window_y, 300, 200));
            //saveImage(image);
            // Escaneo OCR de la captura
            Tesseract1 ocr = new Tesseract1();
            ocr.setLanguage(TRAINED_DATA);
            String result = null;
            try {
                result = ocr.doOCR(image);
            } catch (TesseractException e) {
                e.printStackTrace();
            }
            int numRecords = 0;
            if (result != null) {
                // Procesamiento de salida
                String[] lineas = result.split("\n");
                for (String l : lineas) {
                    if (l.contains("ARCHIVOS")) {
                        System.err.println(l);
                        numRecords = Integer.parseInt(l.split("[\t ]+")[1]);
                        // Salida del bucle
                        break;
                    }
                }
            }
            sleep(1000);
            RobotAdapter.type(robot, "\n");
            exitDosBox();
            return numRecords;
        }
        throw new RuntimeException("Error al iniciar la aplicacion legada");
    }

    private void saveImage(BufferedImage image, String filename) {
        File file = new File(filename);
        try {
            ImageIO.write(image, filename.split("\\.")[1], file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateWindowsConst() {
        ProcessBuilder wmctrl_builder = new ProcessBuilder(WMCTRL_PATH, "-lG");
        Process wmctrl = null;
        try {
            wmctrl = wmctrl_builder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (wmctrl != null) {
            BufferedReader salida = new BufferedReader(new InputStreamReader(wmctrl.getInputStream()));
            String linea = null;
            try {
                linea = salida.readLine();
                while (linea != null) {
                    // Juntamos multiples espacios en uno
                    linea = linea.replaceAll("[\t ]+", " ");
                    if (linea.contains("DOSBox")) {
                        // Es el proceso que buscamos
                        //System.err.println(linea);
                        // La forma es {id desktop x y width height}
                        String[] splitted = linea.split(" ");
                        window_x = Integer.parseInt(splitted[2]);
                        window_y = Integer.parseInt(splitted[3]);
//                        System.err.format("X: %d --- Y: %d\n", window_x, window_y);
                        // Salida del bucle
                        break;
                    } else {
                        linea = salida.readLine();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                salida.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void exitDosBox() {
        if (dosbox != null) {
            RobotAdapter.type(robot, "8");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
            RobotAdapter.type(robot, "S\n");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
            dosbox.destroy();
            dosbox = null;
        }

    }


}
