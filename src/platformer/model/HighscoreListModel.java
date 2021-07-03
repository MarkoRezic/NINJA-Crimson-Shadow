package platformer.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HighscoreListModel {
    SimpleIntegerProperty position = new SimpleIntegerProperty();
    SimpleStringProperty username = new SimpleStringProperty();
    SimpleIntegerProperty highscore = new SimpleIntegerProperty();
    public HighscoreListModel ()
    {

    }
    public HighscoreListModel (Integer position, String username, Integer highscore) {
        this.position = new SimpleIntegerProperty(position);
        this.username = new SimpleStringProperty(username);
        this.highscore = new SimpleIntegerProperty(highscore);
    }
    public int getPosition() { return position.get(); }
    public String getUsername() {
        return username.get();
    }
    public Integer getHighscore() {
        return highscore.get();
    }

    public static ObservableList<HighscoreListModel> highscoreList(Integer level) {
        ObservableList<HighscoreListModel> lista = FXCollections.observableArrayList();
        Baza DB = new Baza();
        ResultSet rs = DB.select("SELECT username, level" + level + "_score FROM users ORDER BY level" + level + "_score DESC LIMIT 10");
        try {
            int i = 1;
            while (rs.next()) {
                lista.add(new HighscoreListModel(
                        i,
                        rs.getString("username"),
                        rs.getInt("level" + level + "_score")));
                i++;
            }
            for(i=i; i<=10; i++){
                lista.add(new HighscoreListModel(
                        i,
                        "",
                        0));
            }
        } catch (SQLException ex) {
            System.out.println("Nastala je greška prilikom iteriranja.");
        }
        return lista;
    }

    @Override
    public String toString(){
        return position.getValue() + ". " + (position.getValue() > 9 ? "" : " ") + username.getValue();
    }
}
