<%-- Document : thank Created on : Mar 3, 2026, 11:39:20 PM Author : Lenovo --%>

    <%@page contentType="text/html" pageEncoding="UTF-8" %>
        <!DOCTYPE html>
        <html>

        <head>
            <title>Thanh toán thành công</title>
            <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        </head>

        <body class="bg-light text-center" style="padding-top: 100px;">
            <div class="container">
                <div class="card shadow p-5 border-0 mx-auto" style="max-width: 600px; border-radius: 20px;">
                    <div class="mb-4">
                        <i class="fa-solid fa-circle-check text-success" style="font-size: 5rem;"></i>
                    </div>
                    <h2 class="fw-bold">THANH TOÁN THÀNH CÔNG!</h2>
                    <p class="text-muted">${not empty message ? message : "Cảm ơn bạn đã tin tưởng PHONEShop. Đơn hàng
                        của bạn đang được hệ thống xử lý."}</p>
                    <hr>
                    <a href="home" class="btn btn-warning rounded-pill px-5 fw-bold">TIẾP TỤC MUA SẮM</a>
                </div>
            </div>
        </body>

        </html>