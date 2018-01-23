package nl.tudelft.jpacman.sprite; import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import nl.tudelft.jpacman.PacmanConfigurationException;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.npc.ghost.GhostColor; import nl.tudelft.jpacman.Api; public class PacManSprites extends SpriteStore {

	/**
	 * The sprite files are vertically stacked series for each direction, this
	 * array denotes the order.
	 */
	private static final Direction[] DIRECTIONS = { Direction.NORTH,
			Direction.EAST, Direction.SOUTH, Direction.WEST };

	/**
	 * The image size in pixels.
	 */
	private static final int SPRITE_SIZE = 16;

	/**
	 * The amount of frames in the pacman animation.
	 */
	private static final int PACMAN_ANIMATION_FRAMES = 4;

	/**
	 * The amount of frames in the pacman dying animation.
	 */
	private static final int PACMAN_DEATH_FRAMES = 11;
	
	/**
	 * The amount of frames in the ghost animation.
	 */
	private static final int GHOST_ANIMATION_FRAMES = 2;

	/**
	 * The delay between frames.
	 */
	private static final int ANIMATION_DELAY = 200;

	/**
	 * @return A map of animated Pac-Man sprites for all directions.
	 */
	public Map<Direction, Sprite> getPacmanSprites() {Api.hit("/src/main/java/nl/tudelft/jpacman/sprite/PacManSprites.java", "getPacmanSprites", "lines 54:2 - 54:72"); return directionSprite("/sprite/pacman.png", PACMAN_ANIMATION_FRAMES);}

	/**
	 * @return The animation of a dying Pac-Man.
	 */
	public AnimatedSprite getPacManDeathAnimation() {Api.hit("/src/main/java/nl/tudelft/jpacman/sprite/PacManSprites.java", "getPacManDeathAnimation", "lines 61:2 - 61:39"); String resource = "/sprite/dead.png"; Api.hit("/src/main/java/nl/tudelft/jpacman/sprite/PacManSprites.java", "getPacManDeathAnimation", "lines 63:2 - 63:42"); Sprite baseImage = loadSprite(resource); Api.hit("/src/main/java/nl/tudelft/jpacman/sprite/PacManSprites.java", "getPacManDeathAnimation", "lines 64:2 - 65:28"); AnimatedSprite animation = createAnimatedSprite(baseImage, PACMAN_DEATH_FRAMES,
				ANIMATION_DELAY, false); Api.hit("/src/main/java/nl/tudelft/jpacman/sprite/PacManSprites.java", "getPacManDeathAnimation", "lines 66:2 - 66:32"); animation.setAnimating(false); Api.hit("/src/main/java/nl/tudelft/jpacman/sprite/PacManSprites.java", "getPacManDeathAnimation", "lines 68:2 - 68:19"); return animation;}

	/**
	 * Returns a new map with animations for all directions.
	 * 
	 * @param resource
	 *            The resource name of the sprite.
	 * @param frames
	 *            The number of frames in this sprite.
	 * @return The animated sprite facing the given direction.
	 */
	private Map<Direction, Sprite> directionSprite(String resource, int frames) {Api.hit("/src/main/java/nl/tudelft/jpacman/sprite/PacManSprites.java", "directionSprite", "lines 81:2 - 81:50"); Map<Direction, Sprite> sprite = new HashMap<>(); Api.hit("/src/main/java/nl/tudelft/jpacman/sprite/PacManSprites.java", "directionSprite", "lines 83:2 - 83:42"); Sprite baseImage = loadSprite(resource); Api.hit("/src/main/java/nl/tudelft/jpacman/sprite/PacManSprites.java", "directionSprite", "lines 84:2 - 91:3"); for (int i = 0; i < DIRECTIONS.length; i++) {
			Sprite directionSprite = baseImage.split(0, i * SPRITE_SIZE, frames
					* SPRITE_SIZE, SPRITE_SIZE);
			AnimatedSprite animation = createAnimatedSprite(directionSprite,
					frames, ANIMATION_DELAY, true);
			animation.setAnimating(true);
			sprite.put(DIRECTIONS[i], animation);
		} Api.hit("/src/main/java/nl/tudelft/jpacman/sprite/PacManSprites.java", "directionSprite", "lines 93:2 - 93:16"); return sprite;}

	/**
	 * Returns a map of animated ghost sprites for all directions.
	 * 
	 * @param color
	 *            The colour of the ghost.
	 * @return The Sprite for the ghost.
	 */
	public Map<Direction, Sprite> getGhostSprite(GhostColor color) {Api.hit("/src/main/java/nl/tudelft/jpacman/sprite/PacManSprites.java", "getGhostSprite", "lines 104:2 - 104:23"); assert color != null; Api.hit("/src/main/java/nl/tudelft/jpacman/sprite/PacManSprites.java", "getGhostSprite", "lines 106:2 - 107:13"); String resource = "/sprite/ghost_" + color.name().toLowerCase()
				+ ".png"; Api.hit("/src/main/java/nl/tudelft/jpacman/sprite/PacManSprites.java", "getGhostSprite", "lines 108:2 - 108:59"); return directionSprite(resource, GHOST_ANIMATION_FRAMES);}

	/**
	 * @return The sprite for the wall.
	 */
	public Sprite getWallSprite() {Api.hit("/src/main/java/nl/tudelft/jpacman/sprite/PacManSprites.java", "getWallSprite", "lines 115:2 - 115:40"); return loadSprite("/sprite/wall.png");}

	/**
	 * @return The sprite for the ground.
	 */
	public Sprite getGroundSprite() {Api.hit("/src/main/java/nl/tudelft/jpacman/sprite/PacManSprites.java", "getGroundSprite", "lines 122:2 - 122:41"); return loadSprite("/sprite/floor.png");}

	/**
	 * @return The sprite for the
	 */
	public Sprite getPelletSprite() {Api.hit("/src/main/java/nl/tudelft/jpacman/sprite/PacManSprites.java", "getPelletSprite", "lines 129:2 - 129:42"); return loadSprite("/sprite/pellet.png");}

	/**
	 * Overloads the default sprite loading, ignoring the exception. This class
	 * assumes all sprites are provided, hence the exception will be thrown as a
	 * {@link RuntimeException}.
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public Sprite loadSprite(String resource) {Api.hit("/src/main/java/nl/tudelft/jpacman/sprite/PacManSprites.java", "loadSprite", "lines 141:2 - 145:3"); try {
			return super.loadSprite(resource);
		} catch (IOException e) {
			throw new PacmanConfigurationException("Unable to load sprite: " + resource, e);
		}}
}
