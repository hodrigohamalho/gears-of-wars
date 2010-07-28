package ucb.ia.pathexample;

import ucb.ia.slick.util.pathfinding.Mover;
import ucb.ia.slick.util.pathfinding.TileBasedMap;

/**
 * The data map from our example game. This holds the state and context of each tile
 * on the map. It also implements the interface required by the path finder. It's implementation
 * of the path finder related methods add specific handling for the types of units
 * and terrain in the example game.
 * 
 * @author Kevin Glass
 * 		   Karina Macedo - kmacedovarela@gmail.com
 *         Rodrigo Ramalho - hodrigohamalho@gmail.com
 *         Marcus Vinicius - marckyn@gmail.com
 *
 */
public class GameMap implements TileBasedMap {
	/** The map width in tiles */
	public static final int WIDTH = 30;
	/** The map height in tiles */
	public static final int HEIGHT = 30;
	
	/** Indicate grass terrain at a given location */
	public static final int GRASS = 0;
	/** Indicate water terrain at a given location */
	public static final int WATER = 1;
	/** Indicate trees terrain at a given location */
	public static final int TREES = 2;
	/** Indicate a plane is at a given location */
	public static final int PLANE = 3;
	public static final int PLANE_X = 20;
	public static final int PLANE_Y = 25;
	/** Indicate a boat is at a given location */
	public static final int BOAT = 4;
	public static final int BOAT_X = 2;
	public static final int BOAT_Y = 19;
	/** Indicate a tank is at a given location */
	public static final int TANK = 5;
	public static final int TANK_X = 15;
	public static final int TANK_Y = 15;
	
	
	/** Indicate the missil is at a given location */
	public static final int MISSIL = 6;
	
	/** The terrain settings for each tile in the map */
	public static int[][] terrain = new int[WIDTH][HEIGHT];
	/** The unit in each tile of the map */
	private int[][] units = new int[WIDTH][HEIGHT];
	/** Indicator if a given tile has been visited during the search */
	private boolean[][] visited = new boolean[WIDTH][HEIGHT];
	
	/**
	 * Create a new test map with some default configuration
	 */
	public GameMap() {
		// create some test data
		
		//�gua esquerda
		fillArea(0,0,2,30,WATER);
		fillArea(0,16,4,14,WATER);
		
		//�gua Baixo
		fillArea(0,28,30,2,WATER);
		fillArea(26,27,4,3,WATER);
		
		//�gua Direita
		fillArea(28,24,2,6,WATER);
		
		//�rvores Direita
		fillArea(17,5,10,3,TREES);
		fillArea(20,8,5,3,TREES);
		
		//�rvores Esquerda
		fillArea(8,17,7,3,TREES);
		fillArea(10,15,3,3,TREES);
		
		//�rvore topo linha acima
		fillArea(0,0,30,2,TREES);
		
		//�rvore topo direita
		fillArea(15,2,3,2,TREES);
		
		//�rvore topo esquerda
		fillArea(2,2,2,2,TREES);
		
		//Posi��o das unidades
		units[15][15] = TANK;
		units[2][19] = BOAT;
		units[20][25] = PLANE;
	}

	/**
	 * Fill an area with a given terrain type
	 * 
	 * @param x The x coordinate to start filling at
	 * @param y The y coordinate to start filling at
	 * @param width The width of the area to fill
	 * @param height The height of the area to fill
	 * @param type The terrain type to fill with
	 */
	private void fillArea(int x, int y, int width, int height, int type) {
		for (int xp=x;xp<x+width;xp++) {
			for (int yp=y;yp<y+height;yp++) {
				terrain[xp][yp] = type;
			}
		}
	}
	
	/**
	 * Clear the array marking which tiles have been visted by the path 
	 * finder.
	 */
	public void clearVisited() {
		for (int x=0;x<getWidthInTiles();x++) {
			for (int y=0;y<getHeightInTiles();y++) {
				visited[x][y] = false;
			}
		}
	}
	
	/**
	 * @see TileBasedMap#visited(int, int)
	 */
	public boolean visited(int x, int y) {
		return visited[x][y];
	}
	
	/**
	 * Get the terrain at a given location
	 * 
	 * @param x The x coordinate of the terrain tile to retrieve
	 * @param y The y coordinate of the terrain tile to retrieve
	 * @return The terrain tile at the given location
	 */
	public int getTerrain(int x, int y) {
		return terrain[x][y];
	}
	
	/**
	 * Get the unit at a given location
	 * 
	 * @param x The x coordinate of the tile to check for a unit
	 * @param y The y coordinate of the tile to check for a unit
	 * @return The ID of the unit at the given location or 0 if there is no unit 
	 */
	public int getUnit(int x, int y) {
		return units[x][y];
	}
	
	/**
	 * Set the unit at the given location
	 * 
	 * @param x The x coordinate of the location where the unit should be set
	 * @param y The y coordinate of the location where the unit should be set
	 * @param unit The ID of the unit to be placed on the map, or 0 to clear the unit at the
	 * given location
	 */
	public void setUnit(int x, int y, int unit) {
		units[x][y] = unit;
	}
	
	/**
	 * @see TileBasedMap#blocked(Mover, int, int)
	 */
	public boolean blocked(Mover mover, int x, int y) {
		// if theres a unit at the location, then it's blocked
		if (getUnit(x,y) != 0) {
			return true;
		}
		
		int unit = ((UnitMover) mover).getType();
		
		// planes can move anywhere
		if (unit == PLANE) {
			return false;
		}
		
		//Missil n�o ultrapassa arvores
		if (unit == MISSIL) {
			return terrain[x][y] == TREES;
		}
		
		// tanks can only move across grass
		if (unit == TANK) {
			return terrain[x][y] != GRASS;
		}
		// boats can only move across water
		if (unit == BOAT) {
			return terrain[x][y] != WATER;
		}
		
		// unknown unit so everything blocks
		return true;
	}

	/**
	 * @see TileBasedMap#getCost(Mover, int, int, int, int)
	 */
	public float getCost(Mover mover, int sx, int sy, int tx, int ty) {
		return 1;
	}

	/**
	 * @see TileBasedMap#getHeightInTiles()
	 */
	public int getHeightInTiles() {
		return WIDTH;
	}

	/**
	 * @see TileBasedMap#getWidthInTiles()
	 */
	public int getWidthInTiles() {
		return HEIGHT;
	}

	/**
	 * @see TileBasedMap#pathFinderVisited(int, int)
	 */
	public void pathFinderVisited(int x, int y) {
		visited[x][y] = true;
	}

	public int[][] getTerrain() {
		return terrain;
	}

	public void setTerrain(int[][] terrain) {
		this.terrain = terrain;
	}
	
	
}
