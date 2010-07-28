package ucb.ia.slick.util.pathfinding.heuristics;

import ucb.ia.slick.util.pathfinding.AStarHeuristic;
import ucb.ia.slick.util.pathfinding.Mover;
import ucb.ia.slick.util.pathfinding.TileBasedMap;

/**
 * A heuristic that drives the search based on the Manhattan distance
 * between the current location and the target
 * 
 * @author Kevin Glass
 * 		   Karina Macedo - kmacedovarela@gmail.com
 *         Rodrigo Ramalho - hodrigohamalho@gmail.com
 *         Marcus Vinicius - marckyn@gmail.com
 *
 */
public class ManhattanHeuristic implements AStarHeuristic {
	/** The minimum movement cost from any one square to the next */
	private int minimumCost;
	
	/**
	 * Create a new heuristic 
	 * 
	 * @param minimumCost The minimum movement cost from any one square to the next
	 */
	public ManhattanHeuristic(int minimumCost) {
		this.minimumCost = minimumCost;
	}
	
	/**
	 * @see AStarHeuristic#getCost(TileBasedMap, Mover, int, int, int, int)
	 */
	public float getCost(TileBasedMap map, Mover mover, int x, int y, int tx,
			int ty) {
		return minimumCost * (Math.abs(x-tx) + Math.abs(y-ty));
	}

}
