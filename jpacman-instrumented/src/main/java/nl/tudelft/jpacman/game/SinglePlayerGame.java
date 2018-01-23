package nl.tudelft.jpacman.game; import java.util.List;

import nl.tudelft.jpacman.level.Level;
import nl.tudelft.jpacman.level.Player;

import com.google.common.collect.ImmutableList; import nl.tudelft.jpacman.Api; public class SinglePlayerGame extends Game {

	/**
	 * The player of this game.
	 */
	private final Player player;

	/**
	 * The level of this game.
	 */
	private final Level level;

	/**
	 * Create a new single player game for the provided level and player.
	 * 
	 * @param p
	 *            The player.
	 * @param l
	 *            The level.
	 */
	protected SinglePlayerGame(Player p, Level l) {
		assert p != null;
		assert l != null;

		this.player = p;
		this.level = l;
		level.registerPlayer(p);
	}

	@Override
	public List<Player> getPlayers() {Api.hit("/src/main/java/nl/tudelft/jpacman/game/SinglePlayerGame.java", "getPlayers", "lines 46:2 - 46:34"); return ImmutableList.of(player);}

	@Override
	public Level getLevel() {Api.hit("/src/main/java/nl/tudelft/jpacman/game/SinglePlayerGame.java", "getLevel", "lines 51:2 - 51:15"); return level;}
}
