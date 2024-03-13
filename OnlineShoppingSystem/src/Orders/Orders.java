package src.Orders;

import src.DataBase.DataBase;
import src.ShoppingCart.Cart;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Orders {

    private String createNewOrderID() throws SQLException {
        DataBase db = DataBase.getDataBase();
        String sql = "SELECT MAX(OrderID) As key FROM Orders";
		ResultSet rs = db.query(sql);
        int keyNumber = 0;
        if (rs.next()) {
            String latestKey = rs.getString("key");
            if (latestKey != null) {
                String numberPart = latestKey.substring(1); // Extract the numeric part
                try {
                    keyNumber = Integer.parseInt(numberPart); // Convert the numeric part to int
                    keyNumber++;         // Increment the value
                } catch (NumberFormatException e) {
                    System.out.println("Error in creating a new Orders key");
                }
            }
        }
        String newKey = String.format("O%04d", keyNumber);
        return newKey;
    }
    public String createOrder(String AID, String address) throws SQLException {
        DataBase db = DataBase.getDataBase();
        String unPaidID = getUnPaidOrderID(AID);
        if (unPaidID != null) {
            System.out.println("You have a order not yet paid!\nPaid it first before creating a new one.");
            return null;
        }
        String orderKey = null;
        try {
            Cart cart = new Cart();
            ResultSet rs = cart.getCart(AID);
            if (!rs.next()) {
                System.out.println("No any items in your shopping cart.\nLet's go and purchase!");
                return null;
            }
            else {
                orderKey = createNewOrderID();
                String sql = "INSERT INTO Orders (OrderID, Address, OrderDate, AccountID, PaymentStatus, ShippingStatus)" +
                        " VALUES ('"+ orderKey +"', '"+ address+"',TO_DATE('"+ getDate()+"', 'YYYY-MM-DD'), '" +
                        AID+ "', 'Pending', 'WaitForPaid')";
                db.modifyTable(sql);
                do {
                    String productID = rs.getString("ProductID");
                    int quantity = rs.getInt("Quantity");
                    String checkingSql = "SELECT ProductID, Stock FROM Product WHERE ProductID = '" + productID + "'";

                    ResultSet checkRs = db.query(checkingSql);
                    if (!checkRs.next()){
                        System.out.println("Sorry, the product '" + productID + "' has been removed.\nNo longer able to sale." +
                                                   "\nSo we removed it in your shopping cart.\nPlease try again.");
                        String delProductInCart = "DELETE FROM ShoppingCart WHERE ProductID = '" + productID + "' AND AccountID = '" + AID+"'";
                        db.modifyTable(delProductInCart);
                        delOrder(orderKey);
                        return null;
                    }
                    int stock = checkRs.getInt("Stock");
                    if (quantity > stock){
                        System.out.println("The maximum stock of product '"+ productID +"' we have is " + stock + ". \nPlease change the quantity first.");
                        delOrder(orderKey);
                        return null;
                    }
                    String sqls = "INSERT INTO OrderDetails (Quantity, ProductID, OrderID)" +
                                          " VALUES ("+ quantity +", '"+ productID+"','" +orderKey+ "')";
                    db.modifyTable(sqls);
                    String changeStockSql = "UPDATE Product SET Stock = Stock - " + quantity + " WHERE ProductID = '" + productID + "'";
                    db.modifyTable(changeStockSql);
                }
                while (rs.next());

                cart.delFromCart(AID);
                System.out.println("Successfully create order!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            
        }
        return orderKey;
    }

    public void delUnPaidOrder(String AID) {
        DataBase db = DataBase.getDataBase();
        try {
            String unPaidOrderID = getUnPaidOrderID(AID);
            String productInOrder = "SELECT ProductID, Quantity FROM OrderDetails WHERE OrderID = '" + unPaidOrderID + "'";
            ResultSet rs = db.query(productInOrder);
            while (rs.next()) {
                String PID = rs.getString("ProductID");
                int quan = rs.getInt("Quantity");
                String updateStockSql = "UPDATE Product SET Stock = Stock + " + quan + " WHERE ProductID = '" + PID + "'";
                db.modifyTable(updateStockSql);
                String deleteInTable = "DELETE FROM OrderDetails WHERE OrderID = '" + unPaidOrderID + "' AND ProductID = '" + PID + "'";
                db.modifyTable(deleteInTable);
            }
            String updateStatus = "UPDATE Orders SET PaymentStatus = 'Cancelled', ShippingStatus = 'Cancelled' WHERE OrderID = '" + unPaidOrderID + "'";
            db.modifyTable(updateStatus);
            System.out.println("This order is successfully deleted.");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void delOrder(String OID) {
        DataBase db = DataBase.getDataBase();
        try {
            String productInOrder = "SELECT ProductID, Quantity FROM OrderDetails WHERE OrderID = '" + OID + "'";
            ResultSet rs = db.query(productInOrder);
            while (rs.next()) {
                String PID = rs.getString("ProductID");
                int quan = rs.getInt("Quantity");
                String updateStockSql = "UPDATE Product SET Stock = Stock + " + quan + " WHERE ProductID = '" + PID + "'";
                db.modifyTable(updateStockSql);
            }
            String deleteInTable = "DELETE FROM OrderDetails WHERE OrderID = '" + OID + "'";
            db.modifyTable(deleteInTable);
            String updateStatus =  "DELETE FROM Orders WHERE OrderID = '" + OID +"'";
            db.modifyTable(updateStatus);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getDate() throws SQLException {
        DataBase db = DataBase.getDataBase();
        String getDateSql = "SELECT TO_CHAR (SYSDATE, 'YYYY-MM-DD') \"NOW\" FROM DUAL";
        ResultSet rs = db.query(getDateSql);
        String date = null;
        if (rs.next()) {
            date = rs.getString("NOW");
        }
        else System.out.println("Get date error!");
        return date;
    }

    public String getUnPaidOrderID(String AID) throws SQLException {
        DataBase db = DataBase.getDataBase();
        String sql = "SELECT OrderID FROM Orders WHERE PaymentStatus = 'Pending' AND AccountID = '"+ AID +"'";
        ResultSet rs = db.query(sql);
        if (!rs.next()) {
            return null;
        }
        else {
			String OrderID = rs.getString("OrderID");
            return OrderID;
        }
    }
    public void printOrderDetails(String OID) throws SQLException {
        DataBase db = DataBase.getDataBase();
        String sql = "SELECT ProductID, Quantity FROM OrderDetails WHERE OrderID = '"+ OID +"'";
        ResultSet rs = db.query(sql);
        if (!rs.next()) {
            System.out.println("No order details found!");
        }
        else {
            do {
                String ProductID = rs.getString("ProductID");
                int Quantity = rs.getInt("Quantity");
                System.out.println("[ ProductID : " + ProductID + ", Quantity : " + Quantity + " ]");
            }
            while (rs.next());
            String totalPriceSql = "SELECT SUM(P.Price * OD.Quantity) AS totalPrice FROM Orders O, Product P, OrderDetails OD " +
                    "WHERE P.ProductID = OD.ProductID AND OD.OrderID = O.OrderID AND O.OrderID = '" + OID + "'";
            ResultSet totalPriceRs = db.query(totalPriceSql);
            totalPriceRs.next();
            System.out.println("The total price of this order is $" + totalPriceRs.getDouble("totalPrice"));
            String sqladd = "SELECT address FROM Orders WHERE OrderID = '"+ OID + "'";
            ResultSet rss = db.query(sqladd);
            rss.next();
            System.out.println("The shipping address of this order is " + rss.getString("address"));
        }
    }
    public void undatePaymentStatus(String orderID){
        DataBase db = DataBase.getDataBase();
        try {
            String sql = "UPDATE Orders SET PaymentStatus = 'Paid', ShippingStatus = 'Preparing' WHERE OrderID = '"+orderID+"'";
            db.modifyTable(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
