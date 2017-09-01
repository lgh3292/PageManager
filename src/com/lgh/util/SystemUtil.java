package com.lgh.util;

import com.lgh.common.Authoritor;

/**
 * 
 * @author Administrator
 *
 */
public class SystemUtil {
     public static void setProxy(Authoritor authoritor){
    	 System.setProperty("http.proxySet", "true");
    	 System.setProperty("http.proxyHost", authoritor.getIp());
    	 System.setProperty("http.proxyPort", authoritor.getPort()+"");
     }
	
}
