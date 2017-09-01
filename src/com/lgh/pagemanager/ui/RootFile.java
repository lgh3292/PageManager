/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lgh.pagemanager.ui;

import java.io.File;
import java.net.URI;

/**
 *
 * @author Administrator
 */
public class RootFile extends File{

    public RootFile(URI uri) {
        super(uri);
    }

    public RootFile(File parent, String child) {
        super(parent, child);
    }

    public RootFile(String parent, String child) {
        super(parent, child);
    }

    public RootFile(String pathname) {
        super(pathname);
    }

    @Override
    public File[] listFiles() {
        return File.listRoots();
    }

    @Override
    public boolean isDirectory() {
        return true;
    }

    @Override
    public boolean exists() {
        return true;
    }



}
