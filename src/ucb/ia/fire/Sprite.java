package ucb.ia.fire;

import java.awt.Graphics2D;

/**
 * @author Vinicius Godoy de Medonça - http://www.pontov.com.br/site/index.php/colaboradores/54
 */
public interface Sprite {
	void update(long time);
	void draw(Graphics2D g2d);
}
