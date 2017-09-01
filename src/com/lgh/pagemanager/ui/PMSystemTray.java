/*
 * SystemTray.java
 *
 * Created on 2007��8��15��, ����2:40
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.lgh.pagemanager.ui;

import com.lgh.pagemanager.interaface.ISystemTray;
import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JFrame;

import com.lgh.pagemanager.pic.Pic;
import java.awt.BorderLayout;
import java.awt.Desktop;
import java.net.URL;
import javax.swing.SwingUtilities;

/**
 * һ����������ϵͳ����ͼ�����
 * @author tewang
 */
public class PMSystemTray extends MouseAdapter implements Runnable, ActionListener {

    private SystemTray systemTray;
    private TrayIcon trayIcon;
    private PopupMenu pop = new PopupMenu("menu");
    private MenuItem exit = new MenuItem("exit");
    private MenuItem restart = new MenuItem("restart");
    private MenuItem setbakpath = new MenuItem("setbakpath");
    private MenuItem lan = new MenuItem("LAN");
    private MenuItem mp3 = new MenuItem("Mp3Palyer");
    private MenuItem baidu = new MenuItem("baidu");
    private MenuItem google = new MenuItem("Google");
    private ISystemTray iSystemTray;

    public void close() {
        systemTray.remove(trayIcon);
    }

    public void hideComponent(JFrame jf) {
        try {
            systemTray.add(trayIcon);
            jf.setVisible(false);
        } catch (AWTException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * ���ݴ�������JFrame������Ӧ�Ĳ���,�Ȱ�JFrame����Ϊfalse,Ȼ����С����������,��˫��ͼ��ʱ�ְѾ���ԭ����
     */
    public PMSystemTray(ISystemTray iSystemTray) {
        this.iSystemTray = iSystemTray;
        pop.add(google);
        pop.add(baidu);
        pop.add(mp3);
        pop.add(lan);
        pop.add(setbakpath);
        pop.add(restart);
        pop.add(exit);
        trayIcon = new TrayIcon(Pic.HEAD, "�ļ�ϵͳ", pop);
        //        trayIcon = new TrayIcon(Pic.HEAD,"������,�ļ�ϵͳ");
        exit.addActionListener(this);
        setbakpath.addActionListener(this);
        restart.addActionListener(this);
        lan.addActionListener(this);
        mp3.addActionListener(this);
        baidu.addActionListener(this);
        google.addActionListener(this);
        trayIcon.addMouseListener(this);
        systemTray = SystemTray.getSystemTray();
        try {
            systemTray.add(trayIcon);
        } catch (AWTException ex) {
            ex.printStackTrace();
        }
    //        new Thread(this).start();
    }

    public void initTray() {
        PopupMenu pop = new PopupMenu("�˵�");
        MenuItem mi1 = new MenuItem("�˳�");
        pop.add(mi1);
        systemTray = SystemTray.getSystemTray();
        trayIcon = new TrayIcon(Pic.HEAD, "��С��", pop);
        mi1.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                System.exit(0);
            }
        });
    }

    /**
     * ������Ϊ����չ���ǵ�
     */
    public JButton getTray(final JFrame jf) {
        JButton jb = new JButton("��С��");
        jb.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                try {
                    systemTray.add(trayIcon);
                    jf.setVisible(false);
                } catch (AWTException ex) {
                    ex.printStackTrace();
                }
            }
        });
        trayIcon.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    jf.setVisible(true);
                }
            }
        });
        return jb;
    }

      
    public void run() {
        while (true) {
            trayIcon.setImage(Pic.HEAD);
            try {
                Thread.sleep(450);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            trayIcon.setImage(Pic.HEADNULL);
            try {
                Thread.sleep(450);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == exit) {  //�˳�ϵͳ
           System.exit(0);
        } else if (e.getSource() == setbakpath) { //���ñ���·��
        } else if (e.getSource() == restart) {  //����ϵͳ
           
        } else if (e.getSource() == lan) {
        } else if (e.getSource() == mp3) {
           
        } else if (e.getSource() == baidu) {
            deskTopBrose("http://www.baidu.com");
        } else if (e.getSource() == google) {
            deskTopBrose("http://www.google.cn");
        }
    }  

    /**
     * �򿪰ٶ�����
     */
    private void deskTopBrose(String url) {
        try {
            Desktop.getDesktop().browse(new URL(url).toURI());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ��ӦSystemIconͼ��
     */
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == trayIcon) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                if (iSystemTray.getUI().isVisible()) {
                    SwingUtilities.invokeLater(new Runnable() {

                        public void run() {
                            iSystemTray.getUI().setVisible(false);
                        }
                    });
                } else {
                    iSystemTray.getUI().setVisible(true);
                    iSystemTray.getUI().setState(JFrame.NORMAL);//���û��������С��ʱ,��ʱ���SystemTcon��һʱ������JFrame,������������ǿ�Ƶ���
                }

            }
        }
    }

    private class MyMenuItem extends MenuItem {

        public MyMenuItem(String name) {
            super(name);
        }
    }

   public static void main(String[] args) {
        JFrame jf = new JFrame("asdfl");
        PMSystemTray mst = new PMSystemTray(null);
        jf.setSize(500,500);
        jf.add(mst.getTray(jf),BorderLayout.CENTER);
        jf.setLocationRelativeTo(null);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setVisible(true);
    }
}
