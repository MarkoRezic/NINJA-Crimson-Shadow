package platformer.gamestate;

import javafx.application.Platform;
import javafx.stage.Stage;
import platformer.audio.AudioPlayer;
import platformer.audio.ClipPlayer;
import platformer.ninjamenu.NinjaMenuApp;
import platformer.tilemap.Background;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class MenuState extends GameState {
	
	private Background bg;
	private static ClipPlayer menuHover = new ClipPlayer("/resources/SFX/menu-hover.mp3");
	
	private int currentChoice = 0;
	private String[] options = {
		"Level 1",
		"Level 2",
		"Level 3",
		"Quit"
	};
	
	private Color titleColor;
	private Font titleFont;
	private Font font;
	
	public MenuState(GameStateManager gsm) {
		
		this.gsm = gsm;
		NinjaMenuApp.gameWindow.setIconImage(new ImageIcon(getClass().getResource("/resources/images/shuriken.png")).getImage());
		
		try {
			
			bg = new Background("/resources/backgrounds/menubg.png", 1);
			bg.setVector(-0.1, 0);
			
			titleColor = new Color(255, 255, 255);
			titleFont = new Font(
					"Century Gothic",
					Font.PLAIN,
					28);
			
			font = new Font("Arial", Font.PLAIN, 12);
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void init() {}
	
	public void update() {
		bg.update();
	}
	
	public void draw(Graphics2D g) {
		
		// draw bg
		bg.draw(g);
		
		// draw title
		g.setColor(titleColor);
		g.setFont(titleFont);
		g.drawString("Ninja Menu", 80, 70);
		
		// draw menu options
		g.setFont(font);
		for(int i = 0; i < options.length; i++) {
			if(i == currentChoice) {
				g.setColor(Color.RED);
			}
			else {
				g.setColor(Color.WHITE);
			}
			g.drawString(options[i] + (i == currentChoice ? " ■" : ""), 145, 140 + i * 15);
		}
		
	}
	
	private void select() {
		if(currentChoice == 0) {
			// level 1
			gsm.setState(GameStateManager.LEVEL1STATE);
			NinjaMenuApp.menuMusic.stop();
		}
		if(currentChoice == 1) {
			// level 2
			gsm.setState(GameStateManager.LEVEL2STATE);
			NinjaMenuApp.menuMusic.stop();
		}
		if(currentChoice == 2) {
			// level 3
			gsm.setState(GameStateManager.LEVEL3STATE);
			NinjaMenuApp.menuMusic.stop();
		}
		if(currentChoice == 3) {
			// quit
			JFrame gameWindow = NinjaMenuApp.gameWindow;

			Runnable showMenu = new Runnable() {
				@Override
				public void run() {
					Stage stage = (Stage) NinjaMenuApp.root.getScene().getWindow();
					stage.show();
				}
			};
			Platform.runLater(showMenu);
			gameWindow.dispose();
		}
	}

	public void resetPlayerKeys(){}

	public void keyPressed(int k) {
		if(k == KeyEvent.VK_ENTER){
			menuHover.play();
			select();
		}
		if(k == KeyEvent.VK_UP) {
			menuHover.play();
			currentChoice--;
			if(currentChoice == -1) {
				currentChoice = options.length - 1;
			}
		}
		if(k == KeyEvent.VK_DOWN) {
			menuHover.play();
			currentChoice++;
			if(currentChoice == options.length) {
				currentChoice = 0;
			}
		}
	}
	public void keyReleased(int k) {}

	public void mousePressed(int m){}
}










