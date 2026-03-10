<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
            <!DOCTYPE html>
            <html lang="vi">

            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Hồ sơ cá nhân - PhoneShop</title>

                <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/cssreset.css">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/base.css">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/grid.css">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css?v=1004">
                <link rel="stylesheet"
                    href="https://cdn.jsdelivr.net/gh/lykmapipo/themify-icons@0.1.2/css/themify-icons.css">
            </head>

            <body>
                <c:if test="${param.message == 'success'}">
                    <div id="toast-success">
                        <i class="ti-check-box"></i> Cập nhật hồ sơ thành công!
                    </div>
                </c:if>

                <jsp:include page="header.jsp" />

                <div class="profile-wrap">
                    <div class="grid wide">

                        <%-- LẤY DỮ LIỆU RANK VÀ XỬ LÝ THEME --%>
                            <c:set var="rank" value="${sessionScope.ACC.customerType}" />
                            <c:set var="rankClass" value="rank-new" />
                            <c:set var="iconClass" value="ti-user" />

                            <c:choose>
                                <c:when test="${fn:contains(rank, 'Kim Cương')}">
                                    <c:set var="rankClass" value="rank-diamond" />
                                    <c:set var="iconClass" value="ti-diamond" />
                                </c:when>
                                <c:when test="${fn:contains(rank, 'Vàng')}">
                                    <c:set var="rankClass" value="rank-gold" />
                                    <c:set var="iconClass" value="ti-star" />
                                </c:when>
                                <c:when test="${fn:contains(rank, 'Bạc')}">
                                    <c:set var="rankClass" value="rank-silver" />
                                    <c:set var="iconClass" value="ti-medall" />
                                </c:when>
                                <c:when test="${fn:contains(rank, 'Đồng')}">
                                    <c:set var="rankClass" value="rank-bronze" />
                                    <c:set var="iconClass" value="ti-medall-alt" />
                                </c:when>
                            </c:choose>

                            <div class="row">
                                <div class="col l-6 l-o-3 m-8 m-o-2 c-12">
                                    <%-- BỌC TRONG KHUNG VIỀN ANIMATION THEO RANK --%>
                                        <div class="membership-card-wrapper ${rankClass}">
                                            <div class="membership-card-inner">

                                                <div class="profile-header">
                                                    <div class="avatar-hexagon-wrap">
                                                        <div class="avatar-hexagon-border"></div>
                                                        <div class="avatar-hexagon-inner">
                                                            <i class="${iconClass}"></i>
                                                        </div>
                                                    </div>

                                                    <h2 class="profile-header__name">${sessionScope.ACC.fullName}</h2>
                                                    <p class="profile-header__role">Vai trò: ${sessionScope.ACC.role}
                                                    </p>
                                                </div>

                                                <div class="profile-body">
                                                    <div class="info-group">
                                                        <div class="info-label"><i class="ti-id-badge"></i> User ID
                                                        </div>
                                                        <div class="info-value">#${sessionScope.ACC.userID}</div>
                                                    </div>

                                                    <div class="info-group">
                                                        <div class="info-label"><i class="ti-email"></i> Liên kết tài
                                                            khoản</div>
                                                        <div class="info-value">${sessionScope.ACC.email}</div>
                                                    </div>

                                                    <div class="info-group">
                                                        <div class="info-label"><i class="ti-mobile"></i>SĐT
                                                        </div>
                                                        <div class="info-value">
                                                            <c:choose>
                                                                <c:when
                                                                    test="${not empty sessionScope.ACC.phoneNumber}">
                                                                    ${sessionScope.ACC.phoneNumber}
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <span
                                                                        style="color: #ff4757; font-style: italic;">Chưa
                                                                        xác thực</span>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </div>
                                                    </div>

                                                    <div class="info-group">
                                                        <div class="info-label"><i class="ti-location-pin"></i> Khu vực
                                                        </div>
                                                        <div class="info-value">
                                                            <c:choose>
                                                                <c:when test="${not empty sessionScope.ACC.address}">
                                                                    ${sessionScope.ACC.address}
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <span
                                                                        style="color: #8a93a2; font-style: italic;">Chưa
                                                                        cắm mắt (Cập nhật sau)</span>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </div>
                                                    </div>

                                                    <div class="info-group">
                                                        <div class="info-label"><i class="ti-bolt"></i> Rank</div>
                                                        <div class="exp-points">
                                                            ${sessionScope.ACC.rewardPoints} Point
                                                        </div>
                                                    </div>

                                                    <div class="info-group"
                                                        style="text-align: center; border-bottom: none; margin-top: 40px;">
                                                        <div class="info-label" style="margin-bottom: 20px;"><i
                                                                class="ti-target"></i> Mức Xếp Hạng Hiện Tại</div>

                                                        <div class="membership-rank-badge">
                                                            <i class="${iconClass}"></i> ${rank != null ? rank : 'Tân
                                                            Binh'}
                                                        </div>
                                                    </div>

                                                    <div class="profile-actions" style="margin-top: 40px;">
                                                        <a href="edit-profile" class="btn btn-rank-action"
                                                            style="text-decoration: none; padding: 0 25px; line-height: 44px; height: 44px; border-radius: 4px; display: inline-flex; align-items: center;">
                                                            <i class="ti-pencil-alt" style="margin-right: 8px;"></i>
                                                            Chỉnh sửa
                                                        </a>
                                                        <a href="user-logout" class="btn btn--outline-danger"
                                                            style="line-height: 40px; height: 44px; border-radius: 4px; padding: 0 25px; border: 2px solid #ff4757; color: #ff4757;">
                                                            <i class="ti-power-off" style="margin-right: 8px;"></i> Đăng
                                                            Xuất
                                                        </a>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                </div>
                            </div>
                    </div>
                </div>
            </body>

            </html>