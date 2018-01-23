package nl.tudelft.jpacman.board; import nl.tudelft.jpacman.sprite.PacManSprites;
import nl.tudelft.jpacman.sprite.Sprite; import nl.tudelft.jpacman.Api; public class BoardFactory {

	/**
	 * The sprite store providing the sprites for the background.
	 */
	private final PacManSprites sprites;

	/**
	 * Creates a new BoardFactory that will create a board with the provided
	 * background sprites.
	 * 
	 * @param spriteStore
	 *            The sprite store providing the sprites for the background.
	 */
	public BoardFactory(PacManSprites spriteStore) {
		this.sprites = spriteStore;
	}

	/**
	 * Creates a new board from a grid of cells and connects it.
	 * 
	 * @param grid
	 *            The square grid of cells, in which grid[x][y] corresponds to
	 *            the square at position x,y.
	 * @return A new board, wrapping a grid of connected cells.
	 */
	public Board createBoard(Square[][] grid) {Api.hit("/src/main/java/nl/tudelft/jpacman/board/BoardFactory.java", "createBoard", "lines 39:2 - 39:22"); assert grid != null; Api.hit("/src/main/java/nl/tudelft/jpacman/board/BoardFactory.java", "createBoard", "lines 41:2 - 41:32"); Board board = new Board(grid); Api.hit("/src/main/java/nl/tudelft/jpacman/board/BoardFactory.java", "createBoard", "lines 43:2 - 43:31"); int width = board.getWidth(); Api.hit("/src/main/java/nl/tudelft/jpacman/board/BoardFactory.java", "createBoard", "lines 44:2 - 44:33"); int height = board.getHeight(); Api.hit("/src/main/java/nl/tudelft/jpacman/board/BoardFactory.java", "createBoard", "lines 45:2 - 55:3"); for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				Square square = grid[x][y];
				for (Direction dir : Direction.values()) {
					int dirX = (width + x + dir.getDeltaX()) % width;
					int dirY = (height + y + dir.getDeltaY()) % height;
					Square neighbour = grid[dirX][dirY];
					square.link(neighbour, dir);
				}
			}
		} Api.hit("/src/main/java/nl/tudelft/jpacman/board/BoardFactory.java", "createBoard", "lines 57:2 - 57:15"); return board;}

	/**
	 * Creates a new square that can be occupied by any unit.
	 * 
	 * @return A new square that can be occupied by any unit.
	 */
	public Square createGround() {Api.hit("/src/main/java/nl/tudelft/jpacman/board/BoardFactory.java", "createGround", "lines 66:2 - 66:47"); return new Ground(sprites.getGroundSprite());}

	/**
	 * Creates a new square that cannot be occupied by any unit.
	 * 
	 * @return A new square that cannot be occupied by any unit.
	 */
	public Square createWall() {Api.hit("/src/main/java/nl/tudelft/jpacman/board/BoardFactory.java", "createWall", "lines 75:2 - 75:43"); return new Wall(sprites.getWallSprite());}

	/**
	 * A wall is a square that is inaccessible to anyone.
	 * 
	 * @author Jeroen Roosen 
	 */
	private static final class Wall extends Square {

		/**
		 * The background for this square.
		 */
		private final Sprite background;

		/**
		 * Creates a new wall square.
		 * 
		 * @param sprite
		 *            The background for the square.
		 */
		Wall(Sprite sprite) {
			this.background = sprite;
		}

		@Override
		public boolean isAccessibleTo(Unit unit) {Api.hit("/src/main/java/nl/tudelft/jpacman/board/BoardFactory.java", "isAccessibleTo", "lines 102:3 - 102:16"); return false;}

		@Override
		public Sprite getSprite() {Api.hit("/src/main/java/nl/tudelft/jpacman/board/BoardFactory.java", "getSprite", "lines 107:3 - 107:21"); return background;}
	}

	/**
	 * A wall is a square that is accessible to anyone.
	 * 
	 * @author Jeroen Roosen 
	 */
	private static final class Ground extends Square {

		/**
		 * The background for this square.
		 */
		private final Sprite background;

		/**
		 * Creates a new ground square.
		 * 
		 * @param sprite
		 *            The background for the square.
		 */
		Ground(Sprite sprite) {
			this.background = sprite;
		}

		@Override
		public boolean isAccessibleTo(Unit unit) {Api.hit("/src/main/java/nl/tudelft/jpacman/board/BoardFactory.java", "isAccessibleTo", "lines 135:3 - 135:15"); return true;}

		@Override
		public Sprite getSprite() {Api.hit("/src/main/java/nl/tudelft/jpacman/board/BoardFactory.java", "getSprite", "lines 140:3 - 140:21"); return background;}
	}
}
