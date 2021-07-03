package platformer.ninjamenu;

import javafx.animation.FadeTransition;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import platformer.audio.ClipPlayer;
import platformer.controller.LoginController;
import platformer.model.LogiraniKorisnikModel;
import platformer.model.NinjaProfileModel;

public class NinjaMenuProfile extends Pane {

    private Effect glow = new DropShadow(15, Color.WHITE);
    private Effect shadow = new DropShadow(10, Color.BLACK);
    private Effect blur = new BoxBlur(0, 0, 3);

    private static Text highscoreTitle = new Text("HIGHSCORES");
    private static Text[] levelTitles = new Text[3];
    private static Text[] scoreText = new Text[3];
    private static Text ninjaName;
    private static Text ninjaLevel;
    private static Text xp;
    private static RingProgressIndicator xpProgress = new RingProgressIndicator();
    private static Circle xpbg = new Circle();
    private static Text usernameText = new Text("Username");
    private static Text passwordText = new Text("Password");
    private static Text oldNameText = new Text();
    private static Text newNameText = new Text("New");
    private static Text oldPassText = new Text("Old");
    private static Text newPassText = new Text("New");
    private static TextField usernameInput = new TextField();
    private static PasswordField oldpasswordInput = new PasswordField();
    private static PasswordField newpasswordInput = new PasswordField();
    private static Text usernameError = new Text();
    private static Text passwordError = new Text();
    private static Button usernameBtn = new Button();
    private static Button passwordBtn = new Button();
    private static ClipPlayer buttonClick = new ClipPlayer("/resources/SFX/menu-hover.mp3");

    public static ImageView ninjaprofile = new ImageView(new Image("/resources/images/ninja_profile.png"));


    public NinjaMenuProfile() {
        LoginController.updateCurrentUser();

        levelTitles[0] = new Text("Level 1");
        levelTitles[1] = new Text("Level 2");
        levelTitles[2] = new Text("Level 3");
        scoreText[0] = new Text(""+LoginController.currentUser.getLevel1Score());
        scoreText[1] = new Text(""+LoginController.currentUser.getLevel2Score());
        scoreText[2] = new Text(""+LoginController.currentUser.getLevel3Score());

        for(int level = 0; level < 3; level++){
            levelTitles[level].setTranslateX(700 + 100 * level);
            levelTitles[level].setTranslateY(220);
            levelTitles[level].setFont(Font.loadFont(NinjaMenuApp.class.getResource("/resources/fonts/Penumbra-HalfSerif-Std_35114.ttf").toExternalForm(), 20));
            levelTitles[level].setFill(Color.WHITE);
            levelTitles[level].setEffect(glow);

            scoreText[level].setTranslateX(700 + 100 * level);
            scoreText[level].setTranslateY(250);
            scoreText[level].setFont(Font.loadFont(NinjaMenuApp.class.getResource("/resources/fonts/Penumbra-HalfSerif-Std_35114.ttf").toExternalForm(), 20));
            scoreText[level].setFill(Color.WHITE);
            scoreText[level].setEffect(glow);
            scoreText[level].setWrappingWidth(levelTitles[level].getLayoutBounds().getWidth());
            scoreText[level].setTextAlignment(TextAlignment.CENTER);
        }


        highscoreTitle.setFont(Font.loadFont(NinjaMenuApp.class.getResource("/resources/fonts/Penumbra-HalfSerif-Std_35114.ttf").toExternalForm(), 40));
        highscoreTitle.setFill(Color.WHITE);
        highscoreTitle.setEffect(glow);
        highscoreTitle.setTranslateX(700);
        highscoreTitle.setTranslateY(145);

        highscoreTitle.setMouseTransparent(true);

        Polygon nameBorder = new Polygon(
                0, 0,
                250, 0,
                260, 35,
                250, 70,
                0, 70
        );
        nameBorder.setStroke(Color.color(1, 1, 1, 0.5));
        nameBorder.setStrokeWidth(2);
        nameBorder.setFill(Color.color(1, 1, 1, 0.1));
        nameBorder.setTranslateX(150);
        nameBorder.setTranslateY(100);

        ninjaprofile.setFitHeight(100);
        ninjaprofile.setFitWidth(100);
        ninjaprofile.setTranslateX(100);
        ninjaprofile.setTranslateY(85);
        ninjaprofile.setMouseTransparent(true);
        xpProgress.hoverProperty().addListener((obs, wasHovered, isNowHovered)->{
            if(isNowHovered){
                FadeTransition ft = new FadeTransition(Duration.millis(500), ninjaprofile);
                ft.setFromValue(1);
                ft.setToValue(0);
                ft.play();
            }
            else {
                FadeTransition ft = new FadeTransition(Duration.millis(500), ninjaprofile);
                ft.setFromValue(0);
                ft.setToValue(1);
                ft.play();
            }
        });

        ninjaName = new Text(LoginController.currentUser.getUsername());
        ninjaName.setFont(Font.loadFont(NinjaMenuApp.class.getResource("/resources/fonts/Penumbra-HalfSerif-Std_35114.ttf").toExternalForm(), 30));
        ninjaName.setFill(Color.WHITE);
        ninjaName.setEffect(glow);
        ninjaName.setTranslateX(220);
        ninjaName.setTranslateY(145);

        ninjaLevel = new Text("Lvl " + (int)Math.floor(1 + (Math.sqrt(LoginController.currentUser.getExperience())/5) ) + " Ninja" );
        ninjaLevel.setFont(Font.loadFont(NinjaMenuApp.class.getResource("/resources/fonts/Penumbra-HalfSerif-Std_35114.ttf").toExternalForm(), 20));
        ninjaLevel.setFill(Color.WHITE);
        ninjaLevel.setEffect(glow);
        ninjaLevel.setTranslateX(120);
        ninjaLevel.setTranslateY(220);

        xp = new Text("XP: " + LoginController.currentUser.getExperience());
        xp.setFont(Font.loadFont(NinjaMenuApp.class.getResource("/resources/fonts/Penumbra-HalfSerif-Std_35114.ttf").toExternalForm(), 20));
        xp.setFill(Color.WHITE);
        xp.setEffect(glow);
        xp.setTranslateX(120);
        xp.setTranslateY(250);

        String xpPercentString = String.valueOf(Math.sqrt(LoginController.currentUser.getExperience())/5);
        xpPercentString = xpPercentString.substring(xpPercentString.indexOf("."));
        xpProgress.setProgress((int)(100 * Double.valueOf(xpPercentString) ));
        xpProgress.setTranslateX(83);
        xpProgress.setTranslateY(68);

        xpbg.setCenterX(150);
        xpbg.setCenterY(135);
        xpbg.setRadius(49);
        xpbg.setFill(Color.BLACK);

        usernameText.setFont(Font.loadFont(NinjaMenuApp.class.getResource("/resources/fonts/Penumbra-HalfSerif-Std_35114.ttf").toExternalForm(), 30));
        usernameText.setFill(Color.WHITE);
        usernameText.setTranslateX(120);
        usernameText.setTranslateY(400);

        oldNameText = new Text("Old     " + LoginController.currentUser.getUsername());
        oldNameText.setFont(Font.loadFont(NinjaMenuApp.class.getResource("/resources/fonts/Penumbra-HalfSerif-Std_35114.ttf").toExternalForm(), 20));
        oldNameText.setFill(Color.WHITE);
        oldNameText.setTranslateX(50);
        oldNameText.setTranslateY(470);

        newNameText.setFont(Font.loadFont(NinjaMenuApp.class.getResource("/resources/fonts/Penumbra-HalfSerif-Std_35114.ttf").toExternalForm(), 20));
        newNameText.setFill(Color.WHITE);
        newNameText.setTranslateX(50);
        newNameText.setTranslateY(520);

        passwordText.setFont(Font.loadFont(NinjaMenuApp.class.getResource("/resources/fonts/Penumbra-HalfSerif-Std_35114.ttf").toExternalForm(), 30));
        passwordText.setFill(Color.WHITE);
        passwordText.setTranslateX(700);
        passwordText.setTranslateY(400);

        oldPassText.setFont(Font.loadFont(NinjaMenuApp.class.getResource("/resources/fonts/Penumbra-HalfSerif-Std_35114.ttf").toExternalForm(), 20));
        oldPassText.setFill(Color.WHITE);
        oldPassText.setTranslateX(630);
        oldPassText.setTranslateY(470);

        newPassText.setFont(Font.loadFont(NinjaMenuApp.class.getResource("/resources/fonts/Penumbra-HalfSerif-Std_35114.ttf").toExternalForm(), 20));
        newPassText.setFill(Color.WHITE);
        newPassText.setTranslateX(630);
        newPassText.setTranslateY(520);

        usernameInput.setPrefSize(250, 40);
        usernameInput.setStyle("-fx-border-color:white; -fx-background-color: black; -fx-text-fill: white;");
        usernameInput.setTranslateX(120);
        usernameInput.setTranslateY(490);

        oldpasswordInput.setPrefSize(250, 40);
        oldpasswordInput.setStyle("-fx-border-color:white; -fx-background-color: black; -fx-text-fill: white;");
        oldpasswordInput.setTranslateX(700);
        oldpasswordInput.setTranslateY(440);

        newpasswordInput.setPrefSize(250, 40);
        newpasswordInput.setStyle("-fx-border-color:white; -fx-background-color: black; -fx-text-fill: white;");
        newpasswordInput.setTranslateX(700);
        newpasswordInput.setTranslateY(490);

        usernameBtn.setGraphic(new ImageView(new Image("/resources/images/checkIcon.png", 32, 32, false, false)));
        usernameBtn.setStyle("-fx-background-color: black; -fx-border-color: #ccc; ");
        usernameBtn.setPrefHeight(40);
        usernameBtn.setTranslateX(380);
        usernameBtn.setTranslateY(490);
        usernameBtn.hoverProperty().addListener((obs, wasHovered, isNowHovered)->{
            if(isNowHovered){
                usernameBtn.setStyle("-fx-background-color: #222; -fx-border-color: white; ");
            }
            else {
                usernameBtn.setStyle("-fx-background-color: black; -fx-border-color: #ccc; ");
            }
        });
        usernameBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                updateUsername();
                buttonClick.play();
            }
        });

        passwordBtn.setGraphic(new ImageView(new Image("/resources/images/checkIcon.png", 32, 32, false, false)));
        passwordBtn.setStyle("-fx-background-color: black; -fx-border-color: white; ");
        passwordBtn.setPrefHeight(40);
        passwordBtn.setTranslateX(960);
        passwordBtn.setTranslateY(490);
        passwordBtn.hoverProperty().addListener((obs, wasHovered, isNowHovered)->{
            if(isNowHovered){
                passwordBtn.setStyle("-fx-background-color: #222; -fx-border-color: white; ");
            }
            else {
                passwordBtn.setStyle("-fx-background-color: black; -fx-border-color: #ccc; ");
            }
        });
        passwordBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                updatePassword();
                buttonClick.play();
            }
        });

        usernameError.setFont(Font.loadFont(NinjaMenuApp.class.getResource("/resources/fonts/Penumbra-HalfSerif-Std_35114.ttf").toExternalForm(), 14));
        usernameError.setFill(Color.RED);
        usernameError.setTranslateX(120);
        usernameError.setTranslateY(560);

        passwordError.setFont(Font.loadFont(NinjaMenuApp.class.getResource("/resources/fonts/Penumbra-HalfSerif-Std_35114.ttf").toExternalForm(), 14));
        passwordError.setFill(Color.RED);
        passwordError.setTranslateX(700);
        passwordError.setTranslateY(560);

        getChildren().addAll(nameBorder, xpbg, xpProgress, ninjaprofile, ninjaName, ninjaLevel, xp,
                usernameText, oldNameText, newNameText, passwordText, oldPassText, newPassText,
                usernameInput, oldpasswordInput, newpasswordInput, usernameError, passwordError,
                usernameBtn, passwordBtn, highscoreTitle);
        getChildren().addAll(levelTitles);
        getChildren().addAll(scoreText);
    }

    public void updateView(){
        //getChildren().removeAll(ninjaName, oldNameText);
        ninjaName.setText(LoginController.currentUser.getUsername());
        oldNameText.setText("Old     " + LoginController.currentUser.getUsername());
        usernameInput.setText("");
        oldpasswordInput.setText("");
        newpasswordInput.setText("");
        usernameError.setText("");
        passwordError.setText("");
    }

    public void updateUsername(){
        if(usernameInput.getText().equals("")){
            usernameError.setText("Username is required");
            return;
        }
        int checkUsername = NinjaProfileModel.updateUsername(LoginController.currentUser.getUsername(), usernameInput.getText());
        if(checkUsername == 1){
            usernameError.setText("Username is already taken");
            return;
        }
        if(checkUsername == 2){
            usernameError.setText("Error occurred while updating");
            return;
        }
        LoginController.currentUser = LogiraniKorisnikModel.currentUser(usernameInput.getText());
        updateView();
    }

    public void updatePassword(){
        if(oldpasswordInput.getText().equals("")){
            passwordError.setText("Old Password is required");
            return;
        }
        if(newpasswordInput.getText().equals("")){
            passwordError.setText("New Password is required");
            return;
        }
        int checkPassword = NinjaProfileModel.updatePassword(LoginController.currentUser.getUsername(), oldpasswordInput.getText(), newpasswordInput.getText());
        if(checkPassword == 1){
            passwordError.setText("Old password is incorrect");
            return;
        }
        if(checkPassword == 2){
            passwordError.setText("Error occurred while updating");
            return;
        }
        LoginController.currentUser = LogiraniKorisnikModel.currentUser(LoginController.currentUser.getUsername());
        updateView();
    }
}