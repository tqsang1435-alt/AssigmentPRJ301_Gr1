<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Hồ sơ của bạn | PHONEShop</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        <style>
            body {
                background-color: #f8f9fa;
                font-family: 'Inter', sans-serif;
            }
            .profile-wrapper {
                min-height: 90vh;
                display: flex;
                align-items: center;
                justify-content: center;
                padding: 20px 0;
            }
            .profile-card {
                background: #fff;
                border: none;
                border-radius: 20px;
                box-shadow: 0 15px 35px rgba(0,0,0,0.1);
                width: 100%;
                max-width: 500px;
                padding: 40px;
                text-align: center;
            }
            .avatar-box {
                width: 80px;
                height: 80px;
                background: #fff3cd;
                color: #ffc107;
                border-radius: 50%;
                display: flex;
                align-items: center;
                justify-content: center;
                margin: 0 auto 20px;
                font-size: 2rem;
            }
            .info-group {
                text-align: left;
                margin-top: 25px;
            }
            .info-item {
                margin-bottom: 15px;
                border-bottom: 1px solid #f1f1f1;
                padding-bottom: 8px;
            }
            .info-label {
                font-size: 0.85rem;
                color: #6c757d;
                font-weight: 600;
                text-transform: uppercase;
            }
            .info-value {
                font-size: 1.1rem;
                color: #212529;
                font-weight: 500;
                display: block;
                word-break: break-word;
            }
            .btn-update {
                background-color: #0d6efd;
                color: white;
                border-radius: 50px;
                transition: 0.3s;
            }
            .btn-update:hover {
                background-color: #0b5ed7;
                transform: translateY(-2px);
            }
        </style>
    </head>
    <body>

        <jsp:include page="navbar.jsp"/> 

        <div class="container profile-wrapper">
            <div class="profile-card">
                <c:if test="${param.message == 'success'}">
                    <div class="alert alert-success alert-dismissible fade show" role="alert">
                        <i class="fa-solid fa-circle-check me-2"></i> Cập nhật hồ sơ thành công!
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                    </div>
                </c:if>
                <div class="avatar-box shadow-sm">
                    <i class="fa-solid fa-user-astronaut"></i>
                </div>

                <h3 class="fw-bold mb-1">${sessionScope.user.fullName}</h3>
                <p class="text-muted small">Thông tin tài khoản khách hàng</p>

                <div class="info-group">
                    <div class="info-item">
                        <span class="info-label">Tên đăng nhập</span>
                        <span class="info-value">${sessionScope.username}</span>
                    </div>
                    <div class="info-item">
                        <span class="info-label">Email liên hệ</span>
                        <span class="info-value text-truncate">${sessionScope.user.email}</span>
                    </div>
                    <div class="info-item">
                        <span class="info-label">Số điện thoại</span>
                        <span class="info-value">${sessionScope.user.phoneNumber != null ? sessionScope.user.phoneNumber : "Chưa cập nhật"}</span>
                    </div>
                    <%-- THÊM TRƯỜNG ĐỊA CHỈ TẠI ĐÂY --%>
                    <div class="info-item">
                        <span class="info-label">Địa chỉ mặc định</span>
                        <span class="info-value text-primary">
                            <i class="fa-solid fa-location-dot me-1 small"></i>
                            ${sessionScope.user.address != null ? sessionScope.user.address : "Chưa cập nhật địa chỉ"}
                        </span>
                    </div>
                </div>

                <div class="d-grid gap-2 mt-4">
                    <%-- NÚT CHỈNH SỬA --%>
                    <a href="edit-profile" class="btn btn-update fw-bold py-2 shadow-sm">
                        <i class="fa-solid fa-user-pen me-2"></i> Chỉnh sửa hồ sơ
                    </a>

                    <a href="logout" class="btn btn-outline-danger rounded-pill fw-bold py-2 mt-2">
                        <i class="fa-solid fa-power-off me-2"></i> Đăng xuất
                    </a>
                    <a href="home" class="btn btn-link btn-sm text-muted text-decoration-none">Quay lại trang chủ</a>
                </div>

            </div>

        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>