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
     * 初始化菜单
     */
    private void initMenuBar() {
        String[] str1 = {"文件(F)", "编辑(E)", "工具(T)", "窗口(W)"};
        int[] str2 = {KeyEvent.VK_F, KeyEvent.VK_E, KeyEvent.VK_T, KeyEvent.VK_W};
        JMenu[] menus = new JMenu[str1.length];
        for (int i = 0; i < str1.length; i++) {
            menus[i] = new JMenu(str1[i]);
            menus[i].setMnemonic(str2[i]);
            this.add(menus[i]);
        }
        //文件
        String[] fileMenuStr = {
            "打开系统(O)          Ctrl+O",
            "生成ini引导文件      Ctrl+B",
            "设置                 Ctrl+R",
            "软件说明             Ctrl+I"
        };
        PMMenuItem[] fileMenu = new PMMenuItem[fileMenuStr.length];
        for (int i = 0; i < fileMenuStr.length; i++) {
            fileMenu[i] = new PMMenuItem(fileMenuStr[i], "file" + i, this);
            menus[0].add(fileMenu[i]);
        }
        //编辑
        String[] editMenuStr = {
            "撤消(U)            Ctrl+Z",
            "重做(R)            Ctrl+Y",
            "剪切(T)            Ctrl+X",
            "复制(C)            Ctrl+C",
            "粘贴(P)            Ctrl+V",
            "删除(D)       Ctrl+Delete",
            "查找(F)..          Ctrl+F",
            "查找下一个(W)     Ctrl+F3",
            "替换(R)..          Ctrl+H",
            "转到(G)..          Ctrl+G",
            "全选(R)..          Ctrl+A",
            "日期/时间          Ctrl+5",};
        PMMenuItem[] editMenu = new PMMenuItem[editMenuStr.length];
        for (int i = 0; i < editMenu.length; i++) {
            editMenu[i] = new PMMenuItem(editMenuStr[i], "edit" + i, this);
            menus[1].add(editMenu[i]);
            if (i == 1 || i == 3 || i == 4 || i == 6) {
                menus[1].addSeparator();
            }
        }
        //工具
        String[] routineSetMenuStr = {
            "自动关机 Ctrl+I",
            "闹钟与定时提醒",
            "生成文件分析",
            "分析与提取",
            "选项",
            "生成验证码",
            "天气预报",};
        PMMenuItem[] routineSetMenu = new PMMenuItem[routineSetMenuStr.length];
        for (int i = 0; i < routineSetMenuStr.length; i++) {
            routineSetMenu[i] = new PMMenuItem(routineSetMenuStr[i], "tool" + i, this);
            menus[2].add(routineSetMenu[i]);
            if (i == 0 || i == 1 || i == 3) {
                menus[3].addSeparator();
            }
        }
        String[] xingZheQuMenuStr = {"显示大小",};
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
        JMenu jmSkin = new JMenu("皮肤(K)");
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
