package src.Product;

import src.DataBase.DataBase;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Product {

	public void printAllProduct() throws SQLException {
		DataBase db = DataBase.getDataBase();
		String sql = "SELECT * FROM Product";
		ResultSet rs = db.query(sql);
		printProduct(rs);
	}

	private void printProduct(ResultSet rs) throws SQLException {
		if (!rs.next()) {
			System.out.println("No data!");
		}
		else {
			do{
				String productID = rs.getString("ProductID");
				String description = rs.getString("Description");
				double price = rs.getDouble("Price");
				int stock = rs.getInt("Stock"); // to be change
				System.out.println("[ ProductID : " + productID + ", Description : " + description + ", Price : " + price +
						                   ", Stock : " + stock + " ]");
			}
			while (rs.next());
		}
	}
	public void printProductDetail(String PID) throws SQLException {
		DataBase db = DataBase.getDataBase();
		String sql = "SELECT * FROM Product WHERE productID = '"+ PID + "'";
		ResultSet rs = db.query(sql);
		if (!rs.next()) {
			System.out.println("Cannot find the product!\nMake sure you have input correctly.");
		}
		else {
			System.out.println("Here is the product details of ProductID " + PID);
			System.out.println("");
			String productID = rs.getString("ProductID");
			String description = rs.getString("Description");
			double price = rs.getDouble("Price");
			int stock = rs.getInt("Stock"); // to be change
			String specifications = rs.getString("Specifications");
			String customerReviews = rs.getString("CustomerReviews");
			String category = rs.getString("Category");
			String brand = rs.getString("Brand");
			System.out.println("ProductID : " + productID + "\nDescription : " + description + "\nPrice : " + price +
					                   "\nStock : " + stock + "\nSpecifications : " + specifications +
					                   "\nCustomerReviews : " + customerReviews +
					                   "\nCategory : " + category + "\nBrand : " + brand);
		}
	}
	public void filter(String criteria, String op, String str) throws SQLException {
		System.out.println("Here are the items searched:");
		DataBase db = DataBase.getDataBase();
		String sql = "SELECT * FROM Product WHERE " + criteria + " " + op + " '" + str+"'";
		ResultSet rs = db.query(sql);
		printProduct(rs);
	}
	public void filterShow(String criteria) throws SQLException {
		System.out.println("Here is the distinct " + criteria + ":");
		DataBase db = DataBase.getDataBase();
		String sql = "SELECT DISTINCT " + criteria + " FROM Product";
		ResultSet rs = db.query(sql);
		if (!rs.next()) {
				System.out.println("No data!");
			}
		else {
			do {
				String cri = rs.getString(criteria);
				System.out.println(cri);
			}
			while (rs.next());
		}
	}

	public void filterPriceRange(double min, double max) throws SQLException {
		if (min>max){
			System.out.println("Minimum price should smaller than maximum price.");
			return;
		}
		if (min < 0 || max < 0){
			System.out.println("Price should be a positive number.");
			return;
		}
		System.out.println("Here are the items in between $"+ min + " and $" + max +" :");
		DataBase db = DataBase.getDataBase();
		String sql = "SELECT * FROM Product WHERE Price >= " + min + "AND Price <= " + max ;
		ResultSet rs = db.query(sql);
		printProduct(rs);
	}

	public int getStock(String PID){
		int stock = -1;
		try {
			DataBase db = DataBase.getDataBase();
			String findStockSql = "SELECT stock FROM Product WHERE ProductID = '" + PID + "'";
			ResultSet rsStock = db.query(findStockSql);
			if (!rsStock.next()) {
				System.out.println("This product does not exist!");
			} else {
				stock = rsStock.getInt("Stock");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Error in get stock.");
		}
		return stock;
	}
}
