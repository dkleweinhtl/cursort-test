package com.parkmanager.service;

import com.parkmanager.model.Tree;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DatabaseService {
    private static final String DB_URL = "jdbc:sqlite:trees.db";
    private static DatabaseService instance;

    private DatabaseService() {
        initializeDatabase();
    }

    public static DatabaseService getInstance() {
        if (instance == null) {
            instance = new DatabaseService();
        }
        return instance;
    }

    private void initializeDatabase() {
        String sql = """
            CREATE TABLE IF NOT EXISTS trees (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                species TEXT NOT NULL,
                circumference REAL NOT NULL,
                planting_date TEXT NOT NULL,
                estimated_planting_date INTEGER NOT NULL,
                condition INTEGER NOT NULL CHECK (condition >= 1 AND condition <= 10)
            )
        """;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveTree(Tree tree) {
        String sql = """
            INSERT INTO trees (species, circumference, planting_date, estimated_planting_date, condition)
            VALUES (?, ?, ?, ?, ?)
        """;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tree.getSpecies());
            pstmt.setDouble(2, tree.getCircumference());
            pstmt.setString(3, tree.getPlantingDate().toString());
            pstmt.setInt(4, tree.isEstimatedPlantingDate() ? 1 : 0);
            pstmt.setInt(5, tree.getCondition());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateTree(Tree tree) {
        String sql = """
            UPDATE trees
            SET species = ?, circumference = ?, planting_date = ?, estimated_planting_date = ?, condition = ?
            WHERE id = ?
        """;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tree.getSpecies());
            pstmt.setDouble(2, tree.getCircumference());
            pstmt.setString(3, tree.getPlantingDate().toString());
            pstmt.setInt(4, tree.isEstimatedPlantingDate() ? 1 : 0);
            pstmt.setInt(5, tree.getCondition());
            pstmt.setInt(6, tree.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteTree(int id) {
        String sql = "DELETE FROM trees WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Tree> getAllTrees() {
        List<Tree> trees = new ArrayList<>();
        String sql = "SELECT * FROM trees";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Tree tree = new Tree();
                tree.setId(rs.getInt("id"));
                tree.setSpecies(rs.getString("species"));
                tree.setCircumference(rs.getDouble("circumference"));
                tree.setPlantingDate(LocalDate.parse(rs.getString("planting_date")));
                tree.setEstimatedPlantingDate(rs.getInt("estimated_planting_date") == 1);
                tree.setCondition(rs.getInt("condition"));
                trees.add(tree);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return trees;
    }
} 