# Supplier (Quý) — Test Checklist

Mục tiêu: Kiểm tra toàn bộ luồng CRUD cho module Nhà cung cấp (Supplier)

Tiền đề:
- Đã cài đặt và chạy server (Tomcat) với project này.
- Database có bảng `Suppliers` và kết nối `DBContext` hoạt động.

Các bước kiểm thử (thực hiện trên giao diện web):

1) Mở trang danh sách nhà cung cấp
   - URL: `/supplier` hoặc trang tương ứng
   - Kỳ vọng: danh sách suppliers hiện lên, có nút `Add Supplier`.

2) Thử validation: Thêm supplier thiếu tên
   - Mở `Add Supplier`, để trống trường `Supplier Name`, bấm `Add`.
   - Kỳ vọng: hiển thị lỗi đỏ `Supplier name is required.` và form giữ giá trị nhập khác.

3) Thêm supplier hợp lệ
   - Điền `Supplier Name`, `Contact`, `Phone`, `Email`, `Address`, `Logo` rồi bấm `Add`.
   - Kỳ vọng: chuyển về danh sách với thông báo xanh `Supplier added successfully.`; supplier mới xuất hiện.

4) Chỉnh sửa supplier
   - Bấm `Edit` trên một supplier, thay đổi dữ liệu, bấm `Update`.
   - Kỳ vọng: thông báo `Supplier updated successfully.`; dữ liệu cập nhật trên danh sách.

5) Toggle trạng thái (Activate/Deactivate)
   - Bấm nút `Deactivate` (nếu đang active) hoặc `Activate` (nếu inactive).
   - Kỳ vọng: trạng thái (Active/Inactive) đổi, và button nhãn đổi tương ứng.

6) Xóa vĩnh viễn (Delete)
   - Bấm `Delete` và xác nhận dialog.
   - Kỳ vọng: supplier bị remove khỏi danh sách; có thể kiểm tra DB.

7) Tìm kiếm
   - Dùng ô tìm kiếm `keyword` để tìm theo tên.
   - Kỳ vọng: danh sách lọc theo tên chứa từ khóa.

8) Kiểm tra DB (tuỳ chọn)
   - SQL kiểm tra:
     ```sql
     SELECT SupplierID, SupplierName, ContactName, Phone, Email, Address, Logo, status
     FROM Suppliers
     WHERE SupplierName LIKE '%<tên_test>%';
     ```
   - Kiểm tra `status` (1 active, 0 inactive) sau toggle/delete.

Ghi chú xử lý lỗi:
- Nếu gặp lỗi kết nối DB: kiểm tra `DBContext.java` và thông tin connection string.
- Nếu có lỗi compile: chạy build để lấy lỗi cụ thể (IDE hoặc `mvn/ant` tuỳ project).

Kết thúc: Nếu tất cả bước đều OK, phần Supplier của Quý hoàn chỉnh.
