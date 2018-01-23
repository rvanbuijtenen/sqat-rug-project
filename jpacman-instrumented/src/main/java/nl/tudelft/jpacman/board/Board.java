package nl.tudelft.jpacman.board; import nl.tudelft.jpacman.Api; public class Board {

	/**
	 * The grid of squares with board[x][y] being the square at column x, row y.
	 */
	private final Square[][] board;

	/**
	 * Creates a new board.
	 * 
	 * @param grid
	 *            The grid of squares with grid[x][y] being the square at column
	 *            x, row y.
	 */
	Board(Square[][] grid) {
		assert grid != null;
		this.board = grid;
		assert invariant() : "Initial grid cannot contain null squares";
	}
	
	/**
	 * Whatever happens, the squares on the board can't be null.
	 * @return false if any square on the board is null.
	 */
	protected final boolean invariant() {Api.hit("/src/main/java/nl/tudelft/jpacman/board/Board.java", "invariant", "lines 33:2 - 39:3"); for (Square[] row : board) {
			for (Square square : row) {
				if (square == null) {
					return false;
				}
			}
		} Api.hit("/src/main/java/nl/tudelft/jpacman/board/Board.java", "invariant", "lines 40:2 - 40:14"); return true;}

	/**
	 * Returns the number of columns.
	 * 
	 * @return The width of this board.
	 */
	public int getWidth() {Api.hit("/src/main/java/nl/tudelft/jpacman/board/Board.java", "getWidth", "lines 49:2 - 49:22"); return board.length;}

	/**
	 * Returns the number of rows.
	 * 
	 * @return The height of this board.
	 */
	public int getHeight() {Api.hit("/src/main/java/nl/tudelft/jpacman/board/Board.java", "getHeight", "lines 58:2 - 58:25"); return board[0].length;}

	/**
	 * Returns the square at the given <code>x,y</code> position.
	 * 
	 * @param x
	 *            The <code>x</code> position (column) of the requested square.
	 * @param y
	 *            The <code>y</code> position (row) of the requested square.
	 * @return The square at the given <code>x,y</code> position (never null).
	 */
	public Square squareAt(int x, int y) {Api.hit("/src/main/java/nl/tudelft/jpacman/board/Board.java", "squareAt", "lines 71:2 - 71:29"); assert withinBorders(x, y); Api.hit("/src/main/java/nl/tudelft/jpacman/board/Board.java", "squareAt", "lines 72:2 - 72:30"); Square result = board[x][y]; Api.hit("/src/main/java/nl/tudelft/jpacman/board/Board.java", "squareAt", "lines 73:2 - 73:52"); assert result != null : "Follows from invariant."; Api.hit("/src/main/java/nl/tudelft/jpacman/board/Board.java", "squareAt", "lines 74:2 - 74:16"); return result;}

	/**
	 * Determines whether the given <code>x,y</code> position is on this board.
	 * 
	 * @param x
	 *            The <code>x</code> position (row) to test.
	 * @param y
	 *            The <code>y</code> position (column) to test.
	 * @return <code>true</code> iff the position is on this board.
	 */
	public boolean withinBorders(int x, int y) {Api.hit("/src/main/java/nl/tudelft/jpacman/board/Board.java", "withinBorders", "lines 87:2 - 87:63"); return x >= 0 && x < getWidth() && y >= 0 && y < getHeight();}
}
