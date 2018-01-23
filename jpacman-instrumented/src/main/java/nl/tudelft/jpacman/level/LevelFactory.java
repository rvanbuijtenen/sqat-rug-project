package nl.tudelft.jpacman.level; import java.util.List;
import java.util.Map;

import nl.tudelft.jpacman.board.Board;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.npc.NPC;
import nl.tudelft.jpacman.npc.ghost.Ghost;
import nl.tudelft.jpacman.npc.ghost.GhostColor;
import nl.tudelft.jpacman.npc.ghost.GhostFactory;
import nl.tudelft.jpacman.sprite.PacManSprites;
import nl.tudelft.jpacman.sprite.Sprite; import nl.tudelft.jpacman.Api; public class LevelFactory {

	private static final int GHOSTS = 4;
	private static final int BLINKY = 0;
	private static final int INKY = 1;
	private static final int PINKY = 2;
	private static final int CLYDE = 3;

	/**
	 * The default value of a pellet.
	 */
	private static final int PELLET_VALUE = 10;

	/**
	 * The sprite store that provides sprites for units.
	 */
	private final PacManSprites sprites;

	/**
	 * Used to cycle through the various ghost types.
	 */
	private int ghostIndex;

	/**
	 * The factory providing ghosts.
	 */
	private final GhostFactory ghostFact;

	/**
	 * Creates a new level factory.
	 * 
	 * @param spriteStore
	 *            The sprite store providing the sprites for units.
	 * @param ghostFactory
	 *            The factory providing ghosts.
	 */
	public LevelFactory(PacManSprites spriteStore, GhostFactory ghostFactory) {
		this.sprites = spriteStore;
		this.ghostIndex = -1;
		this.ghostFact = ghostFactory;
	}

	/**
	 * Creates a new level from the provided data.
	 * 
	 * @param board
	 *            The board with all ghosts and pellets occupying their squares.
	 * @param ghosts
	 *            A list of all ghosts on the board.
	 * @param startPositions
	 *            A list of squares from which players may start the game.
	 * @return A new level for the board.
	 */
	public Level createLevel(Board board, List<NPC> ghosts,
			List<Square> startPositions) {Api.hit("/src/main/java/nl/tudelft/jpacman/level/LevelFactory.java", "createLevel", "lines 78:2 - 78:53"); CollisionMap collisionMap = new PlayerCollisions(); Api.hit("/src/main/java/nl/tudelft/jpacman/level/LevelFactory.java", "createLevel", "lines 80:2 - 80:64"); return new Level(board, ghosts, startPositions, collisionMap);}

	/**
	 * Creates a new ghost.
	 * 
	 * @return The new ghost.
	 */
	NPC createGhost() {Api.hit("/src/main/java/nl/tudelft/jpacman/level/LevelFactory.java", "createGhost", "lines 89:2 - 89:15"); ghostIndex++; Api.hit("/src/main/java/nl/tudelft/jpacman/level/LevelFactory.java", "createGhost", "lines 90:2 - 90:23"); ghostIndex %= GHOSTS; Api.hit("/src/main/java/nl/tudelft/jpacman/level/LevelFactory.java", "createGhost", "lines 91:2 - 102:3"); switch (ghostIndex) {
		case BLINKY:
			return ghostFact.createBlinky();
		case INKY:
			return ghostFact.createInky();
		case PINKY:
			return ghostFact.createPinky();
		case CLYDE:
			return ghostFact.createClyde();
		default:
			return new RandomGhost(sprites.getGhostSprite(GhostColor.RED));
		}}

	/**
	 * Creates a new pellet.
	 * 
	 * @return The new pellet.
	 */
	public Pellet createPellet() {Api.hit("/src/main/java/nl/tudelft/jpacman/level/LevelFactory.java", "createPellet", "lines 111:2 - 111:61"); return new Pellet(PELLET_VALUE, sprites.getPelletSprite());}

	/**
	 * Implementation of an NPC that wanders around randomly.
	 * 
	 * @author Jeroen Roosen 
	 */
	private static final class RandomGhost extends Ghost {

		/**
		 * The suggested delay between moves.
		 */
		private static final long DELAY = 175L;

		/**
		 * Creates a new random ghost.
		 * 
		 * @param ghostSprite
		 *            The sprite for the ghost.
		 */
		RandomGhost(Map<Direction, Sprite> ghostSprite) {
			super(ghostSprite, (int) DELAY, 0);
		}

		@Override
		public Direction nextMove() {Api.hit("/src/main/java/nl/tudelft/jpacman/level/LevelFactory.java", "nextMove", "lines 138:3 - 138:23"); return randomMove();}
	}
}
