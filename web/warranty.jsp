<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
            <!DOCTYPE html>
            <html lang="vi">

            <head>
                <meta charset="UTF-8">
                <title>Tra cứu bảo hành - PhoneShop</title>
                <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/cssreset.css">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/base.css">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/grid.css">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
                <link rel="stylesheet"
                    href="https://cdn.jsdelivr.net/gh/lykmapipo/themify-icons@0.1.2/css/themify-icons.css">
                <style>
                    .warranty-container {
                        padding: 60px 0;
                        min-height: 65vh;
                        background-color: #f5f5f5;
                    }

                    .warranty-box {
                        background: #fff;
                        padding: 40px;
                        border-radius: 8px;
                        box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
                        max-width: 700px;
                        margin: 0 auto;
                    }

                    .warranty-title {
                        font-size: 2.4rem;
                        color: var(--text-color);
                        margin-bottom: 30px;
                        text-align: center;
                        font-weight: 600;
                        text-transform: uppercase;
                    }

                    .warranty-form {
                        display: flex;
                        gap: 10px;
                        margin-bottom: 30px;
                    }

                    .warranty-input {
                        flex: 1;
                        height: 46px;
                        padding: 0 15px;
                        font-size: 1.5rem;
                        border: 1px solid var(--border-color);
                        border-radius: 4px;
                        outline: none;
                        transition: border-color 0.2s;
                    }

                    .warranty-input:focus {
                        border-color: var(--primary-color);
                    }

                    .warranty-btn {
                        height: 46px;
                        padding: 0 30px;
                        font-size: 1.5rem;
                        background-color: var(--primary-color);
                        color: #fff;
                        border: none;
                        border-radius: 4px;
                        cursor: pointer;
                        font-weight: bold;
                        transition: opacity 0.2s;
                    }

                    .warranty-btn:hover {
                        opacity: 0.9;
                    }

                    .warranty-result {
                        border: 1px solid #eee;
                        border-radius: 4px;
                        padding: 20px;
                        background-color: #fafafa;
                        animation: fadeIn 0.3s ease-in;
                    }

                    .result-header {
                        font-size: 1.8rem;
                        color: var(--primary-color);
                        margin-bottom: 15px;
                        font-weight: bold;
                        border-bottom: 1px solid #eee;
                        padding-bottom: 10px;
                    }

                    .result-item {
                        font-size: 1.5rem;
                        margin-bottom: 12px;
                        display: flex;
                        align-items: center;
                    }

                    .result-label {
                        font-weight: 600;
                        width: 160px;
                        color: #555;
                    }

                    .result-value {
                        flex: 1;
                        color: #222;
                        font-weight: 500;
                    }

                    .error-msg {
                        color: #dc3545;
                        font-size: 1.5rem;
                        text-align: center;
                        margin-top: 15px;
                        background: #f8d7da;
                        padding: 10px;
                        border-radius: 4px;
                    }

                    .status-badge {
                        padding: 5px 12px;
                        border-radius: 20px;
                        font-size: 1.3rem;
                        color: #fff;
                        font-weight: bold;
                    }

                    .status-ok {
                        background-color: #28a745;
                    }

                    .status-exp {
                        background-color: #dc3545;
                    }

                    .status-warn {
                        background-color: #ffc107;
                        color: #000;
                    }
                </style>
            </head>

            <body>
                <jsp:include page="header.jsp" />

                <div class="warranty-container">
                    <div class="grid wide">
                        <div class="warranty-box">
                            <h2 class="warranty-title"><i class="ti-shield"></i> Tra cứu thông tin bảo hành</h2>
                            <form action="warranty-check" method="POST" class="warranty-form">
                                <input type="text" name="imei" class="warranty-input"
                                    placeholder="Nhập số IMEI / Serial Number của sản phẩm..." required
                                    value="${param.imei}">
                                <button type="submit" class="warranty-btn"><i class="ti-search"></i> Kiểm tra</button>
                            </form>

                            <c:if test="${not empty error}">
                                <div class="error-msg"><i class="ti-info-alt"></i> ${error}</div>
                            </c:if>

                            <c:if test="${imei != null}">
                                <div class="warranty-result">
                                    <div class="result-header">Chi tiết thiết bị</div>
                                    <div class="result-item">
                                        <span class="result-label">Số IMEI/Serial:</span>
                                        <span class="result-value">${imei.imeiNumber != null ? imei.imeiNumber :
                                            imei.serialNumber}</span>
                                    </div>
                                    <div class="result-item">
                                        <span class="result-label">Trạng thái:</span>
                                        <span class="result-value">
                                            <c:choose>
                                                <c:when test="${imei.status == 1}"><span
                                                        class="status-badge status-ok">Đã kích hoạt bảo hành</span>
                                                </c:when>
                                                <c:when test="${imei.status == 0}"><span
                                                        class="status-badge status-warn">Sản phẩm chưa kích hoạt (Chưa
                                                        bán)</span></c:when>
                                                <c:otherwise><span class="status-badge status-exp">Bảo hành / Lỗi</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </span>
                                    </div>
                                </div>
                            </c:if>
                        </div>
                    </div>
                </div>
                <jsp:include page="footer.jsp" />
            </body>

            </html>