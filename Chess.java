import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JButton;
import javax.swing.JButton;
import javax.swing.ButtonGroup;
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
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
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

class ChessPanel extends JPanel implements MouseListener, MouseMotionListener, ActionListener {
  private int originX = 37;
  private int originY = 27;
  private int cellSide = 31;

  private Engine engine;
  private Map<String, BufferedImage> keyNameValueImage = new HashMap<String, BufferedImage>();

  private BufferedImage movingImg;
  private Point movingImgXY;
  private Point fromColRow;

  private JPanel promotionPanel;
  private JRadioButton jbQueen, jbRook, jbKnight, jbBishop;

  ChessPanel(Engine engine) {
    this.engine = engine;
    addMouseListener(this);
    addMouseMotionListener(this);

    promotionPanel = createPromotionPanel();
    add(promotionPanel);
//    promotionPanel.setLayout(null);
    promotionPanel.setVisible(false);
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
    Piece piece = engine.pieceAt(p);
    if (piece != null) {
      fromColRow = p;
      movingImg = getPieceImage(piece.imgName);
    }
  }
  
  public void mouseReleased(MouseEvent me) {
    if (fromColRow == null) return;

    Point toColRow = xyToColRow(me.getPoint());
    System.out.print("(" + fromColRow.x + ", " + fromColRow.y + ") -> (" + toColRow.x + ", " + toColRow.y + ") ");

    if (engine.isValidMove(fromColRow, toColRow)) {
      engine.move(fromColRow, toColRow);
      System.out.println("valid move");

      engine.lastMovedPiece = engine.pieceAt(toColRow);
      if (engine.lastMovedPiece.canBePromoted()) {
        promotionPanel.setVisible(true);
      }
    } else {
      System.out.println("invalid move");
    }

    fromColRow = null;
    movingImg = null;
    movingImgXY = null;
    repaint();
    System.out.println(engine);
  }

  // MouseMotionListener
  
  public void mouseMoved(MouseEvent me) {}
  
  public void mouseDragged(MouseEvent me) {
    Point p = me.getPoint();
    movingImgXY = new Point(p.x - cellSide / 2, p.y - cellSide / 2);
    repaint();
  }

  // ActionListener
  
  public void actionPerformed(ActionEvent ae) {
    Piece p = engine.lastMovedPiece;
    if (jbQueen.isSelected()) p.promoteTo(Rank.QUEEN, p.isWhite ? "Queen-white" : "Queen-black");
    if (jbRook.isSelected()) p.promoteTo(Rank.ROOK, p.isWhite ? "Rook-white" : "Rook-black");
    if (jbKnight.isSelected()) p.promoteTo(Rank.KNIGHT, p.isWhite ? "Knight-white" : "Knight-black");
    if (jbBishop.isSelected()) p.promoteTo(Rank.BISHOP, p.isWhite ? "Bishop-white" : "Bishop-black");
    promotionPanel.setVisible(false);
    repaint();
    System.out.println(engine);
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

  private JPanel createPromotionPanel() {
    JPanel promotionPanel = new JPanel();
    promotionPanel.setSize(300, 300);

    jbQueen = new JRadioButton("Q");
    jbQueen.setBounds(10, 10, 100, 30);
    jbRook = new JRadioButton("R");
    jbRook.setBounds(10, 40, 100, 30);
    jbKnight = new JRadioButton("N");
    jbKnight.setBounds(10, 70, 100, 30);
    jbBishop = new JRadioButton("B");
    jbBishop.setBounds(10, 100, 100, 30);
    ButtonGroup bg = new ButtonGroup();
    bg.add(jbQueen);
    bg.add(jbRook);
    bg.add(jbKnight);
    bg.add(jbBishop);
    jbQueen.setSelected(true);

    JButton btnOK = new JButton("OK");
    btnOK.setBounds(10, 130, 100, 30);
    promotionPanel.add(jbQueen);
    promotionPanel.add(jbRook);
    promotionPanel.add(jbKnight);
    promotionPanel.add(jbBishop);
    promotionPanel.add(btnOK);

    btnOK.addActionListener(this); 

    return promotionPanel;
  }
}

class Engine {
  Piece lastMovedPiece;
  private boolean isWhiteTurn = true;
  private Set<Piece> pieces;

  Engine(Set<Piece> pieces) {
    this.pieces = pieces;
  }

  boolean isValidMove(Point from, Point to) {
    return isValid(from, to);
  }

  void move(Point from, Point to) {
    Piece fromPiece = pieceAt(from);
    Piece toPiece = pieceAt(to);
    pieces.remove(fromPiece);
    pieces.remove(toPiece);
    pieces.add(new Piece(to.x, to.y, fromPiece.rank, fromPiece.isWhite, fromPiece.imgName)); 
  }

  private boolean isValid(Point from, Point to) {
    if (from == null || to == null || from == to || !insideBoard(to) || sameColor(from, to)) {
      return false;
    }

    Piece movingPiece = pieceAt(from);
    if (movingPiece == null) {
      return false;
    }

    boolean valid = false;
    switch (movingPiece.rank) {
      case KNIGHT: valid = isValidKnightMove(from, to); break;
      case BISHOP: valid = isValidBishopMove(from, to); break;
      case PAWN: valid = isValidPawnMove(from, to, movingPiece.isWhite); break;
      case ROOK: valid = isValidRookMove(from, to); break;
      case QUEEN: valid = isValidQueenMove(from, to); break;
      case KING: valid = isValidKingMove(from, to); break;
    }

    return valid;
  }
  
  private boolean isValidKingMove(Point from, Point to) {
    return (isStraight(from, to) || isDiagonal(from, to)) && (Math.abs(from.x - to.x) == 1 || Math.abs(from.y - to.y) == 1);
  }

  private boolean isValidQueenMove(Point from, Point to) {
    return (isStraight(from, to) || isDiagonal(from, to)) && numPiecesBetween(from, to) == 0;
  }

  private boolean isValidRookMove(Point from, Point to) {
    return isStraight(from, to) && numPiecesBetween(from, to) == 0;
  }

  private boolean isValidPawnMove(Point from, Point to, boolean isWhite) {
    if (isPawnCapturing(from, to, isWhite)) return true;
    int deltaY = Math.abs(to.y - from.y);
    if (isWhite && from.y != 6 || !isWhite && from.y != 1) {
      return goingStraightForward(from, to, isWhite) && deltaY == 1;
    }
    return goingStraightForward(from, to, isWhite) && (deltaY == 1 || deltaY == 2);
  }

  private boolean isValidBishopMove(Point from, Point to) {
    return isDiagonal(from, to) && numPiecesBetween(from, to) == 0;
  }

  private boolean isValidKnightMove(Point from, Point to) {
    return Math.abs(from.x - to.x) == 1 && Math.abs(from.y - to.y) == 2 || Math.abs(from.x - to.x) == 2 && Math.abs(from.y - to.y) == 1;
  }

  private boolean isPawnCapturing(Point from, Point to, boolean isWhite) {
    Piece target = pieceAt(to);
    return target != null && target.isWhite != isWhite && goingForward(from, to, isWhite) && isDiagonal(from, to) && Math.abs(from.x - to.x) == 1 && Math.abs(from.y - to.y) == 1;
  }

  private boolean goingForward(Point from, Point to, boolean isWhite) {
    return isWhite ? to.y < from.y : to.y > from.y;
  }

  private boolean goingStraightForward(Point from, Point to, boolean isWhite) {
    return goingForward(from, to, isWhite) && isVertical(from, to);
  }


  private int numPiecesBetween(Point p1, Point p2) {
    if (!isStraight(p1, p2) && !isDiagonal(p1, p2)) return 0;
    int numPieces = 0;
    int numPointsBetween, deltaX, deltaY;
    if (isVertical(p1, p2)) {
      numPointsBetween = Math.abs(p2.y - p1.y) - 1;
      deltaX = 0;
      deltaY = p2.y > p1.y ? 1 : -1;
    } else if (isHorizontal(p1, p2)) {
      numPointsBetween = Math.abs(p2.x - p1.x) - 1;
      deltaX = p2.x > p1.x ? 1 : -1;
      deltaY = 0;
    } else {
      numPointsBetween = Math.abs(p2.x - p1.x) - 1;
      deltaX = p2.x > p1.x ? 1 : -1;
      deltaY = p2.y > p1.y ? 1 : -1;
    }
    System.out.println("numPointsBetween = " + numPointsBetween + ", deltaX = " + deltaX + ", deltaY = " + deltaY);
    for (int i = 1; i <= numPointsBetween; i++) {
      if (pieceAt(p1.x + i * deltaX, p1.y + i * deltaY) != null) numPieces++;
    }
    return numPieces;
  }

  private boolean isStraight(Point p1, Point p2) {
    return isHorizontal(p1, p2) || isVertical(p1, p2);
  }

  private boolean isHorizontal(Point p1, Point p2) {
    return p1.y == p2.y && p1 != p2;
  }

  private boolean isVertical(Point p1, Point p2) {
    return p1.x == p2.x && p1 != p2;
  }

  private boolean isDiagonal(Point p1, Point p2) {
    return Math.abs(p1.x - p2.x) == Math.abs(p1.y - p2.y) && p1 != p2;
  }

  private boolean sameColor(Point from, Point to) {
    if (from == null || to == null) return false;
    Piece fromPiece = pieceAt(from);
    Piece toPiece = pieceAt(to);
    if (fromPiece == null || toPiece == null) return false;
    return fromPiece.isWhite == toPiece.isWhite;
  }

  private boolean insideBoard(Point location) {
    return location.x >= 0 && location.x <= 7 && location.y >= 0 && location.y <= 7;
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

  Piece pieceAt(Point p) {
    return pieceAt(p.x, p.y);
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

  boolean canBePromoted() {
    return rank == Rank.PAWN && (isWhite ? row == 0 : row == 7);
  }

  void promoteTo(Rank rank, String imgName) {
    this.rank = rank;
    this.imgName = imgName;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof Piece)) return false;
    Piece otherPiece = (Piece)other;
    //return otherPiece.col == col && otherPiece.row == row && otherPiece.rank == rank && otherPiece.isWhite == isWhite;
    return otherPiece.col == col && otherPiece.row == row;
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + col;
    result = 31 * result + row;
//    result = 31 * result + rank.hashCode();
 //   result = 31 * result + (isWhite ? 1 : 0);
    return result;
  }
}

