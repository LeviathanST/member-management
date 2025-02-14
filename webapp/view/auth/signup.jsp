<!DOCTYPE html>
<html>
	<h1>Sign up Page</h1>
	<form id="myForm">
    <label>Username: </label>
    <input type="text" id="username" required>

    <label>Password: </label>
    <input type="password" id="password" required>

    <button type="button" onclick="sendData()">Submit</button>
</form>

<script>
    function sendData() {
        let user = {
            username: document.getElementById("username").value,
            password: document.getElementById("password").value,
        };

	fetch("<%=request.getContextPath()%>/auth/signup", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(user)
        }).then(response => response.json())
          .then(data => alert("Server Response: " + data.message));
    }
</script>
</html>
