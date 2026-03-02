/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frw.base.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

/**
 *
 * @author Leonardo Barros
 */
public class ImageConverterUtil {

    public static final String FORMAT_FILE = "jpg";
    public static final int HEIGHT = 1200;
    public static final int WIDTH = 825;

    public static byte[] convertImage(BufferedImage imageBuffer) {
        try {
            return convert(imageBuffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] convertImage(byte[] imageArray) {

        try {
            InputStream in = new ByteArrayInputStream(imageArray);
            BufferedImage image = ImageIO.read(in);
            return convert(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    

    public static BufferedImage convertTest( BufferedImage image){
          image = toBufferedImage(image);
        return image;
    }


    private static byte[] convert(BufferedImage image) throws IOException {

        image = toBufferedImage(image);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, FORMAT_FILE , baos);
        return baos.toByteArray();

    }
    
    private static BufferedImage toBufferedImage(Image src) {

        int wOriginal = (src.getWidth(null));
        int hOriginal = src.getHeight(null);

        double usedScale = 1;

        double scaleH = 1;

        if (hOriginal > HEIGHT) {
            scaleH = (float) HEIGHT / (float) hOriginal;
        }

        double scaleW = 1;

        if (wOriginal > WIDTH) {
            scaleW = (float) WIDTH / (float) wOriginal;
        }

        if (scaleH < scaleW) {
            usedScale = scaleH;

        } else {
            usedScale = scaleW;
        }


        wOriginal = (int) (wOriginal * usedScale);
        hOriginal = (int) (hOriginal * usedScale);


//        System.err.println("Scale = " + usedScale);
//        System.err.println("ScaleWidth = " + scaleW);
//        System.err.println("ScaleHeight = " + scaleH);
//
//
//        System.err.println("wOriginal = " + wOriginal);
//        System.err.println("hOriginal = " + hOriginal);

        int type = BufferedImage.TYPE_BYTE_GRAY;  // other options
        BufferedImage dest = new BufferedImage(wOriginal, hOriginal, type);
        Graphics2D g2 = dest.createGraphics();

        AffineTransform trans = new AffineTransform();
        trans.scale(usedScale, usedScale);

        g2.drawImage(src, trans, null);
        g2.dispose();

        return dest;

    }

    //     private static ImageOutputStream save(BufferedImage image, String ext) {
//        String fileName = "savingAnImage";
//        File file = new File(fileName + "." + ext);
//
//
//        try {
//            ImageIO.write(image, ext, stream);  // ignore returned boolean
//        } catch (IOException e) {
//            System.out.println("Write error for " + file.getPath()
//                    + ": " + e.getMessage());
//        }
//
//
//
//        System.err.println("Fle" + file.getAbsolutePath());
//    }
}
