<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>

        <%-- ĐOẠN FAKE DATA ĐỂ TEST GIAO DIỆN --%>
            <% if (request.getAttribute("customerList")==null) { java.util.List list=new java.util.ArrayList();
                java.util.Map c1=new java.util.HashMap(); c1.put("id", "C001" ); c1.put("fullName", "Huân Nguyễn" );
                c1.put("phone", "0901234567" ); c1.put("email", "huan@email.com" ); c1.put("rewardPoints", 1500);
                c1.put("customerType", "Tech Lover" ); java.util.Map c2=new java.util.HashMap(); c2.put("id", "C002" );
                c2.put("fullName", "Lê Văn Quý" ); c2.put("phone", "0911222333" ); c2.put("email", "quy.lead@email.com"
                ); c2.put("rewardPoints", 8500); c2.put("customerType", "Reseller" ); list.add(c1); list.add(c2);
                request.setAttribute("customerList", list); } %>
                <%--===================================================================================--%>

                    <!DOCTYPE html>
                    <html lang="vi">

                    <head>
                        <meta charset="UTF-8">
                        <meta name="viewport" content="width=device-width, initial-scale=1.0">
                        <title>Quản lý Khách hàng - PhoneShop Admin</title>

                        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/cssreset.css">
                        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/base.css">
                        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/grid.css">
                        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">

                        <link rel="stylesheet"
                            href="https://cdn.jsdelivr.net/gh/lykmapipo/themify-icons@0.1.2/css/themify-icons.css">
                    </head>

                    <body>
                        <header class="header">
                            <div class="grid wide">
                                <div class="header__navbar">
                                    <a href="${pageContext.request.contextPath}/" class="header__logo"><i
                                            class="ti-mobile"></i> <label>PhoneShop Admin</label></a>
                                    <ul class="header__nav-list">
                                        <li class="header__nav-item">
                                            <a href="MainController?action=Logout" class="header__nav-link"><i
                                                    class="ti-user"></i> <label>Đăng xuất</label></a>
                                        </li>
                                    </ul>
                                </div>
                            </div>
                        </header>

                        <div class="admin-content">
                            <div class="grid wide">
                                <div class="row">
                                    <div class="col l-12 m-12 c-12">
                                        <div class="card">

                                            <div class="card-header">
                                                <h2 class="card-title">Danh sách Khách hàng</h2>
                                                <button class="btn btn--primary">
                                                    <i class="ti-plus" style="margin-right: 8px;"></i> Thêm khách hàng
                                                </button>
                                            </div>

                                            <div class="filter-group" style="margin-bottom: 20px;">
                                                <input type="text" placeholder="Tìm tên, SĐT..."
                                                    style="padding: 10px 14px; border: 1px solid var(--border-color); border-radius: 4px; width: 300px; outline: none; font-size: 1.4rem;">
                                                <select
                                                    style="padding: 10px 14px; border: 1px solid var(--border-color); border-radius: 4px; outline: none; font-size: 1.4rem;">
                                                    <option value="">Tất cả phân loại</option>
                                                    <option value="Tech Lover">Tech Lover</option>
                                                    <option value="Reseller">Reseller</option>
                                                    <option value="Normal">Khách thường</option>
                                                </select>
                                                <button class="btn btn--normal"><i class="ti-filter"></i> Lọc</button>
                                            </div>

                                            <table class="table">
                                                <thead>
                                                    <tr>
                                                        <th>ID</th>
                                                        <th>Họ và Tên</th>
                                                        <th>Số điện thoại</th>
                                                        <th>Email</th>
                                                        <th>Điểm tích lũy</th>
                                                        <th>Phân loại</th>
                                                        <th>Hành động</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:forEach items="${requestScope.customerList}" var="cus">
                                                        <tr>
                                                            <td>${cus.id}</td>
                                                            <td style="font-weight: 500;">${cus.fullName}</td>
                                                            <td>${cus.phone}</td>
                                                            <td>${cus.email}</td>
                                                            <td>
                                                                ${cus.rewardPoints} <i class="ti-star"
                                                                    style="color: var(--gold-color);"></i>
                                                            </td>
                                                            <td>
                                                                <c:choose>
                                                                    <c:when test="${cus.customerType == 'Tech Lover'}">
                                                                        <span
                                                                            class="badge badge--tech">${cus.customerType}</span>
                                                                    </c:when>
                                                                    <c:when test="${cus.customerType == 'Reseller'}">
                                                                        <span
                                                                            class="badge badge--reseller">${cus.customerType}</span>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <span class="badge badge--normal">Khách
                                                                            thường</span>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </td>
                                                            <td>
                                                                <a href="MainController?action=EditCustomer&id=${cus.id}"
                                                                    class="action-btn" title="Chỉnh sửa"><i
                                                                        class="ti-pencil"></i></a>
                                                                <a href="MainController?action=DeleteCustomer&id=${cus.id}"
                                                                    class="action-btn action-btn--delete" title="Xóa"
                                                                    onclick="return confirm('Ông có chắc muốn xóa khách này không?');"><i
                                                                        class="ti-trash"></i></a>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>

                                                    <c:if test="${empty requestScope.customerList}">
                                                        <tr>
                                                            <td colspan="7"
                                                                style="text-align: center; padding: 30px; color: #888;">
                                                                <i class="ti-info-alt"></i> Chưa có khách hàng nào trong
                                                                hệ thống!
                                                            </td>
                                                        </tr>
                                                    </c:if>
                                                </tbody>
                                            </table>

                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </body>

                    </html>