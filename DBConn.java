/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package count;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;


public class DBConn {
    
    public static Connection connect() {
        Connection conn = null;
        try {
            String url = "jdbc:sqlite:money.db";
            conn = DriverManager.getConnection(url);
            System.out.println("connected successfully by jdbc");
        } catch (SQLException e) {
            System.out.println("connection failed: " + e.getMessage());
        }
        return conn;
        }
    
   public static void createDatabaseIfNotExists() {
    String url = "jdbc:sqlite:money.db";

    String createTableSQL = "CREATE TABLE IF NOT EXISTS money_counts (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "ones INTEGER, " +
            "fives INTEGER, " +
            "tens INTEGER, " +
            "twenties INTEGER, " +
            "fifties INTEGER, " +
            "hundreds INTEGER, " +
            "two_hundreds INTEGER, " +
            "divider INTEGER, " +
            "total INTEGER, " +
            "date TEXT" +
            ");";

    try (Connection conn = DriverManager.getConnection(url);
         Statement stmt = conn.createStatement()) {
        stmt.execute(createTableSQL);
        System.out.println("createdtable success if not exisit");
    } catch (SQLException e) {
        System.out.println("something gone wrong: " + e.getMessage());
}
}

public static int insertMoneyCount(
        double ones, double fives, double tens, double twenties,
        double fifties, double hundreds, double twoHundreds,
        int divider, double total, String date) {

    String url = "jdbc:sqlite:money.db";
    String insertSQL = "INSERT INTO money_counts (" +
            "ones, fives, tens, twenties, fifties, hundreds, two_hundreds, divider, total, date" +
            ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

    try (Connection conn = DriverManager.getConnection(url);
         PreparedStatement pstmt = conn.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {

        pstmt.setDouble(1, ones);
        pstmt.setDouble(2, fives);
        pstmt.setDouble(3, tens);
        pstmt.setDouble(4, twenties);
        pstmt.setDouble(5, fifties);
        pstmt.setDouble(6, hundreds);
        pstmt.setDouble(7, twoHundreds);
        pstmt.setInt(8, divider);
        pstmt.setDouble(9, total);
        pstmt.setString(10, date);

        int affectedRows = pstmt.executeUpdate();

        if (affectedRows > 0) {
            try (var rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    System.out.println("number of this count: " + id);
                    return id;
                }
            }
        }

    } catch (SQLException e) {
        System.out.println("failed: " + e.getMessage());
    }

    return -1; //
}
public static List<Object[]> getAllMoneyCounts() {
    List<Object[]> list = new ArrayList<>();
    try {
        Connection conn = DriverManager.getConnection("jdbc:sqlite:money.db");
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM money_counts ORDER BY id DESC");

        while (rs.next()) {
            Object[] row = new Object[]{
                rs.getInt("id"),
                rs.getDouble("two_hundreds"),
                rs.getDouble("hundreds"),
                rs.getDouble("fifties"),
                rs.getDouble("twenties"),
                rs.getDouble("tens"),
                rs.getDouble("fives"),
                rs.getInt("divider"),
                rs.getDouble("total"),
                rs.getString("date")
            };
            list.add(row);
        }

        rs.close();
        stmt.close();
        conn.close();

    } catch (Exception e) {
        e.printStackTrace();
    }
    return list;
}


} 

