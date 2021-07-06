package platformer.entity.Enemies;

import platformer.entity.*;
import platformer.gamepanel.GamePanel;
import platformer.tilemap.TileMap;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.util.Random;

import javax.imageio.ImageIO;

public class RedNinja extends Enemy {
	
	private BufferedImage[] sprites;
	
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
		
		health = maxHealth = 2;
		damage = 1;
		
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
	
	private void getNextPosition() {
		
		// movement
		if(left) {
			dx -= moveSpeed;
			if(dx < -maxSpeed) {
				dx = -maxSpeed;
			}
		}
		else if(right) {
			dx += moveSpeed;
			if(dx > maxSpeed) {
				dx = maxSpeed;
			}
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
			if(elapsed > 400) {
				flinching = false;
			}
		}
		
		// if it hits a wall, go other direction
		if(right && dx == 0) {
			right = false;
			left = true;
			facingRight = false;
		}
		else if(left && dx == 0) {
			right = true;
			left = false;
			facingRight = true;
		}
		
		// update animation
		animation.update();
		
	}
	
	public void draw(Graphics2D g) {
		setMapPosition();
		if(notOnScreen()) return;

		
		super.draw(g);
		
	}
	
}











