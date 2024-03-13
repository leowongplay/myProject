package src.Contorl;
import src.DataBase.DataBase;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import src.InventoryManagement.*;
import src.Product.Product;
import src.Report.Report;


public class Admin {
	private Scanner scan = new Scanner(System.in);
	String AdmID = null;
	private InventoryManagement im = new InventoryManagement();
	private Report report = new Report();
	private Product ps = new Product();

	public void mainPageAdm(String ID) throws SQLException, IOException, InterruptedException {
		Controller.clearScreen();
		DataBase db = DataBase.getDataBase();
		AdmID = ID;
		System.out.println("""
                ================ Admin Main Page ==============
                """);
		String sql = "SELECT NAME FROM ADMIN WHERE ADMINID = '" + AdmID + "'";
		ResultSet rs = db.query(sql);
		rs.next();
		System.out.println("Welcome " + rs.getString("Name"));
		System.out.print("""
                If you want to go to:

                Inventory Management        -->  Enter '1'
                Reporting and Analytics     -->  Enter '2'
                Quit                        -->  Enter 'Q'
                ===============================================
                """);
		while (true) {
			System.out.print("Enter -> ");
			char opt = scan.next().charAt(0);
			switch (opt) {
				case '1': {
					Controller.clearScreen();
					managementPage();
					return;
				}
				case '2': {
					Controller.clearScreen();
					reportPage();
					return;
				}
				case 'Q': {
					System.out.println("Thank you for using this system!");
					DataBase.dbConnect();
					System.exit(0);
					return;
				}
				default: {
					System.out.println("Please input a correct value!");
					mainPageAdm(AdmID);
				}
			}
		}
	}

	private void managementPage() throws SQLException, IOException, InterruptedException{
		System.out.println("""
                =================================
                Command List
                Add Product 		--> Enter '1'
                Delete Product 		--> Enter '2'
                Update Product 		--> Enter '3'
                Update Stock		--> Enter '4'
                Update Price		--> Enter '5'
                Back				--> Enter 'B'
                =================================
                """);
		System.out.print("Enter -> ");
		char option = scan.next().charAt(0);
		scan.nextLine();
		switch (option) {
			case '1' : {
				try {
					Controller.clearScreen();
					System.out.println("Existing Product: ");
					ps.printAllProduct();

					System.out.print("Please input the productID of the new product ->");
					String productID = scan.nextLine();
					if (productID.isBlank()) {
						System.out.println("Invalid Input!");
						managementPage();
						return;
					}

					System.out.print("Please input the description of the new product ->");
					String description = scan.nextLine();
					if (description.isBlank()) {
						System.out.println("Invalid Input!");
						managementPage();
						return;
					}

					System.out.print("Please input the price of the new product ->");
					float price = scan.nextFloat();
					scan.nextLine();
					if (price <= 0) {
						System.out.println("Invalid Input!");
						managementPage();
						return;
					}

					System.out.print("Please input the specification of the new product ->");
					String specification = scan.nextLine();
					if (specification.isBlank()) {
						System.out.println("Invalid Input!");
						managementPage();
						return;
					}

					System.out.print("Please input the customer review of the new product ->");
					String customerReviews = scan.nextLine();
					if (customerReviews.isBlank()) {
						System.out.println("Invalid Input!");
						managementPage();
						return;
					}

					System.out.print("Please input the category of the new product ->");
					String category = scan.nextLine();
					if (category.isBlank()) {
						System.out.println("Invalid Input!");
						managementPage();
						return;
					}

					System.out.print("Please input the brand of the new product ->");
					String brand = scan.nextLine();
					if (brand.isBlank()) {
						System.out.println("Invalid Input!");
						managementPage();
						return;
					}

					System.out.print("Please input the stock of the new product ->");
					int stock = scan.nextInt();
					if (stock <= 0) {
						System.out.println("Invalid Input!");
						managementPage();
						return;
					}

					im.addProduct(productID, description, price, specification, customerReviews, category, brand, stock);
					managementPage();
					return;

				}catch (Exception e) {
					System.out.println("Invalid Input!");
					managementPage();
					return;
				}
			}
				case '2' : {
					Controller.clearScreen();
					ps.printAllProduct();
					System.out.print("Please input the productID of the product that you want to delete ->");
					String productID = scan.nextLine();
					if(productID.isBlank()){System.out.println("ProductID cannot be empty");managementPage();}
					im.deleteProduct(productID);
					managementPage();
					return;
				}
				case '3' : {
					Controller.clearScreen();
					ps.printAllProduct();
					System.out.print("Please input the productID of the product that you want to update ->");
					String productID = scan.nextLine();
					if(productID.isBlank()){System.out.println("ProductID cannot be empty");managementPage();return;}
					System.out.println("Criterion: Description/Specifications/CustomerReviews/Category/Brand");
					System.out.print("Please input the criterion of the product that you want to update ->");
					String criterion = scan.nextLine();
					if (criterion.equals("Description") || criterion.equals("Specifications") || criterion.equals("CustomerReviews") || criterion.equals("Category") || criterion.equals("Brand") || !criterion.isBlank()) {
						System.out.print("Please input the update value for the criterion ->");
						String updateValue = scan.nextLine();
						if(updateValue.isBlank()){System.out.println("Update Value cannot be empty");managementPage();return;}
						im.updateProduct(productID, criterion, updateValue);
						managementPage();
						return;
					}else{
						System.out.println("Invalid Input");
						managementPage();
						return;
					}
				}
				case '4' : {
					Controller.clearScreen();
					ps.printAllProduct();
					System.out.print("Please input the productID of the product that you want to update ->");
					String productID = scan.nextLine();
					if(productID.isBlank()){System.out.println("ProductID cannot be empty");managementPage();return;}
					try {
						System.out.print("Please input the stock of the new product ->");
						int stock = scan.nextInt();
						if (stock <= 0) {
							System.out.println("Invalid Input");
							managementPage();
							return;
						}
						im.updateStock(productID, stock);
						managementPage();
						return;
					}catch (Exception e){
						System.out.println("Invalid Input");
						managementPage();
						return;
					}
				}
				case '5' : {
					Controller.clearScreen();
					ps.printAllProduct();
					System.out.print("Please input the productID of the product that you want to update ->");
					String productID = scan.nextLine();
					if(productID.isBlank()){System.out.println("ProductID cannot be empty");managementPage();}
					try {
						System.out.print("Please input the price of the new product ->");
						float price = scan.nextFloat();
						if (price <= 0) {
							System.out.println("Invalid Input");
							managementPage();
							return;
						}
						im.updatePrice(productID, price);
						managementPage();
						return;
					} catch (Exception e){
						System.out.println("Invalid Input");
						managementPage();
						return;
					}
				}
				case 'B' : {
					Controller.clearScreen();
					mainPageAdm(AdmID);
					return;
				}
				default: {
					Controller.clearScreen();
					System.out.println("Please input a correct value");
					managementPage();
				}
			}
		}
		private void reportPage() throws SQLException, IOException, InterruptedException{
			System.out.println("""
                =================================
                Command List
                See sales data      --> Enter '1'
                See customer review --> Enter '2'
                Add Report          --> Enter '3'
                Delete Report		--> Enter '4'
                Generate Report		--> Enter '5'
                Add Promotion		--> Enter '6'
                Delete Promotion	--> Enter '7'
                Back				--> Enter 'B'
                =================================
                """);
			System.out.print("Enter ->");
			char option = scan.next().charAt(0);
			scan.nextLine();
			switch (option) {
				case '1': {
					Controller.clearScreen();
					report.salesDataAnalyse();
					reportPage();
					return;
				}
				case '2': {
					Controller.clearScreen();
					System.out.print("Please input the productID ->");
					String productID = scan.nextLine();
					if(productID.isBlank()){System.out.println("ProductID cannot be empty");reportPage();}
					report.customerReview(productID);
					reportPage();
					return;
				}
				case '3': {
					try {
						Controller.clearScreen();
						System.out.print("Please input the ReportID -> ");
						String ReportID = scan.nextLine();
						if (ReportID.isBlank()) {
							System.out.println("ReportID cannot be empty");
							reportPage();
							return;
						}
						System.out.print("Please input the PopularProduct ->");
						String PopularProduct = scan.nextLine();
						if (PopularProduct.isBlank()) {
							System.out.println("Popular Product cannot be empty");
							reportPage();
							return;
						}
						System.out.print("Please input the UserBehavior ->");
						String UserBehavior = scan.nextLine();
						if (UserBehavior.isBlank()) {
							System.out.println("User Behavior cannot be empty");
							reportPage();
							return;
						}
						System.out.print("Please input the DataOnSales ->");
						String DataOnSales = scan.nextLine();
						if (DataOnSales.isBlank()) {
							System.out.println("Data On Sales cannot be empty");
							reportPage();
							return;
						}
						System.out.print("Please input the AdminID ->");
						String AdminID = scan.nextLine();
						if (AdminID.isBlank()) {
							System.out.println("AdminID cannot be empty");
							reportPage();
							return;
						}
						report.manualAddReport(ReportID, PopularProduct, UserBehavior, DataOnSales, AdminID);
						reportPage();
						return;
					} catch (Exception e){
						System.out.println("Invalid Input!");
						reportPage();
						return;
					}

				}
				case '4' : {
					Controller.clearScreen();
					System.out.print("Please input the reportID ->");
					String reportID = scan.nextLine();
					if(reportID.isBlank()){System.out.println("ProductID cannot be empty");reportPage();}
					report.deleteReport(reportID);
					reportPage();
					return;
				}
				case '5' : {
					Controller.clearScreen();
					report.generateReport();
					reportPage();
					return;
				}
				case '6' : {
					try {
						Controller.clearScreen();
						System.out.println("Existing Product: ");
						report.printPromotion();

						System.out.print("Please input the PromotionID of the new Promotion ->");
						String PromotionID = scan.nextLine();
						if (PromotionID.isBlank()) {
							System.out.println("Invalid Input!");
							reportPage();
							return;
						}

						System.out.print("Please input the Price of the new Promotion ->");
						float Price = scan.nextFloat();
						scan.nextLine();
						if (Price <= 0) {
							System.out.println("Invalid Input!");
							reportPage();
							return;
						}

						System.out.print("Please input the Discount of the new Promotion ->");
						float Discount = scan.nextFloat();
						scan.nextLine();
						if (Discount <= 0) {
							System.out.println("Invalid Input!");
							reportPage();
							return;
						}

						System.out.print("Please input the ReportID of the new Promotion ->");
						String ReportID = scan.nextLine();
						if (ReportID.isBlank()) {
							System.out.println("Invalid Input!");
							reportPage();
							return;
						}

						System.out.print("Please input the AdminID of the new Promotion ->");
						String AdminID = scan.nextLine();
						if (AdminID.isBlank()) {
							System.out.println("Invalid Input!");
							reportPage();
							return;
						}

						System.out.print("Please input the StartDate of the new Promotion ->");
						String StartDate = scan.nextLine();
						if (StartDate.isBlank()) {
							System.out.println("Invalid Input!");
							reportPage();
							return;
						}

						System.out.print("Please input the EndDate of the new Promotion ->");
						String EndDate = scan.nextLine();
						if (EndDate.isBlank()) {
							System.out.println("Invalid Input!");
							reportPage();
							return;
						}

						report.addPromotion(PromotionID, Price, Discount, ReportID, AdminID, StartDate, EndDate);
						reportPage();
						return;

					} catch (Exception e) {
						System.out.println("Invalid Input!");
						reportPage();
						return;
					}
				}
				case '7' : {
					Controller.clearScreen();
					System.out.print("Please input the PromotionID ->");
					String PromotionID = scan.nextLine();
					if(PromotionID.isBlank()){System.out.println("PromotionID cannot be empty");reportPage();}
					report.deletePromotion(PromotionID);
					reportPage();
					return;
				}
				case 'B' : {
					Controller.clearScreen();
					mainPageAdm(AdmID);
					return;
				}
				default:{
					Controller.clearScreen();
					System.out.println("Please input a correct value.");
					reportPage();
				}
			}
		}


	}