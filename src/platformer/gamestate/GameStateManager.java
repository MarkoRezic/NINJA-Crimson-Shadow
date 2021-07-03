package platformer.gamestate;

import platformer.audio.ClipPlayer;
import platformer.ninjamenu.NinjaMenuApp;

import java.awt.event.KeyEvent;

public class GameStateManager {

	private GameState[] gameStates;
	private int currentState;
	private int previousState;
	private static ClipPlayer menuHover = new ClipPlayer("/resources/SFX/menu-hover.mp3");

	public static final int NUMGAMESTATES = 7;
	public static final int MENUSTATE = 0;
	public static final int PAUSESTATE = 1;
	public static final int LEVEL1STATE = 2;
	public static final int LEVEL2STATE = 3;
	public static final int LEVEL3STATE = 4;
	public static final int GAMEOVERSTATE = 5;
	public static final int WINSTATE = 6;

	public static int XP;
	public static int score;

	public GameStateManager() {

		XP = 0;
		score = 0;
		gameStates = new GameState[NUMGAMESTATES];

		currentState = MENUSTATE;
		loadState(currentState);

	}

	private void loadState(int state) {
		if(state == MENUSTATE)
			gameStates[state] = new MenuState(this);
		else if(state == LEVEL1STATE)
			gameStates[state] = new Level1State(this);
		else if(state == LEVEL2STATE)
			gameStates[state] = new Level2State(this);
		else if(state == LEVEL3STATE)
			gameStates[state] = new Level3State(this);
		else if(state == PAUSESTATE)
			gameStates[state] = new PauseState(this);
		else if(state == GAMEOVERSTATE)
			gameStates[state] = new GameOverState(this);
		else if(state == WINSTATE)
			gameStates[state] = new WinState(this);
	}

	private void unloadState(int state) {
		gameStates[state] = null;
	}

	public void setState(int state) {
		unloadState(currentState);
		previousState = currentState;
		currentState = state;
		loadState(currentState);
		//gameStates[currentState].init();
	}

	public int getCurrentState(){ return currentState; }
	public int getPreviousState(){ return previousState; }

	public void update() {
		try {
			gameStates[currentState].update();
		} catch(Exception e) {}
	}

	public void draw(java.awt.Graphics2D g) {
		try {
			gameStates[currentState].draw(g);
		} catch(Exception e) {}
	}

	public void pauseState(){
		gameStates[currentState].resetPlayerKeys();
		if(currentState == LEVEL1STATE) Level1State.bgMusic.mediaPlayer.pause();
		if(currentState == LEVEL2STATE) Level2State.bgMusic.mediaPlayer.pause();
		if(currentState == LEVEL3STATE) Level3State.bgMusic.mediaPlayer.pause();

		previousState = currentState;
		currentState = PAUSESTATE;
		loadState(currentState);
		menuHover.play();
	}

	public void unPauseState(){
		gameStates[currentState].resetPlayerKeys();
		if(previousState == LEVEL1STATE) Level1State.bgMusic.mediaPlayer.play();
		if(previousState == LEVEL2STATE) Level2State.bgMusic.mediaPlayer.play();
		if(previousState == LEVEL3STATE) Level3State.bgMusic.mediaPlayer.play();

		currentState = previousState;
		unloadState(PAUSESTATE);
		menuHover.play();
	}

	public void keyPressed(int k) {
		gameStates[currentState].keyPressed(k);
	}

	public void keyReleased(int k) {

		gameStates[currentState].keyReleased(k);

		if(k == KeyEvent.VK_ESCAPE && currentState > PAUSESTATE) {
			pauseState();
		}
		else if(k == KeyEvent.VK_ESCAPE && currentState == PAUSESTATE) {
			unPauseState();
		}
	}

	public void mousePressed(int m){
		gameStates[currentState].mousePressed(m);
	}

}