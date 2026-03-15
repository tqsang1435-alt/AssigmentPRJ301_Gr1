<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <!DOCTYPE html>
    <html lang="vi">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Thanh toán thành công - PhoneShop</title>

        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/cssreset.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/base.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/grid.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css?v=1001">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/gh/lykmapipo/themify-icons@0.1.2/css/themify-icons.css">

        <style>
            .thanks-container {
                display: flex;
                align-items: center;
                justify-content: center;
                min-height: 60vh;
                padding: 40px 0;
                background-color: #f5f5f5;
            }

            .thanks-card {
                background: #fff;
                border-radius: 8px;
                box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
                padding: 50px 40px;
                text-align: center;
                max-width: 600px;
                width: 100%;
                margin: 0 auto;
            }

            .thanks-icon {
                font-size: 8rem;
                color: #28a745;
                margin-bottom: 20px;
            }

            .thanks-title {
                font-size: 2.8rem;
                font-weight: bold;
                color: var(--text-color);
                margin-bottom: 15px;
                text-transform: uppercase;
            }

            .thanks-desc {
                font-size: 1.6rem;
                color: #666;
                margin-bottom: 30px;
                line-height: 1.5;
            }

            .thanks-divider {
                border-top: 1px solid #eee;
                margin-bottom: 30px;
            }
        </style>
    </head>

    <body>
        <jsp:include page="header.jsp" />

        <div class="thanks-container">
            <div class="grid wide">
                <div class="row" style="justify-content: center;">
                    <div class="col l-8 m-10 c-12">
                        <div class="thanks-card">
                            <div class="thanks-icon">
                                <i class="ti-check-box"></i>
                            </div>
                            <h2 class="thanks-title">Đặt hàng thành công!</h2>
                            <p class="thanks-desc">
                                ${not empty message ? message : "Cảm ơn bạn đã tin tưởng PhoneShop. Đơn hàng của bạn
                                đang được hệ thống xử lý và sẽ được giao trong thời gian sớm nhất."}
                            </p>
                            <div class="thanks-divider"></div>
                            <a href="${pageContext.request.contextPath}/home" class="btn btn--primary"
                                style="padding: 0 40px; height: 44px; line-height: 44px; font-size: 1.6rem; border-radius: 30px;">
                                TIẾP TỤC MUA SẮM
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <jsp:include page="footer.jsp" />
    </body>

    </html>