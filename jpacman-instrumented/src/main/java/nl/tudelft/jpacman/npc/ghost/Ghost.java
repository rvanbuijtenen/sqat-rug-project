package nl.tudelft.jpacman.npc.ghost; import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.npc.NPC;
import nl.tudelft.jpacman.sprite.Sprite; import nl.tudelft.jpacman.Api; public abstract class Ghost extends NPC {
	
	/**
	 * The sprite map, one sprite for each direction.
	 */
	private final Map<Direction, Sprite> sprites;

	/**
	 * The base move interval of the ghost.
	 */
	private final int moveInterval;

	/**
	 * The random variation added to the {@link #moveInterval}.
	 */
	private final int intervalVariation;

	/**
	 * Creates a new ghost.
	 *
	 * @param spriteMap
	 *            The sprites for every direction.
	 * @param moveInterval
	 * 			  The base interval of movement.
	 * @param intervalVariation
	 * 			  The variation of the interval.
	 */
	protected Ghost(Map<Direction, Sprite> spriteMap, int moveInterval, int intervalVariation) {
		this.sprites = spriteMap;
		this.intervalVariation = intervalVariation;
		this.moveInterval = moveInterval;
	}

	@Override
	public Sprite getSprite() {Api.hit("/src/main/java/nl/tudelft/jpacman/npc/ghost/Ghost.java", "getSprite", "lines 53:2 - 53:37"); return sprites.get(getDirection());}

	@Override
	public long getInterval() {Api.hit("/src/main/java/nl/tudelft/jpacman/npc/ghost/Ghost.java", "getInterval", "lines 58:2 - 58:74"); return this.moveInterval + new Random().nextInt(this.intervalVariation);}

	/**
	 * Determines a possible move in a random direction.
	 * 
	 * @return A direction in which the ghost can move, or <code>null</code> if
	 *         the ghost is shut in by inaccessible squares.
	 */
	protected Direction randomMove() {Api.hit("/src/main/java/nl/tudelft/jpacman/npc/ghost/Ghost.java", "randomMove", "lines 68:2 - 68:30"); Square square = getSquare(); Api.hit("/src/main/java/nl/tudelft/jpacman/npc/ghost/Ghost.java", "randomMove", "lines 69:2 - 69:49"); List<Direction> directions = new ArrayList<>(); Api.hit("/src/main/java/nl/tudelft/jpacman/npc/ghost/Ghost.java", "randomMove", "lines 70:2 - 74:3"); for (Direction d : Direction.values()) {
			if (square.getSquareAt(d).isAccessibleTo(this)) {
				directions.add(d);
			}
		} Api.hit("/src/main/java/nl/tudelft/jpacman/npc/ghost/Ghost.java", "randomMove", "lines 75:2 - 77:3"); if (directions.isEmpty()) {
			return null;
		} Api.hit("/src/main/java/nl/tudelft/jpacman/npc/ghost/Ghost.java", "randomMove", "lines 78:2 - 78:50"); int i = new Random().nextInt(directions.size()); Api.hit("/src/main/java/nl/tudelft/jpacman/npc/ghost/Ghost.java", "randomMove", "lines 79:2 - 79:27"); return directions.get(i);}
}
