package com.goldthumb.chess;

import java.util.HashSet;
import java.util.Set;

public class ChessModel {
	private Set<ChessPiece> piecesBox = new HashSet<ChessPiece>();
	private Player playerInTurn = Player.WHITE;
	
	void reset() {
		piecesBox.removeAll(piecesBox);
		
		for (int i = 0; i < 2; i++) {
			piecesBox.add(new ChessPiece(0 + i * 7, 7, Player.BLACK, Rank.ROOK, ChessConstants.bRook));
			piecesBox.add(new ChessPiece(0 + i * 7, 0, Player.WHITE, Rank.ROOK, ChessConstants.wRook));

			piecesBox.add(new ChessPiece(1 + i * 5, 7, Player.BLACK, Rank.KNIGHT, ChessConstants.bKnight));
			piecesBox.add(new ChessPiece(1 + i * 5, 0, Player.WHITE, Rank.KNIGHT, ChessConstants.wKnight));

			piecesBox.add(new ChessPiece(2 + i * 3, 7, Player.BLACK, Rank.BISHOP, ChessConstants.bBishop));
			piecesBox.add(new ChessPiece(2 + i * 3, 0, Player.WHITE, Rank.BISHOP, ChessConstants.wBishop));
		}
		
		for (int i = 0; i < 8; i++) {
			piecesBox.add(new ChessPiece(i, 6, Player.BLACK, Rank.PAWN, ChessConstants.bPawn));
			piecesBox.add(new ChessPiece(i, 1, Player.WHITE, Rank.PAWN, ChessConstants.wPawn));
		}
		
		piecesBox.add(new ChessPiece(3, 7, Player.BLACK, Rank.QUEEN, ChessConstants.bQueen));
		piecesBox.add(new ChessPiece(3, 0, Player.WHITE, Rank.QUEEN, ChessConstants.wQueen));
		piecesBox.add(new ChessPiece(4, 7, Player.BLACK, Rank.KING, ChessConstants.bKing));
		piecesBox.add(new ChessPiece(4, 0, Player.WHITE, Rank.KING, ChessConstants.wKing));
		
		playerInTurn = Player.WHITE;
	}
	
	void movePiece(int fromCol, int fromRow, int toCol, int toRow) {
		ChessPiece movingPiece = pieceAt(fromCol, fromRow);
		if (movingPiece == null || fromCol == toCol && fromRow == toRow) {
			return;
		}
		
		ChessPiece target = pieceAt(toCol, toRow);
		if (target != null) {
			if (target.getPlayer() == movingPiece.getPlayer()) {
				return;
			} else {
				piecesBox.remove(target);
			}
		}
		
		piecesBox.remove(movingPiece);
		piecesBox.add(new ChessPiece(toCol, toRow, movingPiece.getPlayer(), movingPiece.getRank(), movingPiece.getImgName()));
		playerInTurn = playerInTurn == Player.WHITE ? Player.BLACK : Player.WHITE;
	}
	
	ChessPiece pieceAt(int col, int row) {
		for (ChessPiece chessPiece : piecesBox) {
			if (chessPiece.getCol() == col && chessPiece.getRow() == row) {
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
					desc += " ";
					switch (p.getRank()) {
					case KING: 
						desc += p.getPlayer() == Player.WHITE ? "k" : "K";
						break;
					case QUEEN: 
						desc += p.getPlayer() == Player.WHITE ? "q" : "Q";
						break;
					case BISHOP: 
						desc += p.getPlayer() == Player.WHITE ? "b" : "B";
						break;
					case ROOK: 
						desc += p.getPlayer() == Player.WHITE ? "r" : "R";
						break;
					case KNIGHT: 
						desc += p.getPlayer() == Player.WHITE ? "n" : "N";
						break;
					case PAWN: 
						desc += p.getPlayer() == Player.WHITE ? "p" : "P";
						break;
					}
				}
			}
			desc += "\n";
		}
		desc += "  0 1 2 3 4 5 6 7";
		
		return desc;
	}
}
