/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package platformer.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LogiraniKorisnikModel {
    SimpleIntegerProperty id = new SimpleIntegerProperty();
    SimpleStringProperty username = new SimpleStringProperty();
    SimpleStringProperty password = new SimpleStringProperty();
    SimpleStringProperty role = new SimpleStringProperty();
    SimpleIntegerProperty level1Score = new SimpleIntegerProperty();
    SimpleIntegerProperty level2Score = new SimpleIntegerProperty();
    SimpleIntegerProperty level3Score = new SimpleIntegerProperty();
    SimpleIntegerProperty experience = new SimpleIntegerProperty();
    public LogiraniKorisnikModel ()
    {

    }
    public LogiraniKorisnikModel (Integer id,String username, String password,String role, Integer level1Score, Integer level2Score, Integer level3Score, Integer experience) {
        this.id = new SimpleIntegerProperty(id);
        this.username = new SimpleStringProperty(username);
        this.password = new SimpleStringProperty(password);
        this.role = new SimpleStringProperty(role);
        this.level1Score = new SimpleIntegerProperty(level1Score);
        this.level2Score = new SimpleIntegerProperty(level2Score);
        this.level3Score = new SimpleIntegerProperty(level3Score);
        this.experience = new SimpleIntegerProperty(experience);
    }
    public int getId() { return id.get(); }
    public String getUsername() {
        return username.get();
    }
    public String getRole() {
        return role.get();
    }
    public int getLevel1Score() { return level1Score.getValue(); }
    public int getLevel2Score() { return level2Score.getValue(); }
    public int getLevel3Score() { return level3Score.getValue(); }
    public int getExperience() { return experience.getValue(); }
    public static ObservableList<LogiraniKorisnikModel> userList() {
        ObservableList<LogiraniKorisnikModel> lista = FXCollections.observableArrayList();
        Baza DB = new Baza();
        ResultSet rs = DB.select("SELECT * FROM users");
        try {
            while (rs.next()) {
                lista.add(new LogiraniKorisnikModel(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString(""),
                        rs.getString("role"),
                        rs.getInt("level1_score"),
                        rs.getInt("level2_score"),
                        rs.getInt("level3_score"),
                        rs.getInt("experience")));
            }
        } catch (SQLException ex) {
            System.out.println("Nastala je greška prilikom iteriranja.");
        }
        return lista;
    }
    public static LogiraniKorisnikModel currentUser(String username) {
        Baza DB = new Baza();
        PreparedStatement ps = DB.prepare("SELECT * FROM users WHERE LOWER(username)=LOWER(?)");
        try {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return (new LogiraniKorisnikModel(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getInt("level1_score"),
                        rs.getInt("level2_score"),
                        rs.getInt("level3_score"),
                        rs.getInt("experience")));
            }
        } catch (SQLException ex) {
            System.out.println("Nastala je greška prilikom iteriranja.");
            return null;
        }
        return null;
    }

    public static boolean login(String username, String password) {
        Baza db = new Baza();
        PreparedStatement ps = db.prepare("SELECT * FROM users WHERE username=? AND password=?");
        try {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException ex) {
            System.out.println("Nastala je greška: "+ex.getMessage());
            return false;
        }
    }

    public String toString(){
        return "id: " + id.getValue() + "\nusername: " + username.getValue() + "\nrole: " + role.getValue();
    }
}
