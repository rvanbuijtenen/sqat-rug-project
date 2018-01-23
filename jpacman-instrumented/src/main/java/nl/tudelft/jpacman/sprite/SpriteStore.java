package nl.tudelft.jpacman.sprite; import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO; import nl.tudelft.jpacman.Api; public class SpriteStore {
	
	/**
	 * We only need to load images once, so we keep track
	 * of them in a hash map.
	 */
	private final Map<String, Sprite> spriteMap;
	
	/**
	 * Create a new sprite store.
	 */
	public SpriteStore() {
		spriteMap = new HashMap<>();
	}
	
	/**
	 * Loads a sprite from a resource on the class path.
	 * Sprites are loaded once, and then stored in the store
	 * so that they can be efficiently retrieved.
	 * 
	 * @param resource
	 *            The resource path.
	 * @return The sprite for the resource.
	 * @throws IOException
	 *             When the resource could not be loaded.
	 */
	public Sprite loadSprite(String resource) throws IOException {Api.hit("/src/main/java/nl/tudelft/jpacman/sprite/SpriteStore.java", "loadSprite", "lines 43:2 - 43:42"); Sprite result = spriteMap.get(resource); Api.hit("/src/main/java/nl/tudelft/jpacman/sprite/SpriteStore.java", "loadSprite", "lines 44:2 - 47:3"); if (result == null) {
			result = loadSpriteFromResource(resource);
			spriteMap.put(resource, result);
		} Api.hit("/src/main/java/nl/tudelft/jpacman/sprite/SpriteStore.java", "loadSprite", "lines 48:2 - 48:16"); return result;}

	/**
	 * Loads a sprite from a resource on the class path.
	 * 
	 * @param resource
	 *            The resource path.
	 * @return A new sprite for the resource.
	 * @throws IOException
	 *             When the resource could not be loaded.
	 */
	private Sprite loadSpriteFromResource(String resource) throws IOException {Api.hit("/src/main/java/nl/tudelft/jpacman/sprite/SpriteStore.java", "loadSpriteFromResource", "lines 61:2 - 68:3"); try (InputStream input = SpriteStore.class.getResourceAsStream(resource)) {
			if (input == null) {
				throw new IOException("Unable to load " + resource
					+ ", resource does not exist.");
			}
			BufferedImage image = ImageIO.read(input);
			return new ImageSprite(image);
		}}

	/**
	 * Creates a new {@link AnimatedSprite} from a base image.
	 * 
	 * @param baseImage
	 *            The base image to convert into an animation.
	 * @param frames
	 *            The amount of frames of the animation.
	 * @param delay
	 *            The delay between frames.
	 * @param loop
	 *            Whether this sprite is a looping animation or not.
	 * @return The animated sprite.
	 */
	public AnimatedSprite createAnimatedSprite(Sprite baseImage, int frames,
			int delay, boolean loop) {Api.hit("/src/main/java/nl/tudelft/jpacman/sprite/SpriteStore.java", "createAnimatedSprite", "lines 86:2 - 86:27"); assert baseImage != null; Api.hit("/src/main/java/nl/tudelft/jpacman/sprite/SpriteStore.java", "createAnimatedSprite", "lines 87:2 - 87:20"); assert frames > 0; Api.hit("/src/main/java/nl/tudelft/jpacman/sprite/SpriteStore.java", "createAnimatedSprite", "lines 89:2 - 89:49"); int frameWidth = baseImage.getWidth() / frames; Api.hit("/src/main/java/nl/tudelft/jpacman/sprite/SpriteStore.java", "createAnimatedSprite", "lines 91:2 - 91:42"); Sprite[] animation = new Sprite[frames]; Api.hit("/src/main/java/nl/tudelft/jpacman/sprite/SpriteStore.java", "createAnimatedSprite", "lines 92:2 - 95:3"); for (int i = 0; i < frames; i++) {
			animation[i] = baseImage.split(i * frameWidth, 0, frameWidth,
					baseImage.getHeight());
		} Api.hit("/src/main/java/nl/tudelft/jpacman/sprite/SpriteStore.java", "createAnimatedSprite", "lines 97:2 - 97:52"); return new AnimatedSprite(animation, delay, loop);}

}
