package com.lgh.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.lgh.pagemanager.ui.StringParser;

public class TextUtil {
	public static String[] str = {
        " private ", " protected ", " public ", " abstract ", " class ",
        " extends ", " final ", " implements ", " interface ", " native ",
        "new ", "static ", "strictfp ", "synchronized", "transient",
        "volatile ", " break", " continue", " return", " do ",
        " while", " if", "else", " for", "instanceof",
        " switch", " case", " default ", " catch", "finally",
        "throw ", "throws ", "try", "import ", "package ",
        "boolean ", "byte ", "char ", "double ", "float ",
        "false ", "super", "this", "void ", "int ",
        "long ", "short ", "null", "true"
    };
	
	
	/**
     * 查找 ,用于代码编辑,查找是否有如:public private ...等使其变蓝色
     */
    public static Vector<StringParser> findAll(String str, CharSequence text) {
        String textToString = text.toString();
        Pattern p = Pattern.compile(str);
        List<MatchResult> results = new ArrayList<MatchResult>();
        Matcher m = p.matcher(text);
        while (!m.hitEnd()) {
            if (m.find()) {
                results.add(m.toMatchResult());
            }
        }
        Vector<StringParser> vector = new Vector<StringParser>();
        if (results.size() > 0) {
            for (int i = 0; i < results.size(); i++) {
                MatchResult current = results.get(i);
                if (i == 0) {
                    if (current.start() > 0) {
                        vector.add(new StringParser(textToString.substring(0, current.start()), false));
                    }
                    vector.add(new StringParser(current.group(), true));
                    if (i == results.size() - 1 && current.end() != textToString.length()) {
                        vector.add(new StringParser(textToString.substring(current.end(), textToString.length()), false));
                    }
                } else {
                    MatchResult pre = results.get(i - 1);
                    vector.add(new StringParser(textToString.substring(pre.end(), current.start()), false));
                    vector.add(new StringParser(current.group(), true));
                    if (i == results.size() - 1 && current.end() != textToString.length()) {
                        vector.add(new StringParser(textToString.substring(current.end(), textToString.length()), false));
                    }
                }
            }
        } else {
            vector.add(new StringParser(textToString, false));
        }
        String ha = "";
        for (StringParser sss : vector) {
            ha = ha + sss.getStr();
        }
        return vector;
    }
    public static String getString() {
        String s = "(";
        for (int i = 0; i < str.length; i++) {
            if (i == str.length - 1) {
                s = s + str[i] + ")";
            } else {
                s = s + str[i] + "|";
            }
        }
        return s;
    }
    
}
