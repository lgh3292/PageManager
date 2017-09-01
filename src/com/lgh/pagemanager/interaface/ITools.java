/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lgh.pagemanager.interaface;

/**
 * 各种各样的其他工具，包括聊天工具，下载工具
 * @author Administrator
 */
public interface ITools {
    /**初始化下载工具**/
    public Object initDownLoads();
    /**LAN聊天工具**/
    public Object initLAN();

    /**初始化邮件搜索工具**/
    public Object initSearch();

    
}
