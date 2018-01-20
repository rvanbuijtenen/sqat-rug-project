package nl.tudelft.jpacman.sprite; import java.awt.Graphics; import nl.tudelft.jpacman.Api; public class EmptySprite implements Sprite {
	public EmptySprite() {
		super();
	}
	@Override
	public void draw(Graphics g,
					 int x,
					 int y,
					 int width,
					 int height) {Api.hit("/src/main/java/nl/tudelft/jpacman/sprite/EmptySprite.java", "draw");}

	@Override
	public Sprite split(int x, int y, int width, int height) {Api.hit("/src/main/java/nl/tudelft/jpacman/sprite/EmptySprite.java", "split"); return new EmptySprite();}

	@Override
	public int getWidth() {Api.hit("/src/main/java/nl/tudelft/jpacman/sprite/EmptySprite.java", "getWidth"); return 0;}

	@Override
	public int getHeight() {Api.hit("/src/main/java/nl/tudelft/jpacman/sprite/EmptySprite.java", "getHeight"); return 0;}

}
