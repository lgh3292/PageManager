/*
 * pic.java
 *
 * Created on 2007年7月25日, 下午5:15
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.lgh.pagemanager.pic;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Hashtable;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 *
 * @author tewang
 */
public final class Pic {

    public static BufferedImage HEAD,  CONNECT,  HEADNULL;
    public static Hashtable<String, Image> hash = new Hashtable<String, Image>();

    /** Creates a new instance of pic */
    public Pic() {
    }

    static {
        try {
            HEAD = ImageIO.read(Pic.class.getResource("head.png"));
            CONNECT = ImageIO.read(Pic.class.getResource("reading.png"));
            HEADNULL = ImageIO.read(Pic.class.getResource("headnull.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 本方法是根据文件的后缀名进行获得相应图标
     */
    public static Image getImage(String postfix) {
        Image bi = null;
        if ((bi = hash.get(postfix)) != null) {
            return bi;
        } else {
            try {
                bi = ImageIO.read(Pic.class.getResource(postfix + ".png"));
                hash.put(postfix, bi);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return bi;
    }

    /**
     * 一个获得ImageIcon的简单方法
     */
    public static ImageIcon getImageIcon(String str) {
        ImageIcon icon = null;
        if (str != null) {
            icon = new ImageIcon(Pic.class.getResource(str));
            return icon;
        } else {
            System.err.println("图片路径:" + str + "不正确");
            return null;
        }
    }

    /**
     * 把图片切成一张一张的
     * @param image
     * @param width 每一张图片的宽度
     * @param height第一张图片的高度
     * @return
     */
    public static BufferedImage[] getBufferedImages(BufferedImage image, int subWidth, int subHeight) {
        int width = image.getWidth();
        int height = image.getHeight();
        int columnCount = width / subWidth;
        int rowCount = height / subHeight;
        BufferedImage[] buf = new BufferedImage[rowCount * columnCount];
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < columnCount; j++) {
                int k = columnCount * i + j;
                buf[k] = image.getSubimage(j * subWidth, i * subHeight, subWidth, subHeight);
            }
        }
        return buf;
    }

    /**
     * 对图片进行透明度处理
     * @param buf
     * @return
     */
    public static BufferedImage doLucency(BufferedImage buf, Color c) {
        int crgb = c.getRGB();
        int width = buf.getWidth();
        int height = buf.getHeight();
        BufferedImage temp = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int j = 0; j < width; j++) {
            for (int k = 0; k < height; k++) {
                int rgb = buf.getRGB(j, k);
                if ((crgb ^ rgb) == 0) {
                    temp.setRGB(j, k, 0x00FFFFFF);
                } else {
                    temp.setRGB(j, k, rgb);
                }
            }
        }
        buf = temp;
        return buf;
    }


    /**
     * 对图片进行透明度处理
     * @param buf
     * @return
     */
    public static BufferedImage[] doLucency(BufferedImage[] buf, Color c) {
        for (int i = 0; i < buf.length; i++) {
            buf[i] = doLucency(buf[i], c);
        }
        return buf;
    }
}
