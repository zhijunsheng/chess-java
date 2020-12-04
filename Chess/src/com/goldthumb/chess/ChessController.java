package com.goldthumb.chess;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.Executors;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class ChessController implements ChessDelegate, ActionListener, Runnable {
	private ChessModel chessModel = new ChessModel();
	private ChessView chessBoardPanel;
	private JButton resetBtn;
	private JButton serverBtn;
	private JButton clientBtn;
	private PrintWriter printWriter;
	private Scanner scanner;
	
	ChessController() {
		chessModel.reset();
		
		var frame = new JFrame("Chess");
		frame.setSize(600, 600);
		frame.setLocation(0, 1300);
		frame.setLayout(new BorderLayout());
		
		chessBoardPanel = new ChessView(this);
		
		frame.add(chessBoardPanel, BorderLayout.CENTER);
		
		var buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		resetBtn = new JButton("Reset");
		resetBtn.addActionListener(this);
		buttonsPanel.add(resetBtn);
		
		serverBtn = new JButton("Listen");
		buttonsPanel.add(serverBtn);
		serverBtn.addActionListener(this);
		
		clientBtn = new JButton("Connect");
		buttonsPanel.add(clientBtn);
		clientBtn.addActionListener(this);
		
		frame.add(buttonsPanel, BorderLayout.PAGE_END);
		
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				printWriter.close();
				scanner.close();
			}
		});
	}

	public static void main(String[] args) {
		new ChessController();
	}

	@Override
	public ChessPiece pieceAt(int col, int row) {
		return chessModel.pieceAt(col, row);
	}

	@Override
	public void movePiece(int fromCol, int fromRow, int toCol, int toRow) {
		chessModel.movePiece(fromCol, fromRow, toCol, toRow);
		chessBoardPanel.repaint();
		if (printWriter != null) {
			printWriter.println(fromCol + "," + fromRow + "," + toCol + "," + toRow);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == resetBtn) {
			chessModel.reset();
			chessBoardPanel.repaint();
		} else if (e.getSource() == serverBtn) {
			var pool = Executors.newFixedThreadPool(1);
			pool.execute(this);
		} else if (e.getSource() == clientBtn) {
			System.out.println("Connect (for socket client) clicked");
			try (var socket = new Socket("localhost", 50000)) {
				scanner = new Scanner(socket.getInputStream());
				printWriter = new PrintWriter(socket.getOutputStream(), true);
				while (scanner.hasNextLine()) {
					var moveStr = scanner.nextLine();
					System.out.println("from server: " + moveStr);
					var moveStrArr = moveStr.split(",");
					var fromCol = Integer.parseInt(moveStrArr[0]);
					var fromRow = Integer.parseInt(moveStrArr[1]);
					var toCol = Integer.parseInt(moveStrArr[2]);
					var toRow = Integer.parseInt(moveStrArr[3]);
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							chessModel.movePiece(fromCol, fromRow, toCol, toRow);
							chessBoardPanel.repaint();
						}
					});
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	@Override
	public void run() {
		try (var listener = new ServerSocket(50000)) {
			System.out.println("server is listening to port 50000");
			try (var socket = listener.accept()) {
				printWriter = new PrintWriter(socket.getOutputStream(), true);
				scanner = new Scanner(socket.getInputStream());
				while (scanner.hasNextLine()) {
					var moveStr = scanner.nextLine();
					System.out.println("from server: " + moveStr);
					var moveStrArr = moveStr.split(",");
					var fromCol = Integer.parseInt(moveStrArr[0]);
					var fromRow = Integer.parseInt(moveStrArr[1]);
					var toCol = Integer.parseInt(moveStrArr[2]);
					var toRow = Integer.parseInt(moveStrArr[3]);
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							chessModel.movePiece(fromCol, fromRow, toCol, toRow);
							chessBoardPanel.repaint();
						}
					});
				}
				printWriter.println("0,1,0,3");
				System.out.println("server: sending a move to client");
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

}
