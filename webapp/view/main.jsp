<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Akatsuki</title>
        <style>
            body {
                margin: 0;
                padding: 0;
                background-color: black;
                color: white;
                text-align: center;
            }
            .container {
                display: flex;
                flex-direction: column;
                align-items: center;
                justify-content: center;
                height: 800px;
            }
            .button-container {
                display: flex;
                gap: 20px;
                margin-top: 20px;
            }
            button {
                width: 300px;
                margin-top: 50px;
                padding: 20px 20px;
                font-size: 1rem;
                font-weight: bold;
                background-color: white;
                color: black;
                border: none;
                border-radius: 5px;
                cursor: pointer;
            }
            button:hover {
                background-color: #ccc;
            }
            img{
                width: 400px;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <img
                src="https://fosigrz.stripocdn.email/content/guids/CABINET_4c786d105fcb845e57f209bb30d51b9deb985ec5526bfc650f087fefc6ca16f6/images/logo_new.png"
                width="width"
                height="height"
                alt="alt"
                />

            <div class="button-container">
                <button onclick="window.location.href = 'view/auth/signup.jsp'">Sign up</button>
                <button onclick="window.location.href = 'view/auth/login.jsp'">Log in</button>
            </div>
        </div>
    </body>
    <script>
    </script>
</html>
