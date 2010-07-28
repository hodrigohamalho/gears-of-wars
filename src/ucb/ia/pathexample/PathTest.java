package ucb.ia.pathexample;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import ucb.ia.fire.Ball;
import ucb.ia.fire.LoopSteps;
import ucb.ia.fire.MainLoop;
import ucb.ia.slick.util.pathfinding.AStarPathFinder;
import ucb.ia.slick.util.pathfinding.Path;
import ucb.ia.slick.util.pathfinding.PathFinder;

/**
 * A simple test to show some path finding at unit
 * movement for a tutorial at http://www.cokeandcode.com
 * 
 * @author Kevin Glass
 * 		   Karina Macedo - kmacedovarela@gmail.com
 *         Rodrigo Ramalho - hodrigohamalho@gmail.com
 *         Marcus Vinicius - marckyn@gmail.com
 *
 */
public class PathTest extends JFrame implements LoopSteps{
	private static final long serialVersionUID = 7845345320230840298L;
	
	/** The map on which the units will move */
	private GameMap map = new GameMap();
	/** The path finder we'll use to search our map */
	private PathFinder finder;
	/** The last path found for the current unit */
	private Path path;

	/** The list of tile images to render the map */
	private Image[] tiles = new Image[7];
	/** The offscreen buffer used for rendering in the wonder world of Java 2D */
	private Image buffer;

	/** The x coordinate of selected unit or -1 if none is selected */
	public static Posicao posicaoTank = new Posicao(GameMap.TANK_X, GameMap.TANK_Y); 
	public static Posicao posicaoPlane = new Posicao(GameMap.PLANE_X, GameMap.PLANE_Y); 
	public static Posicao posicaoBoat = new Posicao(GameMap.BOAT_X, GameMap.BOAT_Y);

	public static int selectedx = -1;
	/** The y coordinate of selected unit or -1 if none is selected */
	public static int selectedy = -1;

	/** The x coordinate of the target of the last path we searched for - used to cache and prevent constantly re-searching */
	private int lastFindX = -1;
	/** The y coordinate of the target of the last path we searched for - used to cache and prevent constantly re-searching */
	private int lastFindY = -1;

	private MainLoop loop = new MainLoop(this, 60);

	private long previous = System.currentTimeMillis();
	private Ball ball;

	private boolean move;
	
	public static int unidadeSelecionada = -1;

	//Tecla pressionada que representa a direção do tiro
	public static Character keyPressed; 

	public PathTest() {
		super("G e a r s   o f    W a r ");
	}

	public void createGame(){
		try {
			tiles[GameMap.TREES] = ImageIO.read(getResource("res/trees.png"));
			tiles[GameMap.GRASS] = ImageIO.read(getResource("res/grass.png"));
			tiles[GameMap.WATER] = ImageIO.read(getResource("res/water.png"));
			tiles[GameMap.TANK] = ImageIO.read(getResource("res/tank.png"));
			tiles[GameMap.PLANE] = ImageIO.read(getResource("res/plane.png"));
			tiles[GameMap.BOAT] = ImageIO.read(getResource("res/boat.png"));

		} catch (IOException e) {
			System.err.println("Failed to load resources: "+e.getMessage());
			System.exit(0);
		}

		finder = new AStarPathFinder(map, 500, true);

		eventos();

		inicializarPosicoes();

		setSize(600,600);
		setResizable(false);
		setVisible(true);
	}

	private void inicializarPosicoes(){

	}

	private void eventos() {
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				handleMousePressed(e.getX(), e.getY());

				try {
					unidadeSelecionada =  new UnitMover(map.getUnit(selectedx, selectedy)).getType();
					
					
					if (unidadeSelecionada >= 0){
						switch (unidadeSelecionada) {
						case GameMap.BOAT:
							posicaoBoat.setX(selectedx);
							posicaoBoat.setY(selectedy);
							break;
						case GameMap.TANK:
							posicaoTank.setX(selectedx);
							posicaoTank.setY(selectedy);
							break;
						case GameMap.PLANE:
							posicaoPlane.setX(selectedx);
							posicaoPlane.setY(selectedy);
							break;
						default:
							break;
						}

					}
				} catch (Exception e2) {
					//Ocorre Quando Ž feito um click em qualqr local que n‹o tenha uma unidade
					System.out.println("N‹o foi selecionada nenhuma unidade.");
				}
			}
		});

		addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				keyPressed = e.getKeyChar();

				startMainLoop();
			}
		});

		addMouseMotionListener(new MouseMotionListener() {
			public void mouseDragged(MouseEvent e) {
			}

			public void mouseMoved(MouseEvent e) {
				handleMouseMoved(e.getX(), e.getY());
			}
		});
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}

	/**
	 * Load a resource based on a file reference
	 * 
	 * @param ref The reference to the file to load
	 * @return The stream loaded from either the classpath or file system
	 * @throws IOException Indicates a failure to read the resource
	 */
	private InputStream getResource(String ref) throws IOException {
		InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(ref);
		if (in != null) {
			return in;
		}

		return new FileInputStream(ref);
	}

	/**
	 * Handle the mouse being moved. In this case we want to find a path from the
	 * selected unit to the position the mouse is at
	 * 
	 * @param x The x coordinate of the mouse cursor on the screen
	 * @param y The y coordinate of the mouse cursor on the screen
	 */
	private void handleMouseMoved(int x, int y) {
		x -= 50;
		y -= 50;
		x /= 16;
		y /= 16;

		if ((x < 0) || (y < 0) || (x >= map.getWidthInTiles()) || (y >= map.getHeightInTiles())) {
			return;
		}

		if (selectedx != -1) {
			if ((lastFindX != x) || (lastFindY != y)) {
				lastFindX = x;
				lastFindY = y;
				path = finder.findPath(new UnitMover(map.getUnit(selectedx, selectedy)), 
						selectedx, selectedy, x, y);
				repaint(0);
			}
		}
	}
	/**
	 * Handle the mouse being pressed. If the mouse is over a unit select it. Otherwise we move
	 * the selected unit to the new target (assuming there was a path found)
	 * 
	 * @param x The x coordinate of the mouse cursor on the screen
	 * @param y The y coordinate of the mouse cursor on the screen
	 */
	private void handleMousePressed(int x, int y) {
		x -= 50;
		y -= 50;
		x /= 16;
		y /= 16;

		if ((x < 0) || (y < 0) || (x >= map.getWidthInTiles()) || (y >= map.getHeightInTiles())) {
			return;
		}

		if (map.getUnit(x, y) != 0) {
			selectedx = x;
			selectedy = y;
			lastFindX = - 1;
		} else {
			if (selectedx != -1) {
				map.clearVisited();
				path = finder.findPath(new UnitMover(map.getUnit(selectedx, selectedy)), 
						selectedx, selectedy, x, y);

				if (path != null) {
					int unit = map.getUnit(selectedx, selectedy);
					map.setUnit(selectedx, selectedy, 0);
					map.setUnit(x,y,unit);
					selectedx = x;
					selectedy = y;
					lastFindX = - 1;
					move = true;
				}
			}
		}

		repaint(0);
	}


	/**
	 * @see java.awt.Container#paint(java.awt.Graphics)
	 */
	public void paint(Graphics graphics) {	
		// create an offscreen buffer to render the map

		System.out.println();

		if (buffer == null) {
			buffer = new BufferedImage(600, 600, BufferedImage.TYPE_INT_ARGB);			
		}
		Graphics g = buffer.getGraphics();

		g.clearRect(0,0,600,600);
		g.translate(50, 50);

		// cycle through the tiles in the map drawing the appropriate
		// image for the terrain and units where appropriate
		for (int x=0;x<map.getWidthInTiles();x++) {
			for (int y=0;y<map.getHeightInTiles();y++) {
				g.drawImage(tiles[map.getTerrain(x, y)],x*16,y*16,null);
				if (map.getUnit(x, y) != 0) {
					g.drawImage(tiles[map.getUnit(x, y)],x*16,y*16,null);
				} else {
					if (path != null) {
						if (path.contains(x, y)) {
							g.setColor(Color.BLUE);
							g.fillRect((x*16)+4, (y*16)+4,7,7);
						}
					}	
				}
			}
		}

		/* Movimentação dos Objetos */
		if (move){
			for (int i=0; i<path.getLength(); i++){
				try {
					g.drawImage(tiles[map.getUnit(selectedx,selectedy)],path.getX(i)*16,path.getY(i)*16,null);
					graphics.drawImage(buffer, 0, 0, null);
					Thread.sleep(100);
				} catch (InterruptedException e) {
					System.out.println("Método Paint! " +e.getMessage());
					e.printStackTrace();
				}
			}

			move = false;
		}

		// if a unit is selected then draw a box around it
		if (selectedx != -1) {
			g.setColor(Color.black);
			g.drawRect(selectedx*16, selectedy*16, 15, 15);
			g.drawRect((selectedx*16)-2, (selectedy*16)-2, 19, 19);
			g.setColor(Color.white);
			g.drawRect((selectedx*16)-1, (selectedy*16)-1, 17, 17);


		}

		// Logica do TIRO
		g = g.create(getInsets().right, 
				getInsets().top, 
				getWidth() - getInsets().left, 
				getHeight() - getInsets().bottom);

		if (ball != null){
			ball.draw((Graphics2D) g); //Desenhamos a bola	
		}	

		g.dispose(); //Liberamos o contexto criado.

		// finally draw the buffer to the real graphics context in one
		// atomic action
		graphics.drawImage(buffer, 0, 0, null);
	}

	/**
	 * Entry point to our simple test game
	 * 
	 * @param argv The arguments passed into the game
	 */
	public static void main(String[] argv) {
		new PathTest().createGame();	
	}

	public void startMainLoop()
	{
		//Iniciamos o main loop
		new Thread(loop, "Main loop").start();
	}

	/**
	 * Cria uma nova bola, e fornece a ela o tamanho da área de pintura. 
	 * Esse tamanho é igual ao tamanho do frame, descontado a largura da 
	 * decoração de sua lateral (insets). 
	 */
	@Override
	public void setup() {
		//Subtrai a decoração da janela da largura e altura máximas
		//percorridas pela bola.
		ball = new Ball(getWidth() , 
				getHeight() );

	}

	/**
	 * Cálculo de quanto tempo transcorreu entre duas chamadas ao update. 
	 * Na criação do JFrame foi denifido um atributo chamado time. 
	 * Ele será usado para representar o momento em que o método update() foi chamado pela ultima vez.
	 */
	@Override
	public void processLogics() {
		//Calcula o tempo entre dois updates
		long time = System.currentTimeMillis() - previous;

		//Chama o update dos sprites, no caso, só a bola
		ball.update(time);		

		//Grava o tempo na saída do método
		previous = System.currentTimeMillis();
	}

	@Override
	public void renderGraphics() {        
	}

	@Override
	public void paintScreen() {
		repaint();
	}

	/**
	 * Método responsável pela pintura de todos sprites.
	 */
	@Override
	public void tearDown() {
		//Não é realmente necessário, pois o jogo acaba.
		//Mas se fosse um fim de fase, seria.
		ball = null;
	}

	public MainLoop getLoop() {
		return loop;
	}

	public void setLoop(MainLoop loop) {
		this.loop = loop;
	}
}
