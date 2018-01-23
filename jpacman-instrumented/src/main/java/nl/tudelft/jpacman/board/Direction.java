package nl.tudelft.jpacman.board; import nl.tudelft.jpacman.Api; public enum Direction {

	/**
	 * North, or up.
	 */
	NORTH(0, -1),

	/**
	 * South, or down.
	 */
	SOUTH(0, 1),

	/**
	 * West, or left.
	 */
	WEST(-1, 0),

	/**
	 * East, or right.
	 */
	EAST(1, 0);

	/**
	 * The delta x (width difference) to an element in the direction in a grid
	 * with 0,0 (x,y) as its top-left element.
	 */
	private final int dx;

	/**
	 * The delta y (height difference) to an element in the direction in a grid
	 * with 0,0 (x,y) as its top-left element.
	 */
	private final int dy;

	/**
	 * Creates a new Direction with the given parameters.
	 * 
	 * @param deltaX
	 *            The delta x (width difference) to an element in the direction
	 *            in a matrix with 0,0 (x,y) as its top-left element.
	 * @param deltaY
	 *            The delta y (height difference) to an element in the direction
	 *            in a matrix with 0,0 (x,y) as its top-left element.
	 */
	Direction(int deltaX, int deltaY) {
		this.dx = deltaX;
		this.dy = deltaY;
	}

	/**
	 * @return The delta x (width difference) for a single step in this
	 *         direction, in a matrix with 0,0 (x,y) as its top-left element.
	 */
	public int getDeltaX() {Api.hit("/src/main/java/nl/tudelft/jpacman/board/Direction.java", "getDeltaX", "lines 62:2 - 62:12"); return dx;}

	/**
	 * @return The delta y (height difference) for a single step in this
	 *         direction, in a matrix with 0,0 (x,y) as its top-left element.
	 */
	public int getDeltaY() {Api.hit("/src/main/java/nl/tudelft/jpacman/board/Direction.java", "getDeltaY", "lines 70:2 - 70:12"); return dy;}
}