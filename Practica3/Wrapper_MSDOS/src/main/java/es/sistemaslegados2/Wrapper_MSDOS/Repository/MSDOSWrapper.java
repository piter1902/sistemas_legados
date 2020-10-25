package es.sistemaslegados2.Wrapper_MSDOS.Repository;

import net.sourceforge.tess4j.Tesseract1;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.stereotype.Repository;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

@Repository
public class MSDOSWrapper {

    // Final Attributes
    private static final String DOSBOX_PATH = "/usr/bin/dosbox";
    private static final String WMCTRL_PATH = "/usr/bin/wmctrl";

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

    public int getRecordNumber() {
        // Ejecutar DosBox
        ProcessBuilder dosbox_builder = new ProcessBuilder(DOSBOX_PATH);
        try {
            dosbox = dosbox_builder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (dosbox != null) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // Para obtener el nº de registros -> Option 4 y arriba a la derecha
            RobotAdapter.type(robot, "4");
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // Obtencion de las coordenadas de la aplicacion legada
            updateWindowsConst();
            // Captura de pantalla
            // TODO: cambiar las restricciones width y height
            BufferedImage image = robot.createScreenCapture(new Rectangle(window_x, window_y, 300, 200));
            //saveImage(image);
            // Escaneo OCR de la captura
            Tesseract1 ocr = new Tesseract1();
            ocr.setLanguage("./tessdata/spa");
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
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            RobotAdapter.type(robot, "\n");
            exitDosBox();
            return numRecords;
        }
        throw new RuntimeException("Error al iniciar la aplicacion legada");
    }

    private void saveImage(BufferedImage image) {
        File file = new File("imagen.jpg");
        try {
            ImageIO.write(image, "jpg", file);
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
