/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package platformer.controller;

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
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import platformer.model.LogiraniKorisnikModel;
import platformer.model.RegistriraniKorisnikModel;
import platformer.ninjamenu.NinjaMenuApp;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author Daniel
 */
public class RegisterController implements Initializable {

    @FXML
    Label statusLbl;
    
    @FXML
    TextField usernameTxt;
    
    @FXML
    PasswordField passwordTxt;

    @FXML
    PasswordField repasswordTxt;
    
    public void register(ActionEvent e) {
        String username = usernameTxt.getText();
        String password = passwordTxt.getText();
        String repassword = repasswordTxt.getText();
        
        if (username.equals("") || password.equals("") || repassword.equals("")) {
            statusLbl.setText("Please enter all required fields");
        }
        else if (!password.equals(repassword)){
            statusLbl.setText("Passwords do not match");
        }
        else {
            int registerCheck = RegistriraniKorisnikModel.register(username, password);
            if (registerCheck == 0) {
                statusLbl.setTextFill(Color.GREEN);
                statusLbl.setText("Registration Success");

                Stage registerStage = (Stage) ((Node)(e.getSource())).getScene().getWindow();
                try {
                    URL url = new File("src/platformer/view/Login.fxml").toURI().toURL();
                    Parent root = FXMLLoader.load(url);

                    Stage loginStage = new Stage();
                    Scene scene = new Scene(root, 700, 450);

                    loginStage.setTitle("Ninja Login");
                    loginStage.setScene(scene);
                    loginStage.getIcons().add(new Image(getClass().getResourceAsStream("/resources/images/shuriken.png")));
                    loginStage.setResizable(false);
                    loginStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent t) {
                            Platform.exit();
                        }
                    });
                    registerStage.close();
                    loginStage.show();
                }
                catch (IOException ie) {
                    System.out.println(ie.getMessage());
                }

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
            }
            else if (registerCheck == 1) {
                statusLbl.setText("Username already taken");
            }
            else {
                statusLbl.setText("An error occurred during registration");
            }
        }
    }

    private void letterbox(final Scene scene, final Pane contentPane) {
        final double initWidth  = scene.getWidth();
        final double initHeight = scene.getHeight();
        final double ratio      = initWidth / initHeight;

        SceneSizeChangeListener sizeListener = new SceneSizeChangeListener(scene, ratio, initHeight, initWidth, contentPane);
        scene.widthProperty().addListener(sizeListener);
        scene.heightProperty().addListener(sizeListener);
    }

    private static class SceneSizeChangeListener implements ChangeListener<Number> {
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
