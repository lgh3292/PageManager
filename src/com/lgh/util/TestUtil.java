package com.lgh.util;

import java.awt.Component;

import javax.swing.JFrame;

public class TestUtil {

	public static void testFrame(Component... c) {
		JFrame jf = new JFrame("test");
		jf.setSize(800, 600);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		for (Component cc : c) {
			jf.add(cc);
		}
		jf.setVisible(true);

	}
}
