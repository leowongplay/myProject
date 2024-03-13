
package src.InventoryManagement;

import src.DataBase.DataBase;


import java.sql.*;

public class InventoryManagement {
    public void addProduct(String productID, String Description, float price, String Specifications, String CustomerReviews, String Category, String Brand, int Stock) throws SQLException {
        try {
            DataBase db = DataBase.getDataBase();
            String id = "SELECT * FROM Product WHERE ProductID = '" + productID + "'";
            ResultSet rs = db.query(id);
            String sql = "INSERT INTO Product (ProductID, Description, Price, Specifications, CustomerReviews, Category, Brand, Stock)" +
                    "VALUES('" + productID + "', '" + Description + "', '" + price + "', '" + Specifications + "', '" + CustomerReviews + "', '" + Category + "', '" + Brand + "', '" + Stock + "')";
            if(rs.next()) {
               System.out.println("ProductID is duplicated");
            }else {
                db.modifyTable(sql);
                System.out.println("New Product had been added. The productID of the new product is " + productID);
            }
        }catch (Exception e){
            System.out.println("Invalid Input!.");
        }
    }

    public void deleteProduct(String productID) throws SQLException {
        DataBase db = DataBase.getDataBase();
        String sql = "DELETE FROM Product WHERE ProductID = '" + productID + "'";
        String id = "SELECT * FROM Product WHERE ProductID = '" + productID + "'";
        ResultSet rs = db.query(id);
        if(!rs.next()){
            System.out.println("No data!");
        }else {
            db.modifyTable(sql);
            System.out.println("Product:" + productID + " had been deleted.");
        }
    }

    public void updateProduct(String productID, String criterion, String changedValue) throws SQLException {
        DataBase db = DataBase.getDataBase();
        String sql = "UPDATE Product SET " + criterion + " = " + "'" + changedValue + "' WHERE ProductID = '" + productID + "'";
        String id = "SELECT * FROM Product WHERE ProductID = '" + productID + "'";
        ResultSet rs = db.query(id);
        if(!rs.next()){
            System.out.println("No data!");
        }else {
            db.modifyTable(sql);
            System.out.println("Product:" + productID + " had made a change on " + criterion);
        }
    }

    public void updateStock(String productID, int stock) throws SQLException {
        DataBase db = DataBase.getDataBase();
        String sql = "UPDATE Product SET Stock = " + stock + " WHERE ProductID = '" + productID + "'";
        String id = "SELECT * FROM Product WHERE ProductID = '" + productID + "'";
        ResultSet rs = db.query(id);
        if(!rs.next() || stock < 0){
            System.out.println("No data!");
        }else {
            db.modifyTable(sql);
            System.out.println("The stock of product: " + productID + " had been changed to " + stock);
        }
    }

    public void updatePrice(String productID, float price) throws SQLException {
        DataBase db = DataBase.getDataBase();
        String sql = "UPDATE Product SET Price = " + price + " WHERE ProductID = '" + productID + "'";
        String id = "SELECT * FROM Product WHERE ProductID = '" + productID + "'";
        ResultSet rs = db.query(id);
        if(!rs.next() || price < 0){
            System.out.println("No data!");
        }else {
            db.modifyTable(sql);
            System.out.println("The Price of product: " + productID + " had been changed to " + price);
        }
    }
}
