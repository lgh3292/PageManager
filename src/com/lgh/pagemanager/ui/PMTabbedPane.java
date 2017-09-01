/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lgh.pagemanager.ui;


import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Icon;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileSystemView;

import com.lgh.pagemanager.interaface.IPMTabbedPane;

/**
 * 
 * @author Administrator
 */
public class PMTabbedPane extends JTabbedPane implements IPMTabbedPane,DropTargetListener,ActionListener {
	private HashMap<String, Object> tabMap = new HashMap<String, Object>();
	private JPopupMenu arrangeClose;
	public PMTabbedPane() {
		initListener();
		
	}
	private void initListener(){
		initMouseListener();
		initTargeListener();
	}
	/**
     * ��ʼ��popupMenu�����������в˵�
     */
    private JPopupMenu getArrangeClose() {
        //*******�ر��ĵ�����ѡ��
    	if(arrangeClose==null){
    	   	 arrangeClose = new JPopupMenu();
    		 PMMenuItem jmCloseThis = new PMMenuItem("�ر�", "jmCloseThis", this);
    	     PMMenuItem jmCloseAll = new PMMenuItem("�ر�����", "jmCloseAll", this);
    	     PMMenuItem jmCloseOther = new PMMenuItem("�ر�����", "jmCloseOther", this);
    	     arrangeClose.add(jmCloseThis);
    	     arrangeClose.add(jmCloseAll);
    	     arrangeClose.add(jmCloseOther);
    	}
       return arrangeClose;
    }
    
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        if (actionCommand != null) {
             if (actionCommand.equals("jmCloseThis")) {
                remove("1");
            } else if (actionCommand.equals("jmCloseOther")) {
            	remove("2");
            } else if (actionCommand.equals("jmCloseAll")) {
            	remove("3");
            }
        }
    }
    /**
     * remove the compoennt 1.this 2.other 3.all
     * @param type
     */
    private void remove(String type){
    	if(type.trim().equals("1")){
    		
    	}else if(type.trim().equals("2")){
    		
    	}else if(type.trim().equals("3")){
    		
    	}
    }
	/**
     * ��ʼ�����ļ���
     */
    private void initMouseListener() {
        this.addMouseListener(new MouseListener() {

            public void mouseClicked(MouseEvent e) {
            	
            }

            public void mousePressed(MouseEvent e) {
            }

            public void mouseReleased(MouseEvent e) {
            	if(e.isPopupTrigger()){
            		getArrangeClose().show(PMTabbedPane.this, e.getX(), e.getY());
                    SwingUtilities.updateComponentTreeUI(arrangeClose);	
            	}
            }

            public void mouseEntered(MouseEvent e) {
            }

            public void mouseExited(MouseEvent e) {
                
            }
        });
    }
	 /**
     * ��ʼ���϶��ļ���
     */
    private void initTargeListener() {
        new DropTarget(this, DnDConstants.ACTION_COPY_OR_MOVE, this);
    }
	public void add(final File file) {
		// ����굥��ʱ,���ȥ������Ӧ���ļ�,����ļ������ھ�Ѱ���û��Ƿ��½�һ���ļ�
		for (int i = 0; i < this.getTabCount(); i++) {
			if (this.getTitleAt(i).equals(file.getName())) {// ��������Ѿ�����,�������򿪵Ľ���
				this.setSelectedIndex(i);
				return;
			}
		}
		JSplashWindow.getInstance("", true);// false˵�����滹û�д�
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				PMTextPane jtextPane = new PMTextPane();
				final JScrollPane jsp = new JScrollPane(jtextPane);
				boolean go = jtextPane.manageFile(file); //
				// �����ļ�,��ȡ���ļ�����jtextPane
//				 if (go) {
//				 jtabbedPane.jumpToJTablePane(file, jsp);
//				 jtextPane.setOpen(true);
//				 }
				addTab(file.getName(), FileSystemView.getFileSystemView().getSystemIcon(file), jsp, file.getAbsolutePath());
				setSelectedComponent(jsp);
				tabMap.put(file.getAbsolutePath(), file.getName());
			}
		});

	}
	public void dragEnter(DropTargetDragEvent dtde) {
		// TODO Auto-generated method stub
		
	}
	public void dragExit(DropTargetEvent dte) {
		// TODO Auto-generated method stub
		
	}
	public void dragOver(DropTargetDragEvent dtde) {
		// TODO Auto-generated method stub
		
	}
	public void drop(DropTargetDropEvent dtde) {
		if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
            try {
                dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                List<File> list = (List) (dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor));
                for (File file : list) {
                    add(file);
                }
                dtde.dropComplete(true);
            } catch (UnsupportedFlavorException ex) {
                Logger.getLogger(PMTabbedPane.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(PMTabbedPane.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (dtde.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            System.out.println("sringFlavaor");
        } else {
            System.out.println("nothing is supper");
        }		
	}
	public void dropActionChanged(DropTargetDragEvent dtde) {
		// TODO Auto-generated method stub
		
	}
}
