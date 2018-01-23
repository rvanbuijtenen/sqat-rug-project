package nl.tudelft.jpacman.level; import java.util.Map;

import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Unit;
import nl.tudelft.jpacman.sprite.AnimatedSprite;
import nl.tudelft.jpacman.sprite.Sprite; import nl.tudelft.jpacman.Api; public class Player extends Unit {

	/**
	 * The amount of points accumulated by this player.
	 */
	private int score;

	/**
	 * The animations for every direction.
	 */
	private final Map<Direction, Sprite> sprites;

	/**
	 * The animation that is to be played when Pac-Man dies.
	 */
	private final AnimatedSprite deathSprite;

	/**
	 * <code>true</code> iff this player is alive.
	 */
	private boolean alive;

	/**
	 * Creates a new player with a score of 0 points.
	 * 
	 * @param spriteMap
	 *            A map containing a sprite for this player for every direction.
	 * @param deathAnimation
	 *            The sprite to be shown when this player dies.
	 */
	public Player(Map<Direction, Sprite> spriteMap, AnimatedSprite deathAnimation) {
		this.score = 0;
		this.alive = true;
		this.sprites = spriteMap;
		this.deathSprite = deathAnimation;
		deathSprite.setAnimating(false);
	}

	/**
	 * Returns whether this player is alive or not.
	 * 
	 * @return <code>true</code> iff the player is alive.
	 */
	public boolean isAlive() {Api.hit("/src/main/java/nl/tudelft/jpacman/level/Player.java", "isAlive", "lines 59:2 - 59:15"); return alive;}

	/**
	 * Sets whether this player is alive or not.
	 * 
	 * @param isAlive
	 *            <code>true</code> iff this player is alive.
	 */
	public void setAlive(boolean isAlive) {Api.hit("/src/main/java/nl/tudelft/jpacman/level/Player.java", "setAlive", "lines 69:2 - 71:3"); if (isAlive) {
			deathSprite.setAnimating(false);
		} Api.hit("/src/main/java/nl/tudelft/jpacman/level/Player.java", "setAlive", "lines 72:2 - 74:3"); if (!isAlive) {
			deathSprite.restart();
		} Api.hit("/src/main/java/nl/tudelft/jpacman/level/Player.java", "setAlive", "lines 75:2 - 75:23"); this.alive = isAlive;}

	/**
	 * Returns the amount of points accumulated by this player.
	 * 
	 * @return The amount of points accumulated by this player.
	 */
	public int getScore() {Api.hit("/src/main/java/nl/tudelft/jpacman/level/Player.java", "getScore", "lines 84:2 - 84:15"); return score;}

	@Override
	public Sprite getSprite() {Api.hit("/src/main/java/nl/tudelft/jpacman/level/Player.java", "getSprite", "lines 89:2 - 91:3"); if (isAlive()) {
			return sprites.get(getDirection());
		} Api.hit("/src/main/java/nl/tudelft/jpacman/level/Player.java", "getSprite", "lines 92:2 - 92:21"); return deathSprite;}

	/**
	 * Adds points to the score of this player.
	 * 
	 * @param points
	 *            The amount of points to add to the points this player already
	 *            has.
	 */
	public void addPoints(int points) {Api.hit("/src/main/java/nl/tudelft/jpacman/level/Player.java", "addPoints", "lines 103:2 - 103:18"); score += points;}
}
