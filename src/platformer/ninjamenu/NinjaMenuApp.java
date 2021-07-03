package platformer.ninjamenu;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.Slider;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;
import platformer.audio.AudioPlayer;
import platformer.audio.ClipPlayer;
import platformer.gamepanel.GamePanel;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class NinjaMenuApp {

    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;

    public static Pane root = new Pane();
    public static GamePanel gamePanel;
    public static JFrame gameWindow;
    public static VBox menuBox = new VBox(-9);
    public static VBox settingsBox = new VBox(-9);
    public static VBox tutorialBox = new VBox(-9);
    public static VBox highscoresBox = new VBox(-9);
    public static VBox profileBox = new VBox(-9);
    public static NinjaMenuHSList leaderboard;
    public static NinjaMenuProfile profileInfo;
    public static Line line;
    static double lineX = 50;
    static double lineY = HEIGHT - 380;
    public static boolean isFullScreen = false;
    public static boolean isMusicMute = true;
    public static boolean isSFXMute = true;
    public static float masterVolume = 1.0f;
    public static NinjaMenuSlider volumeSlider = new NinjaMenuSlider("Volume", 0, 100, 100, "volume");
    public static AudioPlayer menuMusic = new AudioPlayer("/resources/music/Ninja_Menu.mp3");

    public static ImageView menubg = new ImageView(new Image("/resources/images/ninja_bg.png"));
    public static ImageView tutorialbg = new ImageView(new Image("/resources/images/tutorial_bg.png"));
    public static ImageView highscoresbg = new ImageView(new Image("/resources/images/highscores_bg.png"));
    public static ImageView profilebg = new ImageView(new Image("/resources/images/profile_bg.jpg"));

    public static WindowListener exitListener = new WindowAdapter() {

        @Override
        public void windowClosing(WindowEvent e) {

            gameWindow.dispose();
            Runnable showMenu = new Runnable() {
                @Override
                public void run() {
                    Stage stage = (Stage) root.getScene().getWindow();
                    stage.show();
                    menuMusic.play();
                }
            };
            Platform.runLater(showMenu);
        }
    };

    public static List<Pair<String, Runnable>> menuData = Arrays.asList(
            new Pair<String, Runnable>("New Game", () -> {
                Stage stage = (Stage) root.getScene().getWindow();

                gamePanel = new GamePanel();
                gameWindow = new JFrame("Ninja Game");
                gameWindow.setContentPane(gamePanel);
                gameWindow.addWindowListener(exitListener);

                gameWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                gameWindow.setResizable(false);
                if(isFullScreen) {
                    gameWindow.setExtendedState(JFrame.MAXIMIZED_BOTH);
                    gameWindow.setUndecorated(true);
                }
                else{
                    gameWindow.setExtendedState(JFrame.NORMAL);
                    gameWindow.setUndecorated(false);
                }
                gameWindow.pack();
                gameWindow.setLocationRelativeTo(null);
                gameWindow.setVisible(true);
                stage.hide();
            }),
            new Pair<String, Runnable>("Tutorial", () -> {
                addTutorial(lineX + 5, lineY + 295);
                startAnimation(0, 0.5, 0.1);
            }),
            new Pair<String, Runnable>("My Profile", () -> {
                addProfile(lineX + 5, lineY + 295);
                startAnimation(0, 0.5, 0.1);
            }),
            new Pair<String, Runnable>("Highscores", () -> {
                addHighscores(lineX + 5, lineY + 295);
                startAnimation(0, 0.5, 0.1);
            }),
            new Pair<String, Runnable>("Settings", () -> {
                addSettings(lineX + 5, lineY + 5);
                startAnimation(0, 0.5, 0.1);
            }),
            new Pair<String, Runnable>("Exit to Desktop", ()-> {
                Platform.exit();
            })
    );

    public static List<Pair<String, Runnable>> settingsData = Arrays.asList(
            new Pair<String, Runnable>("FullScreen", () -> {
                toggleSettingFullscreen();
                Stage stage = (Stage) root.getScene().getWindow();
                stage.setFullScreen(isFullScreen);
            }),
            new Pair<String, Runnable>("Music", () -> {
                toggleSettingMusic();
            }),
            new Pair<String, Runnable>("SFX", () -> {
                toggleSettingSFX();
            }),
            new Pair<String, Runnable>("Back", ()-> {
                addMenu(lineX + 5, lineY + 5);
                startAnimation(0, 0.5, 0.1);
            })
    );

    public static List<Pair<String, Runnable>> tutorialData = Arrays.asList(
            new Pair<String, Runnable>("Back", ()-> {
                addMenu(lineX + 5, lineY + 5);
                startAnimation(0, 0.5, 0.1);
            })
    );

    public static List<Pair<String, Runnable>> highscoresData = Arrays.asList(
            new Pair<String, Runnable>("Back", ()-> {
                addMenu(lineX + 5, lineY + 5);
                startAnimation(0, 0.5, 0.1);
            })
    );

    public static List<Pair<String, Runnable>> profileData = Arrays.asList(
            new Pair<String, Runnable>("Back", ()-> {
                addMenu(lineX + 5, lineY + 5);
                startAnimation(0, 0.5, 0.1);
            })
    );

    public Pane createContent() {
        Platform.setImplicitExit(false);
        addBackground();
        addTitle();


        addLine(lineX, lineY);
        addSettings(lineX + 5, lineY + 5);
        toggleSettingMusic();
        toggleSettingSFX();
        toggleSettingFullscreen();
        addMenu(lineX + 5, lineY + 5);

        startAnimation(1, 1, 0.15);

        menuMusic.playLoop();

        return root;
    }

    public static void addBackground() {
        //ImageView imageView = new ImageView(new Image(getClass().getResource("/resources/images/ninja_bg.png").toExternalForm()));
        menubg.setFitWidth(WIDTH);
        menubg.setFitHeight(HEIGHT);

        root.getChildren().add(menubg);
    }

    public static void addTitle() {
        NinjaTitle title = new NinjaTitle("NINJA");
        NinjaSubtitle subtitle = new NinjaSubtitle("Crimson Shadow");

        title.setTranslateX(WIDTH / 2 - title.getTitleWidth() / 2);
        title.setTranslateY(HEIGHT / 4);
        subtitle.setTranslateX(WIDTH / 2 - subtitle.getTitleWidth() / 2);
        subtitle.setTranslateY(HEIGHT / 3);

        root.getChildren().add(title);
        root.getChildren().add(subtitle);
    }

    public static void addLine(double x, double y) {
        line = new Line(x, y, x, y + 350);
        line.setStrokeWidth(3);
        line.setStroke(Color.color(1, 1, 1, 0.75));
        line.setEffect(new DropShadow(5, Color.BLACK));
        line.setScaleY(0);

        root.getChildren().add(line);
    }

    public static void startAnimation(double lineDelay, double itemSpeed, double diff) {
        ScaleTransition st = new ScaleTransition(Duration.seconds(lineDelay), line);
        st.setToY(1);
        root.getChildren().get(root.getChildren().indexOf(line)).toFront();
        if(root.getChildren().contains(menuBox)) {
            st.setOnFinished(e -> {

                for (int i = 0; i < menuBox.getChildren().size(); i++) {
                    Node n = menuBox.getChildren().get(i);

                    TranslateTransition tt = new TranslateTransition(Duration.seconds(itemSpeed + i * diff), n);
                    tt.setToX(0);
                    tt.setOnFinished(e2 -> n.setClip(null));
                    tt.play();
                }
            });
        }
        else if(root.getChildren().contains(settingsBox)) {
            st.setOnFinished(e -> {

                for (int i = 0; i < settingsBox.getChildren().size(); i++) {
                    Node n = settingsBox.getChildren().get(i);

                    TranslateTransition tt = new TranslateTransition(Duration.seconds(itemSpeed + i * diff), n);
                    tt.setToX(0);
                    tt.setOnFinished(e2 -> n.setClip(null));
                    tt.play();
                }
            });
        }
        else if(root.getChildren().contains(tutorialBox)) {
            root.getChildren().get(root.getChildren().indexOf(line)).toBack();
            st.setOnFinished(e -> {

                for (int i = 0; i < tutorialBox.getChildren().size(); i++) {
                    Node n = tutorialBox.getChildren().get(i);

                    TranslateTransition tt = new TranslateTransition(Duration.seconds(itemSpeed + i * diff), n);
                    tt.setToX(0);
                    tt.setOnFinished(e2 -> n.setClip(null));
                    tt.play();
                }
            });
        }
        else if(root.getChildren().contains(highscoresBox)) {
            root.getChildren().get(root.getChildren().indexOf(line)).toBack();
            st.setOnFinished(e -> {

                for (int i = 0; i < highscoresBox.getChildren().size(); i++) {
                    Node n = highscoresBox.getChildren().get(i);

                    TranslateTransition tt = new TranslateTransition(Duration.seconds(itemSpeed + i * diff), n);
                    tt.setToX(0);
                    tt.setOnFinished(e2 -> n.setClip(null));
                    tt.play();
                }
            });
        }
        else if(root.getChildren().contains(profileBox)) {
            root.getChildren().get(root.getChildren().indexOf(line)).toBack();
            st.setOnFinished(e -> {

                for (int i = 0; i < profileBox.getChildren().size(); i++) {
                    Node n = profileBox.getChildren().get(i);

                    TranslateTransition tt = new TranslateTransition(Duration.seconds(itemSpeed + i * diff), n);
                    tt.setToX(0);
                    tt.setOnFinished(e2 -> n.setClip(null));
                    tt.play();
                }
            });
        }
        st.play();
    }

    public static void addMenu(double x, double y) {
        if(menuBox.getChildren().isEmpty()) {
            menuBox.setTranslateX(x);
            menuBox.setTranslateY(y);

            menuData.forEach(data -> {
                NinjaMenuItem item = new NinjaMenuItem(data.getKey());
                item.setOnAction(data.getValue());
                item.setTranslateX(-300);

                Rectangle clip = new Rectangle(300, 50);
                clip.translateXProperty().bind(item.translateXProperty().negate());

                item.setClip(clip);

                menuBox.getChildren().addAll(item);
            });
        }
        else {
            menuBox.getChildren().forEach(child -> {
                child.setTranslateX(-300);

                Rectangle clip = new Rectangle(300, 50);
                clip.translateXProperty().bind(child.translateXProperty().negate());

                child.setClip(clip);
            });
        }

        removeSubmenus();
        root.getChildren().add(menuBox);
    }

    public static void addSettings(double x, double y){
        if(settingsBox.getChildren().isEmpty()) {
            settingsBox.setTranslateX(x);
            settingsBox.setTranslateY(y);

            settingsData.forEach(data -> {
                NinjaMenuItem item = new NinjaMenuItem(data.getKey());
                item.setOnAction(data.getValue());
                item.setTranslateX(-300);

                Rectangle clip = new Rectangle(300, 50);
                clip.translateXProperty().bind(item.translateXProperty().negate());

                item.setClip(clip);

                settingsBox.getChildren().addAll(item);
            });
            settingsBox.getChildren().add(3, volumeSlider);
        }
        else {
            settingsBox.getChildren().forEach(child -> {
                child.setTranslateX(-300);

                Rectangle clip = new Rectangle(300, 50);
                clip.translateXProperty().bind(child.translateXProperty().negate());

                child.setClip(clip);
            });
        }
        removeSubmenus();
        root.getChildren().add(settingsBox);
    }

    public static void addTutorial(double x, double y) {
        //ImageView imageView = new ImageView(new Image(getClass().getResource("/resources/images/ninja_bg.png").toExternalForm()));
        tutorialbg.setFitWidth(WIDTH);
        tutorialbg.setFitHeight(HEIGHT);
        if(tutorialBox.getChildren().isEmpty()) {
            tutorialBox.setTranslateX(x);
            tutorialBox.setTranslateY(y);

            tutorialData.forEach(data -> {
                NinjaMenuItem item = new NinjaMenuItem(data.getKey());
                item.setOnAction(data.getValue());
                item.setTranslateX(-300);

                Rectangle clip = new Rectangle(300, 50);
                clip.translateXProperty().bind(item.translateXProperty().negate());

                item.setClip(clip);

                tutorialBox.getChildren().addAll(item);
            });
        }
        else {
            tutorialBox.getChildren().forEach(child -> {
                child.setTranslateX(-300);

                Rectangle clip = new Rectangle(300, 50);
                clip.translateXProperty().bind(child.translateXProperty().negate());

                child.setClip(clip);
            });
        }
        removeSubmenus();
        root.getChildren().addAll(tutorialbg, tutorialBox);
    }

    public static void addHighscores(double x, double y) {
        //ImageView imageView = new ImageView(new Image(getClass().getResource("/resources/images/ninja_bg.png").toExternalForm()));
        highscoresbg.setFitWidth(WIDTH);
        highscoresbg.setFitHeight(HEIGHT);
        leaderboard = new NinjaMenuHSList();

        if(highscoresBox.getChildren().isEmpty()) {
            highscoresBox.setTranslateX(x);
            highscoresBox.setTranslateY(y);

            highscoresData.forEach(data -> {
                NinjaMenuItem item = new NinjaMenuItem(data.getKey());
                item.setOnAction(data.getValue());
                item.setTranslateX(-300);

                Rectangle clip = new Rectangle(300, 50);
                clip.translateXProperty().bind(item.translateXProperty().negate());

                item.setClip(clip);

                highscoresBox.getChildren().addAll(item);
            });
        }
        else {
            highscoresBox.getChildren().forEach(child -> {
                child.setTranslateX(-300);

                Rectangle clip = new Rectangle(300, 50);
                clip.translateXProperty().bind(child.translateXProperty().negate());

                child.setClip(clip);
            });
        }
        removeSubmenus();
        root.getChildren().addAll(highscoresbg, highscoresBox, leaderboard);
    }

    public static void addProfile(double x, double y) {
        //ImageView imageView = new ImageView(new Image(getClass().getResource("/resources/images/ninja_bg.png").toExternalForm()));
        profilebg.setFitWidth(WIDTH);
        profilebg.setFitHeight(HEIGHT);
        profileInfo = new NinjaMenuProfile();

        if(profileBox.getChildren().isEmpty()) {
            profileBox.setTranslateX(x);
            profileBox.setTranslateY(y);

            profileData.forEach(data -> {
                NinjaMenuItem item = new NinjaMenuItem(data.getKey());
                item.setOnAction(data.getValue());
                item.setTranslateX(-300);

                Rectangle clip = new Rectangle(300, 50);
                clip.translateXProperty().bind(item.translateXProperty().negate());

                item.setClip(clip);

                profileBox.getChildren().addAll(item);
            });
        }
        else {
            profileBox.getChildren().forEach(child -> {
                child.setTranslateX(-300);

                Rectangle clip = new Rectangle(300, 50);
                clip.translateXProperty().bind(child.translateXProperty().negate());

                child.setClip(clip);
            });
        }
        removeSubmenus();
        root.getChildren().addAll(profilebg, profileBox, profileInfo);
    }

    public static void toggleSettingFullscreen(){
        isFullScreen = !isFullScreen;
        settingsBox.getChildren().remove(0);
        NinjaMenuItem item = new NinjaMenuItem(settingsData.get(0).getKey() +": " + (isFullScreen ? "ON" : "OFF"));
        item.setOnAction(settingsData.get(0).getValue());
        settingsBox.getChildren().add(0, item);
    }

    public static void toggleSettingMusic(){
        isMusicMute = !isMusicMute;
        menuMusic.mediaPlayer.setMute(isMusicMute);
        settingsBox.getChildren().remove(1);
        NinjaMenuItem item = new NinjaMenuItem(settingsData.get(1).getKey() +": " + (!isMusicMute ? "ON" : "OFF"));
        item.setOnAction(settingsData.get(1).getValue());
        settingsBox.getChildren().add(1, item);
    }

    public static void toggleSettingSFX(){
        isSFXMute = !isSFXMute;
        settingsBox.getChildren().remove(2);
        NinjaMenuItem item = new NinjaMenuItem(settingsData.get(2).getKey() +": " + (!isSFXMute ? "ON" : "OFF"));
        item.setOnAction(settingsData.get(2).getValue());
        settingsBox.getChildren().add(2, item);
    }

    public static void removeSubmenus(){
        root.getChildren().removeAll(menuBox, settingsBox, tutorialBox, tutorialbg, highscoresBox, highscoresbg, leaderboard, profileBox, profilebg, profileInfo);
    }

}
