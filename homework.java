package homework;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class hw2 {
	static Node bestNode = null;
	static Integer best = Integer.MIN_VALUE;
	static String play = null;
	static Boolean didRaid = Boolean.FALSE;

	public static class Node {
		private Integer[][] currentBoard;
		private String[][] markedBoard;
		private Node parent;
		private Integer x, y;
		private String move;
		// private List<Node> children;
		private Boolean isRaidPossible;
		private Integer Score;

		public Boolean getIsRaidPossible() {
			return isRaidPossible;
		}

		public void setIsRaidPossible(Boolean isRaidPossible) {
			this.isRaidPossible = isRaidPossible;
		}

		public Integer getDepth() {
			return depth;
		}

		public void setDepth(Integer depth) {
			this.depth = depth;
		}

		String Player;
		Integer depth;

		public String[][] getMarkedBoard() {
			return markedBoard;
		}

		public void setMarkedBoard(String[][] markedBoard) {
			this.markedBoard = markedBoard;
		}

		public String getPlayer() {
			return Player;
		}

		public void setPlayer(String player) {
			Player = player;
		}

		public Integer[][] getCurrent() {
			return currentBoard;
		}

		public void setCurrent(Integer[][] current) {
			this.currentBoard = current;
		}

		public Node getParent() {
			return parent;
		}

		public void setParent(Node parent) {
			this.parent = parent;
		}

		/*
		 * public List<Node> getChildren() { return children; }
		 * 
		 * public void setChildren(List<Node> children) { this.children =
		 * children; }
		 */

		Node(int n, Node parent, Integer[][] matrix, String player,
				String[][] marked, Integer d, int i, int j) {
			this.parent = parent;
			this.currentBoard = new Integer[n + 1][n + 1];
			this.currentBoard = matrix.clone();
			// this.children = new LinkedList<Node>();
			this.Player = player;
			this.depth = d;
			this.markedBoard = marked.clone();
			this.isRaidPossible = Boolean.FALSE;
			this.setX(i);
			this.move = "Stake";
			this.setY(j);
		}

		public Node(Integer n) {
			this.currentBoard = new Integer[n + 1][n + 1];
			this.markedBoard = new String[n + 1][n + 1];
		}

		public Integer getX() {
			return x;
		}

		public void setX(Integer x) {
			this.x = x;
		}

		public Integer getY() {
			return y;
		}

		public void setY(Integer y) {
			this.y = y;
		}

		public Integer getScore() {
			return Score;
		}

		public void setScore(Integer score) {
			Score = score;
		}

		public String getMove() {
			return move;
		}

		public void setMove(String move) {
			this.move = move;
		}
	}

	public static void main(String[] args) {
		FileReader reader = null;
		Scanner in = null;
		try {
			reader = new FileReader("resources/input.txt");
			in = new Scanner(reader);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if (reader == null)
			return;
		Integer n = Integer.valueOf(in.nextLine());
		String algorithm = in.nextLine();
		String player = in.nextLine();
		Integer depth = in.nextInt();
		Integer[][] board = new Integer[n + 1][n + 1];
		String[][] boardMark = new String[n + 1][n + 1];
		for (int i = 1; i <= n; i++) {
			in.nextLine();
			for (int j = 1; j <= n; j++) {
				board[i][j] = in.nextInt();
			}

		}
		in.nextLine();
		for (int i = 1; i <= n; i++) {
			String line = in.nextLine();
			int j = 1;
			for (String c : line.split("")) {
				boardMark[i][j++] = c;
			}
		}
		// print board
		System.out.println("n: " + n + " algo: " + algorithm + " player: "
				+ player + " depth: " + depth);
		for (int i = 1; i <= n; i++) {
			for (int j = 1; j <= n; j++) {
				System.out.print(board[i][j] + " ");
			}
			System.out.print("\n");
		}
		System.out.println("boardmark");
		for (int i = 1; i <= n; i++) {
			for (int j = 1; j <= n; j++) {
				System.out.print(boardMark[i][j] + " ");
			}
			System.out.print("\n");
		}
		String played = !"x".equalsIgnoreCase(player) ? "X" : "O";
		Node root = new Node(n, null, board, played, boardMark, 0, 0, 0);
		play = player;
		bestNode = new Node(n);
		bestNode.setScore(Integer.MIN_VALUE);
		bestNode.setMarkedBoard(getClone(boardMark, n,
				bestNode.getMarkedBoard()));
		// Integer calculateScore=0;//,v=Integer.MIN_VALUE,a=Integer.MIN_VALUE;
		if ("MINIMAX".equalsIgnoreCase(algorithm)) {
			Max(n, boardMark, board, depth, root, player);
		} else {
			Alpha(n, boardMark, board, depth, root, player, Integer.MIN_VALUE,
					Integer.MAX_VALUE);
		}
		char start = 'A';
		System.out.println(bestNode.getScore() + " move: " + bestNode.getMove()
				+ " coordinates: " + (char) (start + bestNode.getY() - 1) + " "
				+ bestNode.getX());
		System.out.println("best boardmark");
		for (int i = 1; i <= n; i++) {
			for (int j = 1; j <= n; j++) {
				System.out.print(bestNode.getMarkedBoard()[i][j] + " ");
			}
			System.out.print("\n");
		}
		File outputFile = new File("output.txt");

		try {
			FileWriter writer = new FileWriter(outputFile.getAbsoluteFile());
			BufferedWriter bufferWriter = new BufferedWriter(writer);
			bufferWriter.write((char) (start + bestNode.getY() - 1) + ""
					+ bestNode.getX() + " " + bestNode.getMove() + "\n");
			for (int i = 1; i <= n; i++) {
				for (int j = 1; j <= n; j++) {
					bufferWriter.write(bestNode.getMarkedBoard()[i][j]);
				}
				bufferWriter.write("\n");
			}
			bufferWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static Integer Max(Integer n, String[][] boardMark,
			Integer[][] board, Integer depth, Node root, String player) {
		List<Node> list = generateTree(depth, root, n, board, player);
		if (root.getDepth() >= depth || list == null || list.isEmpty())
			return root.getScore();
		Integer v = Integer.MIN_VALUE;

		if (list != null) {
			for (Node e : list) {
				v = Math.max(v,
						Min(n, e.getMarkedBoard(), board, depth, e, player));
				System.out.println(e.getX() + " " + e.getY() + " " + v
						+ " depth: " + e.getDepth());
				if (best < v && e.getDepth() == 1) {
					best = v;
					bestNode = e;
					System.out
							.println("boardmark " + e.getX() + " " + e.getY());
					for (int i = 1; i <= n; i++) {
						for (int j = 1; j <= n; j++) {
							System.out.print(e.getMarkedBoard()[i][j] + " ");
						}
						System.out.print("\n");
					}
				}
			}
		}
		return v;

	}

	private static Integer Min(Integer n, String[][] boardMark,
			Integer[][] board, Integer depth, Node root, String player) {
		String played = !"x".equalsIgnoreCase(player) ? "X" : "O";
		List<Node> list = generateTree(depth, root, n, board, played);
		if (root.getDepth() >= depth || list == null || list.isEmpty())
			return root.getScore();
		Integer v = Integer.MAX_VALUE;
		if (list != null) {
			for (Node e : list) {
				v = Math.min(v,
						Max(n, e.getMarkedBoard(), board, depth, e, played));
			}
		}
		return v;
	}

	private static Integer Alpha(Integer n, String[][] boardMark,
			Integer[][] board, Integer depth, Node root, String player,
			Integer alpha, Integer beta) {
		List<Node> list = generateTree(depth, root, n, board, player);
		if (root.getDepth() >= depth || list == null || list.isEmpty())
			return root.getScore();
		Integer v = Integer.MIN_VALUE;
		if (list != null) {
			for (Node e : list) {
				v = Math.max(
						v,
						Beta(n, e.getMarkedBoard(), board, depth, e, player,
								alpha, beta));
				if (v >= beta) {
					return v;
				}
				alpha = Integer.max(alpha, v);
				if (best < v && e.getDepth() == 1) {
					best = v;
					bestNode = e;
					System.out.println("boardmark");
					for (int i = 1; i <= n; i++) {
						for (int j = 1; j <= n; j++) {
							System.out.print(e.getMarkedBoard()[i][j] + " ");
						}
						System.out.print("\n");
					}
				}
			}
		}
		return v;
	}

	private static Integer Beta(Integer n, String[][] boardMark,
			Integer[][] board, Integer depth, Node root, String player,
			Integer alpha, Integer beta) {
		String played = !"x".equalsIgnoreCase(player) ? "X" : "O";
		List<Node> list = generateTree(depth, root, n, board, played);
		if (root.getDepth() >= depth || list == null || list.isEmpty())
			return root.getScore();
		Integer v = Integer.MAX_VALUE;
		if (list != null) {
			for (Node e : list) {
				v = Math.min(
						v,
						Alpha(n, e.getMarkedBoard(), board, depth, e, played,
								alpha, beta));
				if (v <= alpha) {
					return v;
				}
				beta = Integer.min(beta, v);
			}
		}
		return v;

	}

	private static Integer computescore(String[][] markedBoard, Integer n,
			Integer[][] board) {
		Integer myScore = 0, opponentScore = 0;
		for (int i = 1; i <= n; i++) {
			for (int j = 1; j <= n; j++) {
				if (play.equalsIgnoreCase(markedBoard[i][j])) {
					myScore += board[i][j];
				} else if (!play.equalsIgnoreCase(markedBoard[i][j])
						&& !markedBoard[i][j].equalsIgnoreCase(".")) {
					opponentScore += board[i][j];
				}
			}
		}
		return myScore - opponentScore;
	}

	private static LinkedList<Node> generateTree(Integer depth, Node root,
			Integer n, Integer[][] board, String turn) {
		Node temp = root;
		String[][] tempboard = root.getMarkedBoard();
		int d = temp.getDepth() + 1;
		//Boolean flag = Boolean.FALSE;
		String player = null;
		LinkedList<Node> children = new LinkedList<Node>();
		player = temp.getPlayer().equalsIgnoreCase("x") ? "O" : "X";
		if (d <= depth) {
			for (int i = 1; i <= n; i++) {
				for (int j = 1; j <= n; j++) {
					if (!checkMarked(temp.getMarkedBoard(), i, j)) {
						String[][] marked = new String[n + 1][n + 1];
						marked = getClone(tempboard, n, marked);
						marked[i][j] = player;
						Node child = new Node(n, temp, board, player, marked,
								d, i, j);
						child.setScore(computescore(marked, n, board));
						children.add(child);
					}
				}
			}
			for (int i = 1; i <= n; i++) {
				for (int j = 1; j <= n; j++) {
					//flag = Boolean.FALSE;
					didRaid = Boolean.FALSE;
					String[][] marked = new String[n + 1][n + 1];
					marked = getClone(tempboard, n, marked);
					marked[i][j] = player;
					if (!checkMarked(temp.getMarkedBoard(), i, j)
							&& checkraidPossible(i, j, marked, n, player)) {
						marked = raid(marked, i, j, n, player);
						Node child = new Node(n, temp, board, player, marked,
								d, i, j);
						child.setMove("Raid");
						child.setIsRaidPossible(Boolean.TRUE);
						child.setScore(computescore(marked, n, board));
						children.add(child);
					}
				}
			}
		}
		return children;
	}

	private static String[][] raid(String[][] marked, int i, int j, Integer n,
			String player) {
		if (i + 1 <= n && i - 1 >= 1 && j + 1 <= n && j - 1 >= 1) {
			if (checkMarked(marked, i - 1, j)
					&& !marked[i - 1][j].equalsIgnoreCase(player)) {
				marked[i - 1][j] = player;
				didRaid = Boolean.TRUE;
			}
			if (checkMarked(marked, i + 1, j)
					&& !marked[i + 1][j].equalsIgnoreCase(player)) {
				marked[i + 1][j] = player;
				didRaid = Boolean.TRUE;
			}
			if (checkMarked(marked, i, j + 1)
					&& !marked[i][j + 1].equalsIgnoreCase(player)) {
				marked[i][j + 1] = player;
				didRaid = Boolean.TRUE;
			}
			if (checkMarked(marked, i, j - 1)
					&& !marked[i][j - 1].equalsIgnoreCase(player)) {
				marked[i][j - 1] = player;
				didRaid = Boolean.TRUE;
			}
		} else if (i + 1 > n && i - 1 >= 1 && j + 1 <= n && j - 1 >= 1) {
			if (checkMarked(marked, i - 1, j)
					&& !marked[i - 1][j].equalsIgnoreCase(player)) {
				marked[i - 1][j] = player;
				didRaid = Boolean.TRUE;
			}
			if (checkMarked(marked, i, j + 1)
					&& !marked[i][j + 1].equalsIgnoreCase(player)) {
				marked[i][j + 1] = player;
				didRaid = Boolean.TRUE;
			}
			if (checkMarked(marked, i, j - 1)
					&& !marked[i][j - 1].equalsIgnoreCase(player)) {
				marked[i][j - 1] = player;
				didRaid = Boolean.TRUE;
			}
		} else if (i + 1 <= n && i - 1 < 1 && j + 1 <= n && j - 1 >= 1) {

			if (checkMarked(marked, i + 1, j)
					&& !marked[i + 1][j].equalsIgnoreCase(player)) {
				marked[i + 1][j] = player;
				didRaid = Boolean.TRUE;
			}
			if (checkMarked(marked, i, j + 1)
					&& !marked[i][j + 1].equalsIgnoreCase(player)) {
				marked[i][j + 1] = player;
				didRaid = Boolean.TRUE;
			}
			if (checkMarked(marked, i, j - 1)
					&& !marked[i][j - 1].equalsIgnoreCase(player)) {
				marked[i][j - 1] = player;
				didRaid = Boolean.TRUE;
			}

		} else if (i + 1 <= n && i - 1 >= 1 && j + 1 > n && j - 1 >= 1) {
			if (checkMarked(marked, i - 1, j)
					&& !marked[i - 1][j].equalsIgnoreCase(player)) {
				marked[i - 1][j] = player;
				didRaid = Boolean.TRUE;
			}
			if (checkMarked(marked, i + 1, j)
					&& !marked[i + 1][j].equalsIgnoreCase(player)) {
				marked[i + 1][j] = player;
				didRaid = Boolean.TRUE;
			}

			if (checkMarked(marked, i, j - 1)
					&& !marked[i][j - 1].equalsIgnoreCase(player)) {
				marked[i][j - 1] = player;
				didRaid = Boolean.TRUE;
			}

		} else if (i + 1 <= n && i - 1 >= 1 && j + 1 <= n && j - 1 < 1) {
			if (checkMarked(marked, i - 1, j)
					&& !marked[i - 1][j].equalsIgnoreCase(player)) {
				marked[i - 1][j] = player;
				didRaid = Boolean.TRUE;
			}
			if (checkMarked(marked, i + 1, j)
					&& !marked[i + 1][j].equalsIgnoreCase(player)) {
				marked[i + 1][j] = player;
				didRaid = Boolean.TRUE;
			}
			if (checkMarked(marked, i, j + 1)
					&& !marked[i][j + 1].equalsIgnoreCase(player)) {
				marked[i][j + 1] = player;
				didRaid = Boolean.TRUE;
			}
		} else if (i + 1 > n && i - 1 >= 1 && j + 1 > n && j - 1 >= 1) {
			if (checkMarked(marked, i - 1, j)
					&& !marked[i - 1][j].equalsIgnoreCase(player)) {
				marked[i - 1][j] = player;
				didRaid = Boolean.TRUE;
			}
			if (checkMarked(marked, i, j - 1)
					&& !marked[i][j - 1].equalsIgnoreCase(player)) {
				marked[i][j - 1] = player;
				didRaid = Boolean.TRUE;
			}
		} else if (i + 1 > n && i - 1 >= 1 && j + 1 <= n && j - 1 < 1) {
			if (checkMarked(marked, i - 1, j)
					&& !marked[i - 1][j].equalsIgnoreCase(player)) {
				marked[i - 1][j] = player;
				didRaid = Boolean.TRUE;
			}
			if (checkMarked(marked, i, j + 1)
					&& !marked[i][j + 1].equalsIgnoreCase(player)) {
				marked[i][j + 1] = player;
				didRaid = Boolean.TRUE;
			}
		} else if (i + 1 <= n && i - 1 < 1 && j + 1 > n && j - 1 >= 1) {
			if (checkMarked(marked, i + 1, j)
					&& !marked[i + 1][j].equalsIgnoreCase(player)) {
				marked[i + 1][j] = player;
				didRaid = Boolean.TRUE;
			}
			if (checkMarked(marked, i, j - 1)
					&& !marked[i][j - 1].equalsIgnoreCase(player)) {
				marked[i][j - 1] = player;
				didRaid = Boolean.TRUE;
			}
		} else if (i + 1 <= n && i - 1 < 1 && j + 1 <= n && j - 1 < 1) {
			if (checkMarked(marked, i + 1, j)
					&& !marked[i + 1][j].equalsIgnoreCase(player)) {
				marked[i + 1][j] = player;
				didRaid = Boolean.TRUE;
			}
			if (checkMarked(marked, i, j + 1)
					&& !marked[i][j + 1].equalsIgnoreCase(player)) {
				marked[i][j + 1] = player;
				didRaid = Boolean.TRUE;
			}
		}
		return marked;
	}

	private static String[][] getClone(String[][] tempboard, Integer n,
			String[][] marked) {
		for (int i = 0; i < marked.length; i++)
			marked[i] = tempboard[i].clone();
		return marked;
	}

	private static Boolean checkMarked(String[][] markedBoard, int i, int j) {
		if (".".equalsIgnoreCase(markedBoard[i][j]))
			return Boolean.FALSE;
		return Boolean.TRUE;
	}

	private static Boolean checkraidPossible(int i, int j, String[][] marked,
			Integer n, String player) {
		Boolean flag = Boolean.FALSE;
		if (i + 1 <= n && i - 1 >= 1 && j + 1 <= n && j - 1 >= 1) {
			if (checkMarked(marked, i - 1, j) || checkMarked(marked, i + 1, j)
					|| checkMarked(marked, i, j + 1)
					|| checkMarked(marked, i, j - 1)) {
				if (marked[i - 1][j].equalsIgnoreCase(player)
						|| marked[i + 1][j].equalsIgnoreCase(player)
						|| marked[i][j + 1].equalsIgnoreCase(player)
						|| marked[i][j - 1].equalsIgnoreCase(player))
					flag = Boolean.TRUE;
			}
		} else if (i + 1 > n && i - 1 >= 1 && j + 1 <= n && j - 1 >= 1) {
			if (checkMarked(marked, i - 1, j) || checkMarked(marked, i, j + 1)
					|| checkMarked(marked, i, j - 1)) {
				if (marked[i - 1][j].equalsIgnoreCase(player)
						|| marked[i][j + 1].equalsIgnoreCase(player)
						|| marked[i][j - 1].equalsIgnoreCase(player))
					flag = Boolean.TRUE;
			}
		} else if (i + 1 <= n && i - 1 < 1 && j + 1 <= n && j - 1 >= 1) {
			if (checkMarked(marked, i + 1, j) || checkMarked(marked, i, j + 1)
					|| checkMarked(marked, i, j - 1)) {
				if (marked[i + 1][j].equalsIgnoreCase(player)
						|| marked[i][j + 1].equalsIgnoreCase(player)
						|| marked[i][j - 1].equalsIgnoreCase(player))
					flag = Boolean.TRUE;
			}
		} else if (i + 1 <= n && i - 1 >= 1 && j + 1 > n && j - 1 >= 1) {
			if (checkMarked(marked, i - 1, j) || checkMarked(marked, i + 1, j)
					|| checkMarked(marked, i, j - 1)) {
				if (marked[i - 1][j].equalsIgnoreCase(player)
						|| marked[i + 1][j].equalsIgnoreCase(player)
						|| marked[i][j - 1].equalsIgnoreCase(player))
					flag = Boolean.TRUE;
			}
		} else if (i + 1 <= n && i - 1 >= 1 && j + 1 <= n && j - 1 < 1) {
			if (checkMarked(marked, i - 1, j) || checkMarked(marked, i + 1, j)
					|| checkMarked(marked, i, j + 1)) {
				if (marked[i - 1][j].equalsIgnoreCase(player)
						|| marked[i + 1][j].equalsIgnoreCase(player)
						|| marked[i][j + 1].equalsIgnoreCase(player))
					flag = Boolean.TRUE;
			}
		} else if (i + 1 > n && i - 1 >= 1 && j + 1 > n && j - 1 >= 1) {
			if (checkMarked(marked, i - 1, j) || checkMarked(marked, i, j - 1)) {
				if (marked[i - 1][j].equalsIgnoreCase(player)
						|| marked[i][j - 1].equalsIgnoreCase(player))
					flag = Boolean.TRUE;
			}
		} else if (i + 1 > n && i - 1 >= 1 && j + 1 <= n && j - 1 < 1) {
			if (checkMarked(marked, i - 1, j) || checkMarked(marked, i, j + 1)) {
				if (marked[i - 1][j].equalsIgnoreCase(player)
						|| marked[i][j + 1].equalsIgnoreCase(player))
					flag = Boolean.TRUE;
			}
		} else if (i + 1 <= n && i - 1 < 1 && j + 1 > n && j - 1 >= 1) {
			if (checkMarked(marked, i + 1, j) || checkMarked(marked, i, j - 1)) {
				if (marked[i + 1][j].equalsIgnoreCase(player)
						|| marked[i][j - 1].equalsIgnoreCase(player))
					flag = Boolean.TRUE;
			}
		} else if (i + 1 <= n && i - 1 < 1 && j + 1 <= n && j - 1 < 1) {
			if (checkMarked(marked, i + 1, j) || checkMarked(marked, i, j + 1)) {
				if (marked[i + 1][j].equalsIgnoreCase(player)
						|| marked[i][j + 1].equalsIgnoreCase(player))
					flag = Boolean.TRUE;
			}
		}
		return flag;
	}

}
