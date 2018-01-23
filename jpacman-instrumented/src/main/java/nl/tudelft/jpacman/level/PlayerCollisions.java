package nl.tudelft.jpacman.level; import nl.tudelft.jpacman.board.Unit;
import nl.tudelft.jpacman.npc.ghost.Ghost; import nl.tudelft.jpacman.Api; public class PlayerCollisions implements CollisionMap {

	@Override
	public void collide(Unit mover, Unit collidedOn) {Api.hit("/src/main/java/nl/tudelft/jpacman/level/PlayerCollisions.java", "collide", "lines 22:2 - 30:3"); if (mover instanceof Player) {
			playerColliding((Player) mover, collidedOn);
		}
		else if (mover instanceof Ghost) {
			ghostColliding((Ghost) mover, collidedOn);
		}
		else if (mover instanceof Pellet) {
			pelletColliding((Pellet) mover, collidedOn);
		}}
	
	private void playerColliding(Player player, Unit collidedOn) {Api.hit("/src/main/java/nl/tudelft/jpacman/level/PlayerCollisions.java", "playerColliding", "lines 34:2 - 36:3"); if (collidedOn instanceof Ghost) {
			playerVersusGhost(player, (Ghost) collidedOn);
		} Api.hit("/src/main/java/nl/tudelft/jpacman/level/PlayerCollisions.java", "playerColliding", "lines 38:2 - 40:3"); if (collidedOn instanceof Pellet) {
			playerVersusPellet(player, (Pellet) collidedOn);
		}}
	
	private void ghostColliding(Ghost ghost, Unit collidedOn) {Api.hit("/src/main/java/nl/tudelft/jpacman/level/PlayerCollisions.java", "ghostColliding", "lines 44:2 - 46:3"); if (collidedOn instanceof Player) {
			playerVersusGhost((Player) collidedOn, ghost);
		}}
	
	private void pelletColliding(Pellet pellet, Unit collidedOn) {Api.hit("/src/main/java/nl/tudelft/jpacman/level/PlayerCollisions.java", "pelletColliding", "lines 50:2 - 52:3"); if (collidedOn instanceof Player) {
			playerVersusPellet((Player) collidedOn, pellet);
		}}
	
	
	/**
	 * Actual case of player bumping into ghost or vice versa.
     *
     * @param player The player involved in the collision.
     * @param ghost The ghost involved in the collision.
	 */
	public void playerVersusGhost(Player player, Ghost ghost) {Api.hit("/src/main/java/nl/tudelft/jpacman/level/PlayerCollisions.java", "playerVersusGhost", "lines 63:2 - 63:25"); player.setAlive(false);}
	
	/**
	 * Actual case of player consuming a pellet.
     *
     * @param player The player involved in the collision.
     * @param pellet The pellet involved in the collision.
	 */
	public void playerVersusPellet(Player player, Pellet pellet) {Api.hit("/src/main/java/nl/tudelft/jpacman/level/PlayerCollisions.java", "playerVersusPellet", "lines 73:2 - 73:23"); pellet.leaveSquare(); Api.hit("/src/main/java/nl/tudelft/jpacman/level/PlayerCollisions.java", "playerVersusPellet", "lines 74:2 - 74:38"); player.addPoints(pellet.getValue());}

}
