-- ADD COLUMNS RAM, ROM, Color vào bảng Products để lưu thông tin cấu hình và màu sắc của sản phẩm
ALTER TABLE [dbo].[Products] ADD [RAM] [nvarchar](50) NULL;
ALTER TABLE [dbo].[Products] ADD [ROM] [nvarchar](50) NULL;
ALTER TABLE [dbo].[Products] ADD [Color] [nvarchar](50) NULL;
