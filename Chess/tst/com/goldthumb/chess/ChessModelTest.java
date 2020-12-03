package com.goldthumb.chess;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ChessModelTest {
	
	@Test
	void testPieceAt() {
		ChessModel chessModel = new ChessModel();
		assertNull(chessModel.pieceAt(0, 0));
		chessModel.reset();
		assertNotNull(chessModel.pieceAt(0, 0));
		assertEquals(Player.WHITE, chessModel.pieceAt(0, 0).player);
		assertEquals(Rank.ROOK, chessModel.pieceAt(0, 0).rank);
	}

	@Test
	void testToString() {
		ChessModel chessModel = new ChessModel();
		assertTrue(chessModel.toString().contains("0 . . . . . . . ."));
		chessModel.reset();
		System.out.println(chessModel);
		assertTrue(chessModel.toString().contains("0 r n b q k b n r"));
	}

}
