package src;

import src.DataBase.DataBase;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import src.Contorl.*;
import src.UserAccount.UserAccount;

public class OSS {
	private static Scanner scan = new Scanner(System.in);
	public static void main(String[] args) throws SQLException, IOException, InterruptedException {
		new DataBase();
		Welcome();
	}
	public void login()throws SQLException, IOException, InterruptedException{
		src.DataBase.DataBase db = src.DataBase.DataBase.getDataBase();
		String username, password;
		String AccID ;
		scan.nextLine();
		System.out.println("==============================================");
		System.out.print("Please enter the Username or AdminID and password step by step\nUsername or AdminID--> ");
		username = scan.nextLine();
		System.out.print("Please enter the password --> ");
		password = scan.nextLine();
		String sql = "SELECT AdminID FROM Admin WHERE AdminID = '" + username +"' AND Password = '"  + password + "'";
		ResultSet rs = db.query(sql);
			if (!rs.next()) {
				String sqltemp = "SELECT AccountID FROM UserAccount WHERE UserName = '" + username + "' AND Password = '" + password + "'";
				rs = db.query(sqltemp);
				if (!rs.next()) {
					System.out.println("Sorry, there are no Accounts that fulfill your username and password here, please try again");
					System.out.println("Press enter to continue");
					login();
				} else {
					System.out.println("Welcome " + username + "!");
					AccID = rs.getString(1);
					Controller tempController = new Controller();
					tempController.ReturnIDToController(AccID);
					tempController.mainPage();

				}
			} else {
				System.out.println("Welcome Admin" + username + "!");
				Admin temp1 = new Admin();
				AccID = rs.getString(1);
				temp1.mainPageAdm(AccID);
			}
	}


	public static void  Welcome() throws SQLException, IOException, InterruptedException{
		//  int ind ;
		System.out.println("""
                ==============================================
                Welcome to our Online Shopping System!

                 1)Create Account
                 2)Login 

                ==============================================""");
		System.out.print("Please enter the number --> ");
		char ind = scan.next().charAt(0);
		// ind = Integer.parseInt(String.valueOf(scan));
		String AccID ;
		switch (ind) {
			case '1':{
				UserAccount uatemp = new UserAccount();
				AccID =uatemp.CreatUserAcc();
				Controller temp1  = new Controller() ;
				temp1.ReturnIDToController(AccID);
				temp1.mainPage();
				break;
			}
			case '2':{
				OSS temp = new OSS();
				temp.login();
				break;
			}
			default:{
				System.out.println("Sorry, wrong input, please enter again.");
				Welcome();
			}
		}

	}
}
