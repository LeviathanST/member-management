<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="dto.guild.GuildInfoDTO" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Guild Information</title>
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
        .guild-info-container {
            width: 60%;
            padding: 20px;
            background-color: #F5F5F5;
            border-radius: 5px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            margin: auto;
            color: #1c1c1c;
        }
        h1 {
            text-align: left;
            color: red;
        }
        .guild-info {
            border-bottom: 1px solid #ddd;
            padding: 10px 0;
        }
        .guild-info p {
            margin: 10px 0;
        }
        .permission-note {
            color: #555;
            font-style: italic;
        }
        .action-buttons button {
            margin-left: 10px;
            padding: 5px 10px;
            border: none;
            border-radius: 4px;
            color: white;
            cursor: pointer;
        }
        .btn-edit {
            background-color: #007bff;
        }
        .btn-delete {
            background-color: #dc3545;
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
            <li class="dropdown">
                <button class="drop-btn">Quản lý tài khoản</button>
                <div class="dropdown-content">
                    <a href="userList.jsp">Danh sách tài khoản</a>
                </div>
            </li>
            <li class="dropdown">
                <button class="drop-btn">Quản lý sự kiện</button>
                <div class="dropdown-content">
                    <a href="roleList.jsp">Danh sách sự kiện</a>
                </div>
            </li>
            <li class="dropdown">
                <button class="drop-btn">Quản lý khóa học</button>
                <div class="dropdown-content">
                    <a href="courseList.jsp">Danh sách khóa học</a>
                </div>
            </li>
            <li class="dropdown">
                <a href="main.jsp" class="logout"><button class="drop-btn">Đăng xuất</button></a>
            </li>
        </ul>
    </nav>
    <div class="container">
        <!-- Sidebar -->
        <div class="sidebar">
            <ul>
                <li><a href='<%=request.getContextPath()%>/guild/members?name=<%=request.getParameter("name")%>'>Member</a></li>
                <li><a href='<%=request.getContextPath()%>/guild/roles?name=<%=request.getParameter("name")%>'>Role</a></li>
                <li><a href='<%=request.getContextPath()%>/guild/events?name=<%=request.getParameter("name")%>'>Event</a></li>
                <li><a href='#' class="active">Information</a></li>
            </ul>
        </div>
        <!-- Guild Info -->
        <div class="guild-info-container">
            <h1>Guild Information</h1>
            <div class="guild-info">
                <% 
                    GuildInfoDTO info = (GuildInfoDTO) request.getAttribute("info");
                    if (info != null) {
                        String guildName = info.getGuildName();
                %>
                <p><strong>Guild Name:</strong> <%= guildName %></p>
                <p><strong>Total Members:</strong> <%= info.getTotalMember() %></p>
                <% 
                    Boolean ade = (Boolean) request.getAttribute("ade");
                    if (ade != null && ade) {
                %>
                    <p class="permission-note">You have permission to edit this guild.</p>
                    <div class="action-buttons">
                        <button class="btn-edit" onclick="editGuild('<%= guildName %>')">Edit Guild Name</button>
                    </div>
                <% 
                    } else {
                %>
                    <p class="permission-note">You do not have permission to edit this guild.</p>
                <% 
                    }
                } else {
                %>
                    <p>No guild information available.</p>
                <% } %>
            </div>
        </div>
    </div>

    <script>
        const name = '<%= request.getParameter("name") %>';
        const route = "<%= request.getContextPath() %>/guild/info?name=" + name;

        function editGuild(guildName) {
            const newName = prompt("Enter the new guild name:", guildName);
            if (newName && newName !== guildName) {
                const data = {
                    newGuildName: newName
                };
                fetch(route, {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json',
                        'Accept': 'application/json'
                    },
                    body: JSON.stringify(data)
                })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        location.reload(); // Refresh page on success
                    } else {
                        alert(data.message || 'Failed to edit guild name.');
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert('An error occurred while editing the guild.');
                });
            } else if (!newName) {
                alert('Invalid new guild name');
            }
        }

        
    </script>
</body>
</html>
