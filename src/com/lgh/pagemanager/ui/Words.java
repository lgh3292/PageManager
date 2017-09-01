package com.lgh.pagemanager.ui;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tewang
 */
public class Words {

    private static Words words;
    private List<String> wordsList;
    private File file;

    public Words() {
        try {
            file = new File("words.txt");
            wordsList = new ArrayList<String>();
        } catch (Exception ex) {
            Logger.getLogger(Words.class.getName()).log(Level.SEVERE, null, ex);
        }
        initWordsList();
    }

    /**
     * »ñµÃwordsList 
     */
    public List getWordsList() {
        return wordsList;
    }

    private void initWordsList() {
        BufferedReader br = null;
        try {
        	if(!file.exists())file.createNewFile();
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String str = null;
            while ((str = br.readLine()) != null) {
                wordsList.add(str);
            }
        } catch (Exception ex) {
            Logger.getLogger(Words.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(Words.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static synchronized Words getInstance() {
        if (words == null) {
            words = new Words();
        }
        return words;
    }

    public static void main(String[] args) {
        Words.getInstance();
    }
}
