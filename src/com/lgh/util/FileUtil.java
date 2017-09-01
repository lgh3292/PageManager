/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lgh.util;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class FileUtil {
    /**
     * ����һ���ļ�
     * @param path
     * @return
     */
    public static File createNewFile(String path){
        File file = new File(path);
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(FileUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return file;
    }
     /**
     * ����һ���ļ���
     * @param path
     * @return
     */
    public static File createNewFloder(String path){
        File file = new File(path);
        if(!file.exists()){
            try {
                file.mkdirs();
            } catch (Exception ex) {
                Logger.getLogger(FileUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return file;
    }
    
    /**
     * �����ļ����ж�����ļ��ǲ��ǿ�ִ���ļ���ʼ����ʱ,������
     * ���ļ��Ƿ�Ϊ��ִ���ļ�,�����,��Ҫ�����Ӧϵͳ��ͼ��,��
     * �����ǻ����������ҵ����͵�ͼ��,Ĭ�������ͼ����û�е�
     *  ��ʱ����Ҫ�Լ�����ͼ��,����һ���ȽϺÿ��������Լ���ͼ��
     */
    public static boolean canExecuteFile(File file) {
    	String name  =file.getName();
        if (name.length() > 4) {
            char cc = name.charAt(name.length() - 4);
            int index = name.lastIndexOf('.');
            if (index < 0 || index < name.length() - 4) {
                return false;
            } else if (name.endsWith("txt")) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }
}
