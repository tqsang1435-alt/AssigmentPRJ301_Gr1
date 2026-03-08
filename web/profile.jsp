<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Hồ sơ cá nhân - PhoneShop</title>
    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/cssreset.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/base.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/grid.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css?v=999">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/gh/lykmapipo/themify-icons@0.1.2/css/themify-icons.css">

    <style>
        /* CSS riêng cho trang Profile */
        .profile-header__name { margin: 0; font-size: 2.4rem; }
        .profile-header__role { margin: 5px 0 0; opacity: 0.9; font-size: 1.4rem; }
        
        .profile-actions { text-align: center; margin-top: 35px; display: flex; justify-content: center; gap: 15px; }
        .btn--outline-danger { color: #dc3545; border: 1px solid #dc3545; background: transparent; text-decoration: none; display: inline-flex; align-items: center; }
        .btn--outline-danger:hover { background-color: #dc3545; color: #fff; }
        .btn i { margin-right: 6px; }

        /* CSS cho Thông báo thành công (Được lấy từ nhánh Dat và độ lại) */
        #toast-success {
            position: fixed;
            top: 20px;
            right: 20px;
            background: #28a745; 
            color: white;
            padding: 14px 24px;
            border-radius: 4px; 
            font-size: 1.4rem;
            font-weight: 500;
            box-shadow: 0 4px 12px rgba(0,0,0,0.15);
            z-index: 9999;
            animation: fadeInOut 4s forwards;
            display: flex;
            align-items: center;
        }
        #toast-success i { margin-right: 8px; font-size: 1.8rem; }
        
        @keyframes fadeInOut {
            0% { opacity: 0; transform: translateY(-20px); }
            10% { opacity: 1; transform: translateY(0); }
            80% { opacity: 1; transform: translateY(0); }
            100% { opacity: 0; transform: translateY(-20px); }
        }
    </style>
</head>

<body>
    <c:if test="${param.message == 'success'}">
        <div id="toast-success">
            <i class="ti-check-box"></i> Cập nhật hồ sơ thành công!
        </div>
    </c:if>

    <header class="header">
        <div class="grid wide">
            <div class="header__navbar">
                <a href="${pageContext.request.contextPath}/" class="header__logo" title="Về trang chủ">
                    <i class="ti-mobile"></i><label>PhoneShop</label>
                </a>
            </div>
        </div>
    </header>

    <div class="profile-wrap">
        <div class="grid wide">
            <div class="row">
                <div class="col l-6 l-o-3 m-8 m-o-2 c-12">
                    <div class="profile-card">
                        <div class="profile-header">
                            <div class="profile-avatar"><i class="ti-user"></i></div>
                            <h2 class="profile-header__name">${sessionScope.ACC.fullName}</h2>
                            <p class="profile-header__role">Vai trò: ${sessionScope.ACC.role}</p>
                        </div>

                        <div class="profile-body">
                            <div class="info-group">
                                <div class="info-label"><i class="ti-id-badge"></i> Mã người dùng</div>
                                <div class="info-value">${sessionScope.ACC.userID}</div>
                            </div>
                            
                            <div class="info-group">
                                <div class="info-label"><i class="ti-email"></i> Email liên hệ</div>
                                <div class="info-value">${sessionScope.ACC.email}</div>
                            </div>

                            <div class="info-group">
                                <div class="info-label"><i class="ti-mobile"></i> Số điện thoại</div>
                                <div class="info-value">
                                    <c:choose>
                                        <c:when test="${not empty sessionScope.ACC.phoneNumber}">
                                            ${sessionScope.ACC.phoneNumber}
                                        </c:when>
                                        <c:otherwise>
                                            <span style="color: #999; font-style: italic;">Chưa cập nhật</span>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>

                            <div class="info-group">
                                <div class="info-label"><i class="ti-location-pin"></i> Địa chỉ mặc định</div>
                                <div class="info-value" style="color: var(--primary-color); font-weight: 500;">
                                    <c:choose>
                                        <c:when test="${not empty sessionScope.ACC.address}">
                                            ${sessionScope.ACC.address}
                                        </c:when>
                                        <c:otherwise>
                                            <span style="color: #999; font-style: italic; font-weight: normal;">Chưa cập nhật địa chỉ</span>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>

                            <div class="profile-actions">
                                <a href="edit-profile" class="btn btn--primary" style="text-decoration: none; padding: 0 20px; line-height: 34px;">
                                    <i class="ti-pencil-alt"></i> Chỉnh sửa hồ sơ
                                </a>
                                <a href="user-logout" class="btn btn--outline-danger" style="line-height: 34px;">
                                    <i class="ti-power-off"></i> Đăng xuất
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>

</html>