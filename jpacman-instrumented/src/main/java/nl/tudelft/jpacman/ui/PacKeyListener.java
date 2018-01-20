package nl.tudelft.jpacman.ui; import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Map; import nl.tudelft.jpacman.Api; class PacKeyListener implements KeyListener {

	/**
	 * The mappings of keyCode to action.
	 */
	private final Map<Integer, Action> mappings;
	
	/**
	 * Create a new key listener based on a set of keyCode-action pairs.
	 * @param keyMappings The mappings of keyCode to action.
	 */
	PacKeyListener(Map<Integer, Action> keyMappings) {
		assert keyMappings != null;
		this.mappings = keyMappings;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {Api.hit("/src/main/java/nl/tudelft/jpacman/ui/PacKeyListener.java", "keyPressed"); assert e != null; Action action = mappings.get(e.getKeyCode());
		if (action != null) {
			action.doAction();
		}}

	@Override
	public void keyTyped(KeyEvent e) {Api.hit("/src/main/java/nl/tudelft/jpacman/ui/PacKeyListener.java", "keyTyped");}
	
	@Override
	public void keyReleased(KeyEvent e) {Api.hit("/src/main/java/nl/tudelft/jpacman/ui/PacKeyListener.java", "keyReleased");}
}