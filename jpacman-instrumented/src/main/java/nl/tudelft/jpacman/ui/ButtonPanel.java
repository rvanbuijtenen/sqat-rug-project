package nl.tudelft.jpacman.ui; import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel; import nl.tudelft.jpacman.Api; class ButtonPanel extends JPanel {
	
	/**
	 * Default serialisation ID.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Create a new button panel with a button for every action.
	 * @param buttons The map of caption - action for each button.
	 * @param parent The parent frame, used to return window focus.
	 */
	ButtonPanel(final Map<String, Action> buttons, final JFrame parent) {
		super();
		assert buttons != null;
		assert parent != null;
		
		for (final String caption : buttons.keySet()) {
			JButton button = new JButton(caption);
			button.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {Api.hit("/src/main/java/nl/tudelft/jpacman/ui/ButtonPanel.java", "actionPerformed", "lines 39:5 - 39:37"); buttons.get(caption).doAction(); Api.hit("/src/main/java/nl/tudelft/jpacman/ui/ButtonPanel.java", "actionPerformed", "lines 40:5 - 40:35"); parent.requestFocusInWindow();}
			});
			add(button);
		}
	}
}
