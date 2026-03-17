<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
            <!DOCTYPE html>
            <html lang="vi">

            <head>
                <meta charset="UTF-8">
                <title>Quản lý Sản phẩm - PhoneShop Admin</title>
                <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/cssreset.css">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/base.css">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/grid.css">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
                <link rel="stylesheet"
                    href="https://cdn.jsdelivr.net/gh/lykmapipo/themify-icons@0.1.2/css/themify-icons.css">
            </head>

            <body>
                <div class="admin-container">
                    <%-- SIDEBAR --%>
                        <jsp:include page="admin-sidebar.jsp"></jsp:include>

                        <div class="admin-main">
                            <div class="admin-header">
                                <h3>Quản lý Sản phẩm</h3>
                                <div class="admin-user-info">Xin chào, <strong>${sessionScope.ACC.fullName}</strong>
                                </div>
                            </div>

                            <div class="admin-content">
                                <div class="card">
                                    <div class="card-header">
                                        <h3 class="card-title">Danh sách sản phẩm</h3>
                                        <a href="add-product" class="btn btn--primary"><i class="ti-plus"></i> Thêm
                                            mới</a>
                                    </div>

                                    <%-- Filter Form --%>
                                        <form action="admin-product-list" method="get"
                                            style="margin-bottom: 20px; display: flex; gap: 10px; align-items: center;">
                                            <input type="text" name="searchName" class="form-control"
                                                placeholder="Tìm theo tên..." value="${searchName}"
                                                style="width: 200px;">
                                            <select name="ramFilter" class="form-control" style="width: 150px;">
                                                <option value="">-- Chọn RAM --</option>
                                                <c:forEach items="${listRAM}" var="ram">
                                                    <option value="${ram}" ${selectedRam==ram ? 'selected' : '' }>${ram}
                                                    </option>
                                                </c:forEach>
                                            </select>
                                            <select name="romFilter" class="form-control" style="width: 150px;">
                                                <option value="">-- Chọn ROM --</option>
                                                <c:forEach items="${listROM}" var="rom">
                                                    <option value="${rom}" ${selectedRom==rom ? 'selected' : '' }>${rom}
                                                    </option>
                                                </c:forEach>
                                            </select>
                                            <button type="submit" class="btn btn--primary">Lọc</button>
                                            <a href="admin-product-list" class="btn">Reset</a>
                                        </form>

                                        <div class="card-body">
                                            <table class="table">
                                                <thead>
                                                    <tr>
                                                        <th>ID</th>
                                                        <th>Hình ảnh</th>
                                                        <th>Tên sản phẩm</th>
                                                        <th>Giá</th>
                                                        <th>Kho</th>
                                                        <th>RAM</th>
                                                        <th>ROM</th>
                                                        <th>Màu</th>
                                                        <th>Hành động</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:forEach items="${listP}" var="p">
                                                        <tr>
                                                            <td>#${p.productID}</td>
                                                            <td>
                                                                <img src="${not empty p.imageURL ? p.imageURL : pageContext.request.contextPath += '/assets/img/default-phone.png'}"
                                                                    alt=""
                                                                    style="width: 50px; height: 50px; object-fit: contain;"
                                                                    onerror="this.onerror=null;this.src='${pageContext.request.contextPath}/assets/img/default-phone.png';">
                                                            </td>
                                                            <td>${p.productName}</td>
                                                            <td style="color: var(--primary-color); font-weight: bold;">
                                                                <fmt:formatNumber value="${p.price}" type="currency"
                                                                    currencySymbol="₫" />
                                                            </td>
                                                            <td>${p.stockQuantity}</td>
                                                            <td>${p.ram}</td>
                                                            <td>${p.rom}</td>
                                                            <td>${p.color}</td>
                                                            <td>
                                                                <a href="loadProduct?pid=${p.productID}"
                                                                    class="action-btn" title="Sửa"><i
                                                                        class="ti-pencil"></i></a>
                                                                <a href="delete?pid=${p.productID}"
                                                                    class="action-btn action-btn--delete" title="Xóa"
                                                                    onclick="return confirm('Bạn có chắc muốn xóa sản phẩm này?')"><i
                                                                        class="ti-trash"></i></a>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                </tbody>
                                            </table>

                                            <%-- Pagination Controls --%>
                                                <c:if test="${totalPages > 1}">
                                                    <div class="pagination"
                                                        style="display: flex; justify-content: center; gap: 5px; margin-top: 20px;">
                                                        <%-- Previous Button --%>
                                                            <c:if test="${currentPage > 1}">
                                                                <a href="admin-product-list?page=${currentPage - 1}&searchName=${searchName}&ramFilter=${selectedRam}&romFilter=${selectedRom}"
                                                                    class="btn">Trước</a>
                                                            </c:if>

                                                            <%-- Page Numbers --%>
                                                                <c:set var="showEllipsis" value="false" />
                                                                <c:forEach begin="1" end="${totalPages}" var="i">
                                                                    <c:choose>
                                                                        <c:when
                                                                            test="${i == 1 || i == totalPages || (i >= currentPage - 2 && i <= currentPage + 2)}">
                                                                            <c:set var="showEllipsis" value="false" />
                                                                            <c:choose>
                                                                                <c:when test="${currentPage == i}">
                                                                                    <a href="#"
                                                                                        class="btn btn--primary">${i}</a>
                                                                                </c:when>
                                                                                <c:otherwise>
                                                                                    <a href="admin-product-list?page=${i}&searchName=${searchName}&ramFilter=${selectedRam}&romFilter=${selectedRom}"
                                                                                        class="btn">${i}</a>
                                                                                </c:otherwise>
                                                                            </c:choose>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <c:if test="${!showEllipsis}">
                                                                                <span class="btn"
                                                                                    style="pointer-events: none; border: none; background: transparent; padding: 0 5px; box-shadow: none;">...</span>
                                                                                <c:set var="showEllipsis"
                                                                                    value="true" />
                                                                            </c:if>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </c:forEach>

                                                                <%-- Next Button --%>
                                                                    <c:if test="${currentPage < totalPages}">
                                                                        <a href="admin-product-list?page=${currentPage + 1}&searchName=${searchName}&ramFilter=${selectedRam}&romFilter=${selectedRom}"
                                                                            class="btn">Tiếp</a>
                                                                    </c:if>
                                                    </div>
                                                </c:if>
                                        </div>
                                </div>
                            </div>
                        </div>
                </div>
            </body>

            </html>