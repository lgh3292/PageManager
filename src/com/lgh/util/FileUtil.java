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
     * 创建一个文件
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
     * 创建一个文件夹
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
     * 根据文件名判断这个文件是不是可执行文件初始化树时,会先判
     * 断文件是否为可执行文件,如果是,就要获得相应系统的图标,如
     * 果不是或者是属于我的类型的图标,默认情况下图标是没有的
     *  这时就需要自己设置图标,设置一个比较好看的属于自己的图标
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
