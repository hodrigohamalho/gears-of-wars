package ucb.ia.slick.util.pathfinding.heuristics;

import ucb.ia.slick.util.pathfinding.AStarHeuristic;
import ucb.ia.slick.util.pathfinding.Mover;
import ucb.ia.slick.util.pathfinding.TileBasedMap;

/**
 * A heuristic that uses the tile that is closest to the target
 * as the next best tile.
 * 
 * @author Kevin Glass
 * 		   Karina Macedo - kmacedovarela@gmail.com
 *         Rodrigo Ramalho - hodrigohamalho@gmail.com
 *         Marcus Vinicius - marckyn@gmail.com
 *
 */
public class ClosestHeuristic implements AStarHeuristic {
	/**
	 * @see AStarHeuristic#getCost(TileBasedMap, Mover, int, int, int, int)
	 */
	public float getCost(TileBasedMap map, Mover mover, int x, int y, int tx, int ty) {		


		float dx = Math.abs(tx - x);
		float dy = Math.abs(ty - y);

		float result = (float) (Math.sqrt((dx*dx)+(dy*dy)));

		return result;
	}


	/**
	Euclidean Distance:
	The most obvious way to determine the best step is to always pick the step that is the closest to the target. This isn't always perfect for environments with a lot of obstacles or that are maze like but it does provide a simple heuristic to understand. It would look like:

	    dx = targetX - currentX;
	    dy = targetY - currentY;
	    heuristic = sqrt((dx*dx)+(dy*dy));

	Remembering that the heuristic is evaluated frequently during the path finding process we can see that this may not always be a good idea. That sqrt() call is (on most hardware) expensive.

	Manhattan Distance:
	Another common approach is to replace absolute distance with "Manhattan Distance". This is an approximation of the distance between two points based on adding the horizontal distance and vertical distances rather than computing the exact difference. That would look like this:

	    dx = abs(targetX - currentX);
	    dy = abs(targetY - currentY);
	    heuristic = dx+dy;

	 */
}
