
class Chess {
  public static void main(String[] args) {
    Engine engine = new Engine();
    System.out.println(engine);
  }
}

class Engine {
  private boolean isWhiteTurn = true;

  public String toString() {
    int rows = 8;
    int cols = 8;
    String brdStr = "";
    for (int r = 0; r < rows; r++) {
      for (int c = 0; c < cols; c++) {
        brdStr += " .";
      }
      brdStr += "\n";
    }
    return brdStr;
  } 
}

enum Rank {
  KING,
  QUEEN,
  BISHOP,
  KNIGHT,
  ROOK,
  PAWN,
}

class Piece {
  int col;
  int row;
  Rank rank;
  boolean isWhite;

  Piece(int col, int row, Rank rank, boolean isWhite) {
    this.col = col;
    this.row = row;
    this.rank = rank;
    this.isWhite = isWhite;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof Piece)) return false;
    Piece otherPiece = (Piece)other;
    return otherPiece.col == col && otherPiece.row == row && otherPiece.rank == rank && otherPiece.isWhite == isWhite;
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + col;
    result = 31 * result + row;
    result = 31 * result + rank.hashCode();
    result = 31 * result + (isWhite ? 1 : 0);
    return result;
  }
}

