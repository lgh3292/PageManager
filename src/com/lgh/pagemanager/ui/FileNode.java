/*
 * FileNode.java
 *
 * Created on 2007年9月18日, 下午2:23
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.lgh.pagemanager.ui;

import java.awt.Desktop;
import java.io.File;

import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 * 
 * @author tewang
 */
public class FileNode extends DefaultMutableTreeNode {
	private boolean explored = false;

	private File file;

	public FileNode() {
		
	}

	public String getAbsolutePath() {
		return file.getAbsolutePath();
	}

	public FileNode(File file) {
		this.file = file;
		this.setUserObject(file);
	}

	public boolean getAllowsChildren() {
		return this.isDirectory();
	}

	public File getFile() {
		return (File) this.getUserObject();
	}

	public boolean isDirectory() {
		return this.getFile().isDirectory();
	}

	public boolean isLeaf() {
		return !isDirectory();
	}

	/**
	 * 获得此节点的名字
	 */
	public String getName() {
		return getFile().getName();
	}

	/**
	 * 设置文件名,更改文件名
	 */
	public boolean setName(String name) {
		File f = new File(name);
		if (file.renameTo(f)) {
			this.setUserObject(f);
			// bug出现:给文件夹重命名,会出现错误,原因可能是修改了之后,找不到子节点,应该reexplore
			// 纠正代码如下:
			if (this.isDirectory() && this.isExplored()) {// 当这是对文件路进行重命名时而且文件被打开应该调用reexplore
				this.reexplore();
			}
			return true;
		}
		return false;
	}

	public boolean isExplored() {
		return explored;
	}

	public String toString() {
		File file = (File) this.getUserObject();
		String fileName = file.toString();
		int index = fileName.lastIndexOf(File.separator);
		return (index != -1 && index != fileName.length() - 1) ? fileName
				.substring(index + 1) : fileName;
	}

	public void explore() {
		if (!this.isDirectory()) {
			return;
		}
		if (!this.isExplored()) {
			File file = getFile();
			File[] children = file.listFiles();
			for (int i = 0; i < children.length; ++i) {
					this.add(new FileNode(children[i]));
			}
			explored = true;
		}
	}

	public void reexplore() {
		this.removeAllChildren();
		explored = false;
		explore();
	}
}
