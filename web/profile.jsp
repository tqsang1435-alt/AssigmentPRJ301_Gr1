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
                <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css?v=1006">
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
                            <c:set var="discountPercent" value="0" />

                            <c:choose>
                                <c:when test="${fn:contains(rank, 'Diamond')}">
                                    <c:set var="rankClass" value="rank-diamond" />
                                    <c:set var="iconClass" value="ti-diamond" />
                                    <c:set var="discountPercent" value="15" />
                                </c:when>
                                <c:when test="${fn:contains(rank, 'Gold')}">
                                    <c:set var="rankClass" value="rank-gold" />
                                    <c:set var="iconClass" value="ti-star" />
                                    <c:set var="discountPercent" value="10" />
                                </c:when>
                                <c:when test="${fn:contains(rank, 'Silver')}">
                                    <c:set var="rankClass" value="rank-silver" />
                                    <c:set var="iconClass" value="ti-medall" />
                                    <c:set var="discountPercent" value="5" />
                                </c:when>
                                <c:when test="${fn:contains(rank, 'Bronze')}">
                                    <c:set var="rankClass" value="rank-bronze" />
                                    <c:set var="iconClass" value="ti-medall-alt" />
                                    <c:set var="discountPercent" value="2" />
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
                                                    <!-- <div class="info-group">
                                                        <div class="info-label"><i class="ti-id-badge"></i> User ID
                                                        </div>
                                                        <div class="info-value">#${sessionScope.ACC.userID}</div>
                                                    </div> -->

                                                    <div class="info-group">
                                                        <div class="info-label"><i class="ti-email"></i> Email
                                                        </div>
                                                        <div class="info-value">${sessionScope.ACC.email}</div>
                                                    </div>

                                                    <div class="info-group">
                                                        <div class="info-label"><i class="ti-mobile"></i>Số điện thoại
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
                                                        <div class="info-label"><i class="ti-location-pin"></i> Địa chỉ
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
                                                        <div class="info-label"><i class="ti-bolt"></i> Điểm thưởng
                                                        </div>
                                                        <div class="exp-points">
                                                            ${sessionScope.ACC.rewardPoints} Điểm
                                                        </div>
                                                    </div>

                                                    <div class="info-group"
                                                        style="text-align: center; border-bottom: none; margin-top: 20px;padding-bottom: 0;">
                                                        <div class="info-label" style="margin-bottom: 10px;">
                                                            <i class="ti-target"></i> Bậc hạng
                                                        </div>

                                                        <div class="membership-rank-badge">
                                                            <i class="${iconClass}"></i> ${rank != null ? rank : 'New
                                                            Member'}
                                                        </div>
                                                        <c:if test="${discountPercent > 0}">
                                                            <p class="rank-discount-info"
                                                                style="margin-top: 10px; color: #28a745; font-weight: 500;">
                                                                (Giảm giá ${discountPercent}% cho mọi đơn hàng)
                                                            </p>
                                                        </c:if>
                                                    </div>

                                                </div>

                                                <div class="profile-actions" style="margin-bottom: 25px;margin-top: 0;">
                                                    <a href="edit-profile" class="btn btn-rank-action"
                                                        style="text-decoration: none; padding: 0 25px; line-height: 44px; height: 44px; border-radius: 4px; display: inline-flex; align-items: center;">
                                                        <i class="ti-pencil-alt" style="margin-right: 8px;"></i>
                                                        Chỉnh sửa
                                                    </a>
                                                    <a href="user-logout" class="btn btn--outline-danger"
                                                        onclick="return confirm('Bạn có chắc chắn muốn đăng xuất?');"
                                                        style="line-height: 40px; height: 44px; border-radius: 4px; padding: 0 25px; border: 2px solid #ff4757; color: #ff4757;">
                                                        <i class="ti-power-off" style="margin-right: 8px;"></i>
                                                        Đăng xuất
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