/*
 * MyJTree.java
 *
 * Created on 2007��9��18��, ����1:10
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.lgh.pagemanager.ui;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.lgh.pagemanager.interaface.IPMTabbedPane;
import com.lgh.util.FileUtil;

/**
 * 
 * @author tewang
 */
public class PMTree extends AbstractPMTree implements ActionListener {

	private PMTreeCellRenderer myTreeCellRenderer = new PMTreeCellRenderer();

	private JPopupMenu clickFilePopupMenu; // 1

	private JPopupMenu clickFolderPopupMenu;// 2

	private JPopupMenu arrangeSequence;// 3

	private IPMTabbedPane tabbedPane;
	public PMTree(FileNode sourceNode) {
		super(sourceNode);
		this.sourceNode = sourceNode;
		this.setModel(new DefaultTreeModel(sourceNode));
		init();
	}
	public PMTree(FileNode sourceNode,IPMTabbedPane tabbedPane) {
		this(sourceNode);
		this.tabbedPane = tabbedPane;
	}
	private void init() {
		sourceNode.explore();
		initKeyListener();
		initOtherListener();
	}

	private void initKeyListener() {
		this.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				int keyCode = e.getKeyCode();
				if (keyCode == KeyEvent.VK_F2) {
					FileNode tempNode = (FileNode) (getLeadSelectionPath()
							.getLastPathComponent());
					// operatorJTree(RENAME, tempNode);
				} else if (keyCode == KeyEvent.VK_DELETE
						|| (keyCode == KeyEvent.VK_DELETE && e.isShiftDown())) {
					FileNode tempNode = (FileNode) (getLeadSelectionPath()
							.getLastPathComponent());
					// operatorJTree(DEL, tempNode);
				}
			}
		});
	}

	private JPopupMenu getPopupMenu(int type) {
		// �Ҽ�������������********һ:�ļ�
		if (clickFilePopupMenu == null) {
			clickFilePopupMenu = new JPopupMenu(); // ѡ���ļ�ʱ
			clickFilePopupMenu.add(new PMMenuItem("��", "jmOpenFile", this));
			clickFilePopupMenu.add(new PMMenuItem("���ļ���", "jmOpenItsFolder",this));
			clickFilePopupMenu.add(new PMMenuItem("������", "jmRename", this));
			clickFilePopupMenu.add(new PMMenuItem("ɾ��", "jmDel", this));
			clickFilePopupMenu.add(new PMMenuItem("ȡ��", "jmcancel", this));
			clickFilePopupMenu.add(new PMMenuItem("����", "jmProperties", this));

		}
		// �Ҽ�������������********��:�ļ���
		if (clickFolderPopupMenu == null) {
			clickFolderPopupMenu = new JPopupMenu();// ѡ���ļ���ʱ
			clickFolderPopupMenu.add(new PMMenuItem("���ļ����ڵ��ļ���","jmOpenItsFolder", this));
			clickFolderPopupMenu.add(new PMMenuItem("�½��ļ���","jmCreateNewFolder", this));
			clickFolderPopupMenu.add(new PMMenuItem("�½��ı�", "jmNewNode", this));
			clickFolderPopupMenu.add(new PMMenuItem("��ӿ�ִ���ļ�", "jmAddFile",this));
			clickFolderPopupMenu.add(new PMMenuItem("����ļ���", "jmAddFolder",this));
			clickFolderPopupMenu.add(new PMMenuItem("������", "jmRename",this));
			clickFolderPopupMenu.add(new PMMenuItem("ɾ��", "jmDel", this));
			clickFolderPopupMenu.add(new PMMenuItem("ȡ��", "jmcancel",this));
			clickFolderPopupMenu.add(new PMMenuItem("����", "jmProperties",this));
		}
		// �Ҽ�����˳��
		if (arrangeSequence == null) {
			arrangeSequence = new JPopupMenu();
			arrangeSequence.add(new PMMenuItem("����������", "jmDate", this));
			arrangeSequence.add(new PMMenuItem("����������", "jmType", this));
		}
		switch (type) {
		case 1:
			return clickFilePopupMenu;
		case 2:
			return clickFolderPopupMenu;
		case 3:
			return arrangeSequence;
		default:
			return null;
		}
	}

	/**
	 * ����jtree����Ⱦ��
	 */
	public PMTreeCellRenderer getMyTreeCellRenderer() {
		return myTreeCellRenderer;
	}

	/**
	 * ��Ӧ����¼�
	 */
	public void responseMouseEvent() {
		new Thread(new Runnable() {
			public void run() {
				TreePath tp = getLeadSelectionPath();
				if (tp != null) {
					FileNode fileNode = (FileNode) (getLeadSelectionPath().getLastPathComponent());
					doTreeAction("jmOpenFile", fileNode);
				}
			}
		}).start();
	}

	private void initOtherListener() {
		this.setCellRenderer(myTreeCellRenderer);
		this.setEditable(false);
		this.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseReleased(MouseEvent m) {
				try {
					int k = m.getButton();
					if (m.getClickCount() == 2) {
							responseMouseEvent();
					} else if (k == MouseEvent.BUTTON3) { // **********************����Ҽ��������¼�
						int x = m.getX();
						int y = m.getY();
						int row = getRowForLocation(x, y);
						if (row != -1 && !isSelectionEmpty()) {// ��ѡ��ĳһ��
							jtreePopupMenu(x, y);// solve the right key
													// popupMenu,x and y is the
													// event origin
						} else { // ���û��ѡ��,����ʾ(�����������밴��������)
							getPopupMenu(3).show(PMTree.this, m.getX(),
									m.getY());
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		this.setRootVisible(true);
		this.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
		SwingUtilities.updateComponentTreeUI(this);
	}

	/**
	 * �����Ҽ��¼�,�û���jtree�ϵ���Ҽ�ʱ�����Ĳ˵�.���������ѡ�����ļ�,��
	 */
	private void jtreePopupMenu(int x, int y) {
		int[] array = this.getSelectionRows();
		this.setSelectionRows(array);
		if (array.length == 1) {
			FileNode treeNode = (FileNode) this.getSelectionPath()
					.getLastPathComponent();
			if (treeNode.getAllowsChildren()) {
				getPopupMenu(2).show(this, x, y);
			} else if (!treeNode.getAllowsChildren()) {
				getPopupMenu(1).show(this, x, y);
			}
		} else if (array.length >= 2) {
			JOptionPane.showMessageDialog(null, "�Բ���,�ݲ�֧��������");
			return;
			// TreePath[] treePaths = jtree.getSelectionPaths();
			// for(TreePath tp:treePaths){
			// DefaultMutableTreeNode treeNode =
			// (DefaultMutableTreeNode)tp.getLastPathComponent();
			// System.out.println(treeNode);
			// }
		} else {
			JOptionPane.showMessageDialog(null, "���������������!,�����˳�");
			System.exit(0);
		}
	}

	public void actionPerformed(ActionEvent e) {
		// *****�Ҽ�����������ѡ��
		try {
			final FileNode fileNode = (FileNode) (this.getLeadSelectionPath().getLastPathComponent());
			if (e.getActionCommand().equals("jmOpenFile")|| e.getActionCommand().equals("jmOpenFolder")) {
				doTreeAction("jmOpenFile", fileNode);
			} else if (e.getActionCommand().equals("jmOpenItsFolder")) {
				Desktop.getDesktop().open(fileNode.getFile());
			}else if (e.getActionCommand().equals("jmCreateNewFolder")) {//�½��ļ���
//				operatorJTree(CREATE_NEW_FOLDER, tempNode);
			 } else if (e.getActionCommand().equals("jmNewNode")) { //********�½��ı�
				FileNode tempNode = (FileNode)
				(this.getLeadSelectionPath().getLastPathComponent());
//				operatorJTree(CREATE_NEW_NODE, tempNode);
			 } else if (e.getActionCommand().equals("jmAddFile")) { //********����ļ�
//				 operatorJTree(ADD_FILE, tempNode);
			 } else if (e.getActionCommand().equals("jmAddFolder")) { //********����ļ���
//			 	operatorJTree(ADD_FOLDER, tempNode);
			 } else if (e.getActionCommand().equals("jmRename")) { //********������
//				 operatorJTree(RENAME, tempNode);
			 } else if (e.getActionCommand().equals("jmRenameFolder")) {
				 //********�ļ���������
//				 operatorJTree(RENAME, tempNode);
			 } else if (e.getActionCommand().equals("jmDel") || e.getActionCommand().equals("jmDelFolder")) { //********�ļ�ɾ��//********�ļ���ɾ��
//				 operatorJTree(DEL, tempNode);
			 } else if (e.getActionCommand().equals("jmProperties")) { //***.equals("����
//				 operatorJTree(FILE_PROPERTIES, tempNode);
			 } else if (e.getActionCommand().equals("jmPropertiesFolder")) {
//				 operatorJTree(FILE_PROPERTIES, tempNode);
			 } else if (e.getActionCommand().equals("jmcancel")) { //********ȡ��
				 System.out.println("jmcancel");
			 } else if (e.getActionCommand().equals("jmcancelFolder")) {
				 //********�ļ���ȡ��
				 System.out.println("jmcancel");
			 } else if (e.getActionCommand().equals("jmcancelFolder")) {
				 //********�ļ���ȡ��
				 //�ļ���ȡ��
			 } //*****����˳��
			 else if (e.getActionCommand().equals("jmDate")) {
				 System.out.println("����������");
			 } else if (e.getActionCommand().equals("jmType")) {
				 System.out.println("����������");
			 }
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * implement the interface TreeOperatorListener �����Ĳ���
	 * 
	 * @param type
	 * @param mt
	 */
	public void doTreeAction(String type, final FileNode fileNode) {
		if(type.equals("jmOpenFile")||type.equals("jmOpenFolder")){
			// ��Ҫ�ж��ļ��Ƿ�Ϊ��ִ���ļ�,�Ĺ��ǵĻ�����Desktop��,�������JTextPane��
			new Thread(new Runnable() {
				public void run() {
					if (FileUtil.canExecuteFile(fileNode.getFile())) {
						try {
							JSplashWindow.getInstance("");
							Desktop.getDesktop().open(fileNode.getFile());
							JSplashWindow.close();
						} catch (Exception ex) {
							JSplashWindow.close();
							JOptionPane.showMessageDialog(null,"���ļ�ʧ��,�����ļ��Ƿ����");
							ex.printStackTrace();
						}
					} else {
						if (fileNode.isLeaf()&& !fileNode.getAllowsChildren()) {// ˵���˽����Ҷ�ӽ��,�����ǲ�������չ�Ľ��
							tabbedPane.add(fileNode.getFile());
						} else {
							SwingUtilities.invokeLater(new Runnable() {
								public void run() {
									int childCount = fileNode.getChildCount();
									DefaultTreeModel defaultTreeModel = (DefaultTreeModel) getModel();
									for (int i = 0; i < childCount; i++) {
										defaultTreeModel.nodeChanged(fileNode.getChildAt(i));
									}
								}
							});
						}
					}
				}
			}).start();
		}
//		Object[] object = this.getLeadSelectionPath().getPath();
//		synchronized (tempNode) {
//			switch (type) {
//			case OPEN_FILE_FOLDER:
//				utilJTree.openFolder(tempNode.getFile().getParent());
//				break;
//			case OPEN_FOLDER:
//				utilJTree.openFolder(UtilJTree.getJTreePath(object));
//				break;
//			case ADD_FOLDER:
//				// ����ļ���
//				String strTo = UtilJTree.getJTreePath(object); // �ļ�����Ŀ�ĵ�
//				utilJTree.addFolder(this, strTo, tempNode);
//				SwingUtilities.updateComponentTreeUI(this);
//				break;
//			case ADD_FILE:
//				// ��ӿ�ִ���ļ�
//				String to = UtilJTree.getJTreePath(object); // �ļ�����Ŀ�ĵ�
//				utilJTree.addFile(this, to, tempNode);
//				SwingUtilities.updateComponentTreeUI(this);
//				break;
//			case CREATE_NEW_FOLDER:
//				// *****************************************************�½��ļ���
//				utilJTree.newFolder(tempNode, object, PageManagerView
//						.getFilePath(), jtabbedPane);
//				break;
//			case CREATE_NEW_NODE:
//				// *****************************************************�½��ļ�
//				utilJTree.newFile(tempNode);
//				SwingUtilities.updateComponentTreeUI(this);
//				break;
//			case RENAME:
//				// *****************************************************������
//				utilJTree.reName(tempNode);
//				break;
//			case DEL:
//				utilJTree.del(tempNode, co, object, PageManagerView
//						.getFilePath(), jtabbedPane);
//				break;
//			case FILE_PROPERTIES:
//				break;
//			case CLICK:
//				// *************************************������굥���¼�
//				// ��Ҫ�ж��ļ��Ƿ�Ϊ��ִ���ļ�,�Ĺ��ǵĻ�����Desktop��,�������JTextPane��
//				final String fileName = tempNode.toString();
//				new Thread(new Runnable() {
//
//					public void run() {
//						if (UtilFile.canExecuteFile(fileName)) {
//							try {
//								JSplashWindow.getInstance("");
//								Desktop.getDesktop().open(tempNode.getFile());
//								JSplashWindow.close();
//							} catch (Exception ex) {
//								JSplashWindow.close();
//								JOptionPane.showMessageDialog(null,
//										"���ļ�ʧ��,�����ļ��Ƿ����");
//								ex.printStackTrace();
//							}
//						} else {
//							if (tempNode.isLeaf()
//									&& !tempNode.getAllowsChildren()) {
//								// ˵���˽����Ҷ�ӽ��,�����ǲ�������չ�Ľ��
//								doubleClick(tempNode.getFile());
//							} else {
//								SwingUtilities.invokeLater(new Runnable() {
//
//									public void run() {
//										int childCount = tempNode
//												.getChildCount();
//										DefaultTreeModel defaultTreeModel = (DefaultTreeModel) getModel();
//										for (int i = 0; i < childCount; i++) {
//											defaultTreeModel
//													.nodeChanged(tempNode
//															.getChildAt(i));
//										}
//									}
//								});
//							}
//						}
//					}
//				}).start();
//				break;
//			}
//
//		}
	}
 
	public IPMTabbedPane getTabbedPane() {
		return tabbedPane;
	}
	public void setTabbedPane(IPMTabbedPane tabbedPane) {
		this.tabbedPane = tabbedPane;
	}

	 
}
