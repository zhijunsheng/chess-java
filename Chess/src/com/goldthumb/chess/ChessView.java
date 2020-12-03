package com.goldthumb.chess;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ChessView extends JPanel {

	private static final long serialVersionUID = -3320545318004171146L;
	
	private ChessDelegate chessDelegate;
	
	private double scaleFactor = 0.9;
	private int originX = -1;
	private int originY = -1;
	private int cellSide = -1;
	
	private Map<String, Image> keyNameValueImage = new HashMap<String, Image>();
	
	ChessView(ChessDelegate chessDelegate) {
		this.chessDelegate = chessDelegate;
		
		String[] imageNames = {
			ChessConstants.bBishop,
			ChessConstants.wBishop,
			ChessConstants.bKing,
			ChessConstants.wKing,
			ChessConstants.bKnight,
			ChessConstants.wKnight,
			ChessConstants.bPawn,
			ChessConstants.wPawn,
			ChessConstants.bQueen,
			ChessConstants.wQueen,
			ChessConstants.bRook,
			ChessConstants.wRook,
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
		
		int smaller = Math.min(getSize().width, getSize().height);
		cellSide = (int) (((double)smaller) * scaleFactor / 8);
		originX = (getSize().width - 8 * cellSide) / 2;
		originY = (getSize().height - 8 * cellSide) / 2;
		
		Graphics2D g2 = (Graphics2D)g;
		
		drawBoard(g2);
		drawPieces(g2);
	}
	
	private void drawPieces(Graphics2D g2) {
		for (int row = 7; row >= 0; row--) {
			for (int col = 0; col < 8; col++) {
				ChessPiece p = chessDelegate.pieceAt(col, row);
				if (p != null) {
					drawImage(g2, col, row, p.imgName);
				}
			}
		}
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
