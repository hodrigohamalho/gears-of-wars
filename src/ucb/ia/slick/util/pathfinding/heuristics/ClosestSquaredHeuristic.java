package ucb.ia.slick.util.pathfinding.heuristics;

import ucb.ia.slick.util.pathfinding.AStarHeuristic;
import ucb.ia.slick.util.pathfinding.Mover;
import ucb.ia.slick.util.pathfinding.TileBasedMap;

/**
 * A heuristic that uses the tile that is closest to the target
 * as the next best tile. In this case the sqrt is removed
 * and the distance squared is used instead
 * 
 * @author Kevin Glass
 * 		   Karina Macedo - kmacedovarela@gmail.com
 *         Rodrigo Ramalho - hodrigohamalho@gmail.com
 *         Marcus Vinicius - marckyn@gmail.com
 *
 */
public class ClosestSquaredHeuristic implements AStarHeuristic {

	/**
	 * @see AStarHeuristic#getCost(TileBasedMap, Mover, int, int, int, int)
	 */
	public float getCost(TileBasedMap map, Mover mover, int x, int y, int tx, int ty) {		
		float dx = tx - x;
		float dy = ty - y;
		
		return ((dx*dx)+(dy*dy));
	}

}
