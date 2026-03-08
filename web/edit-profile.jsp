<%-- 
    Document   : edit-profile
    Created on : Mar 4, 2026, 8:15:25 AM
    Author     : Lenovo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Chỉnh sửa hồ sơ | PHONEShop</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        body { background-color: #f0f2f5; font-family: 'Inter', sans-serif; }
        .edit-wrapper { min-height: 100vh; display: flex; align-items: center; justify-content: center; padding: 40px 0; }
        .edit-card {
            background: #fff; border-radius: 20px; box-shadow: 0 10px 30px rgba(0,0,0,0.1);
            width: 100%; max-width: 550px; padding: 35px;
        }
        .form-label { font-weight: 600; color: #495057; margin-bottom: 8px; }
        .form-control { border-radius: 10px; padding: 12px; border: 1px solid #dee2e6; }
        .form-control:focus { border-color: #ffc107; box-shadow: 0 0 0 0.25rem rgba(255, 193, 7, 0.25); }
        .btn-save { background: #ffc107; color: #000; border: none; font-weight: 700; border-radius: 10px; padding: 12px; transition: 0.3s; }
        .btn-save:hover { background: #e0a800; transform: translateY(-2px); }
    </style>
</head>
<body>
    <jsp:include page="navbar.jsp"/>

    <div class="container edit-wrapper">
        <div class="edit-card">
            <div class="text-center mb-4">
                <div class="d-inline-block p-3 rounded-circle bg-light mb-3">
                    <i class="fa-solid fa-user-gear fa-3x text-warning"></i>
                </div>
                <h3 class="fw-bold">Cập nhật thông tin</h3>
                <p class="text-muted small">Thông tin chính xác giúp chúng tôi giao hàng tốt hơn</p>
            </div>

            <%-- Hiển thị thông báo lỗi nếu Servlet trả về lỗi --%>
            <c:if test="${param.error == 'fail'}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    Cập nhật thất bại. Vui lòng kiểm tra lại thông tin!
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </c:if>

            <%-- Form gửi dữ liệu đến Servlet xử lý --%>
            <form action="update-profile" method="POST">
                <div class="mb-3">
                    <label class="form-label">Họ và tên</label>
                    <input type="text" name="fullName" class="form-control" 
                           value="${sessionScope.user.fullName}" required>
                </div>

                <div class="mb-3">
                    <label class="form-label">Email liên hệ</label>
                    <input type="email" name="email" class="form-control bg-light" 
                           value="${sessionScope.user.email}" readonly>
                </div>

                <div class="mb-3">
                    <label class="form-label">Số điện thoại</label>
                    <input type="tel" name="phone" class="form-control" 
                           value="${sessionScope.user.phoneNumber}" 
                           placeholder="Nhập số điện thoại của bạn"
                           pattern="[0-9]{10,11}" title="Vui lòng nhập số điện thoại hợp lệ">
                </div>

                <div class="mb-4">
                    <label class="form-label">Địa chỉ nhận hàng <span class="text-danger">*</span></label>
                    <%-- Trường địa chỉ bắt buộc để không bị trống khi đặt hàng --%>
                    <textarea name="address" class="form-control" rows="3" required 
                              placeholder="Số nhà, tên đường, phường/xã, quận/huyện...">${sessionScope.user.address}</textarea>
                </div>

                <div class="d-grid gap-2">
                    <button type="submit" class="btn btn-save shadow-sm">
                        <i class="fa-solid fa-check-double me-2"></i> Lưu thay đổi
                    </button>
                    <a href="profile" class="btn btn-outline-secondary border-0 mt-1">Hủy bỏ và quay lại</a>
                </div>
            </form>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>