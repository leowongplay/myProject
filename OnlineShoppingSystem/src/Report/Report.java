
package src.Report;

import src.DataBase.DataBase;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;


public class Report {
    public static void salesDataAnalyse() throws SQLException {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(1);
        //int month = earlier.getMonthValue();
        //int year = earlier.getYear();
        int month = 11;
        int year = 2023;
        DataBase db = DataBase.getDataBase();
        try {
            System.out.println("The rank of product sold last month.");
            String sql = "SELECT DISTINCT od.ProductID, SUM(od.Quantity) AS Total_Quantity FROM Orders o LEFT JOIN OrderDetails od ON o.OrderID = od.OrderID" +
                    " WHERE EXTRACT(MONTH FROM o.OrderDate) = '" + month + "' AND EXTRACT(YEAR FROM o.OrderDate) = '" + year + "' GROUP BY od.ProductID ORDER BY Total_Quantity DESC";
            ResultSet rs = db.query(sql);
            if (!rs.next()) {
                System.out.println("No data!");
            }
            while(rs.next()){
                String id = rs.getString("ProductID");
                int sold = rs.getInt("Total_Quantity");
                System.out.println("ProductID: " + id + " Sold: " + sold);
            }
            rs.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void customerReview(String productID) throws SQLException {
        DataBase db = DataBase.getDataBase();
        System.out.println("Customer review on product: " + productID);
        String sql = "SELECT CustomerReviews FROM Product WHERE ProductID = '" + productID + "'";
        ResultSet rs = db.query(sql);
        if (!rs.next()) {
            System.out.println("No data!");
        }
        String cr = rs.getString("CustomerReviews");
        System.out.println("Customer Review: " + cr);
    }

    public static void printReport() throws SQLException{
        DataBase db = DataBase.getDataBase();
        String all = "SELECT * FROM Report";
        ResultSet idSet = db.query(all);
        while (idSet.next()){
            String rID = idSet.getNString("ReportID");
            System.out.println("ReportID: " + rID);
        }
        idSet.close();
    }

    public static void manualAddReport(String ReportID, String PopularProduct, String UserBehavior, String DataOnSales, String AdminID) throws SQLException {
        DataBase db = DataBase.getDataBase();
        String id = "SELECT ReportID FROM Report WHERE ReportID = '" + ReportID + "'";
        ResultSet rs = db.query(id);
        if (rs.next()) {
            System.out.println("ReportID is duplicated!");
        } else {
            String sql = "INSERT INTO Report(ReportID, PopularProduct, UserBehavior, DataOnSales, AdminID)" +
                    "VALUES('" + ReportID + "', '" + PopularProduct + "', '" + UserBehavior + "', '" + DataOnSales + "', '" + AdminID + "')";
            db.modifyTable(sql);
            System.out.println("1 Row Created.");
        }
    }

        public static void deleteReport(String ReportID) throws SQLException{
            DataBase db = DataBase.getDataBase();
            String sql = "DELETE FROM Report WHERE ReportID = '" + ReportID + "'";
            String id = "SELECT * FROM Report WHERE ReportID = '" + ReportID + "'";
            ResultSet rs = db.query(id);
            if(!rs.next()){
                System.out.println("No Data!");
            }else{
                db.modifyTable(sql);
                System.out.println("Report: " + ReportID + " had been deleted");
            }
        }

        public static void generateReport() throws SQLException{
            DataBase db = DataBase.getDataBase();
            LocalDate now = LocalDate.now();
            LocalDate earlierMonth = now.minusMonths(1);
            int monthM = earlierMonth.getMonthValue();
            int yearM = earlierMonth.getYear();
            LocalDate earlierYear = now.minusYears(1);
            int yearY = earlierYear.getYear();
            boolean dataOnLastYear = true;
            String sql_lastYear = "SELECT * FROM Orders WHERE EXTRACT(YEAR FROM OrderDate) = '" + yearY + "'";
            ResultSet year = db.query(sql_lastYear);
            if(!year.next()){dataOnLastYear = false;}
            System.out.println("""
        =======================================
                    ANALYSIS REPORT""");
            System.out.println("        Create Time: " + now);
            System.out.println("""
        =======================================
                Product Status Information""");


            String totalProduct = "SELECT COUNT(ProductID) FROM Product";
            ResultSet rs_totalProduct = db.query(totalProduct);
            while (rs_totalProduct.next()){
                String totalP = rs_totalProduct.getString("COUNT(ProductID)");
                System.out.println("Total Number of Products: " + totalP);
            }
            rs_totalProduct.close();

            String totalBuy = "SELECT SUM(QUANTITY) FROM OrderDetails";
            ResultSet rs_totalBuy = db.query(totalBuy);
            while(rs_totalBuy.next()) {
                String totalB = rs_totalBuy.getString("SUM(QUANTITY)");
                System.out.println("Total Number of Sold Products: " + totalB);
            }
            rs_totalBuy.close();

            System.out.println("Most Sold Product Last Month: ");
            String mostBuyLastMonth = "SELECT DISTINCT od.ProductID, SUM(od.Quantity) AS Total_Quantity FROM Orders o LEFT JOIN OrderDetails od ON o.OrderID = od.OrderID" +
                    " WHERE EXTRACT(MONTH FROM o.OrderDate) = '" + monthM + "' AND EXTRACT(YEAR FROM o.OrderDate) = '" + yearM + "' GROUP BY od.ProductID ORDER BY Total_Quantity DESC";
            ResultSet mostM = db.query(mostBuyLastMonth);
            while(mostM.next()) {
                String productID = mostM.getString("ProductID");
                if (productID != null) {
                    String sql_mostM = "SELECT * FROM Product WHERE ProductID = '" + productID + "'";
                    ResultSet rs_mostM = db.query(sql_mostM);
                    while (rs_mostM.next()) {
                        String ProductID = rs_mostM.getString("ProductID");
                        String Description = rs_mostM.getString("Description");
                        Float Price = rs_mostM.getFloat("Price");
                        String Specifications = rs_mostM.getString("Specifications");
                        String CustomerReviews = rs_mostM.getString("CustomerReviews");
                        String Category = rs_mostM.getString("Category");
                        String Brand = rs_mostM.getString("Brand");
                        int Stock = rs_mostM.getInt("Stock");
                        System.out.println("[ProductID]: " + ProductID + "\n[Description]: " + Description + "\n[Price]: " + Price + "\n[Specifications]: " + Specifications + "\n[CustomerReviews]: " + CustomerReviews +
                                "\n[Category]: " + Category + "\n[Brand]: " + Brand + "\n[Stock]: " + Stock);
                    }
                    rs_mostM.close();
                    break;
                }
            }
            mostM.close();
            System.out.println("Least Sold Product Last Month: ");
            String LeastBuyLastMonth = "SELECT DISTINCT od.ProductID, COALESCE(SUM(od.Quantity), 0) AS Total_Quantity FROM Orders o LEFT JOIN OrderDetails od ON o.OrderID = od.OrderID" +
                    " WHERE EXTRACT(MONTH FROM o.OrderDate) = '" + monthM + "' AND EXTRACT(YEAR FROM o.OrderDate) = '" + yearM + "' GROUP BY od.ProductID ORDER BY Total_Quantity";
            ResultSet LeastM = db.query(LeastBuyLastMonth);
            while(LeastM.next()) {
                String productID = LeastM.getString("ProductID");
                if (productID != null) {
                    String sql_mostM = "SELECT * FROM Product WHERE ProductID = '" + productID + "'";
                    ResultSet rs_LeastM = db.query(sql_mostM);
                    while (rs_LeastM.next()) {
                        String ProductID = rs_LeastM.getString("ProductID");
                        String Description = rs_LeastM.getString("Description");
                        Float Price = rs_LeastM.getFloat("Price");
                        String Specifications = rs_LeastM.getString("Specifications");
                        String CustomerReviews = rs_LeastM.getString("CustomerReviews");
                        String Category = rs_LeastM.getString("Category");
                        String Brand = rs_LeastM.getString("Brand");
                        int Stock = rs_LeastM.getInt("Stock");
                        System.out.println("[ProductID]: " + ProductID + "\n[Description]: " + Description + "\n[Price]: " + Price + "\n[Specifications]: " + Specifications + "\n[CustomerReviews]: " + CustomerReviews +
                                "\n[Category]: " + Category + "\n[Brand]: " + Brand + "\n[Stock]: " + Stock);
                    }
                    rs_LeastM.close();
                    break;
                }
            }
            LeastM.close();

            System.out.println("Most Sold Product Last Year: ");
            if(!dataOnLastYear){System.out.println("NONE");}
            String mostBuyLastYear = "SELECT DISTINCT od.ProductID, SUM(od.Quantity) AS Total_Quantity FROM Orders o LEFT JOIN OrderDetails od ON o.OrderID = od.OrderID" +
                    " WHERE EXTRACT(YEAR FROM o.OrderDate) = '" + yearY + "' GROUP BY od.ProductID ORDER BY Total_Quantity DESC";
            ResultSet mostY = db.query(mostBuyLastYear);
            while(mostY.next()) {
                String productID = mostY.getString("ProductID");
                if (productID != null) {
                    String sql_mostY = "SELECT * FROM Product WHERE ProductID = '" + productID + "'";
                    ResultSet rs_mostY = db.query(sql_mostY);
                    while (rs_mostY.next()) {
                        String ProductID = rs_mostY.getString("ProductID");
                        String Description = rs_mostY.getString("Description");
                        Float Price = rs_mostY.getFloat("Price");
                        String Specifications = rs_mostY.getString("Specifications");
                        String CustomerReviews = rs_mostY.getString("CustomerReviews");
                        String Category = rs_mostY.getString("Category");
                        String Brand = rs_mostY.getString("Brand");
                        int Stock = rs_mostY.getInt("Stock");
                        System.out.println("[ProductID]: " + ProductID + "\n[Description]: " + Description + "\n[Price]: " + Price + "\n[Specifications]: " + Specifications + "\n[CustomerReviews]: " + CustomerReviews +
                                "\n[Category]: " + Category + "\n[Brand]: " + Brand + "\n[Stock]: " + Stock);
                    }
                    rs_mostY.close();
                    break;
                }
            }
            mostY.close();

            System.out.println("Least Sold Product Last Year: ");
            if(!dataOnLastYear){System.out.println("NONE");}
            String LeastBuyLastYear = "SELECT DISTINCT od.ProductID, COALESCE(SUM(od.Quantity), 0) AS Total_Quantity FROM Orders o LEFT JOIN OrderDetails od ON o.OrderID = od.OrderID" +
                    " WHERE EXTRACT(YEAR FROM o.OrderDate) = '" + yearY + "' GROUP BY od.ProductID ORDER BY Total_Quantity";
            ResultSet leastY = db.query(LeastBuyLastYear);
            while(leastY.next()) {
                String productID = leastY.getString("ProductID");
                if (productID != null) {
                    String sql_leastY = "SELECT * FROM Product WHERE ProductID = '" + productID + "'";
                    ResultSet rs_leastY = db.query(sql_leastY);
                    while (rs_leastY.next()) {
                        String ProductID = rs_leastY.getString("ProductID");
                        String Description = rs_leastY.getString("Description");
                        Float Price = rs_leastY.getFloat("Price");
                        String Specifications = rs_leastY.getString("Specifications");
                        String CustomerReviews = rs_leastY.getString("CustomerReviews");
                        String Category = rs_leastY.getString("Category");
                        String Brand = rs_leastY.getString("Brand");
                        int Stock = rs_leastY.getInt("Stock");
                        System.out.println("[ProductID]: " + ProductID + "\n[Description]: " + Description + "\n[Price]: " + Price + "\n[Specifications]: " + Specifications + "\n[CustomerReviews]: " + CustomerReviews +
                                "\n[Category]: " + Category + "\n[Brand]: " + Brand + "\n[Stock]: " + Stock);
                    }
                    rs_leastY.close();
                    break;
                }
            }
            mostY.close();

            salesDataAnalyse();

            System.out.println("""
        =======================================
                     END OF REPORT
                    """);

        }

        public static void addPromotion(String PromotionID, Float Price, Float Discount, String ReportID, String AdminID, String StartDate, String EndDate) throws SQLException{
            DataBase db = DataBase.getDataBase();
            String id = "SELECT PromotionID FROM Promotion WHERE ReportID = '" + PromotionID + "'";
            ResultSet rs = db.query(id);
            if (rs.next()) {
                System.out.println("PromotionID is duplicated!");
            } else {
                String sql = "INSERT INTO Promotion(PromotionID, Price, Discount, ReportID, AdminID, StartDate, EndDate)" +
                        "VALUES('" + PromotionID + "', '" + Price + "', '" + Discount + "', '" + ReportID + "', '" + AdminID + "', TO_DATE('" + StartDate + "', 'YYYY-MM-DD'), TO_DATE('" + EndDate + "', 'YYYY-MM-DD'))";
                db.modifyTable(sql);
                System.out.println("1 Row Created.");
            }
        }

        public static void deletePromotion(String PromotionID) throws SQLException {
            DataBase db = DataBase.getDataBase();
            String sql = "DELETE FROM Promotion WHERE PromotionID = '" + PromotionID + "'";
            String id = "SELECT * FROM Promotion WHERE PromotionID = '" + PromotionID + "'";
            ResultSet rs = db.query(id);
            if(!rs.next()){
                System.out.println("No data!");
            }else {
                db.modifyTable(sql);
                System.out.println("Promotion: " + PromotionID + " had been deleted.");
            }
        }

    public void updateProduct(String PromotionID, String criterion, String changedValue) throws SQLException {
        DataBase db = DataBase.getDataBase();
        String sql = "UPDATE Promotion SET " + criterion + " = " + "'" + changedValue + "' WHERE PromotionID = '" + PromotionID + "'";
        String id = "SELECT * FROM Promotion WHERE PromotionID = '" + PromotionID + "'";
        ResultSet rs = db.query(id);
        if(!rs.next()){
            System.out.println("No data!");
        }else {
            db.modifyTable(sql);
            System.out.println("Promotion:" + PromotionID + " had made a change on " + criterion);
        }
    }

    public void printPromotion() throws SQLException {
        DataBase db = DataBase.getDataBase();
        String all = "SELECT * FROM Promotion";
        ResultSet idSet = db.query(all);
        while (idSet.next()){
            String rID = idSet.getNString("PromotionID");
            System.out.println("PromotionID: " + rID);
        }
        idSet.close();
    }



}

