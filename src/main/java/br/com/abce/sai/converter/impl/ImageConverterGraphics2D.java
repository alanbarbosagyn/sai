package br.com.abce.sai.converter.impl;

import br.com.abce.sai.converter.ImageConverter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ImageConverterGraphics2D implements ImageConverter {

    @Override
    public BufferedImage resizeImageGr(BufferedImage originalImage, int targetWidth, int targetHeight) throws IOException {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        graphics2D.dispose();
        return resizedImage;
    }
}
