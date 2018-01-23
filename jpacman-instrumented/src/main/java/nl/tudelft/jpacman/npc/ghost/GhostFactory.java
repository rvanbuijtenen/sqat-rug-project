package nl.tudelft.jpacman.npc.ghost; import nl.tudelft.jpacman.sprite.PacManSprites; import nl.tudelft.jpacman.Api; public class GhostFactory {

	/**
	 * The sprite store containing the ghost sprites.
	 */
	private final PacManSprites sprites;

	/**
	 * Creates a new ghost factory.
	 * 
	 * @param spriteStore The sprite provider.
	 */
	public GhostFactory(PacManSprites spriteStore) {
		this.sprites = spriteStore;
	}

	/**
	 * Creates a new Blinky / Shadow, the red Ghost.
	 * 
	 * @see Blinky
	 * @return A new Blinky.
	 */
	public Ghost createBlinky() {Api.hit("/src/main/java/nl/tudelft/jpacman/npc/ghost/GhostFactory.java", "createBlinky", "lines 33:2 - 33:60"); return new Blinky(sprites.getGhostSprite(GhostColor.RED));}

	/**
	 * Creates a new Pinky / Speedy, the pink Ghost.
	 * 
	 * @see Pinky
	 * @return A new Pinky.
	 */
	public Ghost createPinky() {Api.hit("/src/main/java/nl/tudelft/jpacman/npc/ghost/GhostFactory.java", "createPinky", "lines 43:2 - 43:60"); return new Pinky(sprites.getGhostSprite(GhostColor.PINK));}

	/**
	 * Creates a new Inky / Bashful, the cyan Ghost.
	 * 
	 * @see Inky
	 * @return A new Inky.
	 */
	public Ghost createInky() {Api.hit("/src/main/java/nl/tudelft/jpacman/npc/ghost/GhostFactory.java", "createInky", "lines 53:2 - 53:59"); return new Inky(sprites.getGhostSprite(GhostColor.CYAN));}

	/**
	 * Creates a new Clyde / Pokey, the orange Ghost.
	 * 
	 * @see Clyde
	 * @return A new Clyde.
	 */
	public Ghost createClyde() {Api.hit("/src/main/java/nl/tudelft/jpacman/npc/ghost/GhostFactory.java", "createClyde", "lines 63:2 - 63:62"); return new Clyde(sprites.getGhostSprite(GhostColor.ORANGE));}
}
