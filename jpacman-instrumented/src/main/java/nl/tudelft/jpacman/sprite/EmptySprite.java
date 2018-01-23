package nl.tudelft.jpacman.sprite; import java.awt.Graphics; import nl.tudelft.jpacman.Api; public class EmptySprite implements Sprite {
	public EmptySprite() {
		super();
	}
	@Override
	public void draw(Graphics g,
					 int x,
					 int y,
					 int width,
					 int height) {
		// nothing to draw.
	}

	@Override
	public Sprite split(int x, int y, int width, int height) {Api.hit("/src/main/java/nl/tudelft/jpacman/sprite/EmptySprite.java", "split", "lines 26:2 - 26:27"); return new EmptySprite();}

	@Override
	public int getWidth() {Api.hit("/src/main/java/nl/tudelft/jpacman/sprite/EmptySprite.java", "getWidth", "lines 31:2 - 31:11"); return 0;}

	@Override
	public int getHeight() {Api.hit("/src/main/java/nl/tudelft/jpacman/sprite/EmptySprite.java", "getHeight", "lines 36:2 - 36:11"); return 0;}

}
