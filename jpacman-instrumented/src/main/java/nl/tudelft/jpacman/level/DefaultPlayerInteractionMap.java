package nl.tudelft.jpacman.level; import nl.tudelft.jpacman.board.Unit;
import nl.tudelft.jpacman.level.CollisionInteractionMap.CollisionHandler;
import nl.tudelft.jpacman.npc.ghost.Ghost; import nl.tudelft.jpacman.Api; public class DefaultPlayerInteractionMap implements CollisionMap {

	private final CollisionMap collisions = defaultCollisions();

	@Override
	public void collide(Unit mover, Unit movedInto) {Api.hit("/src/main/java/nl/tudelft/jpacman/level/DefaultPlayerInteractionMap.java", "collide"); collisions.collide(mover, movedInto);}

	/**
	 * Creates the default collisions Player-Ghost and Player-Pellet.
	 * 
	 * @return The collision map containing collisions for Player-Ghost and
	 *         Player-Pellet.
	 */
	public static CollisionInteractionMap defaultCollisions() {Api.hit("/src/main/java/nl/tudelft/jpacman/level/DefaultPlayerInteractionMap.java", "handleCollision"); CollisionInteractionMap collisionMap = new CollisionInteractionMap(); collisionMap.onCollision(Player.class, Ghost.class,
				new CollisionHandler<Player, Ghost>() {

					@Override
					public void handleCollision(Player player, Ghost ghost) {Api.hit("/src/main/java/nl/tudelft/jpacman/level/DefaultPlayerInteractionMap.java", "handleCollision"); player.setAlive(false);}
				});

		collisionMap.onCollision(Player.class, Pellet.class,
				new CollisionHandler<Player, Pellet>() {

					@Override
					public void handleCollision(Player player, Pellet pellet) {Api.hit("/src/main/java/nl/tudelft/jpacman/level/DefaultPlayerInteractionMap.java", "handleCollision"); pellet.leaveSquare(); player.addPoints(pellet.getValue());}
				});
		return collisionMap;}
}
