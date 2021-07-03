/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package platformer.model;

import platformer.controller.LoginController;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NinjaProfileModel {

    public static int updateUsername(String oldname, String newname) {
        Baza db = new Baza();
        PreparedStatement select = db.prepare("SELECT username FROM users WHERE LOWER(username)=LOWER(?)");
        PreparedStatement insert = db.prepare("UPDATE users SET username = ? WHERE username = ?");
        try {
            select.setString(1, newname);
            ResultSet resultSet = select.executeQuery();

            if(resultSet.next()){
                return 1;
            }

            insert.setString(1, newname);
            insert.setString(2, oldname);
            insert.executeUpdate();

            return 0;
        } catch (SQLException ex) {
            System.out.println("Nastala je greška: "+ex.getMessage());
            return 2;
        }
    }

    public static int updatePassword(String username, String oldpass, String newpass) {
        Baza db = new Baza();
        PreparedStatement select = db.prepare("SELECT username FROM users WHERE LOWER(username)=LOWER(?) AND password=?");
        PreparedStatement insert = db.prepare("UPDATE users SET password = ? WHERE LOWER(username) = LOWER(?)");
        try {
            select.setString(1, username);
            select.setString(2, oldpass);
            ResultSet resultSet = select.executeQuery();

            if(!resultSet.next()){
                return 1;
            }

            insert.setString(1, newpass);
            insert.setString(2, username);
            insert.executeUpdate();

            return 0;
        } catch (SQLException ex) {
            System.out.println("Nastala je greška: "+ex.getMessage());
            return 2;
        }
    }

    public static void levelFinish(int level, int XP, int score){
        Baza db = new Baza();
        PreparedStatement addXP = db.prepare("UPDATE users SET experience = experience + ? WHERE LOWER(username) = LOWER(?)");
        PreparedStatement updateScore = db.prepare("UPDATE users SET level" + level + "_score = ? WHERE LOWER(username) = LOWER(?) AND level" + level + "_score < " + score);
        try {

            addXP.setString(1, String.valueOf(XP));
            addXP.setString(2, LoginController.currentUser.getUsername());
            addXP.executeUpdate();

            updateScore.setString(1, String.valueOf(score));
            updateScore.setString(2, LoginController.currentUser.getUsername());
            updateScore.executeUpdate();

        } catch (SQLException ex) {
            System.out.println("Nastala je greška: "+ex.getMessage());
        }
    }

    public static void levelAttempt(int level,int status, int score){
        Baza db = new Baza();
        PreparedStatement insert = db.prepare("INSERT INTO level_attempts (user_id, level_id, status_id, score) VALUES (?,?,?,?)");
        try {

            insert.setString(1, String.valueOf(LoginController.currentUser.getId()));
            insert.setString(2, String.valueOf(level));
            insert.setString(3, String.valueOf(status));
            insert.setString(4, String.valueOf(score));
            insert.executeUpdate();

        } catch (SQLException ex) {
            System.out.println("Nastala je greška: "+ex.getMessage());
        }
    }
}
