DROP TABLE Promotion;
DROP TABLE Report;
DROP TABLE  Admin;
DROP TABLE  OrderDetails;
DROP TABLE  Orders;
DROP TABLE  ShoppingCart;
DROP TABLE  Product;
DROP TABLE  UserAccount;


CREATE TABLE UserAccount (
  AccountID VARCHAR2(10) ,
  EmailAddress VARCHAR2(50) UNIQUE NOT NULL,
  Password VARCHAR2(16) NOT NULL,
  Username VARCHAR2(16) NOT NULL,
  FirstName VARCHAR2(32),
  LastName VARCHAR2(32) NOT NULL,
  Address VARCHAR2(32),
  PRIMARY KEY (AccountID)
);

CREATE TABLE Product (
  ProductID VARCHAR2(10),
  Description VARCHAR2(64) NOT NULL,
  Price DECIMAL(10, 2) NOT NULL,
  Specifications VARCHAR2(128) NOT NULL,
  CustomerReviews VARCHAR2(256),
  Category VARCHAR2(32) NOT NULL,
  Brand VARCHAR2(32) NOT NULL,
  Stock INT NOT NULL,
  PRIMARY KEY (ProductID)
);

CREATE TABLE ShoppingCart (
  ProductID VARCHAR2(10) NOT NULL,
  AccountID VARCHAR2(10) NOT NULL,
  Quantity INT NOT NULL,
  PRIMARY KEY (ProductID, AccountID),
  FOREIGN KEY (ProductID) REFERENCES Product(ProductID),
  FOREIGN KEY (AccountID) REFERENCES UserAccount(AccountID)
);

CREATE TABLE Orders (
  OrderID VARCHAR2(10) NOT NULL,
  AccountID VARCHAR2(10),
  Address VARCHAR2(256),
  OrderDate TIMESTAMP,
  PaymentStatus VARCHAR2(16) NOT NULL,
  ShippingStatus VARCHAR2(16),
  PRIMARY KEY (OrderID),
  FOREIGN KEY (AccountID) REFERENCES UserAccount(AccountID)
);

CREATE TABLE OrderDetails (
  Quantity INT NOT NULL,
  ProductID VARCHAR2(10) NOT NULL,
  OrderID VARCHAR2(10) NOT NULL,
  FOREIGN KEY (ProductID) REFERENCES Product(ProductID),
  FOREIGN KEY (OrderID) REFERENCES Orders(OrderID)
);


CREATE TABLE Admin (
  AdminID VARCHAR2(10),
  Name VARCHAR2(64) NOT NULL,
  Role VARCHAR2(32) NOT NULL,
  Password VARCHAR2(16) NOT NULL,
  PRIMARY KEY(AdminID)
);

CREATE TABLE Report (
  ReportID VARCHAR2(10) NOT NULL,
  AdminID VARCHAR2(10) NOT NULL,
  PopularProduct VARCHAR2(64) NOT NULL,
  UserBehavior VARCHAR2(256) NOT NULL,
  DataOnSales VARCHAR2(256) NOT NULL,
  PRIMARY KEY (ReportID),
  FOREIGN KEY (AdminID) REFERENCES Admin(AdminID)
);

CREATE TABLE Promotion (
  PromotionID VARCHAR2(10),
  Price DECIMAL(10, 2) NOT NULL,
  Discount DECIMAL(5, 2) NOT NULL,
  ReportID VARCHAR2(10),
  AdminID VARCHAR2(10),
  StartDate TIMESTAMP NOT NULL,
  EndDate TIMESTAMP,
  PRIMARY KEY (PromotionID),
  FOREIGN KEY (ReportID) REFERENCES Report(ReportID),
  FOREIGN KEY (AdminID) REFERENCES Admin(AdminID)
);

-- Insert data into UserAccount table
 INSERT INTO UserAccount (AccountID, EmailAddress, Password, Username, FirstName, LastName, Address)
VALUES ('AC0001', 'john.doe@example.com', 'password1', 'johndoe', 'John', 'Doe', '123 Main St.');

INSERT INTO UserAccount (AccountID, EmailAddress, Password, Username, FirstName, LastName, Address)
VALUES ('AC0002', 'jane.smith@example.com', 'password2', 'janesmith', 'Jane', 'Smith', '456 Elm St.');

INSERT INTO UserAccount (AccountID, EmailAddress, Password, Username, FirstName, LastName, Address)
VALUES ('AC0003', 'david.johnson@example.com', 'password3', 'davidjohnson', 'David', 'Johnson', '789 Oak St.');

INSERT INTO UserAccount (AccountID, EmailAddress, Password, Username, FirstName, LastName, Address)
VALUES ('AC0004', 'sarah.williams@example.com', 'password4', 'sarahwilliams', 'Sarah', 'Williams', '321 Maple Ave.');

INSERT INTO UserAccount (AccountID, EmailAddress, Password, Username, FirstName, LastName, Address)
VALUES ('AC0005', 'emily.brown@example.com', 'password5', 'emilybrown', 'Emily', 'Brown', '654 Pine Rd.');

INSERT INTO UserAccount (AccountID, EmailAddress, Password, Username, FirstName, LastName, Address)
VALUES ('AC0006', 'michael.davis@example.com', 'password6', 'michaeldavis', 'Michael', 'Davis', '987 Cedar Ln.');

INSERT INTO UserAccount (AccountID, EmailAddress, Password, Username, FirstName, LastName, Address)
VALUES ('AC0007', 'jessica.wilson@example.com', 'password7', 'jessicawilson', 'Jessica', 'Wilson', '135 Birch Dr.');

INSERT INTO UserAccount (AccountID, EmailAddress, Password, Username, FirstName, LastName, Address)
VALUES ('AC0008', 'andrew.anderson@example.com', 'password8', 'andrewanderson', 'Andrew', 'Anderson', '753 Spruce Ct.');

INSERT INTO UserAccount (AccountID, EmailAddress, Password, Username, FirstName, LastName, Address)
VALUES ('AC0009', 'samantha.taylor@example.com', 'password9', 'samanthataylor', 'Samantha', 'Taylor', '246 Oakwood Ave.');

INSERT INTO UserAccount (AccountID, EmailAddress, Password, Username, FirstName, LastName, Address)
VALUES ('AC0010', 'matthew.thompson@example.com', 'password10', 'matthewthompson', 'Matthew', 'Thompson', '579 Pine St.');

-- Insert data into Product table
INSERT INTO Product (ProductID, Description, Price, Specifications, CustomerReviews, Category, Brand, Stock)
VALUES ('P0001', 'T-Shirt', 10.99, 'Size: M, Color: Red', 'Great quality', 'Clothing', 'XYZ', 134);

INSERT INTO Product (ProductID, Description, Price, Specifications, CustomerReviews, Category, Brand, Stock)
VALUES ('P0002', 'Jeans', 19.99, 'Size: 32, Color: Blue', 'Very comfortable', 'Clothing', 'ABC', 527);

INSERT INTO Product (ProductID, Description, Price, Specifications, CustomerReviews, Category, Brand, Stock)
VALUES ('P0003', 'Sneakers', 5.99, 'Size: 9, Color: Black', 'Good for running', 'Footwear', 'XYZ', 123);

INSERT INTO Product (ProductID, Description, Price, Specifications, CustomerReviews, Category, Brand, Stock)
VALUES ('P0004', 'Socks', 8.99, 'Size: One Size, Color: White', 'Soft and durable', 'Clothing', 'PQR', 83);

INSERT INTO Product (ProductID, Description, Price, Specifications, CustomerReviews, Category, Brand, Stock)
VALUES ('P0005', 'Backpack', 15.99, 'Color: Gray', 'Spacious and sturdy', 'Accessories', 'MNO', 157);

  -- Insert data into ShoppingCart table
  INSERT INTO ShoppingCart (ProductID, AccountID, Quantity)
  VALUES ('P0001', 'AC0001', 2);
  INSERT INTO ShoppingCart (ProductID, AccountID, Quantity)
  VALUES ('P0003', 'AC0001', 1);
  INSERT INTO ShoppingCart (ProductID, AccountID, Quantity)
  VALUES ('P0002', 'AC0002', 3);
  INSERT INTO ShoppingCart (ProductID, AccountID, Quantity)
  VALUES ('P0004', 'AC0003', 1);
  INSERT INTO ShoppingCart (ProductID, AccountID, Quantity)
  VALUES ('P0005', 'AC0003', 2);
  INSERT INTO ShoppingCart (ProductID, AccountID, Quantity)
  VALUES ('P0001', 'AC0004', 4);
  INSERT INTO ShoppingCart (ProductID, AccountID, Quantity)
  VALUES ('P0003', 'AC0005', 1);
INSERT INTO ShoppingCart (ProductID, AccountID, Quantity)
VALUES ('P0005', 'AC0006', 3);
INSERT INTO ShoppingCart (ProductID, AccountID, Quantity)
VALUES ('P0002', 'AC0007', 2);
INSERT INTO ShoppingCart (ProductID, AccountID, Quantity)
VALUES ('P0004', 'AC0008', 1);

-- Insert data into Order table
INSERT INTO Orders (OrderID, Address, OrderDate, AccountID, PaymentStatus, ShippingStatus)
VALUES ('O0001', '123 Main St.', TO_DATE('2023-11-01', 'YYYY-MM-DD'), 'AC0001', 'Pending', 'Shipped');
INSERT INTO Orders (OrderID, Address, OrderDate, AccountID, PaymentStatus, ShippingStatus)
VALUES ('O0002', '456 Elm St.', TO_DATE('2023-11-02', 'YYYY-MM-DD'), 'AC0002', 'Paid', 'Delivered');
INSERT INTO Orders (OrderID, Address, OrderDate, AccountID, PaymentStatus, ShippingStatus)
VALUES ('O0003', '789 Oak St.', TO_DATE('2023-11-03', 'YYYY-MM-DD'), 'AC0003', 'Paid', 'Delivered');
INSERT INTO Orders (OrderID, Address, OrderDate, AccountID, PaymentStatus, ShippingStatus)
VALUES ('O0004', '321 Maple Ave.', TO_DATE('2023-11-04', 'YYYY-MM-DD'), 'AC0004', 'Pending', 'In Transit');
INSERT INTO Orders (OrderID, Address, OrderDate, AccountID, PaymentStatus, ShippingStatus)
VALUES ('O0005', '654 Pine Rd.', TO_DATE('2023-11-05', 'YYYY-MM-DD'), 'AC0005', 'Paid', 'Delivered');
INSERT INTO Orders (OrderID, Address, OrderDate, AccountID, PaymentStatus, ShippingStatus)
VALUES ('O0006', '987 Cedar Ln.', TO_DATE('2023-11-06', 'YYYY-MM-DD'), 'AC0006', 'Paid', 'Delivered');
INSERT INTO Orders (OrderID, Address, OrderDate, AccountID, PaymentStatus, ShippingStatus)
VALUES ('O0007', '135 Birch Dr.', TO_DATE('2023-11-07', 'YYYY-MM-DD'), 'AC0007', 'Pending', 'In Transit');
INSERT INTO Orders (OrderID, Address, OrderDate, AccountID, PaymentStatus, ShippingStatus)
VALUES ('O0008', '753 Spruce Ct.', TO_DATE('2023-11-08', 'YYYY-MM-DD'), 'AC0008', 'Paid', 'Delivered');

-- Insert data into OrderDetails table
INSERT INTO OrderDetails (Quantity, ProductID, OrderID)
VALUES (2, 'P0001', 'O0001');
INSERT INTO OrderDetails (Quantity, ProductID, OrderID)
VALUES (3, 'P0002', 'O0001');
INSERT INTO OrderDetails (Quantity, ProductID, OrderID)
VALUES (1, 'P0003', 'O0002');
INSERT INTO OrderDetails (Quantity, ProductID, OrderID)
VALUES (4, 'P0001', 'O0002');
INSERT INTO OrderDetails (Quantity, ProductID, OrderID)
VALUES (2, 'P0004', 'O0003');
INSERT INTO OrderDetails (Quantity, ProductID, OrderID)
VALUES (1, 'P0005', 'O0003');
INSERT INTO OrderDetails (Quantity, ProductID, OrderID)
VALUES (3, 'P0002', 'O0004');
INSERT INTO OrderDetails (Quantity, ProductID, OrderID)
VALUES (1, 'P0003', 'O0005');
INSERT INTO OrderDetails (Quantity, ProductID, OrderID)
VALUES (2, 'P0004', 'O0006');
INSERT INTO OrderDetails (Quantity, ProductID, OrderID)
VALUES (1, 'P0005', 'O0006');



-- Insert data into Admin table
INSERT INTO Admin (AdminID, Name, Role, Password)
VALUES ('AD0001', 'John Doe', 'Administrator', 'password123');
INSERT INTO Admin (AdminID, Name, Role, Password)
VALUES ('AD0002', 'Jane Smith', 'Manager', 'secret456');
INSERT INTO Admin (AdminID, Name, Role, Password)
VALUES ('AD0003', 'David Johnson', 'Supervisor', 'admin789');

-- Insert data into Report table
INSERT INTO Report (ReportID, PopularProduct, UserBehavior, DataOnSales, AdminID)
VALUES ('RP0001', 'PP0001', 'Customers frequently view and add Product A to their shopping carts.', 'Sales of Product A have increased by 20% in the last month.', 'AD0001');
INSERT INTO Report (ReportID, PopularProduct, UserBehavior, DataOnSales, AdminID)
VALUES ('RP0002', 'PP0002', 'Product B has the highest number of customer reviews.', 'Sales of Product B have remained steady over the past quarter.', 'AD0002');
INSERT INTO Report (ReportID, PopularProduct, UserBehavior, DataOnSales, AdminID)
VALUES ('RP0003', 'PP0001', 'Product C is often recommended as an alternative to other similar products.', 'Sales of Product C have decreased by 10% compared to the previous year.', 'AD0003');
INSERT INTO Report (ReportID, PopularProduct, UserBehavior, DataOnSales, AdminID)
VALUES ('RP0004', 'PP0003', 'Product D is frequently featured in customer wishlists.', 'Sales of Product D have shown significant growth in international markets.', 'AD0001');

-- Insert data into Promotion table
INSERT INTO Promotion (PromotionID, Price, Discount, ReportID, AdminID, StartDate, EndDate)
VALUES ('PM0001', 29.99, 0.1, 'RP0001', 'AD0001', TO_DATE('2023-11-01', 'YYYY-MM-DD'), TO_DATE('2023-11-07', 'YYYY-MM-DD'));
INSERT INTO Promotion (PromotionID, Price, Discount, ReportID, AdminID, StartDate, EndDate)
VALUES ('PM0002', 49.99, 0.2, 'RP0002', 'AD0002', TO_DATE('2023-11-03', 'YYYY-MM-DD'), TO_DATE('2023-11-10', 'YYYY-MM-DD'));
INSERT INTO Promotion (PromotionID, Price, Discount, ReportID, AdminID, StartDate, EndDate)
VALUES ('PM0003', 19.99, 0.15, 'RP0003', 'AD0001', TO_DATE('2023-11-05', 'YYYY-MM-DD'), TO_DATE('2023-11-12', 'YYYY-MM-DD'));

COMMIT;
