/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lgh.pagemanager.ui;

import java.awt.event.ActionListener;
import javax.swing.JMenuItem;

/**
 *
 * @author Administrator
 */
public class PMMenuItem  extends JMenuItem   {

    public PMMenuItem(String text) {
        super(text);
    }

    public PMMenuItem(String text, String actionCommand, ActionListener listener) {
        super(text);
        this.setActionCommand(actionCommand);
        this.addActionListener(listener);
    }

    /**
     * ��Ӻ����ü���
     * @param actionCommand
     * @param listener
     */
    public void addCommandAndListener(String actionCommand, ActionListener listener) {
        this.setActionCommand(actionCommand);
        this.addActionListener(listener);
    }

//
//    private String text;//����
//    private Image bgImage;//����
//    private Image enterImage;//������ʱ
//    private boolean enter;//����Ƿ����˵�
//    private boolean isSeparator;//�Ƿ�ֻ��һ��separator
//
//    public PMMenuItem(String text, Image bgImage, Image enterImage, String action) {
//        super(text);
//        this.text = text;
//        this.bgImage = bgImage;
//        this.enterImage = enterImage;
//        this.addMouseListener(this);
//        this.setActionCommand(action);
//        this.setPreferredSize(new Dimension(bgImage.getWidth(this), bgImage.getHeight(this)));
//    }
//
//    /**
//     *  separator�Ĺ��캯��
//     * @param image
//     */
//    public PMMenuItem(Image image) {
//        isSeparator = true;
//        this.bgImage = image;
//        this.setPreferredSize(new Dimension(image.getWidth(this), image.getHeight(this)));
//    }
//
//    @Override
//    public void paintComponent(Graphics g) {
//        g.drawImage(bgImage, 0, 0, this);
//        if (!isSeparator) {
//            g.drawString(text, 30, 14);
//            if (enter) {
//                g.drawImage(enterImage, 5, 0, this);
//            }
//        }
//    }
//
//    private void cancel() {
//        enter = false;
//        repaint();
//    }
//
//    public void mouseClicked(MouseEvent e) {
//    }
//
//    public void mousePressed(MouseEvent e) {
//        cancel();
//    }
//
//    public void mouseReleased(MouseEvent e) {
//        cancel();
//    }
//
//    public void mouseEntered(MouseEvent e) {
//        enter = true;
//        repaint();
//    }
//
//    public void mouseExited(MouseEvent e) {
//        cancel();
//    }
}

