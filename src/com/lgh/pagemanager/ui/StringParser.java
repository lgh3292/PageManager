package com.lgh.pagemanager.ui;

/**
 * �ַ�������,�жϸ��ַ��Ƿ�Ϊ����
 * @author tewang
 */
public class StringParser {

    private String str; //�ַ�������
    private boolean common; //�Ƿ�Ϊ����

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
