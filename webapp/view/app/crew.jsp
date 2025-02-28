<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Crew Management</title>
    <style>
        body {
            margin: 0;
            padding: 0;
            background-color: #1c1c1c;
            color: #fff;
        }
        .navbar {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 1rem;
            background-color: #222;
        }
        .navbar-logo {
            color: #e32b2b;
            display: flex;
            justify-content: center;
            align-items: center;
            gap: 20px;
        }
        .navbar-links {
            list-style-type: none;
            margin: 0;
            padding: 0;
            display: flex;
        }
        .navbar-links li {
            margin-left: 1rem;
            position: relative;
        }
        .dropdown .drop-btn {
            background-color: #444;
            color: white;
            margin-right: 20px;
            padding: 1rem 1rem;
            border: none;
            cursor: pointer;
            border-radius: 0.5rem;
        }
        .dropdown-content {
            display: none;
            position: absolute;
            top: 100%;
            left: 0;
            min-width: 170px;
            background-color: #333;
            box-shadow: 0px 8px 16px rgba(0, 0, 0, 0.2);
            z-index: 1;
            border-radius: 0.5rem;
        }
        .dropdown-content a {
            padding: 0.5rem 1rem;
            display: block;
            color: #fff;
            text-decoration: none;
        }
        .dropdown-content a:hover {
            background-color: #e32b2b;
        }
        .dropdown:hover .dropdown-content {
            display: block;
        }
        .container {
            display: flex;
            width: 100%;
        }
        .sidebar {
            width: 250px;
            height: 100vh;
            background-color: #222222;
            top: 0;
            left: 0;
            padding-top: 20px;
            box-sizing: border-box;
        }
        .sidebar ul {
            list-style-type: none;
            padding: 0;
        }
        .sidebar ul li {
            margin: 15px 0;
        }
        .sidebar ul li a {
            color: #844A4D;
            text-decoration: none;
            font-weight: normal;
            display: block;
            padding: 10px;
            border-radius: 5px;
            font-size: 28px;
            transition: background-color 0.3s, font-weight 0.3s;
        }
        .sidebar ul li a:hover {
            background-color: #714545;
            color: #EA4850;
        }
        .sidebar ul li a.active {
            font-weight: bold;
            background-color: #714545;
            color: #EA4850;
        }
        .crew-list-container {
            width: 60%;
            padding: 20px;
            background-color: #F5F5F5;
            border-radius: 5px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            margin: auto;
        }
        h1 {
            text-align: left;
            color: red;
        }
        .crew-item {
            display: flex;
            justify-content: space-between;
            align-items: center;
            border-bottom: 1px solid #ddd;
        }
        .crew-info {
            display: flex;
            flex-direction: column;
            color: #1c1c1c;
        }
        .crew-actions button {
            margin-left: 10px;
            padding: 5px 10px;
            border: none;
            border-radius: 4px;
            color: white;
            cursor: pointer;
        }
        .add-crew {
            text-align: left;
            margin-bottom: 20px;
        }
        .add-crew input {
            margin-bottom: 10px;
            padding: 5px;
            border-radius: 4px;
            border: 1px solid #ddd;
            display: block;
        }
        .add-crew button {
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            background-color: #28a745;
            color: white;
            cursor: pointer;
        }
        .modal {
            display: none;
            position: fixed;
            z-index: 1000;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0,0,0,0.5);
        }
        .modal-content {
            background-color: #fff;
            margin: 15% auto;
            padding: 20px;
            border-radius: 5px;
            width: 80%;
            max-width: 400px;
            color: #1c1c1c;
        }
        .modal-content input {
            width: 100%;
            padding: 8px;
            margin: 10px 0;
            border: 1px solid #ddd;
            border-radius: 4px;
            box-sizing: border-box;
        }
        .modal-content button {
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }
        .modal-content .btn-submit {
            background-color: #28a745;
            color: white;
        }
        .modal-content .btn-close {
            background-color: #dc3545;
            color: white;
            margin-left: 10px;
        }
    </style>
</head>
<body>
    <nav class="navbar">
        <div class="navbar-logo">
            <img class="logo" height="70px" width="70px"
                src="https://fosigrz.stripocdn.email/content/guids/CABINET_4c786d105fcb845e57f209bb30d51b9deb985ec5526bfc650f087fefc6ca16f6/images/logo_new.png"
                alt="Logo" />
        </div>
            <form action="<%=request.getContextPath()%>/auth/logout" method="post">
        <button type="submit" class="drop-btn">Logout</button>
    </form>
        </ul>
    </nav>
    <div class="container">
        <div class="sidebar">
            <ul>
                <li><a href='<%=request.getContextPath()%>/app/members'>Member</a></li>
                <li><a href='<%=request.getContextPath()%>/app/roles'>Role</a></li>
                <li><a href='<%=request.getContextPath()%>/app/events'>Event</a></li>
                <li><a href='<%=request.getContextPath()%>/app/guilds'>Guild</a></li>
                <li><a href="#" class="active">Crew</a></li>
            </ul>
        </div>
        <div class="crew-list-container">
            <h1>Crew List</h1>
            <div class="add-crew">
                <% 
                    Boolean ade = (Boolean) request.getAttribute("ade");
                    if (ade != null && ade) {
                %>
                    <button onclick="showAddCrewModal()">Add Crew</button>
                <% } %>
                <% if (ade != null && ade) { %>
                    <div id="addCrewModal" class="modal">
                        <div class="modal-content">
                            <h2>Add New Crew</h2>
                            <input type="text" id="crewName" placeholder="Crew Name" required>
                            <input type="text" id="crewCode" placeholder="Crew Code" required>
                            <input type="text" id="username" placeholder="Username" required>
                            <button class="btn-submit" onclick="addCrew()">Submit</button>
                            <button class="btn-close" onclick="closeAddCrewModal()">Close</button>
                        </div>
                    </div>
                <% } %>
            </div>
            <div id="crew-list">
                <% 
                    List<String> crews = (List<String>) request.getAttribute("crews");
                    if (crews != null && !crews.isEmpty()) {
                        for (String crewName : crews) {
                %>
                    <div class="crew-item">
                        <div class="crew-info">
                            <p style="margin: 0; font-family: Arial, sans-serif; font-size: 16px; line-height: 1.5;">
                                <strong style="color: #444; font-weight: 600;">Name:</strong>
                                <a href="<%= request.getContextPath() %>/crew/info?name=<%= crewName %>" style="color: #007bff; text-decoration: none; padding: 2px 6px; border-radius: 4px; transition: background 0.3s;">
                                    <%= crewName %>
                                </a>
                            </p>
                        </div>
                        <div class="crew-actions">
                            <% if (ade != null && ade) { %>
                            <button style="background-color: #dc3545;" onClick='deleteCrew("<%=crewName%>")' class="btn-delete">Delete</button>
                            <% } %>
                        </div>
                    </div>
                <% 
                        }
                    } else {
                %>
                    <p>No crews found.</p>
                <% 
                    }
                %>
            </div>
        </div>
    </div>
    <script>
        const route = '<%=request.getContextPath()%>/app/crews';
        function showAddCrewModal() {
            const modal = document.getElementById("addCrewModal");
            modal.style.display = "block";
        }

        function closeAddCrewModal() {
            const modal = document.getElementById("addCrewModal");
            modal.style.display = "none";
        }

        function addCrew() {
            const crewName = document.getElementById("crewName").value;
            const crewCode = document.getElementById("crewCode").value;
            const username = document.getElementById("username").value;

            if (!crewName || !crewCode || !username) {
                alert("All fields are required!");
                return;
            }

            const formData = {
                crewName: crewName,
                crewCode: crewCode,
                username: username
            };

            fetch(route, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(formData)
            })
            .then(response => {
                if (!response.ok) {
                    throw new Error("Network response was not ok");
                }
                return response.json();
            })
            .then(data => {
                if (data.status === "OK") {
                    alert(data.message);
                    closeAddCrewModal();
                    location.reload();
                } else {
                    alert("Error: " + data.message);
                }
            })
            .catch(error => {
                console.error("Error:", error);
                alert("Failed to create crew: " + error.message);
            });
        }
        function deleteCrew(crewName) {
            const data = {
                name: crewName 
            }
            if (confirm('Are you sure you want to delete the crew"' + crewName + '"?')) {
                fetch(route, {
                    method: 'DELETE',
                    headers: {
                        'Content-Type': 'application/json',
                        'Accept': 'application/json'
                    },
                    body: JSON.stringify(data)
                })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        alert("Removed crew successfully!");
                        alert(data.message);
                    } else {
                        alert(data.message || 'Failed to delete crew.');
                    }
                })
                .catch(error => {
                    alert('An error occurred while deleting the crew.');
                });
            }
        }
    </script>
</body>
</html>
