<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Warranty Lookup</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="container mt-4">
    <h3>Tra cứu bảo hành theo IMEI / Serial</h3>

    <form action="warranty" method="post" class="mb-3">
        <div class="input-group">
            <input type="text" name="serial" class="form-control" placeholder="Nhập IMEI hoặc Serial" required />
            <button class="btn btn-primary" type="submit">Check</button>
        </div>
    </form>

    <c:if test="${imei != null}">
        <div class="card mb-3">
            <div class="card-body">
                <h5 class="card-title">IMEI: ${imei.serialNumber}</h5>
                <p class="card-text">Status code: ${imei.status}</p>

                <c:if test="${not empty orderMeta}">
                    <h6>Order Info (OrderID: ${orderMeta.OrderID})</h6>
                    <p>Order Date: ${orderMeta.OrderDate}</p>
                    <p>Shipping: ${orderMeta.ShippingAddress}</p>
                    <p>Warranty Start: ${orderMeta.WarrantyStart}</p>
                    <p>Warranty End: ${orderMeta.WarrantyEnd}</p>

                    <h6>Order Items</h6>
                    <table class="table table-sm">
                        <tr><th>Product</th><th>Qty</th><th>Price</th></tr>
                        <c:forEach items="${orderItems}" var="it">
                            <tr>
                                <td>${it.ProductName}</td>
                                <td>${it.Quantity}</td>
                                <td>${it.Price}</td>
                            </tr>
                        </c:forEach>
                    </table>
                </c:if>

                <c:if test="${empty orderMeta}">
                    <p>No order associated (not sold yet).</p>
                </c:if>
            </div>
        </div>
    </c:if>

</body>
</html>