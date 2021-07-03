package platformer.tilemap;

import platformer.gamepanel.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Background {
	
	private BufferedImage image;
	private int diff;
	
	private double x;
	private double y;
	private double dx;
	private double dy;
	
	private double moveScale;
	
	public Background(String s, double ms) {
		
		try {
			image = ImageIO.read(
				getClass().getResourceAsStream(s)
			);
			moveScale = ms;
			diff = GamePanel.WIDTH - (GamePanel.WIDTH/image.getWidth())*image.getWidth();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void setPosition(double x, double y) {
		this.x = (x * moveScale) % image.getWidth();
		this.y = (y * moveScale) % image.getHeight();
	}
	
	public void setVector(double dx, double dy) {
		this.dx = dx;
		this.dy = dy;
	}
	
	public void update() {
		x += dx;
		y += dy;
	}
	
	public void draw(Graphics2D g) {
		
		g.drawImage(image, (int)x, (int)y, null);

		if(x <= 0) {
			g.drawImage(
				image,
				(int)x + image.getWidth(),
				(int)y,
				null
			);
		}
		if(x > 0) {
			g.drawImage(
				image,
				(int)x - image.getWidth(),
				(int)y,
				null
			);
		}
		if(x <= -image.getWidth() + diff) {
			g.drawImage(
					image,
					(int)x + 2*image.getWidth(),
					(int)y,
					null
			);
		}
		if(x > GamePanel.WIDTH - diff) {
			g.drawImage(
					image,
					(int)x - 2*image.getWidth(),
					(int)y,
					null
			);
		}

	}
	
}







