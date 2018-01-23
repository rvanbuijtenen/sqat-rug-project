package nl.tudelft.jpacman.game; import java.util.List;

import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.level.Level;
import nl.tudelft.jpacman.level.Level.LevelObserver;
import nl.tudelft.jpacman.level.Player; import nl.tudelft.jpacman.Api; public abstract class Game implements LevelObserver {

	/**
	 * <code>true</code> if the game is in progress.
	 */
	private boolean inProgress;

	/**
	 * Object that locks the start and stop methods.
	 */
	private final Object progressLock = new Object();

	/**
	 * Creates a new game.
	 */
	public Game() {
		inProgress = false;
	}

	/**
	 * Starts or resumes the game.
	 */
	public void start() {Api.hit("/src/main/java/nl/tudelft/jpacman/game/Game.java", "start", "lines 38:2 - 48:3"); synchronized (progressLock) {
			if (isInProgress()) {
				return;
			}
			if (getLevel().isAnyPlayerAlive()
					&& getLevel().remainingPellets() > 0) {
				inProgress = true;
				getLevel().addObserver(this);
				getLevel().start();
			}
		}}

	/**
	 * Pauses the game.
	 */
	public void stop() {Api.hit("/src/main/java/nl/tudelft/jpacman/game/Game.java", "stop", "lines 55:2 - 61:3"); synchronized (progressLock) {
			if (!isInProgress()) {
				return;
			}
			inProgress = false;
			getLevel().stop();
		}}

	/**
	 * @return <code>true</code> iff the game is started and in progress.
	 */
	public boolean isInProgress() {Api.hit("/src/main/java/nl/tudelft/jpacman/game/Game.java", "isInProgress", "lines 68:2 - 68:20"); return inProgress;}

	/**
	 * @return An immutable list of the participants of this game.
	 */
	public abstract List<Player> getPlayers();

	/**
	 * @return The level currently being played.
	 */
	public abstract Level getLevel();

	/**
	 * Moves the specified player one square in the given direction.
	 * 
	 * @param player
	 *            The player to move.
	 * @param direction
	 *            The direction to move in.
	 */
	public void move(Player player, Direction direction) {Api.hit("/src/main/java/nl/tudelft/jpacman/game/Game.java", "move", "lines 90:2 - 93:3"); if (isInProgress()) {
			// execute player move.
			getLevel().move(player, direction);
		}}
	
	@Override
	public void levelWon() {Api.hit("/src/main/java/nl/tudelft/jpacman/game/Game.java", "levelWon", "lines 98:2 - 98:9"); stop();}
	
	@Override
	public void levelLost() {Api.hit("/src/main/java/nl/tudelft/jpacman/game/Game.java", "levelLost", "lines 103:2 - 103:9"); stop();}
}
