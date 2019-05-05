import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.io.File;

class Chess {
  Chess() {
    Engine engine = new Engine(Engine.initPieces());
    System.out.println(engine);
    ChessPanel chessPanel = new ChessPanel(engine);
    chessPanel.setBounds(30, 30, 300, 300);
    JFrame f = new JFrame("Chess");
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.add(chessPanel);
    f.setSize(400, 400);
    f.setLocation(600, 600);
    f.setLayout(null);
    f.setVisible(true);
  }

  public static void main(String[] args) {
    new Chess();
  }
}

class ChessPanel extends JPanel implements MouseListener, MouseMotionListener {
  private int originX = 37;
  private int originY = 27;
  private int cellSide = 31;

  private Engine engine;
  private Map<String, BufferedImage> keyNameValueImage = new HashMap<String, BufferedImage>();

  private BufferedImage movingImg;
  private Point movingImgXY;
  private Point fromColRow;

  ChessPanel(Engine engine) {
    this.engine = engine;
    addMouseListener(this);
    addMouseMotionListener(this);
  }

  @Override
  public void paintComponent(Graphics g) {
    drawGrid(g);
    drawPieces(g);

    if (movingImg != null) {
      g.drawImage(movingImg, movingImgXY.x, movingImgXY.y, null);
    }
  }

  // MouseListener

  public void mouseClicked(MouseEvent me) {}
  public void mouseEntered(MouseEvent me) {}
  public void mouseExited(MouseEvent me) {}

  public void mousePressed(MouseEvent me) {
    Point p = xyToColRow(me.getPoint());
    Piece piece = engine.pieceAt(p.x, p.y);
    if (piece != null) {
      fromColRow = p;
      movingImg = getPieceImage(piece.imgName);
    }
  }
  
  public void mouseReleased(MouseEvent me) {}

  // MouseMotionListener
  
  public void mouseMoved(MouseEvent me) {}
  
  public void mouseDragged(MouseEvent me) {
    Point p = me.getPoint();
    movingImgXY = new Point(p.x - cellSide / 2, p.y - cellSide / 2);
    repaint();
  }

  private Point xyToColRow(Point xy) {
    return new Point((int)((xy.x - originX) / cellSide), (int)((xy.y - originY) / cellSide));
  }

  private void drawGrid(Graphics g) {
    for (int row = 0; row < 8; row++) {
      for (int col = 0; col < 8; col++) {
        g.setColor((row + col) % 2 == 0 ? Color.WHITE : Color.GRAY);
        g.fillRect(originX + col * cellSide, originY + row * cellSide, cellSide, cellSide);
      }
    }
  }

  private void drawPieces(Graphics g) {
    for (Piece p : engine.getPieces()) {
      if (fromColRow != null && fromColRow.x == p.col && fromColRow.y == p.row) {
        continue;
      }
      g.drawImage(getPieceImage(p.imgName), originX + p.col * cellSide, originY + p.row * cellSide, cellSide, cellSide, this);
    }
  }

  private BufferedImage getPieceImage(String imgName) {
    BufferedImage img = keyNameValueImage.get(imgName);
    if (img == null) {
      try {
        img = resize(ImageIO.read(new File("./img/" + imgName + ".png")), cellSide, cellSide);
        keyNameValueImage.put(imgName, img);
      } catch (Exception e) {
//        System.out.println("failed to load image " + imgName);
      }
    }
    return img;
  }

  private BufferedImage resize(BufferedImage img, int w, int h) {
    Image tmpImg = img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
    BufferedImage resized = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2d = resized.createGraphics();
    g2d.drawImage(tmpImg, 0, 0, null);
    g2d.dispose();
    return resized;
  }
}

class Engine {
  private boolean isWhiteTurn = true;

  private Set<Piece> pieces;

  Engine(Set<Piece> pieces) {
    this.pieces = pieces;
  }

  Set<Piece> getPieces() {
    return pieces;
  }
  
  static Set<Piece> initPieces() {
    Set<Piece> pieces = new HashSet<Piece>();
    for (int i = 0; i < 8; i++) {
      pieces.add(new Piece(i, 1, Rank.PAWN, false, "Pawn-black")); 
      pieces.add(new Piece(i, 6, Rank.PAWN, true, "Pawn-white")); 
    }
    for (int i = 0; i < 2; i++) {
      pieces.add(new Piece(i * 7, 0, Rank.ROOK, false, "Rook-black")); 
      pieces.add(new Piece(i * 7, 7, Rank.ROOK, true, "Rook-white")); 
      pieces.add(new Piece(1 + i * 5, 0, Rank.KNIGHT, false, "Knight-black")); 
      pieces.add(new Piece(1 + i * 5, 7, Rank.KNIGHT, true, "Knight-white")); 
      pieces.add(new Piece(2 + i * 3, 0, Rank.BISHOP, false, "Bishop-black")); 
      pieces.add(new Piece(2 + i * 3, 7, Rank.BISHOP, true, "Bishop-white")); 
    }
    pieces.add(new Piece(3, 0, Rank.QUEEN, false, "Queen-black")); 
    pieces.add(new Piece(3, 7, Rank.QUEEN, true, "Queen-white")); 
    pieces.add(new Piece(4, 0, Rank.KING, false, "King-black")); 
    pieces.add(new Piece(4, 7, Rank.KING, true, "King-white")); 
    return pieces;
  }

  Piece pieceAt(int col, int row) {
    for (Piece p: pieces) {
      if (p.col == col && p.row == row) {
        return p;
      }
    }
    return null;
  }

  public String toString() {
    int rows = 8;
    int cols = 8;
    String brdStr = "";
    for (int r = 0; r < rows; r++) {
      for (int c = 0; c < cols; c++) {
        Piece p = pieceAt(c, r);
        if (p== null) {
          brdStr += " .";
        } else {
          switch (p.rank) {
            case PAWN: brdStr += p.isWhite ? " P" : " p"; break;
            case ROOK: brdStr += p.isWhite ? " R" : " r"; break;
            case KNIGHT: brdStr += p.isWhite ? " N" : " n"; break;
            case BISHOP: brdStr += p.isWhite ? " B" : " b"; break;
            case QUEEN: brdStr += p.isWhite ? " Q" : " q"; break;
            case KING: brdStr += p.isWhite ? " K" : " k"; break;
          }
        }
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
  String imgName;

  Piece(int col, int row, Rank rank, boolean isWhite, String imgName) {
    this.col = col;
    this.row = row;
    this.rank = rank;
    this.isWhite = isWhite;
    this.imgName = imgName;
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

