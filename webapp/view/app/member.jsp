<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="dto.role.GetUserDTO" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <style>
            body {
                margin: 0;
                padding: 0;
                background-color: #1c1c1c;
                color: #fff;
            }
    .modal {
        display: none;
        position: fixed;
        z-index: 1;
        left: 0;
        top: 0;
        width: 100%;
        height: 100%;
        background-color: rgba(0, 0, 0, 0.4);
    }

    .modal-content {
        background-color: #fff;
        margin: 10% auto;
        padding: 20px;
        width: 30%;
        border-radius: 10px;
        text-align: center;
        color: #000;
    }

    .close {
        float: right;
        font-size: 28px;
        cursor: pointer;
    }

    .modal-content button {
        margin-top: 10px;
        padding: 10px 15px;
        background-color: #007bff;
        color: white;
        border: none;
        border-radius: 5px;
        cursor: pointer;
    }

    .modal-content button:hover {
        background-color: #0056b3;
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
            .member-list-container {
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

            .member-item {
                display: flex;
                justify-content: space-between;
                align-items: center;

                border-bottom: 1px solid #ddd;
            }

            .member-info {
                display: flex;
                flex-direction: column;
                color: #1c1c1c;
            }

            .member-actions button {
                margin-left: 10px;
                padding: 5px 10px;
                border: none;
                border-radius: 4px;
                color: white;
                cursor: pointer;
            }

            .btn-ban {
                background-color: #ff9800;
            }

            .btn-edit {
                background-color: #007bff;
            }

            .btn-delete {
                background-color: #dc3545;
            }

            .add-member {
                text-align: left;
                margin-bottom: 20px;
            }

            .add-member button {
                padding: 10px 20px;
                border: none;
                border-radius: 5px;
                background-color: #28a745;
                color: white;
                cursor: pointer;
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
            <ul class="navbar-links">
                <form action="<%=request.getContextPath()%>/auth/logout" method="post">
        <button type="submit" class="drop-btn">Logout</button>
    </form>
            </ul>
        </nav>
        <div class="container">
            <!-- Sidebar -->
            <div class="sidebar">
                <ul>
                <li><a href='#' class="active">Member</a></li>
                <li><a href='<%=request.getContextPath()%>/app/roles'>Role</a></li>
                <li><a href='<%=request.getContextPath()%>/app/events' >Event</a></li>
                <li><a href='<%=request.getContextPath()%>/app/guilds' >Guild</a></li>
                <li><a href='<%=request.getContextPath()%>/app/crews' >Crew</a></li>
                </ul>
            </div>
            <div class="member-list-container">
                <h1>Member List</h1>

                <div class="add-member">
                    <button onclick="addMember()">Add Member</button>
                </div>
<div id="editModal" class="modal">
    <div class="modal-content">
        <span class="close" onclick="closeEditModal()">&times;</span>
        <h2>Edit Member Role</h2>
        <p><strong>Full Name:</strong> <span id="modalFullName"></span></p>
        <p><strong>Current Role:</strong> <span id="modalCurrentRole"></span></p>
        <label for="roleSelect">Select New Role:</label>
        <select id="roleSelect"></select>
        <button onclick='confirmEdit()'>Apply Changes</button>
    </div>
</div>
        <div id="member-list">
            <% 
                List<GetUserDTO> members = (List<GetUserDTO>) request.getAttribute("members");
                if (!members.isEmpty()) {
                    for (GetUserDTO member : members) {
                        String username = member.getUsername() != null ? member.getUsername() : "Empty";
                        String fullName = member.getFullName();
                        String role = member.getRole();

            %>
                <div class="member-item">
                    <div class="member-info">
                        <p><strong>FullName:</strong> <%= fullName %></p>
                        <p><strong>Username:</strong> <%= username %></p>
                        <p><strong>Role:</strong> <%= role %></p>
                    </div>
                    <div class="member-actions">
                        <% boolean ade = (Boolean) request.getAttribute("ade"); %>
                <% if (ade) { %>
                        <button class="btn-edit" onclick="editMember('<%= username %>', '<%= fullName %>', '<%= role %>')">Edit</button>
                        <button class="btn-delete" onclick="deleteMember('<%= fullName %>')">Delete</button>
                <% } %>
                    </div>
                </div>
            <% 
                    }
                } else { 
            %>
                <p>No members found.</p>
            <% } %>
        </div>
            </div>

            <script>
            const roles = JSON.parse('<%=request.getAttribute("roles")%>')
            let selectedUsername = "";
            async function addMember() {
                const route = "<%=request.getContextPath()%>/app/members";
                const username = prompt("Enter the username:");
                if (username) {
                    const data = {
                        username: username
                    }
                    const res = await fetch(route, {
                        method: "POST",
                        headers: {
                        "Accept":"application/json", 
                        "Content-Type":"application/json"
                        },
                        body: JSON.stringify(data)
                    }).then(res => res.text()).then(text => JSON.parse(text))
                    alert(res.message);
                } else {
                    alert("Username cannot be empty.");
                }
            }

            function editMember(username, fullName, currentRole) {
                    selectedUsername = username;

                    document.getElementById("modalFullName").textContent = fullName;
                    document.getElementById("modalCurrentRole").textContent = currentRole;

                    const roleSelect = document.getElementById("roleSelect");
                    roleSelect.innerHTML = "";

                    roles.forEach(role => {
                        let option = document.createElement("option");
                        option.value = role;
                        option.textContent = role;
                        if (role === currentRole) option.selected = true;
                        roleSelect.appendChild(option);
                    });

                    document.getElementById("editModal").style.display = "block";
            }
            function closeEditModal() {
                document.getElementById("editModal").style.display = "none";
            }
            async function confirmEdit() {
                const newRole = document.getElementById("roleSelect").value;

                const route = "<%=request.getContextPath()%>/app/members";
                const data = {
                    username: selectedUsername,
                    roleName: newRole
                };

                try {
                    const res = await fetch(route, {
                        method: "PUT",
                        headers: {
                            "Accept": "application/json",
                            "Content-Type": "application/json"
                        },
                        body: JSON.stringify(data)
                    }).then(res => res.json());
                    alert(res.message);
                    closeEditModal();
                } catch (error) {
                    alert("Failed to update role. Please try again.");
                }
            }

            async function deleteMember(username) {
                const route = "<%=request.getContextPath()%>/app/members";
                    if (confirm(`Are you sure you want to delete member ${username}?`)) {
                const data = {
                    username: username
                }
                const res = await fetch(route, {
                    method: "DELETE",
                    headers: {
                    "Accept":"application/json", 
                    "Content-Type":"application/json"
                    },
                    body: JSON.stringify(data)
                }).then(res => res.text()).then(text => JSON.parse(text))
                    alert(res.message);
                   }
                }
            </script>
    </body>
</html>

