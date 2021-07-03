package platformer.entity;

import platformer.audio.ClipPlayer;
import platformer.tilemap.*;

import java.util.ArrayList;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Random;

public class Player extends MapObject {
	
	// player stuff
	private int health;
	private int maxHealth;
	private int shurikenCount;
	private int maxShurikens;
	private boolean dead;
	private boolean flinching;
	private long flinchTimer;
	
	// fireball
	private boolean throwing;
	private int throwCost;
	private int shurikenDamage;
	private ArrayList<Shuriken> shurikens;
	
	// scratch
	private boolean slicing;
	private int sliceDamage;
	private int sliceRange;
	
	// gliding
	private boolean gliding;
	private float glideSpeed;

	// diving
	private boolean diving;
	private float diveSpeed;
	private float maxDiveSpeed;
	
	// animations
	private ArrayList<BufferedImage[]> sprites;
	private final int[] numFrames = {
		15, 7, 5, 6, 6, 6, 6
	};
	
	// animation actions
	private static final int IDLE = 0;
	private static final int WALKING = 1;
	private static final int JUMPING = 2;
	private static final int FALLING = 3;
	private static final int GLIDING = 4;
	private static final int THROWING = 5;
	private static final int SLICING = 6;
	private static final int DIVING = 7;

	private Random r = new Random();
	
	private HashMap<String, ClipPlayer> sfx;
	
	public Player(TileMap tm) {
		
		super(tm);
		
		width = 30;
		height = 30;
		cwidth = 20;
		cheight = 30;
		
		moveSpeed = 0.5;
		maxSpeed = 2.5;
		stopSpeed = 0.8;
		fallSpeed = 0.15;
		maxFallSpeed = 4.0;
		maxDiveSpeed = 6.0f;
		jumpStart = -4.8;
		stopJumpSpeed = 0.3;
		
		facingRight = true;
		
		health = maxHealth = 5;
		shurikenCount = maxShurikens = 600;
		
		throwCost = 200;
		shurikenDamage = 5;
		shurikens = new ArrayList<Shuriken>();
		
		sliceDamage = 8;
		sliceRange = 50;

		glideSpeed = 0.3f;
		diveSpeed = 4f;
		
		// load sprites
		try {
			
			BufferedImage spritesheet = ImageIO.read(
				getClass().getResourceAsStream(
						"/resources/sprites/Player/ninja_sprite.png"
				)
			);
			
			sprites = new ArrayList<BufferedImage[]>();
			int frameW, frameH, offsetH = 0;

			for(int i = 0; i < 7; i++) {
				BufferedImage[] bi =
					new BufferedImage[numFrames[i]];
				switch (i){
					case IDLE:
					case WALKING:
						frameW = 30;
						frameH = 30;
						break;
					case JUMPING:
						frameW = 30;
						frameH = 40;
						break;
					case FALLING:
						frameW = 30;
						frameH = 60;
						break;
					case GLIDING:
						frameW = 40;
						frameH = 60;
						break;
					case THROWING:
						frameW = 45;
						frameH = 30;
						break;
					case SLICING:
						frameW = 80;
						frameH = 30;
						break;
					default:
						frameW = 30;
						frameH = 30;
				}
				
				for(int j = 0; j < numFrames[i]; j++) {

					bi[j] = spritesheet.getSubimage(
							j * frameW,
							offsetH,
							frameW,
							frameH
					);
					
				}
				
				sprites.add(bi);
				offsetH += frameH;
				
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		animation = new Animation();
		currentAction = IDLE;
		animation.setFrames(sprites.get(IDLE));
		animation.setDelay(50);
		
		sfx = new HashMap<String, ClipPlayer>();
		sfx.put("hitFlesh", new ClipPlayer("/resources/SFX/hitFlesh.mp3"));
		for(int i = 0; i<2; i++){ sfx.put("throw"+(i+1), new ClipPlayer("/resources/SFX/throw"+(i+1)+".mp3")); }
		for(int i = 0; i<3; i++){ sfx.put("slash"+(i+1), new ClipPlayer("/resources/SFX/slash"+(i+1)+".mp3")); }
		for(int i = 0; i<3; i++){ sfx.put("damage"+(i+1), new ClipPlayer("/resources/SFX/damage"+(i+1)+".mp3")); }
		for(int i = 0; i<4; i++){ sfx.put("jump"+(i+1), new ClipPlayer("/resources/SFX/jump"+(i+1)+".mp3")); }
		for(int i = 0; i<5; i++){ sfx.put("sword"+(i+1), new ClipPlayer("/resources/SFX/sword"+(i+1)+".mp3")); }

		
	}
	
	public int getHealth() { return health; }
	public int getMaxHealth() { return maxHealth; }
	public int getShurikenCount() { return shurikenCount; }
	public int getMaxShurikens() { return maxShurikens; }
	public boolean isDead() { return dead; }
	
	public void setThrowing() {
		throwing = true;
	}
	public void setSlicing() {
		slicing = true;
	}
	public void setGliding(boolean b) { 
		gliding = b;
	}
	public void setDiving(boolean b) {
		diving = b;
	}
	
	public void checkAttack(ArrayList<Enemy> enemies) {
		
		// loop through enemies
		for(int i = 0; i < enemies.size(); i++) {
			
			Enemy e = enemies.get(i);
			
			// scratch attack
			if(slicing) {
				if(facingRight) {
					if(
						e.getx() > x &&
						e.getx() < x + sliceRange &&
						e.gety() > y - height / 2 &&
						e.gety() < y + height / 2
					) {
						e.hit(sliceDamage);
						sfx.get("slash" + (r.nextInt(3) + 1)).play();
					}
				}
				else {
					if(
						e.getx() < x &&
						e.getx() > x - sliceRange &&
						e.gety() > y - height / 2 &&
						e.gety() < y + height / 2
					) {
						e.hit(sliceDamage);
						sfx.get("slash" + (r.nextInt(3) + 1)).play();
					}
				}
			}
			
			// fireballs
			for(int j = 0; j < shurikens.size(); j++) {
				if(shurikens.get(j).intersects(e)) {
					e.hit(shurikenDamage);
					shurikens.get(j).setHit();
					sfx.get("hitFlesh").play();
					break;
				}
			}
			
			// check enemy collision
			if(intersects(e)) {
				hit(e.getDamage());
			}
			
		}
		
	}
	
	public void hit(int damage) {
		if(flinching) return;
		health -= damage;
		if(health < 0) health = 0;
		if(health == 0) dead = true;
		flinching = true;
		sfx.get("damage" + (r.nextInt(3) + 1)).play();
		flinchTimer = System.nanoTime();
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
		else {
			if(dx > 0) {
				dx -= stopSpeed;
				if(dx < 0) {
					dx = 0;
				}
			}
			else if(dx < 0) {
				dx += stopSpeed;
				if(dx > 0) {
					dx = 0;
				}
			}
		}
		
		// cannot move while attacking, except in air
		/*if(
		(currentAction == SCRATCHING || currentAction == FIREBALL) &&
		!(jumping || falling)) {
			dx = 0;
		}*/
		
		// jumping
		if(jumping && !falling) {
			sfx.get("jump" + (r.nextInt(4) + 1)).play();
			dy = jumpStart;
			falling = true;
		}
		
		// falling
		if(falling) {
			
			if(dy > 0 && gliding) dy += fallSpeed * glideSpeed;
			else if(dy > 0 && diving) dy += fallSpeed * diveSpeed;
			else dy += fallSpeed;
			
			if(dy > 0) jumping = false;
			if(dy < 0 && !jumping) dy += stopJumpSpeed;

			if(diving) {
				if(dy > maxDiveSpeed) dy = maxDiveSpeed;
			}
			else if(dy > maxFallSpeed) dy = maxFallSpeed;
			
		}
		
	}
	
	public void update() {
		
		// update position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		// check attack has stopped
		if(currentAction == SLICING) {
			if(animation.hasPlayedOnce()) slicing = false;
		}
		if(currentAction == THROWING) {
			if(animation.hasPlayedOnce()) throwing = false;
		}
		
		// fireball attack
		shurikenCount += 1;
		if(shurikenCount > maxShurikens) shurikenCount = maxShurikens;
		if(throwing && currentAction != THROWING) {
			if(shurikenCount > throwCost) {
				shurikenCount -= throwCost;
				Shuriken fb = new Shuriken(tileMap, facingRight);
				fb.setPosition(x, y);
				shurikens.add(fb);
				sfx.get("throw" + (r.nextInt(2) + 1)).play();
			}
		}
		
		// update fireballs
		for(int i = 0; i < shurikens.size(); i++) {
			shurikens.get(i).update();
			if(shurikens.get(i).shouldRemove()) {
				shurikens.remove(i);
				i--;
			}
		}
		
		// check done flinching
		if(flinching) {
			long elapsed =
				(System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed > 1000) {
				flinching = false;
			}
		}
		
		// set animation
		if(slicing) {
			if(currentAction != SLICING) {
				sfx.get("sword" + (r.nextInt(5) + 1)).play();
				currentAction = SLICING;
				animation.setFrames(sprites.get(SLICING));
				animation.setDelay(20);
				width = 80;
				height = 30;
			}
		}
		else if(throwing) {
			if(currentAction != THROWING) {
				currentAction = THROWING;
				animation.setFrames(sprites.get(THROWING));
				animation.setDelay(20);
				width = 45;
				height = 30;
			}
		}
		else if(dy > 0) {
			if(gliding) {
				if(currentAction != GLIDING) {
					currentAction = GLIDING;
					animation.setFrames(sprites.get(GLIDING));
					animation.setDelay(100);
					width = 40;
					height = 80;
				}
			}
			else if(currentAction != FALLING) {
				currentAction = FALLING;
				animation.setFrames(sprites.get(FALLING));
				animation.setDelay(20);
				width = 30;
				height = 80;
			}
		}
		else if(dy < 0) {
			if(currentAction != JUMPING) {
				currentAction = JUMPING;
				animation.setFrames(sprites.get(JUMPING));
				animation.setDelay(20);
				width = 30;
				height = 40;
			}
		}
		else if(left || right) {
			if(currentAction != WALKING) {
				currentAction = WALKING;
				animation.setFrames(sprites.get(WALKING));
				animation.setDelay(40);
				width = 30;
				height = 30;
			}
		}
		else {
			if(currentAction != IDLE) {
				currentAction = IDLE;
				animation.setFrames(sprites.get(IDLE));
				animation.setDelay(50);
				width = 30;
				height = 30;
			}
		}
		
		animation.update();
		
		// set direction
		if(currentAction != SLICING && currentAction != THROWING) {
			if(right) facingRight = true;
			if(left) facingRight = false;
		}
		
	}
	
	public void draw(Graphics2D g) {
		
		setMapPosition();
		
		// draw fireballs
		for(int i = 0; i < shurikens.size(); i++) {
			shurikens.get(i).draw(g);
		}
		
		// draw player
		if(flinching) {
			long elapsed =
				(System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed / 100 % 2 == 0) {
				return;
			}
		}
		
		super.draw(g);
		
	}
	
}

















