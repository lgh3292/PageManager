package com.lgh.pagemanager.ui;


import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import javax.swing.undo.UndoManager;

import com.lgh.pagemanager.interaface.IFont;
import com.lgh.util.TestUtil;
import com.lgh.util.TextUtil;

/**
 *
 * @author tewang
 */
public class PMTextPane extends JTextPane implements MouseListener, KeyListener, IFont, DocumentListener {

    public static final int ZHU_SHI = 1;//注释
    public static final int ZHENG_WEN = 2;//正文
    public static final String COMMIT_ACTION = "commit";

    private static enum Mode {
        INSERT, COMPLETION
    };
    private  Mode mode     =  Mode.INSERT;
    private List<String> words;
    private StyledDocument doc;
    private boolean open;//用来指示这个对JTextPane是否已经成功的被打开,如果已经被打开的话,就设置为true
    UndoManager undo = new UndoManager();
    //**********右键菜单
    JPopupMenu jm = new JPopupMenu();
    JMenuItem jmcopy = new JMenuItem("Copy");
    JMenuItem jmcut = new JMenuItem("Cut");
    JMenuItem jmpaste = new JMenuItem("Paste");
    JMenuItem jmcheckall = new JMenuItem("checkall");
    private boolean canUndo = false;//用户是否向jtextPane里添加了东西.
    private Color color;
    private Font font = new Font("宋体",Font.PLAIN,12);
    Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);

    /** Creates a new instance of MyJTextPane */
    public PMTextPane() {
        words = Words.getInstance().getWordsList();
        this.addMouseListener(this);
        this.addKeyListener(this);
        initJPopupMenu();
        this.setFont(font);
        doc = this.getStyledDocument();
        this.getDocument().addUndoableEditListener(undo);
        doc.addUndoableEditListener(new UndoableEditListener() {

            public void undoableEditHappened(UndoableEditEvent e) {
                if (open) { //此时如果jtextPane如果已经有被打开的话,当有东西输入的时候就把它设置为true
                    setCanUndo(true);
                }
            }
        });
//        if (SolveConfigure.getInstance().getConfigureFile().getMjtps().isShowInputTip()) {
            doc.addDocumentListener(this);
            InputMap im = this.getInputMap();
            ActionMap am = this.getActionMap();
            im.put(KeyStroke.getKeyStroke("ENTER"), COMMIT_ACTION);
            am.put(COMMIT_ACTION, new CommitAction());
//        }
        initStyle(doc);
    }

    //inner class to listen user input...
    private class CompletionTask implements Runnable {

        String completion;
        int position;

        CompletionTask(String completion, int position) {
            this.completion = completion;
            this.position = position;
        }

        public void run() {
            try {
                doc.insertString(position, completion, doc.getStyle("content"));
                setCaretPosition(position + completion.length());
                moveCaretPosition(position);
                mode = Mode.COMPLETION;
            } catch (BadLocationException ex) {
                Logger.getLogger(PMTextPane.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private class CommitAction extends AbstractAction {

        public void actionPerformed(ActionEvent ev) {
            if (mode == Mode.COMPLETION) {
                int pos = doc.getLength();
                try {
                    doc.insertString(pos, " ", doc.getStyle("content"));
                    setCaretPosition(pos + 1);
                    mode = Mode.INSERT;
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                replaceSelection("\n");
            }
        }
    }

    /**
     *初始JPopupMenu
     */
    private void initJPopupMenu() {
        jm.add(jmcopy);
        jm.add(jmcut);
        jm.add(jmpaste);
        jm.add(jmcheckall);
        jmcopy.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                copy();
            }
        });
        jmcut.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                cut();
            }
        });
        jmpaste.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                paste();
            }
        });
        jmpaste.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                selectAll();
            }
        });
    }

    /**
     * 初始化doc
     */
    private void initStyle(StyledDocument doc) {
        Style text = doc.addStyle("content", def);
        StyleConstants.setFontFamily(text, "Monospaced");
        //     StyleConstants.setBold(text,true);
        StyleConstants.setFontSize(text, font.getSize());
        StyleConstants.setForeground(text, new Color(0, 0, 0));

        Style system = doc.addStyle("especial", text);
        StyleConstants.setBold(system, true);
        StyleConstants.setFontFamily(system, "Monospaced");
        StyleConstants.setFontSize(system, font.getSize());
        StyleConstants.setForeground(system, new Color(26, 26, 163));

        Style zhushi = doc.addStyle("zhushi", text);
        StyleConstants.setFontFamily(zhushi, "Monospaced");
        StyleConstants.setFontSize(zhushi, font.getSize());
        StyleConstants.setForeground(zhushi, new Color(116, 116, 116));

        Style welcome = doc.addStyle("HTML", text);
        StyleConstants.setForeground(welcome, new Color(110, 199, 225));
        StyleConstants.setFontSize(welcome, font.getSize());

    }

    public int getLength() {
        return doc.getLength();
    }

    @Override
    public Style getStyle(String s) {
        return doc.getStyle(s);
    }

    public void insertImage(ImageIcon image) {
        Style welcome = doc.addStyle("icon", def);
        StyleConstants.setAlignment(welcome, StyleConstants.ALIGN_CENTER);
        if (image != null) {
            StyleConstants.setIcon(welcome, image);
        }
        try {
            doc.insertString(doc.getLength(), " test", doc.getStyle("icon"));
        } catch (BadLocationException ex) {
            Logger.getLogger(PMTextPane.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * insertString
     */
    private void insertString(int length, String s, int type) {
        switch (type) {
            case ZHENG_WEN:
                try {
                    int a = s.trim().indexOf("//");
                    if (a == 0) {
                        doc.insertString(length, s, doc.getStyle("zhushi"));
                        return;
                    }
                    Vector<StringParser> v = TextUtil.findAll(TextUtil.getString(), s);
                    for (StringParser ss : v) {
                        if (ss.isCommon()) {
                            doc.insertString(length, ss.getStr(), doc.getStyle("especial"));
                            length = length + ss.getStr().length();
                        } else {
                            doc.insertString(length, ss.getStr(), doc.getStyle("Monospaced"));
                            length = length + ss.getStr().length();
                        }
                    }
//                            doc.insertString(length,s,style);
                } catch (BadLocationException ex) {
                    ex.printStackTrace();
                }
                break;
            case ZHU_SHI:
                try {
                    doc.insertString(doc.getLength(), s, doc.getStyle("zhushi"));
                } catch (BadLocationException ex) {
                    ex.printStackTrace();
                }
                break;
            }
    }

    /**
     * 把doc的insertString,转换成JTextPane这样方便调用
     */
    @SuppressWarnings("static-access")
    public boolean manageFile(File file) {
        boolean go = false;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String inputLine;
            boolean isZhuShi = false;  //是否为注释
            while ((inputLine = br.readLine()) != null) {
                if (inputLine.trim().indexOf("/*") == 0) { //当遇到"/**注释***/"这个注释时会比较不好设计,
                    //所以就先把它去空格,当遇到以"/*"开头就说明这是一个注释
                    if (inputLine.indexOf("*/") > 0) {
                        this.insertString(doc.getLength(), inputLine + "\n", PMTextPane.ZHU_SHI);
                        continue;
                    } else {
                        isZhuShi = true;
                    }
                } else if (inputLine.indexOf("*/") >= 0) {
                    this.insertString(doc.getLength(), inputLine + "\n", PMTextPane.ZHU_SHI);
                    isZhuShi = false;
                    continue;
                }
                if (isZhuShi) {
                    this.insertString(doc.getLength(), inputLine + "\n", PMTextPane.ZHU_SHI);
                } else if (!isZhuShi) {
                    this.insertString(doc.getLength(), inputLine + "\n", PMTextPane.ZHENG_WEN);
                }
            }
            JSplashWindow.close();
            go = true;
        } catch (Exception e) {//当找汪以文件时就寻问用户是否要新建一个文件
            JSplashWindow.close();
            int select = JOptionPane.showConfirmDialog(null, "系统找不到指定文件,是否新建此文件", "错误", JOptionPane.YES_NO_OPTION);
            if (select == JOptionPane.YES_OPTION) {
                try {
                    File parent = file.getParentFile(); //先判断路径是否可用,全法,或者有重复命名
                    if (!parent.exists()) {
                        parent.mkdirs();
                    }
                    if (parent.isFile()) {
                        JOptionPane.showMessageDialog(null, "对不起,此路径不合法,请确定路径是否已经有相同的名字");
                        return false;
                    }
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                go = true;
            } else if (select == JOptionPane.NO_OPTION) {
                go = false;
            }
        }
        return go;
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {

    }

    public boolean ha(boolean a) {
        return false;
    }

    public void mouseReleased(MouseEvent e) {
        if (e.isPopupTrigger()) {
            int c = e.getButton();
            if (c == MouseEvent.BUTTON1) {//单击放开
            //   System.out.println("1");
            } else if (c == MouseEvent.BUTTON3) { //右键
                if (e.isPopupTrigger()) {
                    jm.show(this, e.getX(), e.getY());
                }
            }
        }

    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        if (e.isControlDown() && e.getKeyCode() == e.VK_Z) {
            if (undo.canUndo()) {
                undo.undo();
            }
        } else if (e.isControlDown() && e.getKeyCode() == e.VK_Y) {
            if (undo.canRedo()) {
                undo.redo();
            }
        }
    }

    public void keyReleased(KeyEvent e) {
    }

    public boolean isCanUndo() {
        return canUndo;
    }

    public void setCanUndo(boolean canUndo) {
        this.canUndo = canUndo;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    /**
     * 设置文本编辑器字体颜色
     */
    public void setMyFont(Font f) {
        this.font = f;
    }

    /**
     * 设置文件编辑器的字体
     */
    public void setMyColor(Color c) {
        this.color = c;
    }

    /**
     * 实现自定义接口TextEditorListener
     */
    public void reDeploy(Font f, Color c) {
        this.setMyColor(c);
        this.setMyFont(f);
        this.setFont(f);
        this.setForeground(c);
    }

    public void insertUpdate(DocumentEvent e) {
        if (e.getLength() != 1) {
            return;
        }
        int pos = e.getOffset();
        String content = null;
        try {
            content = doc.getText(0, pos + 1);
        } catch (BadLocationException ee) {
            ee.printStackTrace();
        }
        // Find where the word starts
        int w;
        for (w = pos; w >= 0; w--) {
            if (!Character.isLetter(content.charAt(w))) {
                break;
            }
        }
        if (pos - w < 1) {
            // Too few chars
            return;
        }
        String prefix = content.substring(w + 1);
        int n = Collections.binarySearch(words, prefix);
        if (n < 0 && -n <= words.size()) {
            String match = words.get(-n - 1);
            if (match.startsWith(prefix)) {
                String completion = match.substring(pos - w);
                // We cannot modify Document from within notification,
                // so we submit a task that does the change later
                //        System.out.println("completion:" + completion);
                SwingUtilities.invokeLater(new CompletionTask(completion, pos + 1));
            }
        } else {
            // Nothing found
            mode = Mode.INSERT;
        }
    }

    public void removeUpdate(DocumentEvent e) {
    }

    public void changedUpdate(DocumentEvent e) {
    }
    
    
    public static void main(String[] args) {
    	File file = new File("c://TPHKLOCK.txt");
    	PMTextPane pane = new PMTextPane();
    	pane.manageFile(file);
		TestUtil.testFrame(pane);
	}
}
