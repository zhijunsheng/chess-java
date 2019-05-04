import java.awt.Point;

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
}

class Piece {
  Point location;
  Rank rank;
  boolean isWhite;
}

