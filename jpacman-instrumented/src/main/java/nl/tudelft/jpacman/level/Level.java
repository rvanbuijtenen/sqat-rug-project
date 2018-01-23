package nl.tudelft.jpacman.level; import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import nl.tudelft.jpacman.board.Board;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.board.Unit;
import nl.tudelft.jpacman.npc.NPC; import nl.tudelft.jpacman.Api; @SuppressWarnings("PMD.TooManyMethods")
public class Level {

	/**
	 * The board of this level.
	 */
	private final Board board;

	/**
	 * The lock that ensures moves are executed sequential.
	 */
	private final Object moveLock = new Object();

	/**
	 * The lock that ensures starting and stopping can't interfere with each
	 * other.
	 */
	private final Object startStopLock = new Object();

	/**
	 * The NPCs of this level and, if they are running, their schedules.
	 */
	private final Map<NPC, ScheduledExecutorService> npcs;

	/**
	 * <code>true</code> iff this level is currently in progress, i.e. players
	 * and NPCs can move.
	 */
	private boolean inProgress;

	/**
	 * The squares from which players can start this game.
	 */
	private final List<Square> startSquares;

	/**
	 * The start current selected starting square.
	 */
	private int startSquareIndex;

	/**
	 * The players on this level.
	 */
	private final List<Player> players;

	/**
	 * The table of possible collisions between units.
	 */
	private final CollisionMap collisions;

	/**
	 The objects observing this level.
	 */
	private final Set<LevelObserver> observers;

	/**
	 * Creates a new level for the board.
	 * 
	 * @param b
	 *            The board for the level.
	 * @param ghosts
	 *            The ghosts on the board.
	 * @param startPositions
	 *            The squares on which players start on this board.
	 * @param collisionMap
	 *            The collection of collisions that should be handled.
	 */
	public Level(Board b, List<NPC> ghosts, List<Square> startPositions,
			CollisionMap collisionMap) {
		assert b != null;
		assert ghosts != null;
		assert startPositions != null;

		this.board = b;
		this.inProgress = false;
		this.npcs = new HashMap<>();
		for (NPC g : ghosts) {
			npcs.put(g, null);
		}
		this.startSquares = startPositions;
		this.startSquareIndex = 0;
		this.players = new ArrayList<>();
		this.collisions = collisionMap;
		this.observers = new HashSet<>();
	}

	/**
	 * Adds an observer that will be notified when the level is won or lost.
	 * 
	 * @param observer
	 *            The observer that will be notified.
	 */
	public void addObserver(LevelObserver observer) {Api.hit("/src/main/java/nl/tudelft/jpacman/level/Level.java", "addObserver", "lines 119:2 - 119:26"); observers.add(observer);}

	/**
	 * Removes an observer if it was listed.
	 * 
	 * @param observer
	 *            The observer to be removed.
	 */
	public void removeObserver(LevelObserver observer) {Api.hit("/src/main/java/nl/tudelft/jpacman/level/Level.java", "removeObserver", "lines 129:2 - 129:29"); observers.remove(observer);}

	/**
	 * Registers a player on this level, assigning him to a starting position. A
	 * player can only be registered once, registering a player again will have
	 * no effect.
	 * 
	 * @param p
	 *            The player to register.
	 */
	public void registerPlayer(Player p) {Api.hit("/src/main/java/nl/tudelft/jpacman/level/Level.java", "registerPlayer", "lines 141:2 - 141:19"); assert p != null; Api.hit("/src/main/java/nl/tudelft/jpacman/level/Level.java", "registerPlayer", "lines 142:2 - 142:33"); assert !startSquares.isEmpty(); Api.hit("/src/main/java/nl/tudelft/jpacman/level/Level.java", "registerPlayer", "lines 144:2 - 146:3"); if (players.contains(p)) {
			return;
		} Api.hit("/src/main/java/nl/tudelft/jpacman/level/Level.java", "registerPlayer", "lines 147:2 - 147:17"); players.add(p); Api.hit("/src/main/java/nl/tudelft/jpacman/level/Level.java", "registerPlayer", "lines 148:2 - 148:53"); Square square = startSquares.get(startSquareIndex); Api.hit("/src/main/java/nl/tudelft/jpacman/level/Level.java", "registerPlayer", "lines 149:2 - 149:19"); p.occupy(square); Api.hit("/src/main/java/nl/tudelft/jpacman/level/Level.java", "registerPlayer", "lines 150:2 - 150:21"); startSquareIndex++; Api.hit("/src/main/java/nl/tudelft/jpacman/level/Level.java", "registerPlayer", "lines 151:2 - 151:42"); startSquareIndex %= startSquares.size();}

	/**
	 * Returns the board of this level.
	 * 
	 * @return The board of this level.
	 */
	public Board getBoard() {Api.hit("/src/main/java/nl/tudelft/jpacman/level/Level.java", "getBoard", "lines 160:2 - 160:15"); return board;}

	/**
	 * Moves the unit into the given direction if possible and handles all
	 * collisions.
	 * 
	 * @param unit
	 *            The unit to move.
	 * @param direction
	 *            The direction to move the unit in.
	 */
	public void move(Unit unit, Direction direction) {Api.hit("/src/main/java/nl/tudelft/jpacman/level/Level.java", "move", "lines 173:2 - 173:22"); assert unit != null; Api.hit("/src/main/java/nl/tudelft/jpacman/level/Level.java", "move", "lines 174:2 - 174:27"); assert direction != null; Api.hit("/src/main/java/nl/tudelft/jpacman/level/Level.java", "move", "lines 176:2 - 178:3"); if (!isInProgress()) {
			return;
		} Api.hit("/src/main/java/nl/tudelft/jpacman/level/Level.java", "move", "lines 180:2 - 193:3"); synchronized (moveLock) {
			unit.setDirection(direction);
			Square location = unit.getSquare();
			Square destination = location.getSquareAt(direction);

			if (destination.isAccessibleTo(unit)) {
				List<Unit> occupants = destination.getOccupants();
				unit.occupy(destination);
				for (Unit occupant : occupants) {
					collisions.collide(unit, occupant);
				}
			}
			updateObservers();
		}}

	/**
	 * Starts or resumes this level, allowing movement and (re)starting the
	 * NPCs.
	 */
	public void start() {Api.hit("/src/main/java/nl/tudelft/jpacman/level/Level.java", "start", "lines 201:2 - 208:3"); synchronized (startStopLock) {
			if (isInProgress()) {
				return;
			}
			startNPCs();
			inProgress = true;
			updateObservers();
		}}

	/**
	 * Stops or pauses this level, no longer allowing any movement on the board
	 * and stopping all NPCs.
	 */
	public void stop() {Api.hit("/src/main/java/nl/tudelft/jpacman/level/Level.java", "stop", "lines 216:2 - 222:3"); synchronized (startStopLock) {
			if (!isInProgress()) {
				return;
			}
			stopNPCs();
			inProgress = false;
		}}

	/**
	 * Starts all NPC movement scheduling.
	 */
	private void startNPCs() {Api.hit("/src/main/java/nl/tudelft/jpacman/level/Level.java", "startNPCs", "lines 229:2 - 235:3"); for (final NPC npc : npcs.keySet()) {
			ScheduledExecutorService service = Executors
					.newSingleThreadScheduledExecutor();
			service.schedule(new NpcMoveTask(service, npc),
					npc.getInterval() / 2, TimeUnit.MILLISECONDS);
			npcs.put(npc, service);
		}}

	/**
	 * Stops all NPC movement scheduling and interrupts any movements being
	 * executed.
	 */
	private void stopNPCs() {Api.hit("/src/main/java/nl/tudelft/jpacman/level/Level.java", "stopNPCs", "lines 243:2 - 245:3"); for (Entry<NPC, ScheduledExecutorService> e : npcs.entrySet()) {
			e.getValue().shutdownNow();
		}}

	/**
	 * Returns whether this level is in progress, i.e. whether moves can be made
	 * on the board.
	 * 
	 * @return <code>true</code> iff this level is in progress.
	 */
	public boolean isInProgress() {Api.hit("/src/main/java/nl/tudelft/jpacman/level/Level.java", "isInProgress", "lines 255:2 - 255:20"); return inProgress;}

	/**
	 * Updates the observers about the state of this level.
	 */
	private void updateObservers() {Api.hit("/src/main/java/nl/tudelft/jpacman/level/Level.java", "updateObservers", "lines 262:2 - 266:3"); if (!isAnyPlayerAlive()) {
			for (LevelObserver o : observers) {
				o.levelLost();
			}
		} Api.hit("/src/main/java/nl/tudelft/jpacman/level/Level.java", "updateObservers", "lines 267:2 - 271:3"); if (remainingPellets() == 0) {
			for (LevelObserver o : observers) {
				o.levelWon();
			}
		}}

	/**
	 * Returns <code>true</code> iff at least one of the players in this level
	 * is alive.
	 * 
	 * @return <code>true</code> if at least one of the registered players is
	 *         alive.
	 */
	public boolean isAnyPlayerAlive() {Api.hit("/src/main/java/nl/tudelft/jpacman/level/Level.java", "isAnyPlayerAlive", "lines 282:2 - 286:3"); for (Player p : players) {
			if (p.isAlive()) {
				return true;
			}
		} Api.hit("/src/main/java/nl/tudelft/jpacman/level/Level.java", "isAnyPlayerAlive", "lines 287:2 - 287:15"); return false;}

	/**
	 * Counts the pellets remaining on the board.
	 * 
	 * @return The amount of pellets remaining on the board.
	 */
	public int remainingPellets() {Api.hit("/src/main/java/nl/tudelft/jpacman/level/Level.java", "remainingPellets", "lines 296:2 - 296:23"); Board b = getBoard(); Api.hit("/src/main/java/nl/tudelft/jpacman/level/Level.java", "remainingPellets", "lines 297:2 - 297:18"); int pellets = 0; Api.hit("/src/main/java/nl/tudelft/jpacman/level/Level.java", "remainingPellets", "lines 298:2 - 306:3"); for (int x = 0; x < b.getWidth(); x++) {
			for (int y = 0; y < b.getHeight(); y++) {
				for (Unit u : b.squareAt(x, y).getOccupants()) {
					if (u instanceof Pellet) {
						pellets++;
					}
				}
			}
		} Api.hit("/src/main/java/nl/tudelft/jpacman/level/Level.java", "remainingPellets", "lines 307:2 - 307:22"); assert pellets >= 0; Api.hit("/src/main/java/nl/tudelft/jpacman/level/Level.java", "remainingPellets", "lines 308:2 - 308:17"); return pellets;}

	/**
	 * A task that moves an NPC and reschedules itself after it finished.
	 * 
	 * @author Jeroen Roosen 
	 */
	private final class NpcMoveTask implements Runnable {

		/**
		 * The service executing the task.
		 */
		private final ScheduledExecutorService service;

		/**
		 * The NPC to move.
		 */
		private final NPC npc;

		/**
		 * Creates a new task.
		 * 
		 * @param s
		 *            The service that executes the task.
		 * @param n
		 *            The NPC to move.
		 */
		NpcMoveTask(ScheduledExecutorService s, NPC n) {
			this.service = s;
			this.npc = n;
		}

		@Override
		public void run() {Api.hit("/src/main/java/nl/tudelft/jpacman/level/Level.java", "run", "lines 343:3 - 343:39"); Direction nextMove = npc.nextMove(); Api.hit("/src/main/java/nl/tudelft/jpacman/level/Level.java", "run", "lines 344:3 - 346:4"); if (nextMove != null) {
				move(npc, nextMove);
			} Api.hit("/src/main/java/nl/tudelft/jpacman/level/Level.java", "run", "lines 347:3 - 347:37"); long interval = npc.getInterval(); Api.hit("/src/main/java/nl/tudelft/jpacman/level/Level.java", "run", "lines 348:3 - 348:59"); service.schedule(this, interval, TimeUnit.MILLISECONDS);}
	}

	/**
	 * An observer that will be notified when the level is won or lost.
	 * 
	 * @author Jeroen Roosen 
	 */
	public interface LevelObserver {

		/**
		 * The level has been won. Typically the level should be stopped when
		 * this event is received.
		 */
		void levelWon();

		/**
		 * The level has been lost. Typically the level should be stopped when
		 * this event is received.
		 */
		void levelLost();
	}
}
