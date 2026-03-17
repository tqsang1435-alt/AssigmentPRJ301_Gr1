<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<fmt:setLocale value="vi_VN"/>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Lịch sử mua hàng | PHONEShop</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/cssreset.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/base.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/grid.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/gh/lykmapipo/themify-icons@0.1.2/css/themify-icons.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css?v=1001">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/responsive.css">

    <style>
        .purchase-history-container {
            background-color: #f5f5f5;
            padding: 40px 0;
            min-height: calc(100vh - 200px);
        }
        
        .history-card {
            background: #fff;
            border-radius: 8px;
            box-shadow: 0 1px 3px rgba(0,0,0,0.1);
            padding: 24px;
        }

        .history-title {
            font-size: 2.2rem;
            color: var(--text-color);
            margin-bottom: 24px;
            padding-bottom: 12px;
            border-bottom: 1px solid #ebebeb;
            font-weight: 500;
        }

        .order-table {
            width: 100%;
            border-collapse: collapse;
        }

        .order-table th, .order-table td {
            padding: 16px;
            text-align: left;
            border-bottom: 1px solid #eee;
            font-size: 1.5rem;
        }

        .order-table th {
            background-color: #fafafa;
            color: #555;
            font-weight: 600;
        }

        .order-table tr:hover {
            background-color: #fcfcfc;
        }

        .status-badge {
            padding: 6px 12px;
            border-radius: 12px;
            font-size: 1.3rem;
            font-weight: 500;
            display: inline-block;
        }

        /* 0: Chờ xử lý, 1: Đang giao, 2: Hoàn thành, 3: Đã hủy */
        .status-0 { background-color: #fff3cd; color: #856404; }
        .status-1 { background-color: #cce5ff; color: #004085; }
        .status-2 { background-color: #d4edda; color: #155724; }
        .status-3 { background-color: #f8d7da; color: #721c24; }
        
        .empty-history {
            text-align: center;
            padding: 40px;
        }

        .empty-history i {
            font-size: 6rem;
            color: #ccc;
            margin-bottom: 16px;
        }

        .empty-history p {
            font-size: 1.6rem;
            color: #666;
            margin-bottom: 24px;
        }

        .btn-shopping {
            display: inline-block;
            padding: 12px 24px;
            background-color: var(--primary-color);
            color: white;
            text-decoration: none;
            border-radius: 4px;
            font-size: 1.5rem;
            transition: opacity 0.2s;
        }

        .btn-shopping:hover {
            opacity: 0.9;
        }
    </style>
</head>
<body>
    <jsp:include page="header.jsp" />

    <div class="purchase-history-container">
        <div class="grid wide">
            <div class="row">
                <div class="col l-12">
                    <div class="history-card">
                        <h2 class="history-title">Lịch sử mua hàng của bạn</h2>
                        
                        <c:choose>
                            <c:when test="${not empty listOrders}">
                                <c:if test="${not empty sessionScope.successMessage}">
                                    <div class="alert alert-success" style="padding: 15px; margin-bottom: 20px; border: 1px solid transparent; border-radius: 4px; color: #155724; background-color: #d4edda; border-color: #c3e6cb;">
                                        ${sessionScope.successMessage}
                                    </div>
                                    <c:remove var="successMessage" scope="session" />
                                </c:if>
                                <c:if test="${not empty sessionScope.errorMessage}">
                                    <div class="alert alert-danger" style="padding: 15px; margin-bottom: 20px; border: 1px solid transparent; border-radius: 4px; color: #721c24; background-color: #f8d7da; border-color: #f5c6cb;">
                                        ${sessionScope.errorMessage}
                                    </div>
                                    <c:remove var="errorMessage" scope="session" />
                                </c:if>

                                <div style="overflow-x: auto;">
                                    <table class="order-table">
                                        <thead>
                                            <tr>
                                                <th>Mã đơn hàng</th>
                                                <th>Ngày đặt</th>
                                                <th>Tổng tiền</th>
                                                <th>Địa chỉ giao hàng</th>
                                                <th>Trạng thái</th>
                                                <th>Hành động</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach items="${listOrders}" var="o">
                                                <tr>
                                                    <td><strong>#${o.orderId}</strong></td>
                                                    <td><fmt:formatDate value="${o.orderDate}" pattern="dd/MM/yyyy HH:mm" /></td>
                                                    <td style="color: var(--primary-color); font-weight: bold;">
                                                        <fmt:formatNumber value="${o.totalMoney}" pattern="#,##0" /> đ
                                                    </td>
                                                    <td>${o.shippingAddress}</td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${o.status == 0}">
                                                                <span class="status-badge status-0">Chờ xử lý</span>
                                                            </c:when>
                                                            <c:when test="${o.status == 1}">
                                                                <span class="status-badge status-1">Đang giao</span>
                                                            </c:when>
                                                            <c:when test="${o.status == 2}">
                                                                <span class="status-badge status-2">Hoàn thành</span>
                                                            </c:when>
                                                            <c:when test="${o.status == 3}">
                                                                <span class="status-badge status-3">Đã hủy</span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span class="status-badge">Không xác định</span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td>
                                                        <c:if test="${o.status == 0}">
                                                            <a href="${pageContext.request.contextPath}/cancel-order?id=${o.orderId}" 
                                                               class="btn-cancel"
                                                               onclick="return confirm('Bạn có chắc chắn muốn hủy đơn hàng #${o.orderId} này không?');"
                                                               style="display: inline-block; padding: 6px 12px; background-color: #dc3545; color: white; border-radius: 4px; text-decoration: none; font-size: 1.3rem;">
                                                                Hủy đơn
                                                            </a>
                                                        </c:if>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="empty-history">
                                    <i class="ti-receipt"></i>
                                    <p>Bạn chưa có đơn hàng nào.</p>
                                    <a href="${pageContext.request.contextPath}/home" class="btn-shopping">Tiếp tục mua sắm</a>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <jsp:include page="footer.jsp" />
    <jsp:include page="chat-widget.jsp"></jsp:include>
</body>
</html>
