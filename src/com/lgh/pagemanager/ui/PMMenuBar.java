/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lgh.pagemanager.ui;

import com.lgh.pagemanager.interaface.IMenuBar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Map;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import org.jvnet.substance.SubstanceLookAndFeel;
import org.jvnet.substance.skin.SkinInfo;

/**
 *
 * @author Administrator
 */
public class PMMenuBar extends JMenuBar implements ActionListener {

    private static final Object lock = new Object();
    private static PMMenuBar mMenuBar;
    private IMenuBar iMenuBar;

    public PMMenuBar(IMenuBar iMenuBar) {
        this.iMenuBar = iMenuBar;
        initMenuBar();
    }

    /**
     * ��ʼ���˵�
     */
    private void initMenuBar() {
        String[] str1 = {"�ļ�(F)", "�༭(E)", "����(T)", "����(W)"};
        int[] str2 = {KeyEvent.VK_F, KeyEvent.VK_E, KeyEvent.VK_T, KeyEvent.VK_W};
        JMenu[] menus = new JMenu[str1.length];
        for (int i = 0; i < str1.length; i++) {
            menus[i] = new JMenu(str1[i]);
            menus[i].setMnemonic(str2[i]);
            this.add(menus[i]);
        }
        //�ļ�
        String[] fileMenuStr = {
            "��ϵͳ(O)          Ctrl+O",
            "����ini�����ļ�      Ctrl+B",
            "����                 Ctrl+R",
            "���˵��             Ctrl+I"
        };
        PMMenuItem[] fileMenu = new PMMenuItem[fileMenuStr.length];
        for (int i = 0; i < fileMenuStr.length; i++) {
            fileMenu[i] = new PMMenuItem(fileMenuStr[i], "file" + i, this);
            menus[0].add(fileMenu[i]);
        }
        //�༭
        String[] editMenuStr = {
            "����(U)            Ctrl+Z",
            "����(R)            Ctrl+Y",
            "����(T)            Ctrl+X",
            "����(C)            Ctrl+C",
            "ճ��(P)            Ctrl+V",
            "ɾ��(D)       Ctrl+Delete",
            "����(F)..          Ctrl+F",
            "������һ��(W)     Ctrl+F3",
            "�滻(R)..          Ctrl+H",
            "ת��(G)..          Ctrl+G",
            "ȫѡ(R)..          Ctrl+A",
            "����/ʱ��          Ctrl+5",};
        PMMenuItem[] editMenu = new PMMenuItem[editMenuStr.length];
        for (int i = 0; i < editMenu.length; i++) {
            editMenu[i] = new PMMenuItem(editMenuStr[i], "edit" + i, this);
            menus[1].add(editMenu[i]);
            if (i == 1 || i == 3 || i == 4 || i == 6) {
                menus[1].addSeparator();
            }
        }
        //����
        String[] routineSetMenuStr = {
            "�Զ��ػ� Ctrl+I",
            "�����붨ʱ����",
            "�����ļ�����",
            "��������ȡ",
            "ѡ��",
            "������֤��",
            "����Ԥ��",};
        PMMenuItem[] routineSetMenu = new PMMenuItem[routineSetMenuStr.length];
        for (int i = 0; i < routineSetMenuStr.length; i++) {
            routineSetMenu[i] = new PMMenuItem(routineSetMenuStr[i], "tool" + i, this);
            menus[2].add(routineSetMenu[i]);
            if (i == 0 || i == 1 || i == 3) {
                menus[3].addSeparator();
            }
        }
        String[] xingZheQuMenuStr = {"��ʾ��С",};
        JCheckBoxMenuItem[] xingZheQuMenu = new JCheckBoxMenuItem[xingZheQuMenuStr.length];
        for (int i = 0; i < xingZheQuMenu.length; i++) {
            xingZheQuMenu[i] = new JCheckBoxMenuItem(xingZheQuMenuStr[i]);
            xingZheQuMenu[i].setActionCommand("window" + i);
            xingZheQuMenu[i].addActionListener(this);
            menus[3].add(xingZheQuMenu[i]);
            if (i == 2 || i == 4) {
                menus[4].addSeparator();
            }
        }
        this.add(getSkinMenu());
    }

    public static JMenu getSkinMenu() {
        JMenu jmSkin = new JMenu("Ƥ��(K)");
        jmSkin.setMnemonic(((char) KeyEvent.VK_K));
        Map<String, SkinInfo> skinMap = SubstanceLookAndFeel.getAllSkins();
        for (final Map.Entry<String, SkinInfo> entry : skinMap.entrySet()) {
            JMenuItem jmiSkin = new JMenuItem(entry.getValue().getDisplayName());
            jmiSkin.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    SubstanceLookAndFeel.setSkin(entry.getValue().getClassName());
                }
            });
            jmSkin.add(jmiSkin);
        }

        jmSkin.addSeparator();
//		final CustomSkin customSkin = new CustomSkin();
//		JMenuItem jmiSkin = new JMenuItem(customSkin.getDisplayName());
//		jmiSkin.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				SubstanceLookAndFeel.setSkin(customSkin);
//			}
//		});

//		jmSkin.add(jmiSkin);

        return jmSkin;
    }

    public synchronized static PMMenuBar getPMMenuBar(IMenuBar iMenuBar) {
        synchronized (lock) {
            if (mMenuBar == null) {
                mMenuBar = new PMMenuBar(iMenuBar);
            }
            return mMenuBar;
        }
    }

    
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
