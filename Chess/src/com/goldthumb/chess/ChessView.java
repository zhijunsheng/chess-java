package com.goldthumb.chess;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ChessView extends JPanel {

	private static final long serialVersionUID = -3320545318004171146L;
	
	int originX = 55;
	int originY = 45;
	int cellSide = 60;
	
	Map<String, Image> keyNameValueImage = new HashMap<String, Image>();
	
	public ChessView() {
		String[] imageNames = {
			"Bishop-black",
			"Bishop-white",
			"King-black",
			"King-white",
			"Knight-black",
			"Knight-white",
			"Pawn-black",
			"Pawn-white",
			"Queen-black",
			"Queen-white",
			"Rook-black",
			"Rook-white",
		};
		
		try {
			for (String imgNm : imageNames) {
				Image img = loadImage(imgNm + ".png");
				keyNameValueImage.put(imgNm, img);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		

	}
	
	@Override
	protected void paintChildren(Graphics g) {
		super.paintChildren(g);
		
		Graphics2D g2 = (Graphics2D)g;
		
		drawBoard(g2);
		
		drawImage(g2, 0, 0, "Rook-black");
		drawImage(g2, 0, 1, "Pawn-black");
	}

	
	private void drawImage(Graphics2D g2, int col, int row, String imgName) {
		Image img = keyNameValueImage.get(imgName);
		g2.drawImage(img, originX + col * cellSide, originY + row * cellSide, cellSide, cellSide, null);
	}
	
	private Image loadImage(String imgFileName) throws Exception  {
		ClassLoader classLoader = getClass().getClassLoader();
		URL resURL = classLoader.getResource("img/" + imgFileName);
		if (resURL == null) {
			return null;
		} else {
			File imgFile = new File(resURL.toURI());
			return ImageIO.read(imgFile);
		}
	}
	
	private void drawBoard(Graphics2D g2) {
		for (int j = 0; j < 4; j++) {
			for (int i = 0; i < 4; i++) {
				drawSquare(g2, 0 + 2 * i, 0 + 2 * j, true);
				drawSquare(g2, 1 + 2 * i, 1 + 2 * j, true);

				drawSquare(g2, 1 + 2 * i, 0 + 2 * j, false);
				drawSquare(g2, 0 + 2 * i, 1 + 2 * j, false);
			}
		}
	}
	
	private void drawSquare(Graphics2D g2, int col, int row, boolean light) {
		g2.setColor(light ? Color.white : Color.gray);		
		g2.fillRect(originX + col * cellSide, originY + row * cellSide, cellSide, cellSide);
	}
}
