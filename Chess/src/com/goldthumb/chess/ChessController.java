package com.goldthumb.chess;

import javax.swing.JFrame;

public class ChessController {

	public static void main(String[] args) {
		JFrame frame = new JFrame("Chess");
		frame.setSize(600, 600);
		frame.setLocation(0, 1300);
		ChessView panel = new ChessView();
		frame.add(panel);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
