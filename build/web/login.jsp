<%-- 
    Document   : login
    Created on : Mar 2, 2026, 9:14:14 PM
    Author     : Lenovo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>PhoneShop Login</title>
    <style>
        body{
            font-family: Arial;
            background: linear-gradient(135deg,#667eea,#764ba2);
            height:100vh;
            display:flex;
            align-items:center;
            justify-content:center;
        }
        .login-box{
            background:white;
            padding:30px;
            width:350px;
            border-radius:10px;
            box-shadow:0 10px 25px rgba(0,0,0,0.2);
        }
        h2{
            text-align:center;
            margin-bottom:20px;
        }
        input{
            width:100%;
            padding:10px;
            margin:8px 0;
            border:1px solid #ccc;
            border-radius:5px;
        }
        button{
            width:100%;
            padding:10px;
            background:#667eea;
            color:white;
            border:none;
            border-radius:5px;
            font-size:16px;
            cursor:pointer;
        }
        button:hover{
            background:#5a67d8;
        }
        .error{
            background:#ffe5e5;
            color:#cc0000;
            padding:8px;
            border-radius:5px;
            margin-bottom:10px;
            text-align:center;
            font-weight:bold;
        }
    </style>
</head>
<% if("success".equals(request.getParameter("register"))){ %>
<div id="toast-success">
    Bạn đã đăng ký thành công
</div>

<style>
#toast-success{
    position:fixed;
    top:20px;
    right:20px;
    background:#4CAF50;
    color:white;
    padding:14px 20px;
    border-radius:8px;
    font-weight:600;
    box-shadow:0 8px 20px rgba(0,0,0,0.2);
    z-index:9999;
    animation:fadeInOut 4s forwards;
}

@keyframes fadeInOut{
    0%{opacity:0; transform:translateY(-10px);}
    10%{opacity:1;}
    90%{opacity:1;}
    100%{opacity:0; transform:translateY(-10px);}
}
</style>
<% } %>
<body>

<div class="login-box">
    <h2>Phone Shop</h2>

    <% if(request.getAttribute("error") != null){ %>
        <div class="error">
            <%= request.getAttribute("error") %>
        </div>
    <% } %>

    <form action="login" method="post">
        <input type="text" name="email" placeholder="Email" required>
        <input type="password" name="password" placeholder="Password" required>
        <button type="submit">Login</button>
        <div style="text-align:center;margin-top:10px">
    Don't have account? <a href="register.jsp">Register</a>
</div>
    </form>
</div>

</body>
</html>
