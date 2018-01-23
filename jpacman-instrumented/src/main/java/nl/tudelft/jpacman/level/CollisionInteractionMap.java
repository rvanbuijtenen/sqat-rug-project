package nl.tudelft.jpacman.level; import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.tudelft.jpacman.board.Unit; import nl.tudelft.jpacman.Api; public class CollisionInteractionMap implements CollisionMap {

	/**
	 * The collection of collision handlers.
	 */
	private final Map<
			Class<? extends Unit>,
			Map<Class<? extends Unit>, CollisionHandler<?, ?>>
	> handlers;

	/**
	 * Creates a new, empty collision map.
	 */
	public CollisionInteractionMap() {
		this.handlers = new HashMap<>();
	}

	/**
	 * Adds a two-way collision interaction to this collection, i.e. the
	 * collision handler will be used for both C1 versus C2 and C2 versus C1.
	 * 
	 * @param <C1>
	 *            The collider type.
	 * @param <C2>
	 *            The collidee (unit that was moved into) type.
	 * 
	 * @param collider
	 *            The collider type.
	 * @param collidee
	 *            The collidee type.
	 * @param handler
	 *            The handler that handles the collision.
	 */
	public <C1 extends Unit, C2 extends Unit> void onCollision(
			Class<C1> collider, Class<C2> collidee,
			CollisionHandler<C1, C2> handler) {Api.hit("/src/main/java/nl/tudelft/jpacman/level/CollisionInteractionMap.java", "onCollision", "lines 52:2 - 52:49"); onCollision(collider, collidee, true, handler);}

	/**
	 * Adds a collision interaction to this collection.
	 * 
	 * @param <C1>
	 *            The collider type.
	 * @param <C2>
	 *            The collidee (unit that was moved into) type.
	 * 
	 * @param collider
	 *            The collider type.
	 * @param collidee
	 *            The collidee type.
	 * @param symetric
	 *            <code>true</code> if this collision is used for both
	 *            C1 against C2 and vice versa;
	 *            <code>false</code> if only for C1 against C2.
	 * @param handler
	 *            The handler that handles the collision.
	 */
	public <C1 extends Unit, C2 extends Unit> void onCollision(
			Class<C1> collider, Class<C2> collidee, boolean symetric,
			CollisionHandler<C1, C2> handler) {Api.hit("/src/main/java/nl/tudelft/jpacman/level/CollisionInteractionMap.java", "onCollision", "lines 77:2 - 77:42"); addHandler(collider, collidee, handler); Api.hit("/src/main/java/nl/tudelft/jpacman/level/CollisionInteractionMap.java", "onCollision", "lines 78:2 - 81:3"); if (symetric) {
			addHandler(collidee, collider, new InverseCollisionHandler<>(
					handler));
		}}

	/**
	 * Adds the collision interaction..
	 * 
	 * @param collider
	 *            The collider type.
	 * @param collidee
	 *            The collidee type.
	 * @param handler
	 *            The handler that handles the collision.
	 */
	private void addHandler(Class<? extends Unit> collider,
			Class<? extends Unit> collidee, CollisionHandler<?, ?> handler) {Api.hit("/src/main/java/nl/tudelft/jpacman/level/CollisionInteractionMap.java", "addHandler", "lines 96:2 - 100:3"); if (!handlers.containsKey(collider)) {
			handlers.put(
					collider,
					new HashMap<>());
		} Api.hit("/src/main/java/nl/tudelft/jpacman/level/CollisionInteractionMap.java", "addHandler", "lines 102:2 - 103:19"); Map<Class<? extends Unit>, CollisionHandler<?, ?>> map = handlers
				.get(collider); Api.hit("/src/main/java/nl/tudelft/jpacman/level/CollisionInteractionMap.java", "addHandler", "lines 104:2 - 104:29"); map.put(collidee, handler);}

	/**
	 * Handles the collision between two colliding parties, if a suitable
	 * collision handler is listed.
	 * 
	 * @param <C1>
	 *            The collider type.
	 * @param <C2>
	 *            The collidee (unit that was moved into) type.
	 * 
	 * @param collider
	 *            The collider.
	 * @param collidee
	 *            The collidee.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <C1 extends Unit, C2 extends Unit> void collide(C1 collider,
			C2 collidee) {Api.hit("/src/main/java/nl/tudelft/jpacman/level/CollisionInteractionMap.java", "collide", "lines 125:2 - 126:25"); Class<? extends Unit> colliderKey = getMostSpecificClass(handlers,
				collider.getClass()); Api.hit("/src/main/java/nl/tudelft/jpacman/level/CollisionInteractionMap.java", "collide", "lines 127:2 - 129:3"); if (colliderKey == null) {
			return;
		} Api.hit("/src/main/java/nl/tudelft/jpacman/level/CollisionInteractionMap.java", "collide", "lines 131:2 - 132:22"); Map<Class<? extends Unit>, CollisionHandler<?, ?>> map = handlers
				.get(colliderKey); Api.hit("/src/main/java/nl/tudelft/jpacman/level/CollisionInteractionMap.java", "collide", "lines 133:2 - 134:25"); Class<? extends Unit> collideeKey = getMostSpecificClass(map,
				collidee.getClass()); Api.hit("/src/main/java/nl/tudelft/jpacman/level/CollisionInteractionMap.java", "collide", "lines 135:2 - 137:3"); if (collideeKey == null) {
			return;
		} Api.hit("/src/main/java/nl/tudelft/jpacman/level/CollisionInteractionMap.java", "collide", "lines 139:2 - 140:22"); CollisionHandler<C1, C2> collisionHandler = (CollisionHandler<C1, C2>) map
				.get(collideeKey); Api.hit("/src/main/java/nl/tudelft/jpacman/level/CollisionInteractionMap.java", "collide", "lines 141:2 - 143:3"); if (collisionHandler == null) {
			return;
		} Api.hit("/src/main/java/nl/tudelft/jpacman/level/CollisionInteractionMap.java", "collide", "lines 145:2 - 145:55"); collisionHandler.handleCollision(collider, collidee);}

	/**
	 * Figures out the most specific class that is listed in the map. I.e. if A
	 * extends B and B is listed while requesting A, then B will be returned.
	 * 
	 * @param map
	 *            The map with the key collection to find a matching class in.
	 * @param key
	 *            The class to search the most suitable key for.
	 * @return The most specific class from the key collection.
	 */
	private Class<? extends Unit> getMostSpecificClass(
			Map<Class<? extends Unit>, ?> map, Class<? extends Unit> key) {Api.hit("/src/main/java/nl/tudelft/jpacman/level/CollisionInteractionMap.java", "getMostSpecificClass", "lines 160:2 - 160:72"); List<Class<? extends Unit>> collideeInheritance = getInheritance(key); Api.hit("/src/main/java/nl/tudelft/jpacman/level/CollisionInteractionMap.java", "getMostSpecificClass", "lines 161:2 - 165:3"); for (Class<? extends Unit> pointer : collideeInheritance) {
			if (map.containsKey(pointer)) {
				return pointer;
			}
		} Api.hit("/src/main/java/nl/tudelft/jpacman/level/CollisionInteractionMap.java", "getMostSpecificClass", "lines 166:2 - 166:14"); return null;}

	/**
	 * Returns a list of all classes and interfaces the class inherits.
	 * 
	 * @param clazz
	 *            The class to create a list of super classes and interfaces
	 *            for.
	 * @return A list of all classes and interfaces the class inherits.
	 */
	@SuppressWarnings("unchecked")
	private List<Class<? extends Unit>> getInheritance(
			Class<? extends Unit> clazz) {Api.hit("/src/main/java/nl/tudelft/jpacman/level/CollisionInteractionMap.java", "getInheritance", "lines 180:2 - 180:56"); List<Class<? extends Unit>> found = new ArrayList<>(); Api.hit("/src/main/java/nl/tudelft/jpacman/level/CollisionInteractionMap.java", "getInheritance", "lines 181:2 - 181:19"); found.add(clazz); Api.hit("/src/main/java/nl/tudelft/jpacman/level/CollisionInteractionMap.java", "getInheritance", "lines 183:2 - 183:16"); int index = 0; Api.hit("/src/main/java/nl/tudelft/jpacman/level/CollisionInteractionMap.java", "getInheritance", "lines 184:2 - 196:3"); while (found.size() > index) {
			Class<?> current = found.get(index);
			Class<?> superClass = current.getSuperclass();
			if (superClass != null && Unit.class.isAssignableFrom(superClass)) {
				found.add((Class<? extends Unit>) superClass);
			}
			for (Class<?> classInterface : current.getInterfaces()) {
				if (Unit.class.isAssignableFrom(classInterface)) {
					found.add((Class<? extends Unit>) classInterface);
				}
			}
			index++;
		} Api.hit("/src/main/java/nl/tudelft/jpacman/level/CollisionInteractionMap.java", "getInheritance", "lines 198:2 - 198:15"); return found;}

	/**
	 * Handles the collision between two colliding parties.
	 * 
	 * @author Michael de Jong
	 * 
	 * @param <C1>
	 *            The collider type.
	 * @param <C2>
	 *            The collidee type.
	 */
	public interface CollisionHandler<C1 extends Unit, C2 extends Unit> {

		/**
		 * Handles the collision between two colliding parties.
		 * 
		 * @param collider
		 *            The collider.
		 * @param collidee
		 *            The collidee.
		 */
		void handleCollision(C1 collider, C2 collidee);
	}

	/**
	 * An symmetrical copy of a collision hander.
	 * 
	 * @author Michael de Jong
	 * 
	 * @param <C1>
	 *            The collider type.
	 * @param <C2>
	 *            The collidee type.
	 */
	private final static class InverseCollisionHandler<C1 extends Unit, C2 extends Unit>
			implements CollisionHandler<C1, C2> {

		/**
		 * The handler of this collision.
		 */
		private final CollisionHandler<C2, C1> handler;

		/**
		 * Creates a new collision handler.
		 * 
		 * @param handler
		 *            The symmetric handler for this collision.
		 */
		private InverseCollisionHandler(CollisionHandler<C2, C1> handler) {
			this.handler = handler;
		}

		/**
		 * Handles this collision by flipping the collider and collidee, making
		 * it compatible with the initial collision.
		 */
		@Override
		public void handleCollision(C1 collider, C2 collidee) {Api.hit("/src/main/java/nl/tudelft/jpacman/level/CollisionInteractionMap.java", "handleCollision", "lines 258:3 - 258:47"); handler.handleCollision(collidee, collider);}
	}

}
