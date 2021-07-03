package platformer.gamepanel;

import javafx.scene.layout.Pane;
import platformer.gamestate.GameStateManager;
import platformer.ninjamenu.NinjaMenuApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel
	implements Runnable, KeyListener, MouseListener {
	
	// dimensions
	public static final int WIDTH = 420;
	public static final int HEIGHT = 240;
	public static float SCALEW = 2;
	public static float SCALEH = 2;
	public static Dimension screenSize;
	
	// game thread
	private Thread thread;
	private boolean running;
	private int FPS = 60;
	private long targetTime = 1000 / FPS;
	
	// image
	private BufferedImage image;
	private Graphics2D g;
	
	// game state manager
	private GameStateManager gsm;
	
	public GamePanel() {
		super();
		if(NinjaMenuApp.isFullScreen == true) {
			screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			SCALEW = (float) screenSize.width / WIDTH;
			SCALEH = (float) screenSize.height / HEIGHT;
		}
		else{
			SCALEW = 2;
			SCALEH = 2;
		}
		setPreferredSize(
			new Dimension((int)(WIDTH * SCALEW), (int)(HEIGHT * SCALEH)));
		setFocusable(true);
		requestFocus();

	}
	
	public void addNotify() {
		super.addNotify();
		if(thread == null) {
			thread = new Thread(this);
			addKeyListener(this);
			addMouseListener(this);
			thread.start();
		}
	}
	
	private void init() {
		
		image = new BufferedImage(
					WIDTH, HEIGHT,
					BufferedImage.TYPE_INT_RGB
				);
		g = (Graphics2D) image.getGraphics();
		running = true;
		
		gsm = new GameStateManager();
		
	}
	
	public void run() {
		
		init();
		
		long start;
		long elapsed;
		long wait;
		
		// game loop
		while(running) {
			
			start = System.nanoTime();
			
			update();
			draw();
			drawToScreen();
			
			elapsed = System.nanoTime() - start;
			
			wait = targetTime - elapsed / 1000000;
			if(wait < 0) wait = 5;
			
			try {
				Thread.sleep(wait);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	private void update() {
		gsm.update();
	}
	private void draw() {
		gsm.draw(g);
	}
	private void drawToScreen() {
		Graphics g2 = getGraphics();
		try {
			g2.drawImage(image, 0, 0,
					(int) (WIDTH * SCALEW), (int) (HEIGHT * SCALEH),
					null);
			g2.dispose();
		}
		catch (Exception e){
			//e.printStackTrace();
			running = false;
		}
	}
	
	public void keyTyped(KeyEvent key) {}
	public void keyPressed(KeyEvent key) {
		gsm.keyPressed(key.getKeyCode());
	}
	public void keyReleased(KeyEvent key) {
		gsm.keyReleased(key.getKeyCode());
	}
	public void mouseClicked(MouseEvent mouseEvent){

	}
	public void mousePressed(MouseEvent mouseEvent){
		gsm.mousePressed(mouseEvent.getButton());
	}
	public void mouseReleased(MouseEvent mouseEvent){

	}
	public void mouseEntered(MouseEvent mouseEvent){

	}
	public void mouseExited(MouseEvent mouseEvent){

	}
}
















