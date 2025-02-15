<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sign Up Page</title>
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
        .signup-container {
            background-color: #342c2c;
            padding: 70px;
            border-radius: 12px;
            width: 350px;
            text-align: center;
        }
        .signup-container h1 {
            font-size: 28px;
            margin-bottom: 20px;
            color: #ffffff;
        }
        .signup-container p {
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
        .signup-button {
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
        .signup-button:hover {
            background-color: #aa130f;
        }
        </style>
    </head>
    <body>
        <div class="signup-container">
            <h1>Sign Up</h1>
            <form id="myForm">
                <input type="text" placeholder="Username" id="username" required>
                <input type="password" placeholder="Password" id="password" required>
                <button type="button" onclick="sendData()" class="signup-button">Sign Up</button>
            </form>
    </body>
    <script>
        function sendData() {
            let user = {
                username: document.getElementById("username").value,
                password: document.getElementById("password").value
            };
            fetch("<%=request.getContextPath()%>/auth/signup", {
                method: "POST",
                headers: {"Content-Type": "application/json"},
                body: JSON.stringify(user)
            }).then(response => response.json())
                    .then(data => alert("Server Response: " + data.message));
        }
    </script>
</html>
