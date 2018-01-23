package nl.tudelft.jpacman.sprite; import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage; import nl.tudelft.jpacman.Api; public class ImageSprite implements Sprite {

	/**
	 * Internal image.
	 */
	private final Image image;

	/**
	 * Creates a new sprite from an image.
	 * 
	 * @param img
	 *            The image to create a sprite from.
	 */
	public ImageSprite(Image img) {
		this.image = img;
	}

	@Override
	public void draw(Graphics g, int x, int y, int width, int height) {Api.hit("/src/main/java/nl/tudelft/jpacman/sprite/ImageSprite.java", "draw", "lines 34:2 - 35:55"); g.drawImage(image, x, y, x + width, y + height, 0, 0,
				image.getWidth(null), image.getHeight(null), null);}

	@Override
	public Sprite split(int x, int y, int width, int height) {Api.hit("/src/main/java/nl/tudelft/jpacman/sprite/ImageSprite.java", "split", "lines 40:2 - 45:3"); if (withinImage(x, y) && withinImage(x + width - 1, y + height - 1)) {
			BufferedImage newImage = newImage(width, height);
			newImage.createGraphics().drawImage(image, 0, 0, width, height, x,
					y, x + width, y + height, null);
			return new ImageSprite(newImage);
		} Api.hit("/src/main/java/nl/tudelft/jpacman/sprite/ImageSprite.java", "split", "lines 46:2 - 46:27"); return new EmptySprite();}

	private boolean withinImage(int x, int y) {Api.hit("/src/main/java/nl/tudelft/jpacman/sprite/ImageSprite.java", "withinImage", "lines 50:2 - 51:14"); return x < image.getWidth(null) && x >= 0 && y < image.getHeight(null)
				&& y >= 0;}

	/**
	 * Creates a new, empty image of the given width and height. Its
	 * transparency will be a bitmask, so no try ARGB image.
	 * 
	 * @param width
	 *            The width of the new image.
	 * @param height
	 *            The height of the new image.
	 * @return The new, empty image.
	 */
	private BufferedImage newImage(int width, int height) {Api.hit("/src/main/java/nl/tudelft/jpacman/sprite/ImageSprite.java", "newImage", "lines 65:2 - 67:31"); GraphicsConfiguration gc = GraphicsEnvironment
				.getLocalGraphicsEnvironment().getDefaultScreenDevice()
				.getDefaultConfiguration(); Api.hit("/src/main/java/nl/tudelft/jpacman/sprite/ImageSprite.java", "newImage", "lines 68:2 - 68:71"); return gc.createCompatibleImage(width, height, Transparency.BITMASK);}

	@Override
	public int getWidth() {Api.hit("/src/main/java/nl/tudelft/jpacman/sprite/ImageSprite.java", "getWidth", "lines 73:2 - 73:30"); return image.getWidth(null);}

	@Override
	public int getHeight() {Api.hit("/src/main/java/nl/tudelft/jpacman/sprite/ImageSprite.java", "getHeight", "lines 78:2 - 78:31"); return image.getHeight(null);}

}
