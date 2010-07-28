package ucb.ia.fire;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;

import ucb.ia.pathexample.GameMap;
import ucb.ia.pathexample.PathTest;

/**
 * @author Vinicius Godoy de MedonÁa - http://www.pontov.com.br/site/index.php/colaboradores/54
 * 		   Karina Macedo - kmacedovarela@gmail.com
 *         Rodrigo Ramalho - hodrigohamalho@gmail.com
 *         Marcus Vinicius - marckyn@gmail.com
 *
 */
public class Ball implements Sprite {
	private static final float SPEED = 250; //Velocidade em 20 pixels / segundo
	private static final int SIZE = 10;

	//Posicao da bola em Pixels
	private float x;
	private float y;

	private float vx = SPEED;
	private float vy = SPEED / 2;

	private int screenWidth;
	private int screenHeight;

	private PathTest pathTest;

	private int tamanhoPixel = 16; // horizontal e vertical

	public Ball(int screenWidth, int screenHeight) {
		this.screenHeight = screenHeight;
		this.screenWidth = screenWidth;
	}

	/**
	 * Logica do movimento da bola.
	 */
	@Override
	public void update(long time) {
		if (x==0f || y==0f ){
			x = (PathTest.selectedx*tamanhoPixel)-1;
			y = (PathTest.selectedy*tamanhoPixel)-25;
		}else{
			//O tempo È em milis. Para obter em segundos, precisamos dividi-lo por 1000.		
			direcionarTiro(time);
		}
		checkCollision();
	}

	private void direcionarTiro(long time) {
		switch (PathTest.keyPressed) {
		case '1':
			x += (time * -vx) / 1000;
			y += (time * vy) / 1000;
			break;
		case '2':
			x += (time * vx) / 100000;
			y += (time * vy) / 1000;
			break;
		case '3':
			x += (time * vx) / 1000;
			y += (time * vy) / 1000;
			break;
		case '4':
			x += (time * -vx) / 1000;
			y += (time * vy) / 100000;
			break;
		case '6':
			x += (time * vx) / 1000;
			y += (time * vy) / 100000;
			break;
		case '7':
			x += (time * -vx) / 1000;
			y += (time * -vy) / 1000;
			break;
		case '8':
			x += (time * vx) / 100000;
			y += (time * -vy) / 1000;
			break;
		case '9':
			x += (time * vx) / 1000;
			y += (time * -vy) / 1000;
			break;
		default:
			throw new IllegalArgumentException();
		}
	}


	/**
	 * Os pontos que ocorre colis„o:
	 * No lado esquerdo da tela, quando x <= 0;
	 * No lado direito da tela, quando x, somado ao largura da bola, for maior que a largura da tela;
	 * No topo da tela, quando y <= 0;
	 * Na base da tela, quando y, somado a altura da bola, for maior que a altura da tela.
	 * 
	 * Quando a colis„o ocorrer, deve-se reposicionar a bola no interior da tela, e ent„o 
	 * inverter a velocidade em x ou y, de acordo com o local onde a bola colidiu.
	 */
	private void checkCollision()
	{
		//Testamos se a bola saiu da tela
		//Se sair, recolocamos na tela e invertemos a velocidade do eixo
		//Isso far· a bola "quicar".		
		if (x < -10) { //Lateral esquerda
			getPathTest().getLoop().stop();
		} else if ((x+SIZE) > screenWidth-120) { //Lateral direita
			getPathTest().getLoop().stop();
		}

		if (y < -50) { //topo
			getPathTest().getLoop().stop();
		} else if (((y+SIZE) > screenHeight-149)) { //baixo
			getPathTest().getLoop().stop();
		}


		verificarColisaoUnidades();

		verificarColisaoArvores();
	}

	private void verificarColisaoArvores(){
		if (GameMap.terrain[getxSquare()][getySquare()] == GameMap.TREES){
			getPathTest().getLoop().stop();
		}
	}

	private void verificarColisaoUnidades() {

		System.out.println(PathTest.posicaoPlane.getX() +"< Aviao X | Bola X > "+ getxSquare());
		System.out.println(PathTest.posicaoPlane.getY() +"< Aviao Y | Bola Y > "+ getySquare());
		if (PathTest.posicaoPlane.getX() - getxSquare() == 0 && PathTest.posicaoPlane.getY() - getySquare() == 0 && PathTest.unidadeSelecionada != GameMap.PLANE){
			getPathTest().getLoop().stop();
			System.out.println("AVIAO");
		}

		//	System.out.println(PathTest.posicaoTank.getX() +"< Tank X | Bola X > "+ getxSquare());
		//	System.out.println(PathTest.posicaoTank.getY() +"< Tank Y | Bola Y > "+ getySquare());
		if (PathTest.posicaoTank.getX() - getxSquare() == 0 &&PathTest.posicaoTank.getY() - getySquare() == 0 && PathTest.unidadeSelecionada != GameMap.TANK ){
			getPathTest().getLoop().stop();
			System.out.println("TANKE");
		}

		//System.out.println(PathTest.posicaoBoat.getX() +"< Boat X | Bola X > "+ getxSquare());
		//System.out.println(PathTest.posicaoBoat.getY() +"< Boat Y | Bola Y > "+ getySquare());
		if (PathTest.posicaoBoat.getX() - getxSquare() == 0 && PathTest.posicaoBoat.getY() - getySquare() == 0 && PathTest.unidadeSelecionada != GameMap.BOAT){
			getPathTest().getLoop().stop();
		}
	}

	/**
	 * MÈtodo de pintura da bola usando antialiasing.
	 */
	@Override
	public void draw(Graphics2D g2d) {
		Graphics2D g = (Graphics2D) g2d.create();		
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(Color.RED);
		g.fill(new Ellipse2D.Float(x, y, SIZE, SIZE));

		g.dispose();
	}

	//gETs E Sets
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public PathTest getPathTest() {
		if (pathTest == null) {
			pathTest = new PathTest();
		}

		return pathTest;
	}

	public void setPathTest(PathTest pathTest) {
		this.pathTest = pathTest;
	}


	// Gets e Sets que transformam a posição da bola de Pixels para "Quadrados" da tela
	public int getxSquare() {
		return (Float.valueOf(x/16).intValue());
	}

	public int getySquare() {
		return Float.valueOf((y)/16).intValue()+2;
	}
}
