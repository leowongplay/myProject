package src.DataBase;

import java.io.Console;
import java.sql.*;
import java.util.Scanner;

import oracle.jdbc.driver.*;

public class DataBase {
    private static OracleConnection conn;
//    private static final String username = "\"12346789d\"";  //Your SID
//    private static final String pwd = "abcd"; //Your oracle pw
    private static final String url = "jdbc:oracle:thin:@studora.comp.polyu.edu.hk:1521:dbms";
    private static DataBase dataBase = null;


    public DataBase(){
        System.out.println("Loading...");
        dataBase = this;
        dbConnect();
    }

    public static DataBase getDataBase() {return dataBase;}

    public static void dbConnect() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter your oracle username: ");    // Your Oracle ID with double quote
            String username = scanner.nextLine();         // e.g. "98765432d"
            System.out.print("Enter your oracle password: ");    // Password of your Oracle Account
            String pwd = scanner.nextLine();
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            conn = (OracleConnection) DriverManager.getConnection(url, username, pwd);
            // System.out.println("Successfully connected.");
        }
        catch (SQLException e) {
            e.printStackTrace();
            System.out.println("DataBase connection failed!");
        }
    }

    public static void dbDisConnect() {
        try{
            conn.close();
            System.out.println("DataBase disconnected!");
        }
        catch (SQLException e) {
            System.out.println("Disconnection failed!");
        }
    }

    public ResultSet query(String Sql){
        ResultSet resultSet = null;
        try {
            Statement statement = conn.createStatement();
            resultSet = statement.executeQuery(Sql);
        }
        catch (SQLException e) {
            System.out.println("Query Failed!");
            e.printStackTrace();
        }
        return resultSet;
    }

    public void modifyTable(String Sql) throws SQLException {
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate(Sql);
        }
        catch (SQLException e) {
            System.out.println("Modify Failed!");
            e.printStackTrace();
        }
    }
}