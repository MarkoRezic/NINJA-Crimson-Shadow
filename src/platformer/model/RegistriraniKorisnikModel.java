/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package platformer.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegistriraniKorisnikModel {

    public static int register(String username, String password) {
        Baza db = new Baza();
        PreparedStatement select = db.prepare("SELECT username FROM users WHERE LOWER(username)=LOWER(?)");
        PreparedStatement insert = db.prepare("INSERT INTO users (username, password) VALUES (?,?)");
        try {
            select.setString(1, username);
            ResultSet resultSet = select.executeQuery();

            if(resultSet.next()){
                return 1;
            }

            insert.setString(1, username);
            insert.setString(2, password);
            insert.executeUpdate();

            return 0;
        } catch (SQLException ex) {
            System.out.println("Nastala je greška: "+ex.getMessage());
            return 2;
        }
    }
}
