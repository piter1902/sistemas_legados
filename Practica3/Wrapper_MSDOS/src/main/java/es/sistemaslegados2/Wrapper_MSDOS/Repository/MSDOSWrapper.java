package es.sistemaslegados2.Wrapper_MSDOS.Repository;

import com.google.gson.Gson;
import es.sistemaslegados2.Wrapper_MSDOS.Models.Program;
import es.sistemaslegados2.Wrapper_MSDOS.OCR.OCRInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Repository
public class MSDOSWrapper {

    // Final Attributes
    private static final String XTERM_PATH = "/usr/bin/xterm";
    private static final String DOSEMU_PATH = "/usr/bin/dosemu";
    private static final String WMCTRL_PATH = "/usr/bin/wmctrl";
    private static final String TRAINED_DATA = "./tessdata/spa";
    public static final int INITIAL_SLEEP = 5000;

    // Attributes
    private Robot robot;
    // Dosbox proccess
    private Process dosemu;
    private int window_x;
    private int window_y;

    // Dosemu Program
    private Scanner output;
    private PrintWriter input;

    // OCR
    @Autowired
    private OCRInterface ocr;


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
        ProcessBuilder dosbox_builder = new ProcessBuilder(DOSEMU_PATH);
        try {
            dosemu = dosbox_builder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Program> ret = new ArrayList<>();
        if (dosemu != null) {
            sleep(INITIAL_SLEEP);
            // Para buscar el programa por nombre -> option 7 y N
            RobotAdapter.type(robot, "6");
            sleep(300);
            RobotAdapter.type(robot, "\n");
            sleep(100);
            // Obtencion de las coordenadas de la aplicacion legada
            updateWindowsConst();
            //saveImage(image);
//            // Escaneo OCR de la captura
//            Tesseract1 ocr = new Tesseract1();
//            ocr.setLanguage(TRAINED_DATA);
//            //OCR congifuration
//            ocr.setTessVariable("preserve_interword_spaces", "1"); //De esta manera mantiene tabulaciones
//            ocr.setTessVariable("user_defined_dpi", "70");
            String result = null;
            sleep(1 * 1000);
            do {
                BufferedImage image = robot.createScreenCapture(new Rectangle(window_x, window_y, 640, 400));
//                saveImage(image, "imagen_menu_" + new Random().nextInt() + ".jpg");
//                try {
//                    result = ocr.doOCR(image);
//                } catch (TesseractException e) {
//                    e.printStackTrace();
//                }
                result = ocr.getText(image);
                if (result != null) {
                    for (String line : result.split("\n")) {
                        //Nº NOMBRE TIPO CINTA REGISTRO
//                      line = line.replaceAll("[\t ]+", " ").trim();
                        line = line.trim();
                        if (line.matches("^[0-9]+ .*$")) {
                            //System.err.println(line);
                            //Es una linea de registro
                            String[] separados = line.split("\\s{2,}");
                            if (separados.length >= 5) {
                                System.err.println("Tenemos: " + new Gson().toJson(separados));
                                if (separados[separados.length - 2].matches(tape + "-?")) {
                                    //Esta comprobación es igual para todos casos ya que tape esta en length - 1
                                    // WARNING: Siempre deberia entrar en case 5
                                    switch (separados.length) {
                                        case 5:
                                            System.err.println("Añadido de 5 -> " + new Gson().toJson(separados));
                                            ret.add(new Program(separados[1], separados[2], separados[3], separados[4]));
                                            break;
                                        case 6:
                                            System.err.println("Añadido de 6 -> " + new Gson().toJson(separados));
                                            String name = separados[1] + " " + separados[2];
                                            ret.add(new Program(name, separados[3], separados[4], separados[5]));
                                            break;
                                        case 7:
                                            System.err.println("Añadido de 7 -> " + new Gson().toJson(separados));
                                            name = separados[1] + " " + separados[2] + " " + separados[3];
                                            ret.add(new Program(name, separados[4], separados[5], separados[6]));
                                            break;
                                        default:
                                            System.err.println("MUCHO TEXTO POR DIOS -> " + new Gson().toJson(separados));
                                    }
                                } else {
                                    System.err.println("NO Añadido -> " + new Gson().toJson(separados) + " : " + separados[separados.length - 2]);
                                }
                            } else {
                                System.err.println("Longitud invalida " + separados.length + " --- " + new Gson().toJson(separados));
                            }
                        }
                    }
                }

                RobotAdapter.type(robot, " ");
                sleep(1 * 1000);
                //Se comprueba si aparece "MENU" tras dar al espacio
                image = robot.createScreenCapture(new Rectangle(window_x, window_y, 640, 400));
//                saveImage(image, "imagen_menu_" + new Random().nextInt() + ".jpg");
//                try {
//                    result = ocr.doOCR(image);
//                } catch (TesseractException e) {
//                    e.printStackTrace();
//                }
                result = ocr.getText(image);
            } while (!result.trim().matches("M *E *N *U") && !result.trim().contains("ACABAR"));
            System.err.println("---> Salida del bucle");
            exitDosemu();
        }
        return ret;
    }

    public List<Program> getProgramByName(String name) {
        // Ejecutar DosBox
        ProcessBuilder dosbox_builder = new ProcessBuilder(DOSEMU_PATH);
        try {
            dosemu = dosbox_builder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (dosemu != null) {
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
//            Tesseract1 ocr = new Tesseract1();
//            ocr.setLanguage(TRAINED_DATA);
//            //OCR congifuration
//            ocr.setTessVariable("preserve_interword_spaces", "1"); //De esta manera mantiene tabulaciones
//            ocr.setTessVariable("user_defined_dpi", "70");
            String result = null;
            // contador de maximo de intentos
            int count = 0;
            // condicion de fin
            boolean fin = false;
            // ultimo id leido
            int last_id = -1;
            do {
                // Captura de pantalla
                BufferedImage image = robot.createScreenCapture(new Rectangle(window_x, window_y, 600, 400));
                // result = ocr.doOCR(image);
                result = ocr.getText(image);
                // Comprueba que en el pantallazo hay un registro. Si no lo hay -> Terminacion
                boolean hay_programa = false;
                System.err.println("Resultado: " + result);
                for (String l : result.split("\n")) {
                    l = l.trim();
                    if (l.matches("^[0-9]+ .*$")) {

                        // Salida mostrada {nR - name utilidad cinta:}
                        // La salida es un programa
                        // Parseamos la salida
                        //l = l.replaceAll("[\t ]+", " ");
                        String[] linea = l.split("\\s{2,}"); //Separamos los disitntos campos
                        for (String prueba : linea) {
                            System.err.println("Parte: " + prueba);
                        }
                        if (linea.length >= 4) {
                            if (last_id == Integer.parseInt(linea[0])) {
                                // Ha leido dos veces el mismo programa. Salida
                                System.err.println("Lee dos veces el mismo programa: " + last_id + " vs " + Integer.parseInt(linea[0]));
                                fin = true;
                                break;
                            } else {
                                //Deberia meterse siempre en el caso 4. Por si acaso se ha dejado el resto de casos
                                switch (linea.length) {
                                    case 4:
                                        System.err.println("Añadido de 5 -> " + new Gson().toJson(linea));
                                        ret.add(new Program(linea[1], linea[2], linea[3], linea[0]));
                                        break;
                                    case 5:
                                        System.err.println("Añadido de 6 -> " + new Gson().toJson(linea));
                                        String prog_name = linea[1] + " " + linea[2];
                                        ret.add(new Program(prog_name, linea[3], linea[4], linea[0]));
                                        break;
                                    case 6:
                                        System.err.println("Añadido de 7 -> " + new Gson().toJson(linea));
                                        prog_name = linea[1] + " " + linea[2] + " " + linea[3];
                                        ret.add(new Program(prog_name, linea[4], linea[5], linea[0]));
                                        break;
                                }

                                hay_programa = true;
                                last_id = Integer.parseInt(linea[0]);
                            }
                            // Minima longitud de la lista.
                        } else {
                            System.err.println("Linea muy corta: " + new Gson().toJson(linea));
                        }
                    } else if (l.contains("NINGUN")) {
                        System.err.println("CONTIENE 'NINGUN' ...");
                        fin = true;
                        break;
                    }
                }
                if (!hay_programa) {
                    System.err.println("NO HAY PROGRAMA");
                    fin = true;
                    break;
                }
                RobotAdapter.type(robot, "N\n");
                sleep(2 * 1000);
                count++;
            } while (!fin && count < 20);
            RobotAdapter.type(robot, "\n");
            sleep(100);
            RobotAdapter.type(robot, "N\n");
            sleep(100);
            exitDosemu();
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
        ProcessBuilder dosbox_builder =
                new ProcessBuilder(XTERM_PATH, "-e", DOSEMU_PATH + "-t ../Database-MSDOS/Database/gwbasic.bat");
        System.err.println("Entro al proceso");
        try {
            dosemu = dosbox_builder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (dosemu != null) {
            createRedirections();
            // Enter al empezar
            input.println("");
            // Sleep ZZZZZZZZZZZ
            sleep(INITIAL_SLEEP);
            // Para obtener el nº de registros -> Option 4 y arriba a la derecha
//            RobotAdapter.type(robot, "4");
            input.println("4");
            sleep(300);
            // Obtencion de las coordenadas de la aplicacion legada
//            updateWindowsConst();
            // Captura de pantalla
//            BufferedImage image = robot.createScreenCapture(new Rectangle(window_x, window_y, 300, 200));
            //saveImage(image);
            // Escaneo OCR de la captura
//            Tesseract1 ocr = new Tesseract1();
//            ocr.setLanguage(TRAINED_DATA);
//            ocr.setTessVariable("user_defined_dpi", "70");
//            String result = null;
//            result = ocr.getText(image);
//            try {
//                result = ocr.doOCR(image);
//            } catch (TesseractException e) {
//                e.printStackTrace();
//            }
            int numRecords = 0;
//            if (result != null) {
            // Procesamiento de salida
//                String[] lineas = result.split("\n");
            String l = output.nextLine();
            System.err.println("Entro al while");
            while (l != null) {
                System.err.println(l);
                if (l.contains("ARCHIVOS")) {
                    System.err.println(l);
                    numRecords = Integer.parseInt(l.split("[\t ]+")[1]);
                    // Salida del bucle
                    break;
                }
                System.err.println("Intento leer la siguiente linea");
                System.err.println("Estado del descriptor: " + output.hasNext());
                l = output.nextLine();
            }
//            }
            sleep(1000);
//            RobotAdapter.type(robot, "\n");
            input.println("");
            exitDosemu();
            return numRecords;
        }
        throw new RuntimeException("Error al iniciar la aplicacion legada");
    }

    private void createRedirections() {
        output = new Scanner(new InputStreamReader(dosemu.getInputStream()));
        input = new PrintWriter(new OutputStreamWriter(dosemu.getOutputStream()));
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

    private void exitDosemu() {
        if (dosemu != null) {
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
            dosemu.destroy();
            dosemu = null;
        }

    }


}
