-- 1. Tạo Database
CREATE DATABASE PhoneShopDB;
GO
USE PhoneShopDB;
GO

-- 2. Bảng User (Dùng cho Module Khách hàng & Phân quyền)
CREATE TABLE Users (
    UserID INT IDENTITY(1,1) PRIMARY KEY,
    FullName NVARCHAR(100),
    Email VARCHAR(100) UNIQUE NOT NULL,
    PhoneNumber VARCHAR(15),
    Address NVARCHAR(255),
    Password VARCHAR(255) NOT NULL, -- Lưu ý: Nên mã hóa MD5/SHA khi lưu
    Role VARCHAR(20) DEFAULT 'Customer', -- 'Admin', 'Staff', 'Customer'
    CreateDate DATETIME DEFAULT GETDATE()
);

-- 3. Bảng Nhà Cung Cấp
CREATE TABLE Suppliers (
    SupplierID INT IDENTITY(1,1) PRIMARY KEY,
    SupplierName NVARCHAR(100) NOT NULL,
    ContactName NVARCHAR(100),
    Phone VARCHAR(20),
    Email VARCHAR(100),
    Address NVARCHAR(255),
    Logo VARCHAR(255) -- Link ảnh logo nhà cung cấp
);

-- 4. Bảng Danh mục (Loại sản phẩm)
CREATE TABLE Categories (
    CategoryID INT IDENTITY(1,1) PRIMARY KEY,
    CategoryName NVARCHAR(50) NOT NULL -- VD: Điện thoại, Tai nghe, Sạc dự phòng
);

-- 5. Bảng Sản phẩm
CREATE TABLE Products (
    ProductID INT IDENTITY(1,1) PRIMARY KEY,
    ProductName NVARCHAR(200) NOT NULL,
    Price DECIMAL(18, 0) NOT NULL,
    StockQuantity INT DEFAULT 0,
    Description NVARCHAR(MAX),
    ImageURL VARCHAR(255),
    CategoryID INT FOREIGN KEY REFERENCES Categories(CategoryID),
    SupplierID INT FOREIGN KEY REFERENCES Suppliers(SupplierID),
    Status BIT DEFAULT 1 -- 1: Đang bán, 0: Ngừng kinh doanh
);

-- 6. Bảng Đơn hàng
CREATE TABLE Orders (
    OrderID INT IDENTITY(1,1) PRIMARY KEY,
    UserID INT FOREIGN KEY REFERENCES Users(UserID),
    OrderDate DATETIME DEFAULT GETDATE(),
    TotalMoney DECIMAL(18, 0),
    ShippingAddress NVARCHAR(255),
    Note NVARCHAR(MAX),
    -- 1: Chờ xác nhận, 2: Đang đóng gói, 3: Đang giao, 4: Hoàn thành, 0: Hủy
    Status INT DEFAULT 1 
);

-- 7. Bảng Chi tiết đơn hàng
CREATE TABLE OrderDetails (
    OrderDetailID INT IDENTITY(1,1) PRIMARY KEY,
    OrderID INT FOREIGN KEY REFERENCES Orders(OrderID),
    ProductID INT FOREIGN KEY REFERENCES Products(ProductID),
    Quantity INT NOT NULL,
    Price DECIMAL(18, 0) NOT NULL
);

-- 8. Bảng IMEI/Serial
CREATE TABLE ProductIMEI (
    IMEI_ID INT IDENTITY(1,1) PRIMARY KEY,
    ProductID INT FOREIGN KEY REFERENCES Products(ProductID),
    IMEI_Number VARCHAR(50) UNIQUE NOT NULL,
    Status INT DEFAULT 0, -- 0: Trong kho, 1: Đã bán, 2: Lỗi/Bảo hành
    OrderID INT FOREIGN KEY REFERENCES Orders(OrderID)
);

-- 9. Bảng Voucher
CREATE TABLE Vouchers (
    VoucherID INT IDENTITY(1,1) PRIMARY KEY,
    Code VARCHAR(50) UNIQUE NOT NULL,
    DiscountType VARCHAR(10) NOT NULL, -- 'percent' or 'fixed'
    DiscountValue DECIMAL(18, 2) NOT NULL,
    MinOrderValue DECIMAL(18, 2) DEFAULT 0,
    MaxDiscount DECIMAL(18, 2) DEFAULT 0,
    ExpiryDate DATETIME,
    UsageLimit INT DEFAULT 0, -- 0 means unlimited
    UsedCount INT DEFAULT 0,
    Status BIT DEFAULT 1
);

-- =============================================
-- INSERT DỮ LIỆU MẪU (DUMMY DATA) ĐỂ TEST
-- =============================================

INSERT INTO Users (FullName, Email, Password, Role) VALUES 
(N'Nguyễn Văn Admin', 'admin@gmail.com', '123456', 'Admin'),
(N'Trần Quốc Sang', 'sang@gmail.com', '123456', 'Customer');

INSERT INTO Suppliers (SupplierName, Phone, Email) VALUES 
(N'Công ty Samsung Vina', '0909123456', 'contact@samsung.vn'),
(N'Apple Distribution', '0909999888', 'vn.support@apple.com');

INSERT INTO Categories (CategoryName) VALUES (N'Điện thoại'), (N'Phụ kiện');

INSERT INTO Products (ProductName, Price, StockQuantity, CategoryID, SupplierID) VALUES 
(N'Samsung Galaxy S24 Ultra', 30000000, 50, 1, 1),
(N'iPhone 15 Pro Max', 34000000, 30, 1, 2);

INSERT INTO Vouchers (Code, DiscountType, DiscountValue, MinOrderValue, MaxDiscount, ExpiryDate, UsageLimit, Status) VALUES
('DISCOUNT10', 'percent', 10.0, 1000000, 500000, DATEADD(MONTH, 1, GETDATE()), 100, 1),
('FIXED500K', 'fixed', 500000, 2000000, 0, DATEADD(MONTH, 2, GETDATE()), 50, 1),
('WELCOME5', 'percent', 5.0, 500000, 100000, DATEADD(MONTH, 3, GETDATE()), 200, 1),
('BIGSALE20', 'percent', 20.0, 3000000, 1000000, DATEADD(MONTH, 1, GETDATE()), 20, 1),
('FLASH300K', 'fixed', 300000, 1500000, 0, DATEADD(DAY, 7, GETDATE()), 30, 1),
('LOYALTY15', 'percent', 15.0, 2000000, 750000, DATEADD(MONTH, 6, GETDATE()), 50, 1),
('NEWUSER10', 'percent', 10.0, 0, 200000, DATEADD(MONTH, 2, GETDATE()), 500, 1),
('SPECIAL25', 'percent', 25.0, 5000000, 1500000, DATEADD(MONTH, 1, GETDATE()), 10, 1),
('SUMMER50', 'percent', 50.0, 10000000, 2000000, DATEADD(DAY, 15, GETDATE()), 5, 1),
('FREESHIP', 'fixed', 50000, 500000, 0, DATEADD(YEAR, 1, GETDATE()), 1000, 1),
('BLACKFRIDAY', 'percent', 30.0, 5000000, 1000000, DATEADD(DAY, 1, GETDATE()), 50, 1),
('XMAS2025', 'fixed', 1000000, 15000000, 0, DATEADD(DAY, 30, GETDATE()), 20, 1);

INSERT INTO Orders (UserID, TotalMoney, Status) VALUES (2, 34000000, 1); -- Đơn mới
INSERT INTO OrderDetails (OrderID, ProductID, Quantity, Price) VALUES (1, 2, 1, 34000000);

-- =============================================
-- ALTER TABLES & NEW ADDITIONS
-- =============================================

ALTER TABLE Suppliers
ADD [status] BIT NOT NULL DEFAULT 1; 

ALTER TABLE ProductIMEI
ADD Warranty_Start DATETIME NULL,
    Warranty_End DATETIME NULL;

INSERT INTO ProductIMEI (ProductID, IMEI_Number, Status, OrderID, Warranty_Start, Warranty_End) VALUES
 (1, 'IMEI-S24-0001', 1, 1, DATEADD(DAY, -10, GETDATE()), DATEADD(MONTH, 12, DATEADD(DAY, -10, GETDATE()))),
 (2, 'IMEI-IP15-0001', 0, NULL, NULL, NULL);

ALTER TABLE Users
ADD RewardPoints INT NOT NULL DEFAULT 0;

ALTER TABLE Users
ADD CustomerType VARCHAR(50) NOT NULL DEFAULT 'New';

ALTER TABLE Orders
ADD VoucherID INT FOREIGN KEY REFERENCES Vouchers(VoucherID);

GO

-- =============================================
-- THỐNG KÊ DOANH THU (Revenue Statistics)
-- =============================================
GO

-- Thống kê doanh thu theo NGÀY
SELECT
    CAST(OrderDate AS DATE) AS [Ngày],
    COUNT(OrderID) AS [Số lượng đơn],
    SUM(TotalMoney) AS [Tổng doanh thu]
FROM
    Orders
WHERE
    Status != 0
GROUP BY
    CAST(OrderDate AS DATE)
ORDER BY
    CAST(OrderDate AS DATE) DESC;

-- Thống kê doanh thu theo THÁNG
SELECT
    YEAR(OrderDate) AS [Năm],
    MONTH(OrderDate) AS [Tháng],
    COUNT(OrderID) AS [Số lượng đơn],
    SUM(TotalMoney) AS [Tổng doanh thu]
FROM
    Orders
WHERE
    Status != 0
GROUP BY
    YEAR(OrderDate),
    MONTH(OrderDate)
ORDER BY
    YEAR(OrderDate) DESC,
    MONTH(OrderDate) DESC;

-- Thống kê doanh thu theo sản phẩm
SELECT
    p.ProductID AS [Mã sản phẩm],
    p.ProductName AS [Tên sản phẩm],
    COUNT(DISTINCT o.OrderID) AS [Số lượng đơn],
    SUM(od.Quantity) AS [Tổng số sản phẩm],
    SUM(od.Quantity * od.Price) AS [Tổng doanh thu]
FROM
    Products p
    INNER JOIN OrderDetails od ON p.ProductID = od.ProductID
    INNER JOIN Orders o ON od.OrderID = o.OrderID
WHERE
    o.Status != 0
GROUP BY
    p.ProductID,
    p.ProductName
ORDER BY
    SUM(od.Quantity * od.Price) DESC;
