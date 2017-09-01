package com.lgh.pagemanager.ui;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.security.AccessControlException;

import javax.swing.ImageIcon;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.jvnet.substance.skin.SubstanceBusinessBlackSteelLookAndFeel;

import com.lgh.pagemanager.pic.Pic;

/**
 * 打开文件时,会有一小段时间停留一会.
 * @author tewang
 */
public class JSplashWindow extends JWindow {

    private static JSplashWindow csw;
    private Image image;
    private String content;
    private String lab;
    private int x1 = 155;
    private int y1 = 220;
    private int x2 = 250;
    private int y2 = 235;
    private DisposeAdapter da;

    /**
     *constructor
     */
    private JSplashWindow(String content) {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        Image temp = Pic.CONNECT;
        ImageIcon icon = new ImageIcon(temp);
        image = icon.getImage();
        image = Pic.CONNECT;
        this.content = content;
        setAlwaysOnTop(true);
        setSize(icon.getIconWidth(), icon.getIconHeight());
        Dimension screen = getToolkit().getScreenSize();
        setLocation((screen.width - getSize().width) / 2, (screen.height - getSize().height) / 2);
        this.setVisible(true);
    }

    /**
     *set close on click
     *use as about dialog
     */
    public void setCloseOnClick(boolean b) {
        if (b) {
            if (da == null) {
                da = new DisposeAdapter();
            }
            addMouseListener(da);
        } else if (da != null) {
            this.removeMouseListener(da);
        }
    }

    /**
     *set the place where user name will be show
     */
    public void setUserLocation(int x, int y) {
        x1 = x;
        y1 = y;
    }

    /**
     *set the place where status will be show
     */
    public void setStatusLocation(int x, int y) {
        x2 = x;
        y2 = y;
    }

    /**
     *paint
     */
    public void paint(Graphics g) {
        g.drawImage(image, 0, 0, this);
        g.drawString(content, x1, y1);
        if (lab != null) {
            g.drawString(lab, x2, y2);
        }
    }

    /**
     *set the status text
     */
    public void setText(String lab) {
        this.lab = lab;
        repaint();
    }

    /**
     *inner class
     *to close the JWindow on click
     */
    private class DisposeAdapter extends MouseAdapter {

        public void mouseClicked(MouseEvent me) {
            dispose();
        }
    }

    public static void close() {
        if (csw != null) {
            csw.dispose();
            csw = null;
        }
    }

    /**
     * 重载
     * @param content
     * @return
     */
    public static synchronized void getInstance(String content) {
         getInstance(content, false);
    }

    /**
     *get the instance of JSplashWindow
     *@return JSplashWindow
     */
    public static synchronized void getInstance(final String content, final boolean onclick) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                if (csw == null) {
                    csw = new JSplashWindow(content);
                }
                csw.setCloseOnClick(onclick);
            }
        });
    }

    public static void main(String[] args) throws Exception {
        try {
            if (System.getProperty("swing.defaultlaf") == null) {
                UIManager.setLookAndFeel(new org.jvnet.substance.skin.SubstanceOfficeBlue2007LookAndFeel());
            }
        } catch (AccessControlException ace) {
            UIManager.setLookAndFeel(new SubstanceBusinessBlackSteelLookAndFeel());
        }
//      
//         SwingUtilities.invokeLater(new Runnable() {
//
//            public void run() {
        JSplashWindow.getInstance("test");
//            }
//        });
    }
}

