/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lgh.pagemanager.ui;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DragSourceMotionListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

/**
 *
 * @author Administrator
 */
public class AbstractPMTree extends JTree implements DragGestureListener, DragSourceListener, DragSourceMotionListener, DropTargetListener {
    private TreePath lastPath;
    private Rectangle2D ghostRect = new Rectangle2D.Float();
    private BufferedImage motionImage = null;//在拖动图标时应该画的图片
    private Point beingPoint = new Point();
    private Point endPoint = new Point();
    protected FileNode sourceNode;
    public AbstractPMTree(FileNode sourceNode){
        this.sourceNode = sourceNode;
        init();
    }
    private void init(){
        initTreeExpandListener();
        initDragGestureListener();
        initDragSourceListener();
        initDragSourceMotionListener();
        initDropTargetListener();
        
        super.setScrollsOnExpand(true);
        new DropTarget(this, DnDConstants.ACTION_COPY_OR_MOVE, this);

    }
      private void initTreeExpandListener() {
        addTreeExpansionListener(new TreeExpansionListener() {
            public void treeCollapsed(TreeExpansionEvent e) {
//                    System.out.println("****************************treeCollapsed");
            }
            public void treeExpanded(TreeExpansionEvent e) {
                //       System.out.println("****************************treeExpanded");
                TreePath path = e.getPath();
                if (path != null) {
                    FileNode node = (FileNode) path.getLastPathComponent();
                    if (!node.isExplored()) {
                        DefaultTreeModel model = (DefaultTreeModel) getModel();
                        node.explore();
                        model.nodeStructureChanged(node);
                    }
                }
            }
        });
    }

    private void initDragGestureListener() {
        DragSource dragSource = DragSource.getDefaultDragSource();
        dragSource.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_COPY_OR_MOVE, this); // drag gesture recognizer

    }

    private void initDropTargetListener() {
    }

    private void initDragSourceListener() {
    }

    private void initDragSourceMotionListener() {
    }

 
  /**
     * 此接口源于 DragGestureRecognizer，当该（子）类的对象检测到拖动启动动作时，调用此接口
     */
       
    public void dragGestureRecognized(DragGestureEvent dge) {
        System.out.println("**************收到dragGestureRecognized");
        TreePath path = this.getLeadSelectionPath();
        if (path == null) {
            return;
        }
        FileNode node = (FileNode) path.getLastPathComponent();
        sourceNode = node;
        // Work out the offset of the drag point from the TreePath bounding
        // rectangle origin
        Rectangle raPath = getPathBounds(path);
        Point ptDragOrigin = dge.getDragOrigin();
        beingPoint.setLocation(ptDragOrigin.x - raPath.x, ptDragOrigin.y - raPath.y);

        // Get the cell renderer (which is a JLabel) for the path being dragged
        int row = this.getRowForLocation(ptDragOrigin.x, ptDragOrigin.y);
        JLabel lbl = (JLabel) getCellRenderer().getTreeCellRendererComponent(this, // tree
                path.getLastPathComponent(), // value
                false, // isSelected (dont want a colored background)
                isExpanded(path), // isExpanded
                getModel().isLeaf(path.getLastPathComponent()), // isLeaf
                row, // row (not important for rendering)
                false // hasFocus (dont want a focus rectangle)
                );
        lbl.setSize((int) raPath.getWidth(), (int) raPath.getHeight());
        // <--
        // The layout manager would normally do this
        // Get a buffered image of the selection for dragging a ghost image
        this.motionImage = new BufferedImage((int) raPath.getWidth(), (int) raPath.getHeight(), BufferedImage.TYPE_INT_ARGB_PRE);
        Graphics2D g2 = motionImage.createGraphics();
        // Ask the cell renderer to paint itself into the BufferedImage
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC, 0.5f));
        lbl.paint(g2);// Make the image ghostlike
        g2.dispose();
//        this.getGraphics().drawImage(motionImage, dge.getDragOrigin().x,dge.getDragOrigin().y, this);
        dge.startDrag(null, // cursor
                motionImage, new Point(5, 5),
                new StringSelection(getFilename()), // transferable
                this); // drag source listener
    }



    /**
     * DragSourceListener 定义了事件接口，Drag 和 Drop 操作的始发端使用它来跟踪用户操作的状态，
     * 并通过 Drag 和 Drop 操作为用户提供合适的“拖动结束”反馈。
     */
     
    public void dragEnter(DragSourceDragEvent dsde) {
//        System.out.println("dragEnter");
    }
      
    public void dragOver(DragSourceDragEvent dsde) {
//        System.out.println("dragOver");
    }
   
    public void dropActionChanged(DragSourceDragEvent dsde) {
//        System.out.println("dropActionChanged");
    }
     
    public void dragExit(DragSourceEvent dse) {
        if (!DragSource.isDragImageSupported()) {
            repaint(ghostRect.getBounds());
        }
    }
     
    public void dragDropEnd(DragSourceDropEvent dsde) {
        motionImage = null;
        sourceNode = null;
    }


    public String getFilename() {
        TreePath path = getLeadSelectionPath();
        FileNode node = (FileNode) path.getLastPathComponent();
        return ((File) node.getUserObject()).getAbsolutePath();
    }


     /**
     * 用于接收拖动操作期间鼠标移动事件的侦听器接口。
     */
     
    public void dragMouseMoved(DragSourceDragEvent dsde) {
        // System.out.println("dragMouseMoved");
    }

    /**
     * DropTargetListener 接口是 DropTarget 类所使用的回调接口，用于提供所涉及的 DropTarget 的 DnD 操作的通知
     */
     
    public void dragEnter(DropTargetDragEvent dtde) {
        //  System.out.println("dragEnter");
    }
     
    public void dragOver(DropTargetDragEvent dtde) {
        Point pt = dtde.getLocation();
        if (pt.equals(endPoint)) {
            return;
        } else {
            endPoint = pt;
        }
        if (motionImage != null) {
            Graphics2D g2 = (Graphics2D) getGraphics();
            // If a drag image is not supported by the platform, then draw my
            // own drag image
            if (!DragSource.isDragImageSupported()) {
                paintImmediately(ghostRect.getBounds()); // Rub out the last
                // ghost image and cue
                // line
                // And remember where we are about to draw the new ghost image
                ghostRect.setRect(pt.x - beingPoint.x, pt.y - beingPoint.y,
                        motionImage.getWidth(), motionImage.getHeight());
                g2.drawImage((motionImage), AffineTransform.getTranslateInstance(ghostRect.getX(), ghostRect.getY()), null);
            }
        }
        TreePath path = getClosestPathForLocation(pt.x, pt.y);
        if (!(path == lastPath)) {
            lastPath = path;
        //     hoverTimer.restart();
        }
    }

     public void dropActionChanged(DropTargetDragEvent dtde) {
        System.out.println("dropActionChanged");
    }

    public void dragExit(DropTargetEvent dte) {
        System.out.println("dragExit");
    }

    public void drop(DropTargetDropEvent e) {
        try {
            DataFlavor stringFlavor = DataFlavor.stringFlavor;
            Transferable tr = e.getTransferable();
            TreePath path = this.getPathForLocation(e.getLocation().x, e.getLocation().y);
            if (path == null) {
                e.rejectDrop();
                return;
            }
            FileNode node = (FileNode) path.getLastPathComponent();
            if (node == null) {
                System.out.println("node为空啦");
            }
            if (e.isDataFlavorSupported(DataFlavor.javaFileListFlavor) && node.isDirectory()) {
                System.out.println("支持文件夹");
                e.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                List list = (List) (e.getTransferable().getTransferData(DataFlavor.javaFileListFlavor));
                Iterator iterator = list.iterator();
                File parent = node.getFile();
                while (iterator.hasNext()) {
                    File f = (File) iterator.next();
                    int order = JOptionPane.showConfirmDialog(null, "确认要把" + "文件" + ":" + f.getName() + "复制到:" + parent.getName(), "请求确认(是/否)", JOptionPane.YES_NO_OPTION);
                    if (order == JOptionPane.NO_OPTION) {
                        return;
                    }
//                    utilFile.moveFile(f, new File(parent, f.getName()));
                }
                node.reexplore();
                e.dropComplete(true);
                this.updateUI();
                JOptionPane.showMessageDialog(null, "添加成功!");
            } else if (e.isDataFlavorSupported(stringFlavor) && node.isDirectory()) {
                System.out.println("支持stringFlavor");
                String filename = (String) tr.getTransferData(stringFlavor);// 所要移动的文件路径
//                System.out.println("filename:"+filename);
//                if (filename.endsWith(".txt") || filename.endsWith(".java") || filename.endsWith(".jsp")
//                || filename.endsWith(".html") || filename.endsWith(".htm") || !UtilFile.canExecuteFile(filename)) {
                File f = new File(filename);
                if (f.exists() && !f.equals(node.getFile())) {
                    e.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                    int order = JOptionPane.showConfirmDialog(null, "确认要把" + "文件" + ":" + f.getName() + "复制到:" + node.getName(), "请求确认(是/否)", JOptionPane.YES_NO_OPTION);
                    if (order == JOptionPane.NO_OPTION) {
                        return;
                    }
                    boolean rename = f.renameTo(new File(node.getFile(), f.getName()));
                    node.reexplore();
                    if (rename) {
                        try {
                            ((FileNode) sourceNode.getParent()).remove(sourceNode);
                        } catch (Exception ee) {
                            e.dropComplete(true);
                            this.updateUI();
                            JOptionPane.showMessageDialog(null, "无法移动文件" + f.getName() + "  目标文件夹与源文件夹相同");
                        }
                    }//如果移动成功
                    e.dropComplete(true);
                    this.updateUI();
//                    JOptionPane.showMessageDialog(null, "添加成功!");
                } else {
                    e.rejectDrop();
                }
            } else {
                e.rejectDrop();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (UnsupportedFlavorException ufe) {
            ufe.printStackTrace();
        } finally {
            motionImage = null;
            this.repaint();
        }
    }

}
