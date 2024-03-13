package src.Contorl;

import src.DataBase.DataBase;
import src.Orders.Orders;
import src.Product.Product;
import src.ShoppingCart.Cart;
import src.UserAccount.UserAccount;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

public class Controller {
	private static Scanner scan = new Scanner(System.in);
	private static Product ps = new Product();
	private static Cart cart = new Cart();
	private static Orders order = new Orders();
	private static UserAccount us = new UserAccount();
	private static String AccID ;
	public void ReturnIDToController( String AccIDFromOSS ){
		AccID = AccIDFromOSS;
	}

	public void mainPage() throws SQLException, IOException, InterruptedException {
		System.out.println("""
                ================== Main Page ==================
                """);
		System.out.println("Welcome " + us.getUserName(AccID));
		System.out.print("""
                If you want to go to:

                Product Page                   -->  Enter '1'
                Shopping Cart Page             -->  Enter '2'
                Your personal information      -->  Enter '3'
                Quit                           -->  Enter 'Q'
                ===============================================
                """);
		while (true) {
			System.out.print("Enter -> ");
			char opt = scan.next().charAt(0);
			switch (opt) {
				case '1': {
					clearScreen();
					productPage(); // list details
					return;
				}
				case '2': {
					clearScreen();
					viewCart();
					return;
				}
				case '3': {
					clearScreen();
					UserAccount temp = new UserAccount();
					temp.ChangeInfo(AccID);
					return;
				}
				case 'Q': {
					System.out.println("Thank you for using this system!");
					DataBase.dbDisConnect();
					System.exit(0);
					return;
				}
				default: {
					System.out.println("Please input a correct value!");
				}
			}
		}
	}
	private void productPage() throws SQLException, IOException, InterruptedException {

		System.out.println("""
				================ Product Page =================
				Listing all product         -->  Enter '1'
				Using the filter function   -->  Enter '2'
				Back                        -->  Enter 'B'
				===============================================""");
		while (true){
			System.out.print("Enter -> ");
			char opt = scan.next().charAt(0);
			if (opt == '1') {clearScreen();ps.printAllProduct();addToCartOrListDetailsPage();return;}
			else if (opt == '2') {clearScreen();filterPage();return;}
			else if (opt == 'B') {clearScreen();mainPage();return;}
			else System.out.println("Please input a correct value!");
		}
	}


	private void filterPage() throws SQLException, IOException, InterruptedException {
		clearScreen();
		System.out.println("""
				================ Product filter ===============
				If you want to filter by :

				Product Description           -->  Enter '1'
				Brand                         -->  Enter '2'
				Category                      -->  Enter '3'
				Price larger or equal to      -->  Enter '4'
				Price smaller or equal to     -->  Enter '5'
				Price Range                   -->  Enter '6'
				Back                          -->  Enter 'B'
				==============================================""");
		while (true){
			System.out.print("Enter -> ");
			char opt = scan.next().charAt(0);
			if (opt == '1') {ps.filterShow("Description");filterPrint("Description");break;}
			else if (opt == '2') {ps.filterShow("Brand");filterPrint("Brand");break;}
			else if (opt == '3') {ps.filterShow("Category");filterPrint("Category");break;}
			else if (opt == '4') {
				while (true) {
					try {
						System.out.print("Price: $");
						String inp = scan.next();
						ps.filter("Price", ">=", String.valueOf(Double.parseDouble(inp)));
						break;
					} catch (Exception e) {
						System.out.println("Price should be a positive number.");
					}
				}
				break;
			}
			else if (opt == '5') {
				while (true) {
					try {
						System.out.print("Price: $");
						String inp = scan.next();
						ps.filter("Price", "<=", String.valueOf(Double.parseDouble(inp)));
						break;
					} catch (Exception e) {
						System.out.println("Price should be a positive number.");
					}
				}
				break;
			}
			else if (opt == '6') {
				while (true) {
					try {
						System.out.print("Minimum Price: $");
						String min = scan.next();
						System.out.print("Maximum Price: $");
						String max = scan.next();
						ps.filterPriceRange(Double.parseDouble(min), Double.parseDouble(max));
						break;
					} catch (Exception e) {
						System.out.println("Price should be a positive number.");
					}
				}
				break;
			}
			else if (opt == 'B') {clearScreen();mainPage();return;}
			else System.out.println("Please input a correct value!");
		}
		addToCartOrListDetailsPage();
	}
	private void filterPrint(String type) throws SQLException, IOException, InterruptedException {
		scan.nextLine(); // to prevent read '\n' after nextXXX. That's system error.
		System.out.println("============================================================");
		System.out.println("Enter the '"+ type +"' you want to discover, or back 'B'.");
		System.out.print("Enter -> ");
		String str = scan.nextLine().trim();
		if (str.equals("B")) {clearScreen();filterPage();}
		else ps.filter(type,"=",str);
	}

	private void addToCartOrListDetailsPage() throws SQLException, IOException, InterruptedException {
		System.out.println("""
				=============================================
				If you want to :

				List Product Details            -->  Enter '1'
				Add Product to Shopping Cart    -->  Enter '2'
				Back                            -->  Enter 'B'
				==============================================""");
		while (true) {
			System.out.print("Enter -> ");
			char opt = scan.next().charAt(0);
			switch (opt) {
				case '1': {
					ListDetails();
					return;
				}
				case '2': {
					addChat();
					return;
				}
				case 'B': {
					clearScreen();
					productPage();
					return;
				}
				default: {
					System.out.println("Invalid input!");
				}
			}
		}
	}

	private void ListDetails() throws SQLException, IOException, InterruptedException {
		System.out.println("""
				===========================================
				If you want to see the product details.
				Enter the ProductID.
				Otherwise, enter 'B' to back.
				===========================================""");
		System.out.print("Enter -> ");
		String opt = scan.next();
		if (opt.equals("B")) {clearScreen();productPage();}
		else {ps.printProductDetail(opt);}
		addChat();
	}

	private void addChat() throws SQLException, IOException, InterruptedException {
		System.out.println("""
				==========================================================
				If you want to add the product into your shopping cart.
				Enter the ProductID
				Otherwise, enter 'B' to back.
				==========================================================""");
		System.out.print("Enter -> ");
		String opt = scan.next();
		if (opt.equals("B")) {clearScreen();}
		else {
			while (true) {
				try {
					System.out.print("Enter the quantity you want to purchase -> ");
					String str = scan.next();
					cart.addToChat(AccID, opt, Integer.parseInt(str));
					break;
				} catch (Exception e) {
					System.out.println("Quantity should be an non-zero integer.");
				}
			}
		}
		productPage();
	}

	private void viewCart() throws SQLException, IOException, InterruptedException {
		System.out.println("=============== Shopping Cart =================");
		cart.printCart(AccID);
		System.out.println("The total price in your shopping cart is $" + cart.getTotalPrice(AccID));
		cartPage();
	}

	private void cartPage() throws SQLException, IOException, InterruptedException {
		System.out.println("""
				==============================================
				If you want to :

				Change quantity    -->  Enter '1'
				Delete product     -->  Enter '2'
				Make payment       -->  Enter '3'
				Back               -->  Enter 'B'
				==============================================""");
		while (true) {
			System.out.print("Enter -> ");
			char opt = scan.next().charAt(0);
			switch (opt) {
				case '1': {
					System.out.print("Enter the ProductID -> ");
					String PID = scan.next();
					while (true) {
						try {
							System.out.print("Enter the new quantity -> ");
							String qua = scan.next();
							cart.changeQuantity(AccID, PID, Integer.parseInt(qua));
							break;
						} catch (Exception e) {
							System.out.println("Quantity should be positive integer.");
                        }
                    }
					viewCart();
					return;
				}
				case '2': {
					System.out.print("Enter the ProductID to delete -> ");
					String PID = scan.next();
					cart.changeQuantity(AccID, PID, 0);
					viewCart();
					return;
				}
				case '3': {
					clearScreen();
					paymentPage();
					return;
				}
				case 'B': {
					clearScreen();
					mainPage();
					return;
				}
				default: {
					System.out.println("Invalid input!");
				}
			}
		}
	}

	private void paymentPage() throws SQLException, IOException, InterruptedException {

		System.out.println("================ PaymentPage ================");
		System.out.println("Your shipping address --> " + us.getAddress(AccID));
		System.out.println("""

				If you want to :

				Change Your Address               -->  Enter '1'
				Make Order                        -->  Enter '2'
				Make Payment for Unpaid Order     -->  Enter '3'
				Back                              -->  Enter 'B'
				==============================================""");
		while (true) {
			System.out.print("Enter -> ");
			char opt = scan.next().charAt(0);
			scan.nextLine();
			switch (opt) {
				case '1': {
					clearScreen();
					System.out.println("Enter the new address:");
					String newAddress = scan.nextLine();
					us.updateAddress(newAddress.trim(),AccID);
					System.out.println("Your new shipping address is " + newAddress);
					paymentPage();
					return;
				}
				case '2': {
					clearScreen();
					String orderID = order.createOrder(AccID, us.getAddress(AccID));
					if (orderID != null) payRequestPage(orderID);
					else viewCart();
					return;
				}
				case '3': {
					clearScreen();
					payUnPaidOrder();
					return;
				}
				case 'B': {
					clearScreen();
					viewCart();
					return;
				}
				default: {
					System.out.println("Invalid input!");
				}
			}
		}
	}

	private void payUnPaidOrder() throws SQLException, IOException, InterruptedException {
		System.out.println("""
				=============================================
				Here is the order have not paid yet.""");
		String orderID = order.getUnPaidOrderID(AccID);
		if (orderID != null) {
			System.out.println("OrderID = " + orderID);
			order.printOrderDetails(orderID);
			System.out.println("""
					If you want to :

					Make Payment                   -->  Enter 'P'
					Delete Order                   -->  Enter 'D'
					Back                           -->  Enter 'B'
					==============================================""");
			while (true) {
				System.out.print("Enter -> ");
				String opt = scan.next();
				if (opt.equals("B")) {
					clearScreen();
					paymentPage();
					return;
				}
				else if (opt.equals("D")) {
					System.out.println("Are you sure you want to delete this order?");
					System.out.println("Enter YES 'Y', or NO 'N'");
					while (true) {
						System.out.print("Enter -> ");
						char opt1 = scan.next().charAt(0);
						if (opt1 == 'Y') {order.delUnPaidOrder(AccID);break;}
						else if (opt1 == 'N') {payUnPaidOrder();break;}
						else System.out.println("Invalid input!");
					}
					mainPage();
					return;
				} else if (opt.equals("P")) {
					payRequestPage(orderID);
					return;
				} else System.out.println("Invalid input!");
			}
		} else {
			clearScreen();
			System.out.println("No any unpaid order!");
			System.out.println("You need to make order first!");
			paymentPage();
		}
	}

	private void payRequestPage(String orderID) throws SQLException, IOException, InterruptedException {
		clearScreen();
		System.out.println("""
				=============================================
				If you want to pay by:

				VISA/MasterCard     -->  Enter '1'
				Google Pay          -->  Enter '2'
				Apple Pay           -->  Enter '3'
				Bitcoin             -->  Enter '4'
				Back                -->  Enter 'B'
				==============================================""");
		while (true) {
			System.out.print("Enter -> ");
			char opt = scan.next().charAt(0);
			switch (opt) {
				case '1', '2', '3', '4':
					clearScreen();
					order.undatePaymentStatus(orderID);
					System.out.println("Successfully Paid!\nThank you for your purchase!");
					System.out.println("Directing to the Main Page...");
					mainPage();
					return;
				case 'B':
					clearScreen();
					System.out.println("Your order has not paid yet.");
					paymentPage();
					return;
				default:
					System.out.println("Invalid input!");
			}
		}

	}

	public static void clearScreen() throws IOException, InterruptedException {
		if (System.getProperty("os.name").contains("Windows"))
			new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
		else
			System.out.print("\033[H\033[2J");
	}
}