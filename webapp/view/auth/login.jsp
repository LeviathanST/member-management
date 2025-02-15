<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Log in Page</title>
        <style>
            body {
            margin: 0;
            padding: 0;
            background-color: #000000;
            color: #ffffff;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }
        .login-container {
            background-color: #342c2c;
            padding: 70px;
            border-radius: 12px;
            width: 350px;
            text-align: center;
        }
        .login-container h1 {
            font-size: 28px;
            margin-bottom: 20px;
            color: #ffffff;
        }
        .login-container p {
            font-size: 14px;
            color: #b3b3b3;
            margin-bottom: 20px;
        }
        input[type="text"], input[type="password"] {
            width: 320px;
            padding: 15px;
            margin: 15px 0;
            border: 1px solid #ffffff;
            border-radius: 8px;
            background-color: #000000;
            color: #ffffff;
            font-size: 16px;
            outline: none;
        }
        input::placeholder {
            color: #b3b3b3;
        }
        input[type="checkbox"] {
            margin-right: 5px;
        }
        .form-options {
            display: flex;
            justify-content: center;
            align-items: center;
            font-size: 14px;
            margin: 10px 0;
            color: #ffffff;
        }
        .form-options a {
            color: #aa130f;
            text-decoration: none;
        }
        .login-button {
            background-color: #902020;
            color: white;
            border: none;
            border-radius: 8px;
            padding: 20px;
            width: 100%;
            font-size: 16px;
            font-weight: bold;
            cursor: pointer;
            margin-top: 20px;
        }
        .login-button:hover {
            background-color: #aa130f;
        }
        </style>
    </head>
    <body>
	<div class="login-container">
            <h1>Log In</h1>
            <form id="myForm">
                <input type="text" id="username" placeholder="Username" required>
                <input type="password" id="password" placeholder="Password" required>
                <button type="button" onclick="sendData()" class="login-button">Log In</button>
            </form>
</form>
</body>
<script>
    function sendData() {
        let user = {
            username: document.getElementById("username").value,
            password: document.getElementById("password").value
        };
	fetch("<%=request.getContextPath()%>/auth/login", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(user)
        }).then(response => response.json())
          .then(data => {alert("Server Response: " + data.message);
          if(data.success) {
              window.location.href = "<%=request.getContextPath()%>/home";
          }});
    }
</script>
</html>
