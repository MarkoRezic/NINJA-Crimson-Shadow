package platformer.entity.Enemies;

import platformer.entity.*;
import platformer.gamepanel.GamePanel;
import platformer.tilemap.TileMap;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.imageio.ImageIO;

public class RedNinja extends Enemy {
	
	private BufferedImage[] sprites;

	private int hitDirection;
	private float slowSpeed;
	
	public RedNinja(TileMap tm) {
		
		super(tm);

		Random r = new Random();
		
		moveSpeed = 0.5;
		maxSpeed = 0.5;
		fallSpeed = 0.2;
		maxFallSpeed = 10.0;
		
		width = 30;
		height = 30;
		cwidth = 20;
		cheight = 30;
		
		health = maxHealth = 20;
		damage = 1;

		hitDirection = 0;
		slowSpeed = 0.1f;
		
		// load sprites
		try {
			
			BufferedImage spritesheet = ImageIO.read(
				getClass().getResourceAsStream(
					"/resources/sprites/Enemies/enemy.png"
				)
			);
			
			sprites = new BufferedImage[5];
			for(int i = 0; i < sprites.length; i++) {
				sprites[i] = spritesheet.getSubimage(
					i * width,
					0,
					width,
					height
				);
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(100);

		if(r.nextFloat() > 0.5) {
			right = true;
			facingRight = true;
		}
		else {
			left = true;
			facingRight = false;
		}
		
	}

	public void setHitDirection(int direction){

		hitDirection = direction;
		dx = 2.5 * direction;

	}
	
	private void getNextPosition() {
		
		// movement
		if(!flinching) {
			if (left) {
				dx -= moveSpeed;
				if (dx < -maxSpeed) {
					dx = -maxSpeed;
				}
			} else if (right) {
				dx += moveSpeed;
				if (dx > maxSpeed) {
					dx = maxSpeed;
				}
			}
		}
		else{
			dx = dx - hitDirection * slowSpeed;
			if(dx >= -slowSpeed && dx <= slowSpeed) dx = 0;
		}
		
		// falling
		if(falling) {
			dy += fallSpeed;
		}
		
	}
	
	public void update() {
		
		// update position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		// check flinching
		if(flinching) {
			long elapsed =
				(System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed > 600) {
				flinching = false;
			}
		}
		else {
			// if it hits a wall, go other direction
			if (right && dx == 0) {
				right = false;
				left = true;
				facingRight = false;
			} else if (left && dx == 0) {
				right = true;
				left = false;
				facingRight = true;
			}
		}
		
		// update animation
		animation.update();
		
	}
	
	public void draw(Graphics2D g) {
		setMapPosition();
		if(notOnScreen()) return;

		if(flinching) {
			long elapsed =
					(System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed / 100 % 3 == 0) {
				return;
			}
		}
		
		super.draw(g);
		g.setColor(new Color(255 - (int)(((float)health/maxHealth)*255), (int)(((float)health/maxHealth)*255), 0));
		g.fillRect((int)(x + xmap - width / 2),
				(int)(y + ymap - height / 2 - 4),
				(int)(((float)health/maxHealth)*30),
				3);
	}
	
}











