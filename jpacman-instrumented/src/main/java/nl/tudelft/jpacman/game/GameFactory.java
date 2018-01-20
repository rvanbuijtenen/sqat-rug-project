package nl.tudelft.jpacman.game; import nl.tudelft.jpacman.level.Level;
import nl.tudelft.jpacman.level.PlayerFactory; import nl.tudelft.jpacman.Api; public class GameFactory {

	/**
	 * The factory providing the player objects.
	 */
	private final PlayerFactory playerFact;

	/**
	 * Creates a new game factory.
	 * 
	 * @param playerFactory
	 *            The factory providing the player objects.
	 */
	public GameFactory(PlayerFactory playerFactory) {
		this.playerFact = playerFactory;
	}

	/**
	 * Creates a game for a single level with one player.
	 * 
	 * @param level
	 *            The level to create a game for.
	 * @return A new single player game.
	 */
	public Game createSinglePlayerGame(Level level) {Api.hit("/src/main/java/nl/tudelft/jpacman/game/GameFactory.java", "createSinglePlayerGame"); return new SinglePlayerGame(playerFact.createPacMan(), level);}

	/**
	 * Returns the player factory associated with this game factory.
	 * @return the player factory associated with this game factory.
	 */
	protected PlayerFactory getPlayerFactory() {Api.hit("/src/main/java/nl/tudelft/jpacman/game/GameFactory.java", "getPlayerFactory"); return playerFact;}
}
