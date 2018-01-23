package nl.tudelft.jpacman; import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import nl.tudelft.jpacman.board.BoardFactory;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.game.Game;
import nl.tudelft.jpacman.game.GameFactory;
import nl.tudelft.jpacman.level.Level;
import nl.tudelft.jpacman.level.LevelFactory;
import nl.tudelft.jpacman.level.MapParser;
import nl.tudelft.jpacman.level.Player;
import nl.tudelft.jpacman.level.PlayerFactory;
import nl.tudelft.jpacman.npc.ghost.GhostFactory;
import nl.tudelft.jpacman.sprite.PacManSprites;
import nl.tudelft.jpacman.ui.Action;
import nl.tudelft.jpacman.ui.PacManUI;
import nl.tudelft.jpacman.ui.PacManUiBuilder; import nl.tudelft.jpacman.Api; @SuppressWarnings("PMD.TooManyMethods")
public class Launcher {

	private static final PacManSprites SPRITE_STORE = new PacManSprites();
	
	public static final String DEFAULT_MAP = "/board.txt";
	private String levelMap = DEFAULT_MAP;

	private PacManUI pacManUI;
	private Game game;

	/**
	 * @return The game object this launcher will start when {@link #launch()}
	 *         is called.
	 */
	public Game getGame() {Api.hit("/src/main/java/nl/tudelft/jpacman/Launcher.java", "getGame", "lines 43:2 - 43:14"); return game;}
	
	/**
	 * The map file used to populate the level.
	 * @return The name of the map file.
	 */
	protected String getLevelMap() {Api.hit("/src/main/java/nl/tudelft/jpacman/Launcher.java", "getLevelMap", "lines 51:2 - 51:18"); return levelMap;}
	
	/**
	 * Set the name of the file containing this level's map.
	 * @param fileName Map to be used.
	 * @return Level corresponding to the given map.
	 */
	public Launcher withMapFile(String fileName) {Api.hit("/src/main/java/nl/tudelft/jpacman/Launcher.java", "withMapFile", "lines 60:2 - 60:22"); levelMap = fileName; Api.hit("/src/main/java/nl/tudelft/jpacman/Launcher.java", "withMapFile", "lines 61:2 - 61:14"); return this;}

	/**
	 * Creates a new game using the level from {@link #makeLevel()}.
	 * 
	 * @return a new Game.
	 */
	public Game makeGame() {Api.hit("/src/main/java/nl/tudelft/jpacman/Launcher.java", "makeGame", "lines 70:2 - 70:36"); GameFactory gf = getGameFactory(); Api.hit("/src/main/java/nl/tudelft/jpacman/Launcher.java", "makeGame", "lines 71:2 - 71:28"); Level level = makeLevel(); Api.hit("/src/main/java/nl/tudelft/jpacman/Launcher.java", "makeGame", "lines 72:2 - 72:42"); return gf.createSinglePlayerGame(level);}

	/**
	 * Creates a new level. By default this method will use the map parser to
	 * parse the default board stored in the <code>board.txt</code> resource.
	 * 
	 * @return A new level.
	 */
	public Level makeLevel() {Api.hit("/src/main/java/nl/tudelft/jpacman/Launcher.java", "makeLevel", "lines 82:2 - 82:36"); MapParser parser = getMapParser(); Api.hit("/src/main/java/nl/tudelft/jpacman/Launcher.java", "makeLevel", "lines 83:2 - 88:3"); try (InputStream boardStream = Launcher.class
				.getResourceAsStream(getLevelMap())) {
			return parser.parseMap(boardStream);
		} catch (IOException e) {
			throw new PacmanConfigurationException("Unable to create level.", e);
		}}
	

	/**
	 * @return A new map parser object using the factories from
	 *         {@link #getLevelFactory()} and {@link #getBoardFactory()}.
	 */
	protected MapParser getMapParser() {Api.hit("/src/main/java/nl/tudelft/jpacman/Launcher.java", "getMapParser", "lines 97:2 - 97:61"); return new MapParser(getLevelFactory(), getBoardFactory());}

	/**
	 * @return A new board factory using the sprite store from
	 *         {@link #getSpriteStore()}.
	 */
	protected BoardFactory getBoardFactory() {Api.hit("/src/main/java/nl/tudelft/jpacman/Launcher.java", "getBoardFactory", "lines 105:2 - 105:44"); return new BoardFactory(getSpriteStore());}

	/**
	 * @return The default {@link PacManSprites}.
	 */
	protected PacManSprites getSpriteStore() {Api.hit("/src/main/java/nl/tudelft/jpacman/Launcher.java", "getSpriteStore", "lines 112:2 - 112:22"); return SPRITE_STORE;}

	/**
	 * @return A new factory using the sprites from {@link #getSpriteStore()}
	 *         and the ghosts from {@link #getGhostFactory()}.
	 */
	protected LevelFactory getLevelFactory() {Api.hit("/src/main/java/nl/tudelft/jpacman/Launcher.java", "getLevelFactory", "lines 120:2 - 120:63"); return new LevelFactory(getSpriteStore(), getGhostFactory());}

	/**
	 * @return A new factory using the sprites from {@link #getSpriteStore()}.
	 */
	protected GhostFactory getGhostFactory() {Api.hit("/src/main/java/nl/tudelft/jpacman/Launcher.java", "getGhostFactory", "lines 127:2 - 127:44"); return new GhostFactory(getSpriteStore());}

	/**
	 * @return A new factory using the players from {@link #getPlayerFactory()}.
	 */
	protected GameFactory getGameFactory() {Api.hit("/src/main/java/nl/tudelft/jpacman/Launcher.java", "getGameFactory", "lines 134:2 - 134:45"); return new GameFactory(getPlayerFactory());}

	/**
	 * @return A new factory using the sprites from {@link #getSpriteStore()}.
	 */
	protected PlayerFactory getPlayerFactory() {Api.hit("/src/main/java/nl/tudelft/jpacman/Launcher.java", "getPlayerFactory", "lines 141:2 - 141:45"); return new PlayerFactory(getSpriteStore());}

	/**
	 * Adds key events UP, DOWN, LEFT and RIGHT to a game.
	 * 
	 * @param builder
	 *            The {@link PacManUiBuilder} that will provide the UI.
	 * @param game
	 *            The game that will process the events.
	 */
	protected void addSinglePlayerKeys(final PacManUiBuilder builder,
			final Game game) {Api.hit("/src/main/java/nl/tudelft/jpacman/Launcher.java", "doAction", "lines 154:2 - 154:42"); final Player p1 = getSinglePlayer(game); Api.hit("/src/main/java/nl/tudelft/jpacman/Launcher.java", "doAction", "lines 156:2 - 180:5"); builder.addKey(KeyEvent.VK_UP, new Action() {

			@Override
			public void doAction() {Api.hit("/src/main/java/nl/tudelft/jpacman/Launcher.java", "doAction", "lines 160:4 - 160:35"); game.move(p1, Direction.NORTH);}
		}).addKey(KeyEvent.VK_DOWN, new Action() {

			@Override
			public void doAction() {Api.hit("/src/main/java/nl/tudelft/jpacman/Launcher.java", "doAction", "lines 166:4 - 166:35"); game.move(p1, Direction.SOUTH);}
		}).addKey(KeyEvent.VK_LEFT, new Action() {

			@Override
			public void doAction() {Api.hit("/src/main/java/nl/tudelft/jpacman/Launcher.java", "doAction", "lines 172:4 - 172:34"); game.move(p1, Direction.WEST);}
		}).addKey(KeyEvent.VK_RIGHT, new Action() {

			@Override
			public void doAction() {Api.hit("/src/main/java/nl/tudelft/jpacman/Launcher.java", "doAction", "lines 178:4 - 178:34"); game.move(p1, Direction.EAST);}
		});}

	private Player getSinglePlayer(final Game game) {Api.hit("/src/main/java/nl/tudelft/jpacman/Launcher.java", "getSinglePlayer", "lines 185:2 - 185:43"); List<Player> players = game.getPlayers(); Api.hit("/src/main/java/nl/tudelft/jpacman/Launcher.java", "getSinglePlayer", "lines 186:2 - 188:3"); if (players.isEmpty()) {
			throw new IllegalArgumentException("Game has 0 players.");
		} Api.hit("/src/main/java/nl/tudelft/jpacman/Launcher.java", "getSinglePlayer", "lines 189:2 - 189:24"); return players.get(0);}

	/**
	 * Creates and starts a JPac-Man game.
	 */
	public void launch() {Api.hit("/src/main/java/nl/tudelft/jpacman/Launcher.java", "launch", "lines 196:2 - 196:20"); game = makeGame(); Api.hit("/src/main/java/nl/tudelft/jpacman/Launcher.java", "launch", "lines 197:2 - 197:71"); PacManUiBuilder builder = new PacManUiBuilder().withDefaultButtons(); Api.hit("/src/main/java/nl/tudelft/jpacman/Launcher.java", "launch", "lines 198:2 - 198:37"); addSinglePlayerKeys(builder, game); Api.hit("/src/main/java/nl/tudelft/jpacman/Launcher.java", "launch", "lines 199:2 - 199:33"); pacManUI = builder.build(game); Api.hit("/src/main/java/nl/tudelft/jpacman/Launcher.java", "launch", "lines 200:2 - 200:19"); pacManUI.start();}

	/**
	 * Disposes of the UI. For more information see {@link javax.swing.JFrame#dispose()}.
	 */
	public void dispose() {Api.hit("/src/main/java/nl/tudelft/jpacman/Launcher.java", "dispose", "lines 207:2 - 207:21"); pacManUI.dispose();}

	/**
	 * Main execution method for the Launcher.
	 * 
	 * @param args
	 *            The command line arguments - which are ignored.
	 * @throws IOException
	 *             When a resource could not be read.
	 */
	public static void main(String[] args) throws IOException {Api.hit("/src/main/java/nl/tudelft/jpacman/Launcher.java", "main", "lines 219:2 - 219:26"); new Launcher().launch();}
}
