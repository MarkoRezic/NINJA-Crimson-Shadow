/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package platformer.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import platformer.model.LogiraniKorisnikModel;
import platformer.ninjamenu.NinjaMenuApp;

/**
 * FXML Controller class
 *
 * @author Daniel
 */
public class LoginController implements Initializable {

    @FXML
    Label statusLbl;
    
    @FXML
    TextField usernameTxt;
    
    @FXML
    PasswordField passwordTxt;

    public static LogiraniKorisnikModel currentUser;
    
    public void login(ActionEvent e) {
        String username = usernameTxt.getText();
        String password = passwordTxt.getText();
        
        if (username.equals("") || password.equals("")) {
            statusLbl.setText("Please enter all required fields");
        } else {
            if (LogiraniKorisnikModel.login(username, password)) {
                statusLbl.setTextFill(Color.GREEN);
                statusLbl.setText("Login Success");
                currentUser = LogiraniKorisnikModel.currentUser(username);
                System.out.println(currentUser);

                if(currentUser.getRole().equals("admin")) {
                    try {
                        Stage adminStage = new Stage();
                        URL url = new File("src/platformer/view/Administracija.fxml").toURI().toURL();
                        Parent root = FXMLLoader.load(url);
                        Scene scene = new Scene(root, 800, 400);

                        adminStage.setTitle("Ninja Administration");
                        adminStage.getIcons().add(new Image(getClass().getResourceAsStream("/resources/images/shuriken.png")));
                        adminStage.setScene(scene);
                        adminStage.setResizable(false);
                        adminStage.show();
                        adminStage.toFront();
                    }
                    catch (IOException ioe){
                        System.out.println(ioe.getMessage());
                    }
                }
                else {
                    NinjaMenuApp menu = new NinjaMenuApp();
                    Stage stage = new Stage();
                    Pane root = menu.createContent();
                    Scene scene = new Scene(root, NinjaMenuApp.WIDTH, NinjaMenuApp.HEIGHT);
                    Image image = new Image("/resources/images/kunai.png");  //pass in the image path

                    scene.setCursor(new ImageCursor(image));
                    stage.setTitle("Ninja Menu");
                    stage.getIcons().add(new Image(getClass().getResourceAsStream("/resources/images/shuriken.png")));
                    stage.setScene(scene);
                    letterbox(scene, root);
                    stage.setResizable(false);
                    stage.setFullScreenExitHint("");
                    stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent t) {
                            Platform.exit();
                            System.exit(0);
                        }
                    });
                    root.setBackground(new Background(new BackgroundFill(Color.web("#000000", 1), new CornerRadii(0, true), new Insets(0))));
                    root.setStyle("-fx-border-color: black;");
                    stage.setFullScreen(true);
                    stage.show();
                    stage.toFront();
                }
                Stage loginStage = (Stage) ((Node) (e.getSource())).getScene().getWindow();
                loginStage.close();

                /*try {
                    root = FXMLLoader.load(getClass().getClassLoader().getResource("kontakti/view/Administracija.fxml"));
                    Stage stage = new Stage();
                    stage.setTitle("Prikaz svih kontakata u bazi podataka");
                    stage.setScene(new Scene(root, 450, 450));
                    stage.show();
                    ((Node)(e.getSource())).getScene().getWindow().hide();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }*/
            } else {
                statusLbl.setText("Incorrect username or password");
            }
        }
    }

    public static void updateCurrentUser(){
        currentUser = LogiraniKorisnikModel.currentUser(currentUser.getUsername());
    }

    public void openRegister(ActionEvent e) throws IOException {
        URL url = new File("src/platformer/view/Register.fxml").toURI().toURL();
        Parent root = FXMLLoader.load(url);

        Stage loginStage = (Stage) ((Node)(e.getSource())).getScene().getWindow();
        Stage registerStage = new Stage();
        Scene scene = new Scene(root, 700, 450);

        registerStage.setTitle("Ninja Register");
        registerStage.setScene(scene);
        registerStage.getIcons().add(new Image(getClass().getResourceAsStream("/resources/images/shuriken.png")));
        registerStage.setResizable(false);
        registerStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                registerStage.close();
                loginStage.show();
            }
        });
        loginStage.close();
        registerStage.show();
    }

    private void letterbox(final Scene scene, final Pane contentPane) {
        final double initWidth  = scene.getWidth();
        final double initHeight = scene.getHeight();
        final double ratio      = initWidth / initHeight;

        SceneSizeChangeListener sizeListener = new SceneSizeChangeListener(scene, ratio, initHeight, initWidth, contentPane);
        scene.widthProperty().addListener(sizeListener);
        scene.heightProperty().addListener(sizeListener);
    }

    public static class SceneSizeChangeListener implements ChangeListener<Number> {
        private final Scene scene;
        private final double ratio;
        private final double initHeight;
        private final double initWidth;
        private final Pane contentPane;

        public SceneSizeChangeListener(Scene scene, double ratio, double initHeight, double initWidth, Pane contentPane) {
            this.scene = scene;
            this.ratio = ratio;
            this.initHeight = initHeight;
            this.initWidth = initWidth;
            this.contentPane = contentPane;
        }

        @Override
        public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
            final double newWidth  = scene.getWidth();
            final double newHeight = scene.getHeight();

            double scaleFactor =
                    newWidth / newHeight > ratio
                            ? newHeight / initHeight
                            : newWidth / initWidth;

            if (scaleFactor >= 1) {
                Scale scale = new Scale(scaleFactor, scaleFactor);
                scale.setPivotX(0);
                scale.setPivotY(0);
                scene.getRoot().getTransforms().setAll(scale);

                contentPane.setPrefWidth (newWidth  / scaleFactor);
                contentPane.setPrefHeight(newHeight / scaleFactor);
            } else {
                contentPane.setPrefWidth (Math.max(initWidth,  newWidth));
                contentPane.setPrefHeight(Math.max(initHeight, newHeight));
            }
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
