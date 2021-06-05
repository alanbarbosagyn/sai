package br.com.abce.sai.converter;

import java.awt.image.BufferedImage;
import java.io.IOException;

public interface ImageConverter {

    BufferedImage resizeImageGr(BufferedImage originalImage, int targetWidth, int targetHeight) throws IOException;
}
