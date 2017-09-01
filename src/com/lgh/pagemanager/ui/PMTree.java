/*
 * MyJTree.java
 *
 * Created on 2007年9月18日, 下午1:10
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
		// 右键弹出操作类型********一:文件
		if (clickFilePopupMenu == null) {
			clickFilePopupMenu = new JPopupMenu(); // 选择文件时
			clickFilePopupMenu.add(new PMMenuItem("打开", "jmOpenFile", this));
			clickFilePopupMenu.add(new PMMenuItem("打开文件夹", "jmOpenItsFolder",this));
			clickFilePopupMenu.add(new PMMenuItem("重命名", "jmRename", this));
			clickFilePopupMenu.add(new PMMenuItem("删除", "jmDel", this));
			clickFilePopupMenu.add(new PMMenuItem("取消", "jmcancel", this));
			clickFilePopupMenu.add(new PMMenuItem("属性", "jmProperties", this));

		}
		// 右键弹出操作类型********二:文件夹
		if (clickFolderPopupMenu == null) {
			clickFolderPopupMenu = new JPopupMenu();// 选择文件夹时
			clickFolderPopupMenu.add(new PMMenuItem("打开文件所在的文件夹","jmOpenItsFolder", this));
			clickFolderPopupMenu.add(new PMMenuItem("新建文件夹","jmCreateNewFolder", this));
			clickFolderPopupMenu.add(new PMMenuItem("新建文本", "jmNewNode", this));
			clickFolderPopupMenu.add(new PMMenuItem("添加可执行文件", "jmAddFile",this));
			clickFolderPopupMenu.add(new PMMenuItem("添加文件夹", "jmAddFolder",this));
			clickFolderPopupMenu.add(new PMMenuItem("重命名", "jmRename",this));
			clickFolderPopupMenu.add(new PMMenuItem("删除", "jmDel", this));
			clickFolderPopupMenu.add(new PMMenuItem("取消", "jmcancel",this));
			clickFolderPopupMenu.add(new PMMenuItem("属性", "jmProperties",this));
		}
		// 右键排列顺序
		if (arrangeSequence == null) {
			arrangeSequence = new JPopupMenu();
			arrangeSequence.add(new PMMenuItem("按日期排列", "jmDate", this));
			arrangeSequence.add(new PMMenuItem("按类型排列", "jmType", this));
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
	 * 返回jtree的渲染器
	 */
	public PMTreeCellRenderer getMyTreeCellRenderer() {
		return myTreeCellRenderer;
	}

	/**
	 * 响应鼠标事件
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
					} else if (k == MouseEvent.BUTTON3) { // **********************鼠标右键产生的事件
						int x = m.getX();
						int y = m.getY();
						int row = getRowForLocation(x, y);
						if (row != -1 && !isSelectionEmpty()) {// 有选择某一行
							jtreePopupMenu(x, y);// solve the right key
													// popupMenu,x and y is the
													// event origin
						} else { // 如果没有选择,则显示(按日期排列与按类型排列)
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
	 * 处理右键事件,用户在jtree上点击右键时弹出的菜单.如果是连续选择多个文件,则
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
			JOptionPane.showMessageDialog(null, "对不起,暂不支持批处理");
			return;
			// TreePath[] treePaths = jtree.getSelectionPaths();
			// for(TreePath tp:treePaths){
			// DefaultMutableTreeNode treeNode =
			// (DefaultMutableTreeNode)tp.getLastPathComponent();
			// System.out.println(treeNode);
			// }
		} else {
			JOptionPane.showMessageDialog(null, "程序遇到意外情况!,即将退出");
			System.exit(0);
		}
	}

	public void actionPerformed(ActionEvent e) {
		// *****右键弹出的类型选择
		try {
			final FileNode fileNode = (FileNode) (this.getLeadSelectionPath().getLastPathComponent());
			if (e.getActionCommand().equals("jmOpenFile")|| e.getActionCommand().equals("jmOpenFolder")) {
				doTreeAction("jmOpenFile", fileNode);
			} else if (e.getActionCommand().equals("jmOpenItsFolder")) {
				Desktop.getDesktop().open(fileNode.getFile());
			}else if (e.getActionCommand().equals("jmCreateNewFolder")) {//新建文件夹
//				operatorJTree(CREATE_NEW_FOLDER, tempNode);
			 } else if (e.getActionCommand().equals("jmNewNode")) { //********新建文本
				FileNode tempNode = (FileNode)
				(this.getLeadSelectionPath().getLastPathComponent());
//				operatorJTree(CREATE_NEW_NODE, tempNode);
			 } else if (e.getActionCommand().equals("jmAddFile")) { //********添加文件
//				 operatorJTree(ADD_FILE, tempNode);
			 } else if (e.getActionCommand().equals("jmAddFolder")) { //********添加文件夹
//			 	operatorJTree(ADD_FOLDER, tempNode);
			 } else if (e.getActionCommand().equals("jmRename")) { //********重命名
//				 operatorJTree(RENAME, tempNode);
			 } else if (e.getActionCommand().equals("jmRenameFolder")) {
				 //********文件夹重命名
//				 operatorJTree(RENAME, tempNode);
			 } else if (e.getActionCommand().equals("jmDel") || e.getActionCommand().equals("jmDelFolder")) { //********文件删除//********文件夹删除
//				 operatorJTree(DEL, tempNode);
			 } else if (e.getActionCommand().equals("jmProperties")) { //***.equals("属性
//				 operatorJTree(FILE_PROPERTIES, tempNode);
			 } else if (e.getActionCommand().equals("jmPropertiesFolder")) {
//				 operatorJTree(FILE_PROPERTIES, tempNode);
			 } else if (e.getActionCommand().equals("jmcancel")) { //********取消
				 System.out.println("jmcancel");
			 } else if (e.getActionCommand().equals("jmcancelFolder")) {
				 //********文件夹取消
				 System.out.println("jmcancel");
			 } else if (e.getActionCommand().equals("jmcancelFolder")) {
				 //********文件夹取消
				 //文件夹取消
			 } //*****排列顺序
			 else if (e.getActionCommand().equals("jmDate")) {
				 System.out.println("按日期排列");
			 } else if (e.getActionCommand().equals("jmType")) {
				 System.out.println("按类型排列");
			 }
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * implement the interface TreeOperatorListener 对树的操作
	 * 
	 * @param type
	 * @param mt
	 */
	public void doTreeAction(String type, final FileNode fileNode) {
		if(type.equals("jmOpenFile")||type.equals("jmOpenFolder")){
			// 先要判断文件是否为可执行文件,哪果是的话就用Desktop打开,否则就用JTextPane打开
			new Thread(new Runnable() {
				public void run() {
					if (FileUtil.canExecuteFile(fileNode.getFile())) {
						try {
							JSplashWindow.getInstance("");
							Desktop.getDesktop().open(fileNode.getFile());
							JSplashWindow.close();
						} catch (Exception ex) {
							JSplashWindow.close();
							JOptionPane.showMessageDialog(null,"打开文件失败,请检查文件是否存在");
							ex.printStackTrace();
						}
					} else {
						if (fileNode.isLeaf()&& !fileNode.getAllowsChildren()) {// 说明此结点是叶子结点,而且是不可以扩展的结点
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
//				// 添加文件夹
//				String strTo = UtilJTree.getJTreePath(object); // 文件复制目的地
//				utilJTree.addFolder(this, strTo, tempNode);
//				SwingUtilities.updateComponentTreeUI(this);
//				break;
//			case ADD_FILE:
//				// 添加可执行文件
//				String to = UtilJTree.getJTreePath(object); // 文件复制目的地
//				utilJTree.addFile(this, to, tempNode);
//				SwingUtilities.updateComponentTreeUI(this);
//				break;
//			case CREATE_NEW_FOLDER:
//				// *****************************************************新建文件夹
//				utilJTree.newFolder(tempNode, object, PageManagerView
//						.getFilePath(), jtabbedPane);
//				break;
//			case CREATE_NEW_NODE:
//				// *****************************************************新建文件
//				utilJTree.newFile(tempNode);
//				SwingUtilities.updateComponentTreeUI(this);
//				break;
//			case RENAME:
//				// *****************************************************重命名
//				utilJTree.reName(tempNode);
//				break;
//			case DEL:
//				utilJTree.del(tempNode, co, object, PageManagerView
//						.getFilePath(), jtabbedPane);
//				break;
//			case FILE_PROPERTIES:
//				break;
//			case CLICK:
//				// *************************************处理鼠标单击事件
//				// 先要判断文件是否为可执行文件,哪果是的话就用Desktop打开,否则就用JTextPane打开
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
//										"打开文件失败,请检查文件是否存在");
//								ex.printStackTrace();
//							}
//						} else {
//							if (tempNode.isLeaf()
//									&& !tempNode.getAllowsChildren()) {
//								// 说明此结点是叶子结点,而且是不可以扩展的结点
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
