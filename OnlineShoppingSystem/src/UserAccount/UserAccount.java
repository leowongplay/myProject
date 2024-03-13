package src.UserAccount;
import src.Contorl.Controller;
import src.DataBase.DataBase;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Scanner;

public class UserAccount{
    private Scanner sc = new Scanner(System.in);
    public void updateAddress( String newAddress, String AccID) throws SQLException{
        src.DataBase.DataBase db = src.DataBase.DataBase.getDataBase();
        String sql = "UPDATE UserAccount SET Address ='" + newAddress + "' WHERE AccountID = '" + AccID +"'" ;
        db.modifyTable(sql);
    }
    public String getUserName(String AID) throws SQLException {
        DataBase db = DataBase.getDataBase();
        String sql = "SELECT USERNAME FROM UserAccount WHERE AccountID = '" + AID + "'";
        ResultSet rs = db.query(sql);
        rs.next();
        return rs.getString("Username");
    }
    public String getAddress(String AID) {
        String address = null;
        try {
            DataBase db = DataBase.getDataBase();
            String sql = "SELECT Address FROM UserAccount WHERE AccountID = '" + AID + "' ";
            ResultSet rs = db.query(sql);
            rs.next();
            address = rs.getString("Address");
        } catch (SQLException e) {
            System.out.println("Address not found!");
        }
        return address;
    }

    public void getInformation(String AccID)throws SQLException, IOException, InterruptedException{
        src.DataBase.DataBase db = src.DataBase.DataBase.getDataBase();
        String sql = "Select * From UserAccount Where AccountID ='" + AccID +"'";
        ResultSet rs = db.query(sql);
        rs.next();
        String Username  = rs.getString("Username");
        String email = rs.getString("EmailAddress");
        String AccountID = rs.getNString("AccountID");
        String FN = rs.getString("FirstName");
        String LN = rs.getString("LastName");
        String Address = rs.getString("Address");
        System.out.println("""
				==============================================
				Information:""");
        System.out.println("\n\nUsername: "+Username);
        System.out.println("Email Address: "+ email);
        System.out.println("AccountID: "+ AccountID);
        System.out.println("Firstname: "+ FN);
        System.out.println("Lastname: "+ LN);
        System.out.println("Address: "+ Address);
        System.out.println("""
				==============================================""");
        ChangeInfo(AccID);
    }

    public String CreatUserAcc()throws SQLException, IOException, InterruptedException{
        src.DataBase.DataBase db = src.DataBase.DataBase.getDataBase();
        String emailAddress = null , password = null , username = null , firstName = null , lastName = null , address = null;
        System.out.println("""
				==============================================
				You are now Creating Account, Please enter the following one by one :
                

				==============================================""");
        System.out.print("Please enter the username --> ");
        username = sc.nextLine();
        String sql = "SELECT COUNT(*) FROM UserAccount WHERE username = '" + username + "'";
        ResultSet rs = db.query(sql);
        rs.next();
        String checkname = rs.getString(1);
        if(checkname.equals("0") ){
            boolean check = true;
            while(check) {
                System.out.print("Please enter the password --> ");
                password = sc.nextLine();
                System.out.print("Please enter the password again-->");
                String tempStr2 = sc.nextLine();
                if(!Objects.equals(password, tempStr2)){
                    System.out.println("\n\nWrong input");
                }
                else{
                    check = false;
                }
            }


            System.out.print("Please enter the email Address --> ");
            emailAddress = sc.nextLine();
            if(!emailAddress.contains("@")){
                Boolean check1 = true;
                while(check1 == true) {
                    System.out.print("Invalid email, Please enter again:\nPlease enter the email Address --> ");
                    emailAddress = sc.nextLine();
                    if(emailAddress.contains("@")){
                        check1 = false;
                    }
                }
            }
            System.out.print("Please enter the firstname --> ");
            firstName = sc.nextLine();
            System.out.print("Please enter the lastname --> ");
            lastName = sc.nextLine();
            System.out.print("Please enter the address --> ");
            address = sc.nextLine();
        }
        else{
            System.out.println("Sorry, this username has existed.\nPlease try again.");
            CreatUserAcc();

        }

        String sqlca = "SELECT MAX(AccountID) As key From UserAccount";
        ResultSet rsca = db.query(sqlca);
        int keyNumber = 0;
        if (rsca.next()) {
            String latestKey = rsca.getString("key");
            if (latestKey != null) {
                String numberPart = latestKey.substring(2);// Extract the numeric part
                try {
                    //System.out.println(keyNumber);
                    keyNumber = Integer.parseInt(numberPart,10);
                    //System.out.println(keyNumber);// Convert the numeric part to int
                    keyNumber++;         // Increment the value
                } catch (NumberFormatException e) {
                    System.out.println("Error in creating a new Orders key");
                }
            }
        }
        String AccID = String.format("AC%04d", keyNumber);
        //System.out.println(AccID);

        String sqltemp = "INSERT INTO UserAccount (AccountID,EmailAddress,Password,Username,FirstName,LastName,Address)VALUES('" + AccID +"', '" + emailAddress + "', '" +password + "', '" + username + "', '" + firstName + "', '" +lastName+ "', '" +address + "')";
        db.modifyTable(sqltemp);
        System.out.println("Created account successfully");
        return AccID;

    }
    public void ChangeInfo (String ID)throws SQLException, IOException, InterruptedException{
        int ind = 0 ;
        String tempStr  ;
        src.DataBase.DataBase db = src.DataBase.DataBase.getDataBase();
        Controller temp = new Controller();

        System.out.println("""
				==============================================
				You are now changing your personal information, Please enter the number of you want to change:
                
                 1)username 
                 2)Password 
                 3)EmailAddress 
                 4)Address
                 
                 Other action
                 5)Check personal information
                 6)Return main
				==============================================""");
        char opt = sc.next().charAt(0);
        sc.nextLine();
        //sc.next();
        switch(opt) {
            case '1' :{
                System.out.println("Please Enter the new user name:");
                tempStr = sc.nextLine();
                String sqlcheck = "SELECT COUNT(*) FROM UserAccount WHERE username = '" + tempStr + "'";
                ResultSet check = db.query(sqlcheck);
                check.next();
                String returnValue = check.getString(1);
                if(returnValue.equals("0") || returnValue.equals(null) ){
                    String sqltemp = "UPDATE UserAccount SET username ='" + tempStr + "' WHERE AccountID = '" + ID+ "'" ;
                    db.modifyTable(sqltemp);
                    System.out.println("Updated. ");
                    System.out.println("Updated.\nNow returning Homepage... ");
                    temp.mainPage();

                }
                else{
                    System.out.println("This username already exists. Please re-enter it.");
                    ChangeInfo(ID);
                    return;
                }
                break;
            }
            case '2' :{
                Boolean check = true;
                tempStr = null;
                while(check) {
                    System.out.println("Please enter the new password:");
                    tempStr = sc.nextLine();
                    String tempStr2;
                    System.out.print("Please enter the new password again-->");
                    tempStr2 = sc.nextLine();
                    if(!Objects.equals(tempStr, tempStr2)){
                        System.out.println("\n\nWrong input");
                    }
                    else{
                        check = false;
                    }
                }
                String sqltemp = "UPDATE UserAccount SET Password ='" + tempStr + "' WHERE AccountID = '" + ID + "'";
                db.modifyTable(sqltemp);
                System.out.println("Updated.\nNow returning Homepage... ");
                temp.mainPage();

            }

            case '3' :{
                Boolean check = true;
                tempStr = null;
                while(check) {
                    System.out.println("Please Enter the new EmailAddress:");
                    tempStr = sc.nextLine();
                    if(!tempStr.contains("@")){
                        System.out.println("\n\nNot a email address, please input again");
                    }
                    else{
                        check = false;
                    }
                }

                String sqltemp = "UPDATE UserAccount SET EmailAddress ='" + tempStr + "' WHERE AccountID = '" + ID + "'";
                db.modifyTable(sqltemp);
                System.out.println("Updated. ");
                System.out.println("Updated.\nNow returning Homepage... ");
                temp.mainPage();

            }

            case '4' :{
                System.out.println("Please Enter the new Address:");
                tempStr = sc.nextLine();
                String sqltemp = "UPDATE UserAccount SET Address ='" + tempStr + "' WHERE AccountID = '" + ID + "'";
                db.modifyTable(sqltemp);
                System.out.println("Updated.\nNow returning Homepage... ");
                temp.mainPage();

            }
            case '5':{
                getInformation(ID);
            }
            case'6' :{
                System.out.println("Returning...");
                temp.mainPage();
            }


        }
    }
}





