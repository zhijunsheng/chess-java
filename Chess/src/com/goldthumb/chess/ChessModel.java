package com.goldthumb.chess;

import java.util.HashSet;
import java.util.Set;

public class ChessModel {
	private Set<ChessPiece> piecesBox = new HashSet<ChessPiece>();
	
	void reset() {
		piecesBox.add(new ChessPiece(0, 7, Player.BLACK, Rank.ROOK, "Rook-black"));
	}
	
	ChessPiece pieceAt(int col, int row) {
		for (ChessPiece chessPiece : piecesBox) {
			if (chessPiece.col == col && chessPiece.row == row) {
				return chessPiece;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		String desc = "";
		
		for (int row = 7; row >= 0; row--) {
			desc += "" + row;
			for (int col = 0; col < 8; col++) {
				ChessPiece p = pieceAt(col, row);
				if (p == null) {
					desc += " .";
				} else {
					desc += " R";
				}
			}
			desc += "\n";
		}
		desc += "  0 1 2 3 4 5 6 7";
		
		return desc;
	}
}
