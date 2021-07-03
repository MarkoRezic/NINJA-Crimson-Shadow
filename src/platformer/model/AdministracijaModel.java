package platformer.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import platformer.controller.AdministracijaKontroler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class AdministracijaModel {
    SimpleIntegerProperty id = new SimpleIntegerProperty();
    SimpleStringProperty username = new SimpleStringProperty();
    SimpleStringProperty role = new SimpleStringProperty();
    SimpleIntegerProperty level1Score = new SimpleIntegerProperty();
    SimpleIntegerProperty level2Score = new SimpleIntegerProperty();
    SimpleIntegerProperty level3Score = new SimpleIntegerProperty();
    Button resetBtn = new Button();
    Button promoteBtn = new Button();
    Button deleteBtn = new Button();
    public AdministracijaModel()
    {

    }
    public AdministracijaModel(Integer id, String username, String role, Integer level1Score, Integer level2Score, Integer level3Score, AdministracijaKontroler controller) {
        this.id = new SimpleIntegerProperty (id);
        this.username = new SimpleStringProperty(username);
        this.role = new SimpleStringProperty(role);
        this.level1Score = new SimpleIntegerProperty (level1Score);
        this.level2Score = new SimpleIntegerProperty (level2Score);
        this.level3Score = new SimpleIntegerProperty (level3Score);
        this.resetBtn =  new Button("Reset");
        this.resetBtn.setOnAction((ActionEvent e) -> {
            try {
                Baza db = new Baza();
                PreparedStatement update = db.prepare("UPDATE users SET level1_score = 0, level2_score = 0, level3_score = 0 WHERE id = ?");
                update.setString(1, id.toString());
                update.executeUpdate();
                controller.updateTable();
            }
            catch (SQLException sqle){
                System.out .println(sqle.getMessage());
            }
        });
        this.resetBtn.setGraphic(new ImageView(new Image("/resources/images/resetIcon.png", 32, 32, false, false)));
        this.resetBtn.setMinWidth(100);

        this.promoteBtn =  new Button("Promote");
        this.promoteBtn.setOnAction((ActionEvent e) -> {
            try {
                Baza db = new Baza();
                PreparedStatement update = db.prepare("UPDATE users SET role='admin' WHERE id=?");
                update.setString(1, id.toString());
                update.executeUpdate();
                controller.updateTable();
            }
            catch (SQLException sqle){
                System.out .println(sqle.getMessage());
            }
        });
        this.promoteBtn.setGraphic(new ImageView(new Image("/resources/images/promoteIcon.png", 32, 32, false, false)));
        this.promoteBtn.setMinWidth(100);

        this.deleteBtn =  new Button("Delete");
        this.deleteBtn.setOnAction((ActionEvent e) -> {
            try {
                Baza db = new Baza();
                PreparedStatement update = db.prepare("DELETE FROM users WHERE id=?");
                update.setString(1, id.toString());
                update.executeUpdate();
                controller.updateTable();
            }
            catch (SQLException sqle){
                System.out .println(sqle.getMessage());
            }
        });
        this.deleteBtn.setGraphic(new ImageView(new Image("/resources/images/deleteIcon.png", 32, 32, false, false)));
        this.deleteBtn.setMinWidth(100);
        if(role.equals("admin")){
            this.promoteBtn.setDisable(true);
            this.deleteBtn.setDisable(true);
        }
    }
    public Integer getId() {
        return id.get();
    }
    public String getUsername() {
        return username.get();
    }
    public String getRole() {
        return role.get();
    }
    public Integer getLevel1Score() { return level1Score.get(); }
    public Integer getLevel2Score() { return level2Score.get(); }
    public Integer getLevel3Score() { return level3Score.get(); }
    public Button getResetBtn() {return this.resetBtn; }
    public Button getPromoteBtn() {return this.promoteBtn; }
    public Button getDeleteBtn() {return this.deleteBtn; }
    public static ObservableList<AdministracijaModel> listaKorisnika(AdministracijaKontroler controller) {
        ObservableList<AdministracijaModel> lista = FXCollections.observableArrayList();
        Baza DB = new Baza();
        ResultSet rs = DB.select("SELECT id, username, role, level1_score, level2_score, level3_score FROM users");
        try {
            while (rs.next()) {
                lista.add(new AdministracijaModel(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("role"),
                        rs.getInt("level1_score"),
                        rs.getInt("level2_score"),
                        rs.getInt("level3_score"),
                        controller
                ));
            }
        } catch (SQLException ex) {
            System.out.println("Nastala je greška prilikom iteriranja.");
        }
        return lista;
    }

}
