<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Register - PhoneShop</title>
        <style>
            body{
                font-family: Arial;
                background: linear-gradient(135deg,#43cea2,#185a9d);
                height:100vh;
                display:flex;
                align-items:center;
                justify-content:center;
            }
            .box{
                background:white;
                padding:30px 35px;
                width:420px;
                border-radius:12px;
                box-shadow:0 12px 30px rgba(0,0,0,0.2);
            }
            h2{
                text-align:center;
                margin-bottom:20px;
            }
            .form-group{
                margin-bottom:14px;
            }
            label{
                font-weight:600;
                display:block;
                margin-bottom:4px;
            }
            .required{
                color:red;
            }
            input, textarea{
                width:100%;
                padding:10px;
                border:1px solid #ccc;
                border-radius:6px;
                font-size:14px;
            }
            textarea{
                resize:none;
                height:70px;
            }
            button{
                width:100%;
                padding:11px;
                margin-top:10px;
                background:#43cea2;
                color:white;
                border:none;
                border-radius:6px;
                font-size:16px;
                font-weight:600;
                cursor:pointer;
            }
            button:hover{
                background:#36b58d;
            }
            .error{
                background:#ffe5e5;
                color:#cc0000;
                padding:9px;
                border-radius:6px;
                margin-bottom:12px;
                text-align:center;
                font-weight:bold;
            }
            .link{
                text-align:center;
                margin-top:12px;
                font-size:14px;
            }
        </style>
    </head>
    <body>

        <div class="box">
            <h2>Create Account</h2>

            <% if (request.getAttribute("error") != null) {%>
            <div class="error"><%=request.getAttribute("error")%></div>
            <% }%>

            <form action="register" method="post">

                <div class="form-group">
                    <label>Full name <span class="required">*</span></label>
                    <input type="text" name="fullName" required minlength="6"
                           value="<%= request.getParameter("fullName") != null ? request.getParameter("fullName") : ""%>">
                </div>

                <div class="form-group">
                    <label>Email <span class="required">*</span></label>
                    <input type="email" name="email" required pattern=".+@gmail\.com"
                           value="<%= request.getParameter("email") != null ? request.getParameter("email") : ""%>">
                </div>

                <div class="form-group">
                    <label>Phone number <span class="required">*</span></label>
                    <input type="text" name="phoneNumber" required pattern="\d{10}"
                           value="<%= request.getParameter("phoneNumber") != null ? request.getParameter("phoneNumber") : ""%>">
                </div>

                <div class="form-group">
                    <label>Password <span class="required">*</span></label>
                    <input type="password" name="password" required minlength="6"
                           value="<%= request.getParameter("password") != null ? request.getParameter("password") : ""%>">
                </div>

                <div class="form-group">
                    <label>Address <span style="color: red;">*</span></label>
                    <textarea name="address" required oninvalid="this.setCustomValidity('Vui lòng nhập địa chỉ giao hàng')" 
                              oninput="this.setCustomValidity('')" class="form-control"><%= request.getParameter("address") != null ? request.getParameter("address") : ""%></textarea>
                </div>

                <button type="submit">Register</button>
            </form>

            <div class="link">
                Already have account? <a href="login.jsp">Login</a>
            </div>
        </div>

    </body>
</html>