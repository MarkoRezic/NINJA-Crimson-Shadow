package platformer.gamestate;

import javafx.application.Platform;
import javafx.stage.Stage;
import platformer.audio.AudioPlayer;
import platformer.audio.ClipPlayer;
import platformer.model.NinjaProfileModel;
import platformer.ninjamenu.NinjaMenuApp;
import platformer.tilemap.Background;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class GameOverState extends GameState {

    private Background bg;
    private static ClipPlayer menuHover = new ClipPlayer("/resources/SFX/menu-hover.mp3");
    private static ClipPlayer gameoverSound = new ClipPlayer("/resources/SFX/gameover.mp3");

    private int currentChoice = 0;
    private String[] options = {
            "Try Again",
            "Level Select",
            "Quit"
    };

    private Color titleColor;
    private Font titleFont;
    private Font font;

    public GameOverState(GameStateManager gsm) {

        this.gsm = gsm;
        NinjaMenuApp.gameWindow.setIconImage(new ImageIcon(getClass().getResource("/resources/images/shuriken.png")).getImage());
        gameoverSound.play();

        try {

            bg = new Background("/resources/backgrounds/gameoverbg.jpg", 1);
            bg.setVector(0, 0);

            titleColor = new Color(255, 255, 255);
            titleFont = new Font(
                    "Century Gothic",
                    Font.PLAIN,
                    28);

            font = new Font("Arial", Font.PLAIN, 12);

            NinjaProfileModel.levelAttempt(gsm.getPreviousState()-1, 1, GameStateManager.score);

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
        //g.drawString("Game Over", 80, 70);

        // draw menu options
        g.setFont(font);
        for(int i = 0; i < options.length; i++) {
            if(i == currentChoice) {
                g.setColor(Color.RED);
            }
            else {
                g.setColor(Color.WHITE);
            }
            g.drawString(options[i] + (i == currentChoice ? " ■" : ""), 175, 190 + i * 15);
        }

    }

    private void select() {
        if(currentChoice == 0) {
            // try again
            gsm.setState(gsm.getPreviousState());
        }
        if(currentChoice == 1) {
            // level select
            gsm.setState(GameStateManager.MENUSTATE);
            NinjaMenuApp.menuMusic.playLoop();
        }
        if(currentChoice == 2) {
            // quit
            JFrame gameWindow = NinjaMenuApp.gameWindow;

            Runnable showMenu = new Runnable() {
                @Override
                public void run() {
                    Stage stage = (Stage) NinjaMenuApp.root.getScene().getWindow();
                    stage.show();
                    NinjaMenuApp.menuMusic.playLoop();
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


