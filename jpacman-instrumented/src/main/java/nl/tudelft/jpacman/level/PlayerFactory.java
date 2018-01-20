package nl.tudelft.jpacman.level; import nl.tudelft.jpacman.sprite.PacManSprites; import nl.tudelft.jpacman.Api; public class PlayerFactory {

	/**
	 * The sprite store containing the Pac-Man sprites.
	 */
	private final PacManSprites sprites;

	/**
	 * Creates a new player factory.
	 * 
	 * @param spriteStore
	 *            The sprite store containing the Pac-Man sprites.
	 */
	public PlayerFactory(PacManSprites spriteStore) {
		this.sprites = spriteStore;
	}

	/**
	 * Creates a new player with the classic Pac-Man sprites.
	 * 
	 * @return A new player.
	 */
	public Player createPacMan() {Api.hit("/src/main/java/nl/tudelft/jpacman/level/PlayerFactory.java", "createPacMan"); return new Player(getSprites().getPacmanSprites(),
				getSprites().getPacManDeathAnimation());}

	/**
	 * The sprites created by the factory.
	 * 
	 * @return The sprites for the player created.
	 */
	protected PacManSprites getSprites() {Api.hit("/src/main/java/nl/tudelft/jpacman/level/PlayerFactory.java", "getSprites"); return sprites;}
}
