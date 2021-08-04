package br.com.abce.sai.converter.impl;

import org.springframework.stereotype.Service;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class ImageCompress {

    public static final float COMPRESSION_QUALITY = 0.5f;

    public byte[] compressTo(BufferedImage sourceImage, String format) throws IOException {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageWriter writer =  ImageIO.getImageWritersByFormatName(format).next();
        ImageOutputStream ios = ImageIO.createImageOutputStream(byteArrayOutputStream);

        try {

            writer.setOutput(ios);

            ImageWriteParam param = writer.getDefaultWriteParam();
            if (param.canWriteCompressed()){
                param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                param.setCompressionQuality(COMPRESSION_QUALITY);
            }

            writer.write(null, new IIOImage(sourceImage, null, null), param);

            return byteArrayOutputStream.toByteArray();

        } finally {
            ios.close();
            writer.dispose();
        }
    }
}
