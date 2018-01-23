package nl.tudelft.jpacman.npc.ghost; import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.board.Unit; import nl.tudelft.jpacman.Api; public final class Navigation {

	private Navigation() {
	}
	
	/**
	 * Calculates the shortest path. This is done by BFS. This search ensures
	 * the traveller is allowed to occupy the squares on the way, or returns the
	 * shortest path to the square regardless of terrain if no traveller is
	 * specified.
	 * 
	 * @param from
	 *            The starting square.
	 * @param to
	 *            The destination.
	 * @param traveller
	 *            The traveller attempting to reach the destination. If
	 *            traveller is set to <code>null</code>, this method will ignore
	 *            terrain and find the shortest path whether it can actually be
	 *            reached or not.
	 * @return The shortest path to the destination or <code>null</code> if no
	 *         such path could be found. When the destination is the current
	 *         square, an empty list is returned.
	 */
	public static List<Direction> shortestPath(Square from, Square to,
			Unit traveller) {Api.hit("/src/main/java/nl/tudelft/jpacman/npc/ghost/Navigation.java", "shortestPath", "lines 43:2 - 45:3"); if (from.equals(to)) {
			return new ArrayList<>();
		} Api.hit("/src/main/java/nl/tudelft/jpacman/npc/ghost/Navigation.java", "shortestPath", "lines 47:2 - 47:41"); List<Node> targets = new ArrayList<>(); Api.hit("/src/main/java/nl/tudelft/jpacman/npc/ghost/Navigation.java", "shortestPath", "lines 48:2 - 48:40"); Set<Square> visited = new HashSet<>(); Api.hit("/src/main/java/nl/tudelft/jpacman/npc/ghost/Navigation.java", "shortestPath", "lines 49:2 - 49:42"); targets.add(new Node(null, from, null)); Api.hit("/src/main/java/nl/tudelft/jpacman/npc/ghost/Navigation.java", "shortestPath", "lines 50:2 - 58:3"); while (!targets.isEmpty()) {
			Node n = targets.remove(0);
			Square s = n.getSquare();
			if (s.equals(to)) {
				return n.getPath();
			}
			visited.add(s);
			addNewTargets(traveller, targets, visited, n, s);
		} Api.hit("/src/main/java/nl/tudelft/jpacman/npc/ghost/Navigation.java", "shortestPath", "lines 59:2 - 59:14"); return null;}

	private static void addNewTargets(Unit traveller, List<Node> targets,
			Set<Square> visited, Node n, Square s) {Api.hit("/src/main/java/nl/tudelft/jpacman/npc/ghost/Navigation.java", "addNewTargets", "lines 64:2 - 71:3"); for (Direction d : Direction.values()) {
			Square target = s.getSquareAt(d);
			if (!visited.contains(target)
					&& (traveller == null || target
							.isAccessibleTo(traveller))) {
				targets.add(new Node(d, target, n));
			}
		}}

	/**
	 * Finds the nearest unit of the given type and returns its location. This
	 * method will perform a breadth first search starting from the given
	 * square.
	 * 
	 * @param type
	 *            The type of unit to search for.
	 * @param currentLocation
	 *            The starting location for the search.
	 * @return The nearest unit of the given type, or <code>null</code> if no
	 *         such unit could be found.
	 */
	public static Unit findNearest(Class<? extends Unit> type,
			Square currentLocation) {Api.hit("/src/main/java/nl/tudelft/jpacman/npc/ghost/Navigation.java", "findNearest", "lines 88:2 - 88:40"); List<Square> toDo = new ArrayList<>(); Api.hit("/src/main/java/nl/tudelft/jpacman/npc/ghost/Navigation.java", "findNearest", "lines 89:2 - 89:40"); Set<Square> visited = new HashSet<>(); Api.hit("/src/main/java/nl/tudelft/jpacman/npc/ghost/Navigation.java", "findNearest", "lines 91:2 - 91:28"); toDo.add(currentLocation); Api.hit("/src/main/java/nl/tudelft/jpacman/npc/ghost/Navigation.java", "findNearest", "lines 93:2 - 106:3"); while (!toDo.isEmpty()) {
			Square square = toDo.remove(0);
			Unit unit = findUnit(type, square);
			if (unit != null) {
				return unit;
			}
			visited.add(square);
			for (Direction d : Direction.values()) {
				Square newTarget = square.getSquareAt(d);
				if (!visited.contains(newTarget) && !toDo.contains(newTarget)) {
					toDo.add(newTarget);
				}
			}
		} Api.hit("/src/main/java/nl/tudelft/jpacman/npc/ghost/Navigation.java", "findNearest", "lines 107:2 - 107:14"); return null;}

	/**
	 * Determines whether a square has an occupant of a certain type.
	 * 
	 * @param type
	 *            The type to search for.
	 * @param square
	 *            The square to search.
	 * @return A unit of type T, iff such a unit occupies this square, or
	 *         <code>null</code> of none does.
	 */
	public static Unit findUnit(Class<? extends Unit> type, Square square) {Api.hit("/src/main/java/nl/tudelft/jpacman/npc/ghost/Navigation.java", "findUnit", "lines 121:2 - 125:3"); for (Unit u : square.getOccupants()) {
			if (type.isInstance(u)) {
				return u;
			}
		} Api.hit("/src/main/java/nl/tudelft/jpacman/npc/ghost/Navigation.java", "findUnit", "lines 126:2 - 126:14"); return null;}

	/**
	 * Helper class to keep track of the path.
	 * 
	 * @author Jeroen Roosen 
	 */
	private static final class Node {

		/**
		 * The direction for this node, which is <code>null</code> for the root
		 * node.
		 */
		private final Direction direction;

		/**
		 * The parent node, which is <code>null</code> for the root node.
		 */
		private final Node parent;

		/**
		 * The square associated with this node.
		 */
		private final Square square;

		/**
		 * Creates a new node.
		 * 
		 * @param d
		 *            The direction, which is <code>null</code> for the root
		 *            node.
		 * @param s
		 *            The square.
		 * @param p
		 *            The parent node, which is <code>null</code> for the root
		 *            node.
		 */
		Node(Direction d, Square s, Node p) {
			this.direction = d;
			this.square = s;
			this.parent = p;
		}

		/**
		 * @return The direction for this node, or <code>null</code> if this
		 *         node is a root node.
		 */
		private Direction getDirection() {Api.hit("/src/main/java/nl/tudelft/jpacman/npc/ghost/Navigation.java", "getDirection", "lines 175:3 - 175:20"); return direction;}

		/**
		 * @return The square for this node.
		 */
		private Square getSquare() {Api.hit("/src/main/java/nl/tudelft/jpacman/npc/ghost/Navigation.java", "getSquare", "lines 182:3 - 182:17"); return square;}

		/**
		 * @return The parent node, or <code>null</code> if this node is a root
		 *         node.
		 */
		private Node getParent() {Api.hit("/src/main/java/nl/tudelft/jpacman/npc/ghost/Navigation.java", "getParent", "lines 190:3 - 190:17"); return parent;}

		/**
		 * Returns the list of values from the root of the tree to this node.
		 * 
		 * @return The list of values from the root of the tree to this node.
		 */
		private List<Direction> getPath() {Api.hit("/src/main/java/nl/tudelft/jpacman/npc/ghost/Navigation.java", "getPath", "lines 199:3 - 201:4"); if (getParent() == null) {
				return new ArrayList<>();
			} Api.hit("/src/main/java/nl/tudelft/jpacman/npc/ghost/Navigation.java", "getPath", "lines 202:3 - 202:43"); List<Direction> path = parent.getPath(); Api.hit("/src/main/java/nl/tudelft/jpacman/npc/ghost/Navigation.java", "getPath", "lines 203:3 - 203:28"); path.add(getDirection()); Api.hit("/src/main/java/nl/tudelft/jpacman/npc/ghost/Navigation.java", "getPath", "lines 204:3 - 204:15"); return path;}
	}
}
