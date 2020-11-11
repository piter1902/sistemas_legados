package es.sistemaslegados2.Wrapper_MSDOS.OCR;

import net.sourceforge.tess4j.Tesseract1;
import net.sourceforge.tess4j.TesseractException;

import java.awt.image.BufferedImage;

public class OCRTesseract implements OCRInterface {

    private static final String TRAINED_DATA = "./tessdata/spa";

    private Tesseract1 ocr;

    public OCRTesseract() {
        this.ocr = new Tesseract1();
        ocr.setLanguage(TRAINED_DATA);
        //OCR congifuration
        ocr.setTessVariable("preserve_interword_spaces", "1");  //De esta manera mantiene tabulaciones
        ocr.setTessVariable("user_defined_dpi", "70");
    }

    @Override
    public String getText(BufferedImage image) {
        String result = "";
        try {
            result = ocr.doOCR(image);
        } catch (TesseractException e) {
            e.printStackTrace();
        }
        return result;
    }
}
