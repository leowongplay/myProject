package src.ShoppingCart;

import src.DataBase.DataBase;
import src.Product.Product;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Cart {

	public void addToChat(String AID, String PID, int quantity) throws SQLException {
		DataBase db = DataBase.getDataBase();
		try {
			if (quantity <= 0) {
				System.out.println("Quantity should be a non-zero integer.");
				return;
			}
			String sql = "SELECT * FROM Product WHERE productID = '" + PID + "'";
			ResultSet rs = db.query(sql);
			if (!rs.next()) {
				System.out.println("Cannot find the product!\nMake sure you have input correctly.");
			} else {
				Product pr = new Product();
				int stock = pr.getStock(PID);
				if (stock < quantity) {
					System.out.println("This product don't have enough stock.");
					return;
				}
				String ifExistedsql = "SELECT Quantity FROM ShoppingCart WHERE productID = '" + PID + "' AND AccountID = '" + AID + "'";
				ResultSet ifExistedRs = db.query(ifExistedsql);
				if (!ifExistedRs.next()) {
					String updateCartSql = "INSERT INTO ShoppingCart (ProductID, AccountID, Quantity) " +
							                       "VALUES ('" + PID + "', '" + AID + "', " + quantity + ")";
					db.modifyTable(updateCartSql);
				} else {
					String changeStockSql = "UPDATE ShoppingCart SET Quantity = Quantity + " + quantity + " WHERE ProductID = '" + PID +
							                        "' AND AccountID = '" + AID + "'";
					db.modifyTable(changeStockSql);
				}
				System.out.println(PID + " successfully add to cart.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public ResultSet getCart(String AID) {
		DataBase db = DataBase.getDataBase();
		String sql = "SELECT S.ProductID, P.Description, S.Quantity, P.Price*S.Quantity AS Total_Price FROM ShoppingCart S, Product P" +
				             " WHERE S.ProductID = P.ProductID AND S.AccountID = '" + AID + "'";
		ResultSet rs = db.query(sql);
		return rs;
	}

	public void delFromCart(String AID) throws SQLException {
		DataBase db = DataBase.getDataBase();
		try {
			String sql = "DELETE FROM ShoppingCart WHERE AccountID = '" + AID + "'";
			db.modifyTable(sql);

		} catch (SQLException e) {
			e.printStackTrace();
			
		}
	}

	public void printCart(String AID) throws SQLException {
		ResultSet rs = getCart(AID);
		if (!rs.next()) {
			System.out.println("No any items in your shopping cart!");
		} else {
			do {
				String productID = rs.getString("ProductID");
				int quantity = rs.getInt("Quantity");
				System.out.println("[ ProductID : " + productID + " Quantity : " + quantity + " ]");
			}
			while (rs.next());
		}
	}

	public void changeQuantity(String AID, String PID, int newQuantity) throws SQLException {
		DataBase db = DataBase.getDataBase();
		try {
			if (newQuantity < 0) {
				System.out.println("Quantity should be a positive integer.");
				return;
			}
			Product pr = new Product();
			int stock = pr.getStock(PID);
			if (stock == -1) return;
			int quan = getQuantity(AID, PID);
			if (quan == -1) return;
			if (newQuantity == quan) {
				System.out.println("New quantity "+ newQuantity +" is equal to current quantity." );
				return;
			}
			if (newQuantity > stock + quan) {
				System.out.println("Not enough stock!");
			} else {
//				String changeStockSql = "UPDATE Product SET Stock = Stock + " + quan + " - " + newQuantity + " WHERE ProductID = '" + PID + "'";
//				db.modifyTable(changeStockSql);
				String sql;
				if (newQuantity == 0) {
					sql = "DELETE FROM ShoppingCart WHERE ProductID = '" + PID + "' AND AccountID = '" + AID + "'";
					System.out.println("Product " + PID + " successfully deleted.");
				} else {
					sql = "UPDATE ShoppingCart SET Quantity = " + newQuantity + " WHERE ProductID = '" + PID + "' AND AccountID = '" +
							      AID + "'";
					System.out.println("Quantity successfully changed to " + newQuantity);
				}
				db.modifyTable(sql);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			
		}
	}

	private int getQuantity(String AID, String PID) throws SQLException {
		int quan = -1;
		try {
			DataBase db = DataBase.getDataBase();
			String findQuantitySql = "SELECT Quantity FROM ShoppingCart WHERE AccountID = '" + AID +
					"' AND ProductID = '" + PID + "'";
			ResultSet findQuantityRS = db.query(findQuantitySql);
			if (!findQuantityRS.next()) {
				System.out.println("This product does not exist!");
			}
			else quan = findQuantityRS.getInt("Quantity");

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Error!");
		}
		return quan;
	}



	public double getTotalPrice(String AID) throws SQLException {
		double total = 0;
		DataBase db = DataBase.getDataBase();
		//	SELECT SUM(P.Price*S.Quantity) AS Total_Price FROM ShoppingCart S, Product P WHERE S.ProductID = P.ProductID AND S.AccountID = 'AC0001';
		String sql = "SELECT SUM(P.Price*S.Quantity) AS Total_Price FROM ShoppingCart S, Product P" +
				             " WHERE S.ProductID = P.ProductID AND S.AccountID = '" + AID + "'";
		ResultSet rs = db.query(sql);
		if (rs.next())
			total = rs.getDouble("Total_Price");
		return total;
	}
}
