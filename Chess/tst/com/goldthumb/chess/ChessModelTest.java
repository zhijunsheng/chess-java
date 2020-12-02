package com.goldthumb.chess;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ChessModelTest {

	@Test
	void testToString() {
		ChessModel chessModel = new ChessModel();
		chessModel.reset();
		System.out.println(chessModel);
	}

}
