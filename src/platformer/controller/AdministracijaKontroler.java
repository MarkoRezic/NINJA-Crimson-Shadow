package platformer.controller;

import javafx.application.Platform;
import javafx.collections.ObservableList;
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
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import platformer.model.AdministracijaModel;
import platformer.ninjamenu.NinjaMenuApp;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdministracijaKontroler implements Initializable {
    @FXML
    TableView usersTable;
    @FXML
    TableColumn idCol;
    @FXML
    TableColumn usernameCol;
    @FXML
    TableColumn roleCol;
    @FXML
    TableColumn level1Col;
    @FXML
    TableColumn level2Col;
    @FXML
    TableColumn level3Col;
    @FXML
    TableColumn resetCol;
    @FXML
    TableColumn promoteCol;
    @FXML
    TableColumn deleteCol;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<AdministracijaModel> data = AdministracijaModel.listaKorisnika(this);
        idCol.setCellValueFactory(new PropertyValueFactory<AdministracijaModel, String>("id"));
        usernameCol.setCellValueFactory(new PropertyValueFactory<AdministracijaModel, String>("username"));
        roleCol.setCellValueFactory(new PropertyValueFactory<AdministracijaModel, String>("role"));
        level1Col.setCellValueFactory(new PropertyValueFactory<AdministracijaModel, String>("level1Score"));
        level2Col.setCellValueFactory(new PropertyValueFactory<AdministracijaModel, String>("level2Score"));
        level3Col.setCellValueFactory(new PropertyValueFactory<AdministracijaModel, String>("level3Score"));
        resetCol.setCellValueFactory(new PropertyValueFactory<AdministracijaModel, Button>("resetBtn"));
        promoteCol.setCellValueFactory(new PropertyValueFactory<AdministracijaModel, Button>("promoteBtn"));
        deleteCol.setCellValueFactory(new PropertyValueFactory<AdministracijaModel, Button>("deleteBtn"));
        usersTable.setItems(data);
    }
    public void updateTable(){
        ObservableList<AdministracijaModel> data = AdministracijaModel.listaKorisnika(this);
        idCol.setCellValueFactory(new PropertyValueFactory<AdministracijaModel, String>("id"));
        usernameCol.setCellValueFactory(new PropertyValueFactory<AdministracijaModel, String>("username"));
        roleCol.setCellValueFactory(new PropertyValueFactory<AdministracijaModel, String>("role"));
        level1Col.setCellValueFactory(new PropertyValueFactory<AdministracijaModel, String>("level1Score"));
        level2Col.setCellValueFactory(new PropertyValueFactory<AdministracijaModel, String>("level2Score"));
        level3Col.setCellValueFactory(new PropertyValueFactory<AdministracijaModel, String>("level3Score"));
        resetCol.setCellValueFactory(new PropertyValueFactory<AdministracijaModel, Button>("resetBtn"));
        promoteCol.setCellValueFactory(new PropertyValueFactory<AdministracijaModel, Button>("promoteBtn"));
        deleteCol.setCellValueFactory(new PropertyValueFactory<AdministracijaModel, Button>("deleteBtn"));
        usersTable.setItems(data);
    }
    public void openLogin(ActionEvent e) throws IOException {
        URL url = new File("src/platformer/view/Login.fxml").toURI().toURL();
        Parent root = FXMLLoader.load(url);

        Stage adminStage = (Stage) ((Node)(e.getSource())).getScene().getWindow();
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
        adminStage.close();
        loginStage.show();
    }
    public void launchGame(ActionEvent e) throws IOException {
        NinjaMenuApp menu = new NinjaMenuApp();
        Stage stage = new Stage();
        Stage adminStage = (Stage) ((Node)(e.getSource())).getScene().getWindow();
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

        adminStage.close();
    }

    private void letterbox(final Scene scene, final Pane contentPane) {
        final double initWidth  = scene.getWidth();
        final double initHeight = scene.getHeight();
        final double ratio      = initWidth / initHeight;

        LoginController.SceneSizeChangeListener sizeListener = new LoginController.SceneSizeChangeListener(scene, ratio, initHeight, initWidth, contentPane);
        scene.widthProperty().addListener(sizeListener);
        scene.heightProperty().addListener(sizeListener);
    }
}
