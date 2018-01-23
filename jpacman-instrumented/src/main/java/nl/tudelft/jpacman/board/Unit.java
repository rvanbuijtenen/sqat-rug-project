package nl.tudelft.jpacman.board; import nl.tudelft.jpacman.sprite.Sprite; import nl.tudelft.jpacman.Api; public abstract class Unit {

	/**
	 * The square this unit is currently occupying.
	 */
	private Square square;
	
	/**
	 * The direction this unit is facing.
	 */
	private Direction direction;

	/**
	 * Creates a unit that is facing east.
	 */
	protected Unit() {
		this.direction = Direction.EAST;
	}
	
	/**
	 * Sets this unit to face the new direction.
	 * @param newDirection The new direction this unit is facing.
	 */
	public void setDirection(Direction newDirection) {Api.hit("/src/main/java/nl/tudelft/jpacman/board/Unit.java", "setDirection", "lines 34:2 - 34:32"); this.direction = newDirection;}
	
	/**
	 * Returns the current direction this unit is facing.
	 * @return The current direction this unit is facing.
	 */
	public Direction getDirection() {Api.hit("/src/main/java/nl/tudelft/jpacman/board/Unit.java", "getDirection", "lines 42:2 - 42:24"); return this.direction;}
	
	/**
	 * Returns the square this unit is currently occupying.
	 * 
	 * @return The square this unit is currently occupying, or <code>null</code>
	 *         if this unit is not on a square.
	 */
	public Square getSquare() {Api.hit("/src/main/java/nl/tudelft/jpacman/board/Unit.java", "getSquare", "lines 52:2 - 52:21"); assert invariant(); Api.hit("/src/main/java/nl/tudelft/jpacman/board/Unit.java", "getSquare", "lines 53:2 - 53:16"); return square;}

	/**
	 * Occupies the target square iff this unit is allowed to as decided by
	 * {@link Square#isAccessibleTo(Unit)}.
	 * 
	 * @param target
	 *            The square to occupy.
	 */
	public void occupy(Square target) {Api.hit("/src/main/java/nl/tudelft/jpacman/board/Unit.java", "occupy", "lines 64:2 - 64:24"); assert target != null; Api.hit("/src/main/java/nl/tudelft/jpacman/board/Unit.java", "occupy", "lines 66:2 - 68:3"); if (square != null) {
			square.remove(this);
		} Api.hit("/src/main/java/nl/tudelft/jpacman/board/Unit.java", "occupy", "lines 69:2 - 69:18"); square = target; Api.hit("/src/main/java/nl/tudelft/jpacman/board/Unit.java", "occupy", "lines 70:2 - 70:19"); target.put(this); Api.hit("/src/main/java/nl/tudelft/jpacman/board/Unit.java", "occupy", "lines 71:2 - 71:21"); assert invariant();}
	
	/**
	 * Leaves the currently occupying square, thus removing this unit from the board.
	 */
	public void leaveSquare() {Api.hit("/src/main/java/nl/tudelft/jpacman/board/Unit.java", "leaveSquare", "lines 78:2 - 81:3"); if (square != null) {
			square.remove(this);
			square = null;
		} Api.hit("/src/main/java/nl/tudelft/jpacman/board/Unit.java", "leaveSquare", "lines 82:2 - 82:21"); assert invariant();}

	/**
	 * Tests whether the square this unit is occupying has this unit listed as
	 * one of its occupiers.
	 * 
	 * @return <code>true</code> if the square this unit is occupying has this
	 *         unit listed as one of its occupiers, or if this unit is currently
	 *         not occupying any square.
	 */
	protected boolean invariant() {Api.hit("/src/main/java/nl/tudelft/jpacman/board/Unit.java", "invariant", "lines 94:2 - 94:64"); return square == null || square.getOccupants().contains(this);}

	/**
	 * Returns the sprite of this unit.
	 * 
	 * @return The sprite of this unit.
	 */
	public abstract Sprite getSprite();

}
