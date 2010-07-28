package ucb.ia.pathexample;

import ucb.ia.slick.util.pathfinding.Mover;

/**
 * A mover to represent one of a ID based unit in our example
 * game map
 *
 * @author Kevin Glass
 * 		   Karina Macedo - kmacedovarela@gmail.com
 *         Rodrigo Ramalho - hodrigohamalho@gmail.com
 *         Marcus Vinicius - marckyn@gmail.com
 *
 */
public class UnitMover implements Mover {
	/** The unit ID moving */
	private int type;
	
	/**
	 * Create a new mover to be used while path finder
	 * 
	 * @param type The ID of the unit moving
	 */
	public UnitMover(int type) {
		this.type = type;
	}
	
	/**
	 * Get the ID of the unit moving
	 * 
	 * @return The ID of the unit moving
	 */
	public int getType() {
		return type;
	}
}
