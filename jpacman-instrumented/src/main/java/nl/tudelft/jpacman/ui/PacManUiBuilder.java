package nl.tudelft.jpacman.ui; import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import nl.tudelft.jpacman.game.Game;
import nl.tudelft.jpacman.ui.ScorePanel.ScoreFormatter; import nl.tudelft.jpacman.Api; public class PacManUiBuilder {

	/**
	 * Caption for the default stop button.
	 */
	private static final String STOP_CAPTION = "Stop";

	/**
	 * Caption for the default start button.
	 */
	private static final String START_CAPTION = "Start";

	/**
	 * Map of buttons and their actions.
	 */
	private final Map<String, Action> buttons;

	/**
	 * Map of key events and their actions.
	 */
	private final Map<Integer, Action> keyMappings;

	/**
	 * <code>true</code> iff this UI has the default buttons.
	 */
	private boolean defaultButtons;
	
	/**
	 * Way to format the score.
	 */
	private ScoreFormatter scoreFormatter = null;

	/**
	 * Creates a new Pac-Man UI builder without any mapped keys or buttons.
	 */
	public PacManUiBuilder() {
		this.defaultButtons = false;
		this.buttons = new LinkedHashMap<>();
		this.keyMappings = new HashMap<>();
	}

	/**
	 * Creates a new Pac-Man UI with the set keys and buttons.
	 * 
	 * @param game
	 *            The game to build the UI for.
	 * @return A new Pac-Man UI with the set keys and buttons.
	 */
	public PacManUI build(final Game game) {Api.hit("/src/main/java/nl/tudelft/jpacman/ui/PacManUiBuilder.java", "build", "lines 64:2 - 64:22"); assert game != null; Api.hit("/src/main/java/nl/tudelft/jpacman/ui/PacManUiBuilder.java", "build", "lines 66:2 - 69:3"); if (defaultButtons) {
			addStartButton(game);
			addStopButton(game);
		} Api.hit("/src/main/java/nl/tudelft/jpacman/ui/PacManUiBuilder.java", "build", "lines 70:2 - 70:66"); return new PacManUI(game, buttons, keyMappings, scoreFormatter);}

	/**
	 * Adds a button with the caption {@value #STOP_CAPTION} that stops the
	 * game.
	 * 
	 * @param game
	 *            The game to stop.
	 */
	private void addStopButton(final Game game) {Api.hit("/src/main/java/nl/tudelft/jpacman/ui/PacManUiBuilder.java", "doAction", "lines 81:2 - 81:22"); assert game != null; Api.hit("/src/main/java/nl/tudelft/jpacman/ui/PacManUiBuilder.java", "doAction", "lines 83:2 - 88:5"); buttons.put(STOP_CAPTION, new Action() {
			@Override
			public void doAction() {Api.hit("/src/main/java/nl/tudelft/jpacman/ui/PacManUiBuilder.java", "doAction", "lines 86:4 - 86:16"); game.stop();}
		});}

	/**
	 * Adds a button with the caption {@value #START_CAPTION} that starts the
	 * game.
	 * 
	 * @param game
	 *            The game to start.
	 */
	private void addStartButton(final Game game) {Api.hit("/src/main/java/nl/tudelft/jpacman/ui/PacManUiBuilder.java", "doAction", "lines 99:2 - 99:22"); assert game != null; Api.hit("/src/main/java/nl/tudelft/jpacman/ui/PacManUiBuilder.java", "doAction", "lines 101:2 - 106:5"); buttons.put(START_CAPTION, new Action() {
			@Override
			public void doAction() {Api.hit("/src/main/java/nl/tudelft/jpacman/ui/PacManUiBuilder.java", "doAction", "lines 104:4 - 104:17"); game.start();}
		});}

	/**
	 * Adds a key listener to the UI.
	 * 
	 * @param keyCode
	 *            The key code of the key as used by {@link java.awt.event.KeyEvent}.
	 * @param action
	 *            The action to perform when the key is pressed.
	 * @return The builder.
	 */
	public PacManUiBuilder addKey(Integer keyCode, Action action) {Api.hit("/src/main/java/nl/tudelft/jpacman/ui/PacManUiBuilder.java", "addKey", "lines 119:2 - 119:25"); assert keyCode != null; Api.hit("/src/main/java/nl/tudelft/jpacman/ui/PacManUiBuilder.java", "addKey", "lines 120:2 - 120:24"); assert action != null; Api.hit("/src/main/java/nl/tudelft/jpacman/ui/PacManUiBuilder.java", "addKey", "lines 122:2 - 122:35"); keyMappings.put(keyCode, action); Api.hit("/src/main/java/nl/tudelft/jpacman/ui/PacManUiBuilder.java", "addKey", "lines 123:2 - 123:14"); return this;}

	/**
	 * Adds a button to the UI.
	 * 
	 * @param caption
	 *            The caption of the button.
	 * @param action
	 *            The action to execute when the button is clicked.
	 * @return The builder.
	 */
	public PacManUiBuilder addButton(String caption, Action action) {Api.hit("/src/main/java/nl/tudelft/jpacman/ui/PacManUiBuilder.java", "addButton", "lines 136:2 - 136:25"); assert caption != null; Api.hit("/src/main/java/nl/tudelft/jpacman/ui/PacManUiBuilder.java", "addButton", "lines 137:2 - 137:28"); assert !caption.isEmpty(); Api.hit("/src/main/java/nl/tudelft/jpacman/ui/PacManUiBuilder.java", "addButton", "lines 138:2 - 138:24"); assert action != null; Api.hit("/src/main/java/nl/tudelft/jpacman/ui/PacManUiBuilder.java", "addButton", "lines 140:2 - 140:31"); buttons.put(caption, action); Api.hit("/src/main/java/nl/tudelft/jpacman/ui/PacManUiBuilder.java", "addButton", "lines 141:2 - 141:14"); return this;}

	/**
	 * Adds a start and stop button to the UI. The actual actions for these
	 * buttons will be added upon building the UI.
	 * 
	 * @return The builder.
	 */
	public PacManUiBuilder withDefaultButtons() {Api.hit("/src/main/java/nl/tudelft/jpacman/ui/PacManUiBuilder.java", "withDefaultButtons", "lines 151:2 - 151:24"); defaultButtons = true; Api.hit("/src/main/java/nl/tudelft/jpacman/ui/PacManUiBuilder.java", "withDefaultButtons", "lines 152:2 - 152:35"); buttons.put(START_CAPTION, null); Api.hit("/src/main/java/nl/tudelft/jpacman/ui/PacManUiBuilder.java", "withDefaultButtons", "lines 153:2 - 153:34"); buttons.put(STOP_CAPTION, null); Api.hit("/src/main/java/nl/tudelft/jpacman/ui/PacManUiBuilder.java", "withDefaultButtons", "lines 154:2 - 154:14"); return this;}
	
	/**
	 * Provide formatter for the score.
	 * 
	 * @param sf
	 *         The score formatter to be used.
	 * 
	 * @return The builder.
	 */
	public PacManUiBuilder withScoreFormatter(ScoreFormatter sf) {Api.hit("/src/main/java/nl/tudelft/jpacman/ui/PacManUiBuilder.java", "withScoreFormatter", "lines 166:2 - 166:22"); scoreFormatter = sf; Api.hit("/src/main/java/nl/tudelft/jpacman/ui/PacManUiBuilder.java", "withScoreFormatter", "lines 167:2 - 167:14"); return this;}
}
