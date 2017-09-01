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
     * 添加和设置监听
     * @param actionCommand
     * @param listener
     */
    public void addCommandAndListener(String actionCommand, ActionListener listener) {
        this.setActionCommand(actionCommand);
        this.addActionListener(listener);
    }

//
//    private String text;//内容
//    private Image bgImage;//背景
//    private Image enterImage;//鼠标进入时
//    private boolean enter;//鼠标是否进入菜单
//    private boolean isSeparator;//是否只是一个separator
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
//     *  separator的构造函数
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

