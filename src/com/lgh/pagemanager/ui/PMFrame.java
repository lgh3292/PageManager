/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lgh.pagemanager.ui;

import com.lgh.pagemanager.interaface.IMenuBar;
import com.lgh.pagemanager.interaface.IPM;
import com.lgh.util.FileUtil;

import java.awt.BorderLayout;
import java.awt.Component;
import java.security.AccessControlException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Administrator
 */
public class PMFrame extends JFrame implements IPM, IMenuBar {

	private static final long serialVersionUID = 1L;

	public PMFrame() {
        initComponent();
    }

    private void initComponent() {
        new PMSystemTray(this);
        this.setSize(800, 600);
        
        this.add(getCenter(), BorderLayout.CENTER);
        this.setJMenuBar(PMMenuBar.getPMMenuBar(this));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public Component getCenter() {
    	PMTabbedPane pmtp = new PMTabbedPane();
        //left
        JTabbedPane jtp = new JTabbedPane();
        jtp.add("文章", new JScrollPane(new PMTree(new FileNode(FileUtil.createNewFloder("文章")),pmtp)));
        jtp.add("本地", new JScrollPane(new PMTree(new FileNode(new RootFile("/")))));
        jtp.add("其他", new JPanel());
        //right
        JSplitPane jsp = new JSplitPane();
        jsp.setOneTouchExpandable(true);
        jsp.setDividerSize(8);
        jsp.setDividerLocation(200);
        jsp.setLeftComponent(jtp);
        jsp.setRightComponent(pmtp);
        return jsp;
    }

    public static void main(String[] args) {
        try {
            if (System.getProperty("swing.defaultlaf") == null) {
                try {
                    UIManager.setLookAndFeel(new org.jvnet.substance.skin.SubstanceOfficeSilver2007LookAndFeel());
                } catch (UnsupportedLookAndFeelException ex) {
                    Logger.getLogger(PMFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (AccessControlException ace) {
            ace.printStackTrace();
        }
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                try {
                    new PMFrame();
                } catch (Exception ex) {
                    Logger.getLogger(PMFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

    }

    public JFrame getUI() {
        return this;
    }
}
