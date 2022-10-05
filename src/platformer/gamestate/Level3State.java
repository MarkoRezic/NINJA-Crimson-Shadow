package platformer.gamestate;

import platformer.audio.ClipPlayer;
import platformer.gamepanel.GamePanel;
import platformer.model.LevelModel;
import platformer.ninjamenu.NinjaMenuApp;
import platformer.tilemap.*;
import platformer.entity.*;
import platformer.entity.Enemies.*;
import platformer.audio.AudioPlayer;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Level3State extends GameState {

    private static TileMap tileMap;
    private Background bg;

    private Player player;

    private ArrayList<Enemy> enemies;
    private ArrayList<Explosion> explosions;
    private Point[] checkpoints;
    private int lastCheckpoint;

    private HUD hud;

    public static AudioPlayer bgMusic;

    private LevelModel levelInfo;

    public Level3State(GameStateManager gsm) {
        this.gsm = gsm;
        init();
    }

    private Random r = new Random();
    private HashMap<String, ClipPlayer> sfx;

    public void init() {

        levelInfo = LevelModel.getLevelInfo(3);

        GameStateManager.XP = 0;
        GameStateManager.score = 0;

        tileMap = new TileMap(30);
        tileMap.loadTiles("/resources/tilesets/ninja_tileset.png");
        tileMap.loadMap("/resources/maps/level3_map.map");
        tileMap.setPosition(0, 0);
        tileMap.setTween(0.07);

        bg = new Background("/resources/backgrounds/level3bg.jpg", 0.3);

        player = new Player(tileMap);
        player.setPosition(520, 40);

        populateEnemies();
        getCheckpoints();

        explosions = new ArrayList<Explosion>();

        hud = new HUD(player);

        sfx = new HashMap<String, ClipPlayer>();
        for(int i = 0; i<3; i++){ sfx.put("death"+(i+1), new ClipPlayer("/resources/SFX/death"+(i+1)+".mp3")); }

        bgMusic = new AudioPlayer("/resources/music/level3.mp3");
        bgMusic.mediaPlayer.setMute(NinjaMenuApp.isMusicMute);
        bgMusic.mediaPlayer.setVolume(NinjaMenuApp.masterVolume);
        bgMusic.stop();
        bgMusic.playLoop();

    }

    private void populateEnemies() {

        enemies = new ArrayList<Enemy>();

        RedNinja r;
        Point[] points = new Point[] {
                new Point(330, 0),
                new Point(300, 0),
                new Point(270, 0),
                new Point(240, 0),
                new Point(210, 0),
                new Point(180, 0),
                new Point(150, 0),
                new Point(120, 0),
                new Point(90, 0),
                new Point(60, 0),
                new Point(30, 0),
                new Point(330, 60),
                new Point(300, 60),
                new Point(270, 60),
                new Point(240, 60),
                new Point(210, 60),
                new Point(180, 60),
                new Point(150, 60),
                new Point(120, 60),
                new Point(90, 60),
                new Point(60, 60),
                new Point(30, 60),
                new Point(330, 120),
                new Point(300, 120),
                new Point(270, 120),
                new Point(240, 120),
                new Point(210, 120),
                new Point(180, 120),
                new Point(150, 120),
                new Point(120, 120),
                new Point(90, 120),
                new Point(60, 120),
                new Point(30, 120),
                new Point(240, 240),
                new Point(210, 240),
                new Point(180, 240),
                new Point(150, 240),
                new Point(120, 240),
                new Point(90, 240),
                new Point(60, 240),
                new Point(30, 240),
                new Point(690, 0),
                new Point(720, 0),
                new Point(750, 0),
                new Point(780, 0),
                new Point(810, 0),
                new Point(840, 0),
                new Point(870, 0),
                new Point(900, 0),
                new Point(930, 0),
                new Point(960, 0),
                new Point(690, 60),
                new Point(720, 60),
                new Point(750, 60),
                new Point(780, 60),
                new Point(810, 60),
                new Point(840, 60),
                new Point(870, 60),
                new Point(900, 60),
                new Point(930, 60),
                new Point(960, 270),
                new Point(780, 270),
                new Point(810, 270),
                new Point(840, 270),
                new Point(870, 270),
                new Point(900, 270),
                new Point(930, 270),
                new Point(960, 270),
                new Point(630, 0),
                new Point(630, 30),
                new Point(630, 60),
                new Point(630, 90),
                new Point(630, 150),
                new Point(630, 210),
                new Point(390, 0),
                new Point(390, 30),
                new Point(390, 60),
                new Point(390, 90),
                new Point(390, 150),
                new Point(390, 210),
        };
        for(int i = 0; i < points.length; i++) {
            r = new RedNinja(tileMap);
            r.setPosition(points[i].x, points[i].y);
            enemies.add(r);
        }

    }

    private void getCheckpoints() {

        checkpoints = new Point[] {
                new Point(520, 40),
                new Point(3050, 170)
        };
        lastCheckpoint = 0;

    }

    private void updateCheckpoints(){
        for(int i = 0; i < checkpoints.length; i++){
            if(player.getx() >= checkpoints[i].x && lastCheckpoint < i){
                lastCheckpoint = i;
            }
        }
        //win condition
        if(enemies.isEmpty()){
            bgMusic.stop();
            GameStateManager.XP += levelInfo.getBaseXP();
            GameStateManager.score += levelInfo.getBaseScore();
            gsm.setState(GameStateManager.WINSTATE);
        }

    }

    public void update() {

        if(player.isDead()){
            bgMusic.stop();
            gsm.setState(GameStateManager.GAMEOVERSTATE);
        }
        checkPlayerAbyss();
        updateCheckpoints();
        // update player
        player.update();
        tileMap.setPosition(
                GamePanel.WIDTH / 2 - player.getx(),
                GamePanel.HEIGHT / 2 - player.gety()
        );

        // set background
        bg.setPosition(tileMap.getx(), tileMap.gety());

        // attack enemies
        player.checkAttack(enemies);

        // update all enemies
        for(int i = 0; i < enemies.size(); i++) {
            Enemy e = enemies.get(i);
            e.update();
            if(e.isDead()) {
                enemies.remove(i);
                if(r.nextFloat() > 0.5) sfx.get("death" + (r.nextInt(3) + 1)).play();
                GameStateManager.XP += levelInfo.getKillXP();
                GameStateManager.score += levelInfo.getKillScore();
                i--;
                explosions.add(
                        new Explosion(e.getx(), e.gety()));
            }
        }

        // update explosions
        for(int i = 0; i < explosions.size(); i++) {
            explosions.get(i).update();
            if(explosions.get(i).shouldRemove()) {
                explosions.remove(i);
                i--;
            }
        }


    }

    public void checkPlayerAbyss(){
        if(player.gety() >= tileMap.getHeight() - player.getCHeight()){
            player.hit(1);
            player.setPosition(checkpoints[lastCheckpoint].x, checkpoints[lastCheckpoint].y);
        }
    }

    public void draw(Graphics2D g) {

        // draw bg
        bg.draw(g);

        // draw tilemap
        tileMap.draw(g);

        // draw player
        player.draw(g);

        // draw enemies
        for(int i = 0; i < enemies.size(); i++) {
            enemies.get(i).draw(g);
        }

        // draw explosions
        for(int i = 0; i < explosions.size(); i++) {
            explosions.get(i).setMapPosition(
                    (int)tileMap.getx(), (int)tileMap.gety());
            explosions.get(i).draw(g);
        }

        // draw hud
        hud.draw(g);

    }

    public void resetPlayerKeys(){
        player.setLeft(false);
        player.setRight(false);
        player.setUp(false);
        player.setDown(false);
        player.setJumping(false);
        player.setGliding(false);
    }

    public void keyPressed(int k) {
        if(k == KeyEvent.VK_A) player.setLeft(true);
        if(k == KeyEvent.VK_D) player.setRight(true);
        if(k == KeyEvent.VK_UP) player.setUp(true);
        if(k == KeyEvent.VK_DOWN) player.setDown(true);
        if(k == KeyEvent.VK_W || k == KeyEvent.VK_SPACE){
            player.setJumping(true);
            player.setGliding(true);
        }
        //if(k == KeyEvent.VK_E) player.setGliding(true);
        if(k == KeyEvent.VK_S) player.setDiving(true);
        if(k == KeyEvent.VK_F) player.setThrowing();
    }

    public void keyReleased(int k) {
        if(k == KeyEvent.VK_A) player.setLeft(false);
        if(k == KeyEvent.VK_D) player.setRight(false);
        if(k == KeyEvent.VK_UP) player.setUp(false);
        if(k == KeyEvent.VK_DOWN) player.setDown(false);
        if(k == KeyEvent.VK_W || k == KeyEvent.VK_SPACE){
            player.setJumping(false);
            player.setGliding(false);
        }
        //if(k == KeyEvent.VK_E) player.setGliding(false);
        if(k == KeyEvent.VK_S) player.setDiving(false);
    }

    public void mousePressed(int m){
        if(m == MouseEvent.BUTTON1) player.setSlicing();
        if(m == MouseEvent.BUTTON3) player.setThrowing();
    }

}












