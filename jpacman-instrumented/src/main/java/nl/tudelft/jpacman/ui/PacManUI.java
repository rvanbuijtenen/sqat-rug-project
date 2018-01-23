package nl.tudelft.jpacman.ui; import java.awt.BorderLayout;
import java.awt.Container;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JPanel;

import nl.tudelft.jpacman.game.Game;
import nl.tudelft.jpacman.ui.ScorePanel.ScoreFormatter; import nl.tudelft.jpacman.Api; public class PacManUI extends JFrame {

	/**
	 * Default serialisation UID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The desired frame rate interval for the graphics in milliseconds, 40
	 * being 25 fps.
	 */
	private static final int FRAME_INTERVAL = 40;

	/**
	 * The panel displaying the player scores.
	 */
	private final ScorePanel scorePanel;

	/**
	 * The panel displaying the game.
	 */
	private final BoardPanel boardPanel;

	/**
	 * Creates a new UI for a JPac-Man game.
	 * 
	 * @param game
	 *            The game to play.
	 * @param buttons
	 *            The map of caption-to-action entries that will appear as
	 *            buttons on the interface.
	 * @param keyMappings
	 *            The map of keyCode-to-action entries that will be added as key
	 *            listeners to the interface.
	 * @param sf
	 *            The formatter used to display the current score. 
	 */
	public PacManUI(final Game game, final Map<String, Action> buttons,
			final Map<Integer, Action> keyMappings, ScoreFormatter sf) {
		super("JPac-Man");
		assert game != null;
		assert buttons != null;
		assert keyMappings != null;
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		PacKeyListener keys = new PacKeyListener(keyMappings);
		addKeyListener(keys);

		JPanel buttonPanel = new ButtonPanel(buttons, this);

		scorePanel = new ScorePanel(game.getPlayers());
		if (sf != null) {
			scorePanel.setScoreFormatter(sf);
		}
		
		boardPanel = new BoardPanel(game);
		
		Container contentPanel = getContentPane();
		contentPanel.setLayout(new BorderLayout());
		contentPanel.add(buttonPanel, BorderLayout.SOUTH);
		contentPanel.add(scorePanel, BorderLayout.NORTH);
		contentPanel.add(boardPanel, BorderLayout.CENTER);

		pack();
	}

	/**
	 * Starts the "engine", the thread that redraws the interface at set
	 * intervals.
	 */
	public void start() {Api.hit("/src/main/java/nl/tudelft/jpacman/ui/PacManUI.java", "run", "lines 102:2 - 102:19"); setVisible(true); Api.hit("/src/main/java/nl/tudelft/jpacman/ui/PacManUI.java", "run", "lines 104:2 - 105:40"); ScheduledExecutorService service = Executors
				.newSingleThreadScheduledExecutor(); Api.hit("/src/main/java/nl/tudelft/jpacman/ui/PacManUI.java", "run", "lines 107:2 - 113:47"); service.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {Api.hit("/src/main/java/nl/tudelft/jpacman/ui/PacManUI.java", "run", "lines 111:4 - 111:16"); nextFrame();}
		}, 0, FRAME_INTERVAL, TimeUnit.MILLISECONDS);}

	/**
	 * Draws the next frame, i.e. refreshes the scores and game.
	 */
	private void nextFrame() {Api.hit("/src/main/java/nl/tudelft/jpacman/ui/PacManUI.java", "nextFrame", "lines 121:2 - 121:23"); boardPanel.repaint(); Api.hit("/src/main/java/nl/tudelft/jpacman/ui/PacManUI.java", "nextFrame", "lines 122:2 - 122:23"); scorePanel.refresh();}
}
