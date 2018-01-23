package nl.tudelft.jpacman.npc.ghost; import java.util.List;
import java.util.Map;

import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.board.Unit;
import nl.tudelft.jpacman.level.Player;
import nl.tudelft.jpacman.sprite.Sprite; import nl.tudelft.jpacman.Api; public class Inky extends Ghost {

	private static final int SQUARES_AHEAD = 2;

	/**
	 * The variation in intervals, this makes the ghosts look more dynamic and
	 * less predictable.
	 */
	private static final int INTERVAL_VARIATION = 50;

	/**
	 * The base movement interval.
	 */
	private static final int MOVE_INTERVAL = 250;

	/**
	 * Creates a new "Inky", a.k.a. Bashful.
	 * 
	 * @param spriteMap
	 *            The sprites for this ghost.
	 */
	public Inky(Map<Direction, Sprite> spriteMap) {
		super(spriteMap, MOVE_INTERVAL, INTERVAL_VARIATION);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>
	 * Bashful has the most complicated AI of all. When the ghosts are not
	 * patrolling their home corners, Bashful considers two things: Shadow's
	 * location, and the location two grid spaces ahead of Pac-Man. Bashful
	 * draws a line from Shadow to the spot that is two squares in front of
	 * Pac-Man and extends that line twice as far. Therefore, if Bashful is
	 * alongside Shadow when they are behind Pac-Man, Bashful will usually
	 * follow Shadow the whole time. But if Bashful is in front of Pac-Man when
	 * Shadow is far behind him, Bashful tends to want to move away from Pac-Man
	 * (in reality, to a point very far ahead of Pac-Man). Bashful is affected
	 * by a similar targeting bug that affects Speedy. When Pac-Man is moving or
	 * facing up, the spot Bashful uses to draw the line is two squares above
	 * and left of Pac-Man.
	 * </p>
	 * 
	 * <p>
	 * <b>Implementation:</b> by lack of a coordinate system there is a
	 * workaround: first determine the square of Blinky (A) and the square 2
	 * squares away from Pac-Man (B). Then determine the shortest path from A to
	 * B regardless of terrain and walk that same path from B. This is the
	 * destination.
	 * </p>
	 */
	// CHECKSTYLE:OFF To keep this more readable.
	@Override
	public Direction nextMove() {Api.hit("/src/main/java/nl/tudelft/jpacman/npc/ghost/Inky.java", "nextMove", "lines 97:2 - 97:66"); Unit blinky = Navigation.findNearest(Blinky.class, getSquare()); Api.hit("/src/main/java/nl/tudelft/jpacman/npc/ghost/Inky.java", "nextMove", "lines 98:2 - 100:3"); if (blinky == null) {
			return randomMove();
		} Api.hit("/src/main/java/nl/tudelft/jpacman/npc/ghost/Inky.java", "nextMove", "lines 102:2 - 102:66"); Unit player = Navigation.findNearest(Player.class, getSquare()); Api.hit("/src/main/java/nl/tudelft/jpacman/npc/ghost/Inky.java", "nextMove", "lines 103:2 - 105:3"); if (player == null) {
			return randomMove();
		} Api.hit("/src/main/java/nl/tudelft/jpacman/npc/ghost/Inky.java", "nextMove", "lines 107:2 - 107:52"); Direction targetDirection = player.getDirection(); Api.hit("/src/main/java/nl/tudelft/jpacman/npc/ghost/Inky.java", "nextMove", "lines 108:2 - 108:48"); Square playerDestination = player.getSquare(); Api.hit("/src/main/java/nl/tudelft/jpacman/npc/ghost/Inky.java", "nextMove", "lines 109:2 - 111:3"); for (int i = 0; i < SQUARES_AHEAD; i++) {
			playerDestination = playerDestination.getSquareAt(targetDirection);
		} Api.hit("/src/main/java/nl/tudelft/jpacman/npc/ghost/Inky.java", "nextMove", "lines 113:2 - 113:41"); Square destination = playerDestination; Api.hit("/src/main/java/nl/tudelft/jpacman/npc/ghost/Inky.java", "nextMove", "lines 114:2 - 115:29"); List<Direction> firstHalf = Navigation.shortestPath(blinky.getSquare(),
				playerDestination, null); Api.hit("/src/main/java/nl/tudelft/jpacman/npc/ghost/Inky.java", "nextMove", "lines 116:2 - 118:3"); if (firstHalf == null) {
			return randomMove();
		} Api.hit("/src/main/java/nl/tudelft/jpacman/npc/ghost/Inky.java", "nextMove", "lines 120:2 - 122:3"); for (Direction d : firstHalf) {
			destination = playerDestination.getSquareAt(d);
		} Api.hit("/src/main/java/nl/tudelft/jpacman/npc/ghost/Inky.java", "nextMove", "lines 124:2 - 125:23"); List<Direction> path = Navigation.shortestPath(getSquare(),
				destination, this); Api.hit("/src/main/java/nl/tudelft/jpacman/npc/ghost/Inky.java", "nextMove", "lines 126:2 - 128:3"); if (path != null && !path.isEmpty()) {
			return path.get(0);
		} Api.hit("/src/main/java/nl/tudelft/jpacman/npc/ghost/Inky.java", "nextMove", "lines 129:2 - 129:22"); return randomMove();}
	// CHECKSTYLE:ON

}
