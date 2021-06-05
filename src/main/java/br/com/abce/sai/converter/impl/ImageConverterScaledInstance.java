package br.com.abce.sai.converter.impl;

import br.com.abce.sai.converter.ImageConverter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ImageConverterScaledInstance implements ImageConverter {

    @Override
    public BufferedImage resizeImageGr(BufferedImage originalImage, int targetWidth, int targetHeight) throws IOException {
        Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_DEFAULT);
        BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);
        return outputImage;
    }
}
