package com.contactbook.Database;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBase {
    private static final String URL = "jdbc:postgresql://localhost:5432/ContactBook";
    private static final String USER = "postgres";
    private static final String PASSWORD = "qamil37";


    public static Connection getConnection() {

        Connection con = null;
        try {
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection(URL,USER, PASSWORD);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }

        return con;

    }

    public static void closeConnection(Connection con) {
        if (con != null){
            try {
                con.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }

        }
    }
}
