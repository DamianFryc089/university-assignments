package pl.edu.pwr.student.damian_fryc.lab3.dao.db;

import java.sql.*;

public abstract class DaoDB {
    static {
        try {
            Class.forName("org.sqlite.JDBC");
            initializeDatabase();
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
    protected Connection conn = null;

    public static void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:db.sqlite")) {
            if (conn != null) {
                String sql = """
                    CREATE TABLE IF NOT EXISTS "customers" (
                        id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                        name TEXT NOT NULL
                    );
        
                    CREATE TABLE IF NOT EXISTS "sellers" (
                        id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                        name TEXT NOT NULL
                    );
        
                    CREATE TABLE IF NOT EXISTS "organizers" (
                        id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                        name TEXT NOT NULL
                    );
        
                    CREATE TABLE IF NOT EXISTS "offers" (
                        id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                        parameters TEXT
                    );
        
                    CREATE TABLE IF NOT EXISTS "orders" (
                        id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                        customer_id INTEGER NOT NULL REFERENCES "customers",
                        organizer_id INTEGER NOT NULL REFERENCES "organizers",
                        offer_id INTEGER NOT NULL REFERENCES "offers",
                        order_parameters TEXT,
                        status TEXT NOT NULL,
                        offer_parameters TEXT
                    );
                """;
                String[] queries = sql.split(";");
                try (Statement stmt = conn.createStatement()) {
                    for (String query : queries) {
                        if (!query.trim().isEmpty()) {
                            stmt.execute(query.trim());
                        }
                    }
//                    System.out.println("Database successfully initialized!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    protected void connect() throws SQLException {
        if (conn != null)
            return;
        String url = "jdbc:sqlite:db.sqlite";
        conn = DriverManager.getConnection(url);
    }

    protected void disconnect() throws SQLException {
        if (conn == null)
            return;
        conn.close();
        conn = null;
    }
}
