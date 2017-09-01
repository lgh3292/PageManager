/*
 * FileNode.java
 *
 * Created on 2007��9��18��, ����2:23
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
	 * ��ô˽ڵ������
	 */
	public String getName() {
		return getFile().getName();
	}

	/**
	 * �����ļ���,�����ļ���
	 */
	public boolean setName(String name) {
		File f = new File(name);
		if (file.renameTo(f)) {
			this.setUserObject(f);
			// bug����:���ļ���������,����ִ���,ԭ��������޸���֮��,�Ҳ����ӽڵ�,Ӧ��reexplore
			// ������������:
			if (this.isDirectory() && this.isExplored()) {// �����Ƕ��ļ�·����������ʱ�����ļ�����Ӧ�õ���reexplore
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
