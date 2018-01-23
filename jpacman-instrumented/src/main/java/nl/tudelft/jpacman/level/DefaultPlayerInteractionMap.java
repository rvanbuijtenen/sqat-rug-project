package nl.tudelft.jpacman.level; import nl.tudelft.jpacman.board.Unit;
import nl.tudelft.jpacman.level.CollisionInteractionMap.CollisionHandler;
import nl.tudelft.jpacman.npc.ghost.Ghost; import nl.tudelft.jpacman.Api; public class DefaultPlayerInteractionMap implements CollisionMap {

	private final CollisionMap collisions = defaultCollisions();

	@Override
	public void collide(Unit mover, Unit movedInto) {Api.hit("/src/main/java/nl/tudelft/jpacman/level/DefaultPlayerInteractionMap.java", "collide", "lines 24:2 - 24:39"); collisions.collide(mover, movedInto);}

	/**
	 * Creates the default collisions Player-Ghost and Player-Pellet.
	 * 
	 * @return The collision map containing collisions for Player-Ghost and
	 *         Player-Pellet.
	 */
	public static CollisionInteractionMap defaultCollisions() {Api.hit("/src/main/java/nl/tudelft/jpacman/level/DefaultPlayerInteractionMap.java", "handleCollision", "lines 34:2 - 34:71"); CollisionInteractionMap collisionMap = new CollisionInteractionMap(); Api.hit("/src/main/java/nl/tudelft/jpacman/level/DefaultPlayerInteractionMap.java", "handleCollision", "lines 36:2 - 43:7"); collisionMap.onCollision(Player.class, Ghost.class,
				new CollisionHandler<Player, Ghost>() {

					@Override
					public void handleCollision(Player player, Ghost ghost) {Api.hit("/src/main/java/nl/tudelft/jpacman/level/DefaultPlayerInteractionMap.java", "handleCollision", "lines 41:6 - 41:29"); player.setAlive(false);}
				}); Api.hit("/src/main/java/nl/tudelft/jpacman/level/DefaultPlayerInteractionMap.java", "handleCollision", "lines 45:2 - 53:7"); collisionMap.onCollision(Player.class, Pellet.class,
				new CollisionHandler<Player, Pellet>() {

					@Override
					public void handleCollision(Player player, Pellet pellet) {Api.hit("/src/main/java/nl/tudelft/jpacman/level/DefaultPlayerInteractionMap.java", "handleCollision", "lines 50:6 - 50:27"); pellet.leaveSquare(); Api.hit("/src/main/java/nl/tudelft/jpacman/level/DefaultPlayerInteractionMap.java", "handleCollision", "lines 51:6 - 51:42"); player.addPoints(pellet.getValue());}
				}); Api.hit("/src/main/java/nl/tudelft/jpacman/level/DefaultPlayerInteractionMap.java", "handleCollision", "lines 54:2 - 54:22"); return collisionMap;}
}
