/*
 * MyTreeCellRendererT.java
 *
 * Created on 2007��7��19��, ����1:16
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.lgh.pagemanager.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JTree;

import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;
import java.io.File;
import javax.swing.Icon;

/**
 *
 * @author tewang
 */
public class PMTreeCellRenderer implements TreeCellRenderer {

    /** Creates a new instance of MyTreeCellRenderer */
    private Map<String, Icon> map = new HashMap<String, Icon>();
    private Map<File, Long> fileLengthMap = new HashMap<File, Long>();
    private Color color = new Color(51, 153, 254);

    public PMTreeCellRenderer() {
//        System.out.println("������һ��CELLE..........");
    }

    /**
     * ʵ��ColorListener
     */
    public void setColr(Color c) {
        this.color = c;
    }

    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        JLabel jlabel = new JLabel();
        jlabel.setOpaque(true);
        jlabel.setFont(new Font("����", Font.PLAIN, 12));
        TreePath tp = tree.getPathForRow(row);
        if (tp != null) {
            FileNode tempNode = (FileNode) tp.getLastPathComponent();
//          System.out.println("����Ⱦ�����·����:"+tempNode.getFile());
//          File file = new File(PageManagerView.getFilePath(tempNode));
            File file = (File) tempNode.getUserObject();
            if (selected) {
                jlabel.setForeground(color);
            }
            if (!file.exists()) {
                jlabel.setText(file.getName()+"������");
                return jlabel;
            }
            if (value == null) {
                jlabel.setText("");
            } else {
                jlabel.setText(value.toString());
            }
            Icon icon = map.get(file.getAbsolutePath());
            if (icon == null) {
                try {
                    icon = FileSystemView.getFileSystemView().getSystemIcon(file);
                    map.put(file.getAbsolutePath(), icon);
                } catch (Throwable e) {
                   
                }
            }
            jlabel.setIcon(icon);
            // ����ͼ�귽��
            /*
            if (tempNode.getAllowsChildren()) {  //�ļ���
            jlabel.setIcon(new ImageIcon(Pic.getImage("folder")));
            UtilFile uf = new UtilFile();
            if (SolveConfigure.getInstance().getConfigureFile().isShowFileLength() && tempNode.isDirectory()) {
            Long fileLenth = fileLengthMap.get(file);
            if (fileLenth != null) {
            jlabel.setText(value.toString() + "(" + uf.fileLengthToString(fileLenth) + ")");
            } else {
            uf.computeFileLength(file);
            fileLengthMap.put(file, uf.getFileLength());
            jlabel.setText(value.toString() + "(" + uf.fileLengthToString() + ")");
            }

            }
            } else { //�ļ�
            String fileName = value.toString();
            if (UtilFile.canExecuteFile(fileName)) {  //����ǿ�ִ���ļ�,�͸�Ϊ��ִ���ļ���ͼ��
            //                    Icon icon = FileSystemView.getFileSystemView().getSystemIcon(file);
            //                    jlabel.setIcon(icon);
            Icon icon = map.get(file.getAbsolutePath());
            if (icon == null) {
            icon = FileSystemView.getFileSystemView().getSystemIcon(file);
            map.put(file.getAbsolutePath(), icon);
            }
            jlabel.setIcon(icon);
            } else {
            jlabel.setIcon(new ImageIcon(Pic.getImage("common")));
            }
            }
             * */
        }
        return jlabel;
    }
}
