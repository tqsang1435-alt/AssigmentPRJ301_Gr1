<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import="java.util.*, vn.edu.phoneshop.model.*" %>

<!DOCTYPE html>
<html langs="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Test Giỏ Hàng - PhoneShop</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: Arial, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            padding: 20px;
        }

        .container {
            max-width: 1000px;
            margin: 0 auto;
            background: white;
            border-radius: 10px;
            padding: 40px;
            box-shadow: 0 10px 40px rgba(0, 0, 0, 0.3);
        }

        h1 {
            color: #333;
            margin-bottom: 30px;
            text-align: center;
        }

        .test-section {
            background: #f9f9f9;
            border-left: 4px solid #667eea;
            padding: 20px;
            margin-bottom: 20px;
            border-radius: 4px;
        }

        .test-section h2 {
            color: #667eea;
            font-size: 18px;
            margin-bottom: 15px;
        }

        .test-case {
            background: white;
            padding: 15px;
            margin-bottom: 10px;
            border-radius: 4px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .test-case p {
            flex: 1;
        }

        .test-url {
            font-family: 'Courier New', monospace;
            color: #e74c3c;
            font-size: 12px;
            margin: 10px 0;
            background: #f0f0f0;
            padding: 10px;
            border-radius: 4px;
            word-break: break-all;
        }

        a.test-link {
            background: #3498db;
            color: white;
            padding: 10px 20px;
            border-radius: 4px;
            text-decoration: none;
            transition: 0.3s;
            white-space: nowrap;
            margin-left: 10px;
        }

        a.test-link:hover {
            background: #2980b9;
        }

        .info-box {
            background: #e8f4f8;
            border-left: 4px solid #3498db;
            padding: 15px;
            margin-bottom: 20px;
            border-radius: 4px;
        }

        .info-box h3 {
            color: #3498db;
            margin-bottom: 10px;
        }

        .info-box p {
            color: #555;
            line-height: 1.6;
        }

        .status-box {
            background: #f0f8f0;
            border-left: 4px solid #27ae60;
            padding: 15px;
            margin-bottom: 20px;
            border-radius: 4px;
        }

        .status-box h3 {
            color: #27ae60;
            margin-bottom: 10px;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 15px;
        }

        th, td {
            border: 1px solid #ddd;
            padding: 12px;
            text-align: left;
        }

        th {
            background: #667eea;
            color: white;
        }

        tr:hover {
            background: #f5f5f5;
        }

        .code {
            background: #f4f4f4;
            border: 1px solid #ddd;
            border-radius: 4px;
            padding: 15px;
            font-family: 'Courier New', monospace;
            overflow-x: auto;
            margin: 10px 0;
        }

        .success {
            color: #27ae60;
            font-weight: bold;
        }

        .warning {
            color: #f39c12;
            font-weight: bold;
        }

        .error {
            color: #e74c3c;
            font-weight: bold;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>🧪 Test Hệ Thống Giỏ Hàng (Shopping Cart)</h1>

        <div class="status-box">
            <h3>✅ Trạng thái Hệ thống</h3>
            <p>
                <%
                    Cart cart = (Cart) session.getAttribute("cart");
                    if (cart != null) {
                        out.print("<span class='success'>Giỏ hàng đã được khởi tạo</span>");
                        out.print("<br>Số sản phẩm: " + cart.getTotalQuantity());
                        out.print("<br>Tổng tiền: " + cart.getTotalPrice());
                    } else {
                        out.print("<span class='warning'>Giỏ hàng chưa được khởi tạo (Sẽ tạo khi thêm sản phẩm đầu tiên)</span>");
                    }
                %>
            </p>
        </div>

        <div class="info-box">
            <h3>ℹ️ Lưu ý quan trọng</h3>
            <p>
                • Các test URL dưới đây cần có sản phẩm trong database (ProductID 1, 2, 3...)<br>
                • Giỏ hàng được lưu trữ trong Session của mỗi user<br>
                • Nếu không có sản phẩm, sẽ không thêm được vào giỏ<br>
                • Kiểm tra database có dữ liệu sản phẩm không trước khi test
            </p>
        </div>

        <!-- TEST 1: Thêm sản phẩm -->
        <div class="test-section">
            <h2>1️⃣ TEST THÊM SẢN PHẨM VÀO GIỎ</h2>

            <div class="test-case">
                <div>
                    <p><strong>Thêm sản phẩm ID=1, SL=1</strong></p>
                    <div class="test-url">/add-to-cart?productID=1&quantity=1</div>
                </div>
                <a href="${pageContext.request.contextPath}/add-to-cart?productID=1&quantity=1&returnURL=${pageContext.request.requestURI}" class="test-link">Test →</a>
            </div>

            <div class="test-case">
                <div>
                    <p><strong>Thêm sản phẩm ID=2, SL=2</strong></p>
                    <div class="test-url">/add-to-cart?productID=2&quantity=2</div>
                </div>
                <a href="${pageContext.request.contextPath}/add-to-cart?productID=2&quantity=2&returnURL=${pageContext.request.requestURI}" class="test-link">Test →</a>
            </div>

            <div class="test-case">
                <div>
                    <p><strong>Thêm sản phẩm ID=3, SL=3</strong></p>
                    <div class="test-url">/add-to-cart?productID=3&quantity=3</div>
                </div>
                <a href="${pageContext.request.contextPath}/add-to-cart?productID=3&quantity=3&returnURL=${pageContext.request.requestURI}" class="test-link">Test →</a>
            </div>

            <div class="test-case">
                <div>
                    <p><strong>Thêm lại sản phẩm ID=1, SL=1 (test cộng dồn SL)</strong></p>
                    <div class="test-url">/add-to-cart?productID=1&quantity=1</div>
                </div>
                <a href="${pageContext.request.contextPath}/add-to-cart?productID=1&quantity=1&returnURL=${pageContext.request.requestURI}" class="test-link">Test →</a>
            </div>
        </div>

        <!-- TEST 2: Xem giỏ hàng -->
        <div class="test-section">
            <h2>2️⃣ TEST XEM GIỎ HÀNG</h2>

            <div class="test-case">
                <div>
                    <p><strong>Xem toàn bộ giỏ hàng</strong></p>
                    <div class="test-url">/view-cart</div>
                </div>
                <a href="${pageContext.request.contextPath}/view-cart" class="test-link">Test →</a>
            </div>
        </div>

        <!-- TEST 3: Cập nhật số lượng -->
        <div class="test-section">
            <h2>3️⃣ TEST CẬP NHẬT SỐ LƯỢNG</h2>

            <div class="test-case">
                <div>
                    <p><strong>Cập nhật sản phẩm ID=1 thành SL=5</strong></p>
                    <div class="test-url">/update-cart?productID=1&quantity=5</div>
                </div>
                <a href="${pageContext.request.contextPath}/update-cart?productID=1&quantity=5" class="test-link">Test →</a>
            </div>

            <div class="test-case">
                <div>
                    <p><strong>Cập nhật sản phẩm ID=2 thành SL=1</strong></p>
                    <div class="test-url">/update-cart?productID=2&quantity=1</div>
                </div>
                <a href="${pageContext.request.contextPath}/update-cart?productID=2&quantity=1" class="test-link">Test →</a>
            </div>
        </div>

        <!-- TEST 4: Xóa sản phẩm -->
        <div class="test-section">
            <h2>4️⃣ TEST XÓA SẢN PHẨM</h2>

            <div class="test-case">
                <div>
                    <p><strong>Xóa sản phẩm ID=3 khỏi giỏ</strong></p>
                    <div class="test-url">/remove-from-cart?productID=3</div>
                </div>
                <a href="${pageContext.request.contextPath}/remove-from-cart?productID=3" class="test-link">Test →</a>
            </div>

            <div class="test-case">
                <div>
                    <p><strong>Xóa sản phẩm ID=2 khỏi giỏ</strong></p>
                    <div class="test-url">/remove-from-cart?productID=2</div>
                </div>
                <a href="${pageContext.request.contextPath}/remove-from-cart?productID=2" class="test-link">Test →</a>
            </div>
        </div>

        <!-- TEST 5: Xóa toàn bộ -->
        <div class="test-section">
            <h2>5️⃣ TEST XÓA TOÀN BỘ GIỎ HÀNG</h2>

            <div class="test-case">
                <div>
                    <p><strong>Xóa toàn bộ giỏ hàng</strong></p>
                    <div class="test-url">/clear-cart</div>
                </div>
                <a href="${pageContext.request.contextPath}/clear-cart" class="test-link">Test →</a>
            </div>
        </div>

        <!-- Bảng URL tham khảo -->
        <div class="test-section">
            <h2>📋 BẢNG THAM KHẢO CÁC URL</h2>
            <table>
                <thead>
                    <tr>
                        <th>Chức năng</th>
                        <th>URL</th>
                        <th>Parameter</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>Thêm vào giỏ</td>
                        <td>/add-to-cart</td>
                        <td>productID, quantity (tùy), returnURL (tùy)</td>
                    </tr>
                    <tr>
                        <td>Xem giỏ hàng</td>
                        <td>/view-cart</td>
                        <td>-</td>
                    </tr>
                    <tr>
                        <td>Cập nhật SL</td>
                        <td>/update-cart</td>
                        <td>productID, quantity</td>
                    </tr>
                    <tr>
                        <td>Xóa sản phẩm</td>
                        <td>/remove-from-cart</td>
                        <td>productID</td>
                    </tr>
                    <tr>
                        <td>Xóa giỏ</td>
                        <td>/clear-cart</td>
                        <td>-</td>
                    </tr>
                </tbody>
            </table>
        </div>

        <!-- Hướng dẫn test -->
        <div class="test-section">
            <h2>🚀 QUY TRÌNH TEST ĐỀ XUẤT</h2>
            <div class="code">
1. Xem giỏ hàng hiện tại (/view-cart)
2. Thêm sản phẩm ID=1, SL=1 (/add-to-cart?productID=1&quantity=1)
3. Xem giỏ hàng lại (/view-cart)
4. Thêm sản phẩm ID=2, SL=2 (/add-to-cart?productID=2&quantity=2)
5. Cập nhật sản phẩm ID=1 thành SL=5 (/update-cart?productID=1&quantity=5)
6. Xem giỏ hàng lần nữa (/view-cart)
7. Xóa sản phẩm ID=1 (/remove-from-cart?productID=1)
8. Xem giỏ hàng và kiểm tra sản phẩm ID=1 đã bị xóa (/view-cart)
9. Xóa toàn bộ giỏ (/clear-cart)
10. Xem giỏ hàng cuối cùng (/view-cart)
            </div>
        </div>

        <!-- Kiểm tra Session -->
        <div class="test-section">
            <h2>🔍 KIỂM TRA SESSION</h2>
            <%
                Cart sessionCart = (Cart) session.getAttribute("cart");
                if (sessionCart != null && sessionCart.getCartSize() > 0) {
            %>
                <div class="code">
                    <strong>Giỏ hàng hiện tại:</strong><br><br>
                    <%
                        for (CartItem item : sessionCart.getCartItems()) {
                            out.print("• " + item.getProduct().getProductName());
                            out.print(" (ID: " + item.getProduct().getProductID() + ")");
                            out.print(" - SL: " + item.getQuantity());
                            out.print(" - Tiền: " + item.getSubtotal() + "<br>");
                        }
                        out.print("<br>");
                        out.print("<strong>Tổng cộng:</strong> " + sessionCart.getTotalQuantity() + " sản phẩm<br>");
                        out.print("<strong>Tổng tiền:</strong> " + sessionCart.getTotalPrice());
                    %>
                </div>
            <%
                } else {
            %>
                <p><span class="warning">Giỏ hàng hiện đang trống</span></p>
            <%
                }
            %>
        </div>
    </div>
</body>
</html>
