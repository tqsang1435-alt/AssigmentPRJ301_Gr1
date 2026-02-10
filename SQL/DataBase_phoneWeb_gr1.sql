-- 1. Tạo Database
CREATE DATABASE PhoneShopDB;
GO
USE PhoneShopDB;
GO

-- 2. Bảng User (Dùng cho Module Khách hàng & Phân quyền)
-- Thành viên: Huỳnh Minh Huân
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

-- 3. Bảng Nhà Cung Cấp (Module của BẠN - Trần Quốc Sang)
-- [Source: 18, 23, 24]
CREATE TABLE Suppliers (
    SupplierID INT IDENTITY(1,1) PRIMARY KEY,
    SupplierName NVARCHAR(100) NOT NULL, -- VD: Apple, Samsung VN, FPT Trading
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

-- 5. Bảng Sản phẩm (Module Product Management)
-- Thành viên: Trương Nam Nhân, Nguyễn Đào Văn Quý
-- [Source: 19, 34, 35]
CREATE TABLE Products (
    ProductID INT IDENTITY(1,1) PRIMARY KEY,
    ProductName NVARCHAR(200) NOT NULL,
    Price DECIMAL(18, 0) NOT NULL, -- Giá bán
    StockQuantity INT DEFAULT 0, -- Số lượng tồn
    Description NVARCHAR(MAX),
    ImageURL VARCHAR(255),
    CategoryID INT FOREIGN KEY REFERENCES Categories(CategoryID),
    SupplierID INT FOREIGN KEY REFERENCES Suppliers(SupplierID), -- Liên kết với bảng của BẠN
    Status BIT DEFAULT 1 -- 1: Đang bán, 0: Ngừng kinh doanh
);

-- 6. Bảng Đơn hàng (Module của BẠN - Trần Quốc Sang)
-- [Source: 41, 42]
CREATE TABLE Orders (
    OrderID INT IDENTITY(1,1) PRIMARY KEY,
    UserID INT FOREIGN KEY REFERENCES Users(UserID), -- Ai đặt hàng?
    OrderDate DATETIME DEFAULT GETDATE(),
    TotalMoney DECIMAL(18, 0), -- Tổng tiền đơn hàng
    ShippingAddress NVARCHAR(255),
    Note NVARCHAR(MAX),
    -- Quan trọng: Trạng thái đơn hàng để xử lý luồng
    -- 1: Chờ xác nhận, 2: Đang đóng gói, 3: Đang giao, 4: Hoàn thành, 0: Hủy
    Status INT DEFAULT 1 
);

-- 7. Bảng Chi tiết đơn hàng (Liên kết giữa Đơn hàng và Sản phẩm)
CREATE TABLE OrderDetails (
    OrderDetailID INT IDENTITY(1,1) PRIMARY KEY,
    OrderID INT FOREIGN KEY REFERENCES Orders(OrderID),
    ProductID INT FOREIGN KEY REFERENCES Products(ProductID),
    Quantity INT NOT NULL,
    Price DECIMAL(18, 0) NOT NULL -- Giá tại thời điểm mua (để tránh giá SP thay đổi sau này)
);

-- 8. Bảng IMEI/Serial (Quản lý kho chi tiết & Bảo hành)
-- [Source: 20, 43]
CREATE TABLE ProductIMEI (
    IMEI_ID INT IDENTITY(1,1) PRIMARY KEY,
    ProductID INT FOREIGN KEY REFERENCES Products(ProductID),
    IMEI_Number VARCHAR(50) UNIQUE NOT NULL,
    Status INT DEFAULT 0, -- 0: Trong kho, 1: Đã bán, 2: Lỗi/Bảo hành
    OrderID INT FOREIGN KEY REFERENCES Orders(OrderID) -- Khi bán thì cập nhật OrderID vào đây
);

-- =============================================
-- INSERT DỮ LIỆU MẪU (DUMMY DATA) ĐỂ TEST
-- =============================================

-- Thêm User mẫu
INSERT INTO Users (FullName, Email, Password, Role) VALUES 
(N'Nguyễn Văn Admin', 'admin@gmail.com', '123456', 'Admin'),
(N'Trần Quốc Sang', 'sang@gmail.com', '123456', 'Customer');

-- Thêm Nhà cung cấp (Phần của bạn test)
INSERT INTO Suppliers (SupplierName, Phone, Email) VALUES 
(N'Công ty Samsung Vina', '0909123456', 'contact@samsung.vn'),
(N'Apple Distribution', '0909999888', 'vn.support@apple.com');

-- Thêm Danh mục
INSERT INTO Categories (CategoryName) VALUES (N'Điện thoại'), (N'Phụ kiện');

-- Thêm Sản phẩm
INSERT INTO Products (ProductName, Price, StockQuantity, CategoryID, SupplierID) VALUES 
(N'Samsung Galaxy S24 Ultra', 30000000, 50, 1, 1),
(N'iPhone 15 Pro Max', 34000000, 30, 1, 2);

-- Thêm Đơn hàng mẫu (Phần của bạn test)
INSERT INTO Orders (UserID, TotalMoney, Status) VALUES (2, 34000000, 1); -- Đơn mới
INSERT INTO OrderDetails (OrderID, ProductID, Quantity, Price) VALUES (1, 2, 1, 34000000);

ALTER TABLE Suppliers
ADD [status] BIT NOT NULL DEFAULT 1; 