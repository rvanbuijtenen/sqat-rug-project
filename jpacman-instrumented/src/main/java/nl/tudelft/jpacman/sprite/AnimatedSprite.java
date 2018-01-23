package nl.tudelft.jpacman.sprite; import java.awt.Graphics; import nl.tudelft.jpacman.Api; public class AnimatedSprite implements Sprite {

	/**
	 * Static empty sprite to serve as the end of a non-looping sprite.
	 */
	private static final Sprite END_OF_LOOP = new EmptySprite();

	/**
	 * The animation itself, in frames.
	 */
	private final Sprite[] animationFrames;

	/**
	 * The delay between frames.
	 */
	private final int animationDelay;

	/**
	 * Whether is animation should be looping or not.
	 */
	private final boolean looping;

	/**
	 * The index of the current frame.
	 */
	private int current;

	/**
	 * Whether this sprite is currently animating or not.
	 */
	private boolean animating;

	/**
	 * The {@link System#currentTimeMillis()} stamp of the last update.
	 */
	private long lastUpdate;

	/**
	 * Creates a new animating sprite that will change frames every interval. By
	 * default the sprite is not animating.
	 * 
	 * @param frames
	 *            The frames of this animation.
	 * @param delay
	 *            The delay between frames.
	 * @param loop
	 *            Whether or not this sprite should be looping.
	 */
	public AnimatedSprite(Sprite[] frames, int delay, boolean loop) {
		this(frames, delay, loop, false);
	}

	/**
	 * Creates a new animating sprite that will change frames every interval.
	 * 
	 * @param frames
	 *            The frames of this animation.
	 * @param delay
	 *            The delay between frames.
	 * @param loop
	 *            Whether or not this sprite should be looping.
	 * @param isAnimating
	 *            Whether or not this sprite is animating from the start.
	 */
	public AnimatedSprite(Sprite[] frames, int delay, boolean loop,
			boolean isAnimating) {
		assert frames.length > 0;

		this.animationFrames = frames.clone();
		this.animationDelay = delay;
		this.looping = loop;
		this.animating = isAnimating;

		this.current = 0;
		this.lastUpdate = System.currentTimeMillis();
	}

	/**
	 * @return The frame of the current index.
	 */
	private Sprite currentSprite() {Api.hit("/src/main/java/nl/tudelft/jpacman/sprite/AnimatedSprite.java", "currentSprite", "lines 92:2 - 92:30"); Sprite result = END_OF_LOOP; Api.hit("/src/main/java/nl/tudelft/jpacman/sprite/AnimatedSprite.java", "currentSprite", "lines 93:2 - 95:3"); if (current < animationFrames.length) {
			result = animationFrames[current];
		} Api.hit("/src/main/java/nl/tudelft/jpacman/sprite/AnimatedSprite.java", "currentSprite", "lines 96:2 - 96:24"); assert result != null; Api.hit("/src/main/java/nl/tudelft/jpacman/sprite/AnimatedSprite.java", "currentSprite", "lines 97:2 - 97:16"); return result;}

	/**
	 * Starts or stops the animation of this sprite.
	 * 
	 * @param isAnimating
	 *            <code>true</code> to animate this sprite or <code>false</code>
	 *            to stop animating this sprite.
	 */
	public void setAnimating(boolean isAnimating) {Api.hit("/src/main/java/nl/tudelft/jpacman/sprite/AnimatedSprite.java", "setAnimating", "lines 108:2 - 108:31"); this.animating = isAnimating;}
	
	/**
	 * (Re)starts the current animation.
	 */
	public void restart() {Api.hit("/src/main/java/nl/tudelft/jpacman/sprite/AnimatedSprite.java", "restart", "lines 115:2 - 115:19"); this.current = 0; Api.hit("/src/main/java/nl/tudelft/jpacman/sprite/AnimatedSprite.java", "restart", "lines 116:2 - 116:47"); this.lastUpdate = System.currentTimeMillis(); Api.hit("/src/main/java/nl/tudelft/jpacman/sprite/AnimatedSprite.java", "restart", "lines 117:2 - 117:21"); setAnimating(true);}

	@Override
	public void draw(Graphics g, int x, int y, int width, int height) {Api.hit("/src/main/java/nl/tudelft/jpacman/sprite/AnimatedSprite.java", "draw", "lines 122:2 - 122:11"); update(); Api.hit("/src/main/java/nl/tudelft/jpacman/sprite/AnimatedSprite.java", "draw", "lines 123:2 - 123:47"); currentSprite().draw(g, x, y, width, height);}

	@Override
	public Sprite split(int x, int y, int width, int height) {Api.hit("/src/main/java/nl/tudelft/jpacman/sprite/AnimatedSprite.java", "split", "lines 128:2 - 128:11"); update(); Api.hit("/src/main/java/nl/tudelft/jpacman/sprite/AnimatedSprite.java", "split", "lines 129:2 - 129:52"); return currentSprite().split(x, y, width, height);}

	/**
	 * Updates the current frame index depending on the current system time.
	 */
	private void update() {Api.hit("/src/main/java/nl/tudelft/jpacman/sprite/AnimatedSprite.java", "update", "lines 136:2 - 136:40"); long now = System.currentTimeMillis(); Api.hit("/src/main/java/nl/tudelft/jpacman/sprite/AnimatedSprite.java", "update", "lines 137:2 - 149:3"); if (animating) {
			while (lastUpdate < now) {
				lastUpdate += animationDelay;
				current++;
				if (looping) {
					current %= animationFrames.length;
				} else if (current == animationFrames.length) {
					animating = false;
				}
			}
		} else {
			lastUpdate = now;
		}}

	@Override
	public int getWidth() {Api.hit("/src/main/java/nl/tudelft/jpacman/sprite/AnimatedSprite.java", "getWidth", "lines 154:2 - 154:33"); assert currentSprite() != null; Api.hit("/src/main/java/nl/tudelft/jpacman/sprite/AnimatedSprite.java", "getWidth", "lines 155:2 - 155:36"); return currentSprite().getWidth();}

	@Override
	public int getHeight() {Api.hit("/src/main/java/nl/tudelft/jpacman/sprite/AnimatedSprite.java", "getHeight", "lines 160:2 - 160:33"); assert currentSprite() != null; Api.hit("/src/main/java/nl/tudelft/jpacman/sprite/AnimatedSprite.java", "getHeight", "lines 161:2 - 161:37"); return currentSprite().getHeight();}

}
