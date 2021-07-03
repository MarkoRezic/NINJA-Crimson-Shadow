/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package platformer.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import platformer.controller.LoginController;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LevelModel {

    SimpleIntegerProperty id = new SimpleIntegerProperty();
    SimpleStringProperty name = new SimpleStringProperty();
    SimpleIntegerProperty baseXP = new SimpleIntegerProperty();
    SimpleIntegerProperty killXP = new SimpleIntegerProperty();
    SimpleIntegerProperty baseScore = new SimpleIntegerProperty();
    SimpleIntegerProperty killScore = new SimpleIntegerProperty();
    public LevelModel ()
    {

    }
    public LevelModel (Integer id,String name, Integer baseXP, Integer killXP, Integer baseScore, Integer killScore) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.baseXP = new SimpleIntegerProperty(baseXP);
        this.killXP = new SimpleIntegerProperty(killXP);
        this.baseScore = new SimpleIntegerProperty(baseScore);
        this.killScore = new SimpleIntegerProperty(killScore);
    }
    public int getId() { return id.get(); }
    public String getName() {
        return name.get();
    }
    public int getBaseXP() { return baseXP.getValue(); }
    public int getKillXP() { return killXP.getValue(); }
    public int getBaseScore() { return baseScore.getValue(); }
    public int getKillScore() { return killScore.getValue(); }

    public static LevelModel getLevelInfo(int level_id){
        Baza DB = new Baza();
        ResultSet rs = DB.select("SELECT * FROM levels WHERE id = " + String.valueOf(level_id));
        try {
            if (rs.next()) {
                return new LevelModel(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("baseXP"),
                        rs.getInt("killXP"),
                        rs.getInt("baseScore"),
                        rs.getInt("killScore"));
            }
        } catch (SQLException ex) {
            System.out.println("Nastala je greška prilikom iteriranja.");
        }
        return new LevelModel(0, "", 0, 0, 0, 0);
    }
}
