<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error Notification</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f8d7da;
            text-align: center;
            padding: 50px;
        }
        .error-container {
            background-color: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            display: inline-block;
            text-align: left;
            white-space: pre-wrap;
            word-wrap: break-word;
        }
        .error-message {
            color: #721c24;
            font-size: 16px;
            font-family: monospace;
        }
    </style>
    <script>
        document.addEventListener("DOMContentLoaded", function() {
            document.getElementById('error-message').textContent = '${requestScope.response}';
        });
    </script>
</head>
<body>
    <div class="error-container">
        <h2>Error Occurred</h2>
        <pre id="error-message" class="error-message"></pre>
    </div>
</body>
</html>
