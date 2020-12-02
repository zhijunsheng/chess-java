package com.goldthumb.chess;

enum Player {
	WHITE,
	BLACK,
}

enum Rank {
	KING,
	QUEEN,
	BISHOP,
	ROOK,
	KNIGHT,
	PAWN,
}

public class ChessPiece {
	int col;
	int row;
	Player player;
	Rank rank;
	String imgName;
	
	public ChessPiece(int col, int row, Player player, Rank rank, String imgName) {
		super();
		this.col = col;
		this.row = row;
		this.player = player;
		this.rank = rank;
		this.imgName = imgName;
	}
	

}
