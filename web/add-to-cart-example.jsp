<%-- 
    Thêm code này vào trang danh sách sản phẩm của bạn (ví dụ: product-list.jsp)
    để hiển thị nút "Thêm vào giỏ hàng"
--%>

<%-- Ở phần hiển thị sản phẩm --%>
<div class="product-card">
    <img src="${product.imageURL}" alt="${product.productName}">
    <h3>${product.productName}</h3>
    <p class="price">
        <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="₫" pattern="###,###" />
    </p>
    
    <%-- Form thêm vào giỏ hàng --%>
    <form action="${pageContext.request.contextPath}/add-to-cart" method="GET">
        <input type="hidden" name="productID" value="${product.productID}">
        <input type="hidden" name="returnURL" value="${pageContext.request.requestURI}">
        
        <div style="display: flex; gap: 10px; margin-bottom: 10px;">
            <input type="number" name="quantity" value="1" min="1" max="99" 
                   style="width: 60px; padding: 8px; border: 1px solid #ddd; border-radius: 4px;">
            <button type="submit" class="btn btn-primary" style="flex: 1;">
                <i class="ti-bag"></i> Thêm vào giỏ
            </button>
        </div>
    </form>
</div>

<%-- 
    Hoặc nếu bạn muốn hiển thị nút đơn giản hơn:
--%>
<a href="${pageContext.request.contextPath}/add-to-cart?productID=${product.productID}&quantity=1&returnURL=${pageContext.request.requestURI}" 
   class="btn btn-primary">
    <i class="ti-bag"></i> Thêm vào giỏ
</a>
