package es.sistemaslegados2.Wrapper_MSDOS.OCR.Impl;

import com.asprise.ocr.Ocr;
import es.sistemaslegados2.Wrapper_MSDOS.OCR.OCRInterface;

import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.IOException;

public class OCRAsprise implements OCRInterface, Closeable {

    private Ocr ocr;

    public OCRAsprise() {
        // Source: https://asprise.com/royalty-free-library/java-ocr-source-code-examples-demos.html
        Ocr.setUp();
        ocr = new Ocr();
        ocr.startEngine("spa", Ocr.SPEED_FASTEST);
    }

    @Override
    public String getText(BufferedImage image) {
        return this.ocr.recognize(image, Ocr.RECOGNIZE_TYPE_ALL, Ocr.OUTPUT_FORMAT_PLAINTEXT);
    }

    @Override
    public void close() throws IOException {
        ocr.stopEngine();
    }
}
