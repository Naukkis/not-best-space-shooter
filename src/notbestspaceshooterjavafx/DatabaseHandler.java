package notbestspaceshooterjavafx;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHandler {

    Connection conn;
    Statement stmt;
    PreparedStatement preStmt;

    public DatabaseHandler() {
        connect();
        try {
            this.stmt = conn.createStatement();
        } catch (SQLException ex) {
            System.out.println("Failed statement");
        }
        createNewTable();
    }

    private void connect() {
        try {
            String url = "jdbc:sqlite:resources/highscore.db";
            conn = DriverManager.getConnection(url);
            conn.setAutoCommit(true);
            System.out.println("Connection to SQLite has been established.");
        } catch (SQLException e) {
            System.out.println("db connection failed: " + e.getMessage());

        }
    }

    private void createNewTable() {
        String sql = "CREATE TABLE IF NOT EXISTS highscores (\n"
                + "	id integer PRIMARY KEY,\n"
                + "	name text NOT NULL,\n"
                + "	score integer\n"
                + ");";
        try {
            stmt.execute(sql);
        } catch (Exception e) {
            System.out.println("create table failed (one already exists, most likely)" + e.getMessage());
        }
    }

    public void insertUser(String username, int score) {
        String sql = "INSERT INTO highscores(name, score) VALUES(?, ?)";

        try {
            preStmt = conn.prepareStatement(sql);
            preStmt.setString(1, username);
            preStmt.setInt(2, score);
            preStmt.executeUpdate();
            System.out.println("Insert successful");
        } catch (SQLException e) {
            System.out.println("Insert failed" + e.getMessage());

        }
    }

    public void removeHighScore(int row) {
        String sql = "DELETE from highscores where id = ?";
        try {
            preStmt = conn.prepareStatement(sql);
            preStmt.setInt(1, row);
            preStmt.executeUpdate();
            System.out.println("delete successful");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public String printTopTen() {
        String sql = "SELECT * from highscores ORDER BY score DESC LIMIT 10";
        String userHighscores = "";

        try {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                userHighscores += rs.getString("name") + "  "
                        + rs.getInt("score") + "\n";
            }

        } catch (SQLException e) {
            System.out.println("Sqlexcep " + e.getMessage());
        }
        return userHighscores;
    }

    public int lastScoreOfTopTen() {
        String sql = "SELECT score from highscores ORDER BY score DESC LIMIT 10";
        int lastScore = 0;
        int i = 0;
        try {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                lastScore = rs.getInt("score");
            }
            System.out.println(lastScore);
            i++;
        } catch (Exception e) {
            System.out.println("last score fail");;
        }
        if (i < 10) {
            lastScore = 0;
        }
        return lastScore;
    }

    public void closeDown() {
        try {
            preStmt.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            System.out.println("all good");
        }
    }
}
