package com.lgh.pagemanager.ui;

/**
 * 字符解析器,判断该字符是否为常量
 * @author tewang
 */
public class StringParser {

    private String str; //字符串内容
    private boolean common; //是否为常量

    /** Creates a new instance of StringParser */
    public StringParser(String str, boolean isCommon) {
        this.setStr(str);
        this.setCommon(isCommon);
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public boolean isCommon() {
        return common;
    }

    public void setCommon(boolean common) {
        this.common = common;
    }
}
