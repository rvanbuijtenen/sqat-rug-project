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
	private Sprite currentSprite() {Api.hit("/src/main/java/nl/tudelft/jpacman/sprite/AnimatedSprite.java", "currentSprite"); Sprite result = END_OF_LOOP; if (current < animationFrames.length) {
			result = animationFrames[current];
		}
		assert result != null;
		return result;}

	/**
	 * Starts or stops the animation of this sprite.
	 * 
	 * @param isAnimating
	 *            <code>true</code> to animate this sprite or <code>false</code>
	 *            to stop animating this sprite.
	 */
	public void setAnimating(boolean isAnimating) {Api.hit("/src/main/java/nl/tudelft/jpacman/sprite/AnimatedSprite.java", "setAnimating"); this.animating = isAnimating;}
	
	/**
	 * (Re)starts the current animation.
	 */
	public void restart() {Api.hit("/src/main/java/nl/tudelft/jpacman/sprite/AnimatedSprite.java", "restart"); this.current = 0; this.lastUpdate = System.currentTimeMillis();
		setAnimating(true);}

	@Override
	public void draw(Graphics g, int x, int y, int width, int height) {Api.hit("/src/main/java/nl/tudelft/jpacman/sprite/AnimatedSprite.java", "draw"); update(); currentSprite().draw(g, x, y, width, height);}

	@Override
	public Sprite split(int x, int y, int width, int height) {Api.hit("/src/main/java/nl/tudelft/jpacman/sprite/AnimatedSprite.java", "split"); update(); return currentSprite().split(x, y, width, height);}

	/**
	 * Updates the current frame index depending on the current system time.
	 */
	private void update() {Api.hit("/src/main/java/nl/tudelft/jpacman/sprite/AnimatedSprite.java", "update"); long now = System.currentTimeMillis(); if (animating) {
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
	public int getWidth() {Api.hit("/src/main/java/nl/tudelft/jpacman/sprite/AnimatedSprite.java", "getWidth"); assert currentSprite() != null; return currentSprite().getWidth();}

	@Override
	public int getHeight() {Api.hit("/src/main/java/nl/tudelft/jpacman/sprite/AnimatedSprite.java", "getHeight"); assert currentSprite() != null; return currentSprite().getHeight();}

}
