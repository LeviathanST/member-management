<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <style>body {
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
.content {
    width: 80%;
    padding: 20px;
    background-color: #000000;
}

h1{
    color: red;
}
h3{
    color: red;
    font-size: 25px;
}
.permissions-container {
    width: 80%;
    padding: 20px;
    background-color: #F5F5F5;
    border-radius: 5px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
    margin: auto;
}
.permissions-container label {
    color: #ff0000;
    font-weight: bold;
    font-size: 30px;
    margin-bottom: 50px;
}
.permissions-container select {
    width: 100px;
    height: 25px;
    border: none;
    margin-bottom: 20px;
    border-radius: 5px;
    font-size: 15px;
}
.permissions-wrapper {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    border: 1px solid white;
    padding: 10px;
    border-radius: 5px;
    background-color: #ffffff;
    position: relative; 
}

.current-permissions,
.available-permissions {
    width: 48%;
}

.vertical-separator {
    width: 2px; 
    background-color: #ccc;
    height: 100%; 
    position: absolute; 
    left: 50%; 
    top: 0; 
    transform: translateX(-50%); 
}

ul {
    list-style: none;
    padding: 0;
    color: #000000;
}

button {
    background-color: white;
    border: none;
    cursor: pointer;
    color: red;
    font-size: 25px;
}
#current-permissions-list li, #available-permissions-list li {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 10px;
    background-color: #D9D9D9;
    padding: 10px;
    font-size: 25px;
    border-radius: 5px;
}

.permissions-list {
    width: 48%;
    padding: 10px;
    box-sizing: border-box;
    background-color: #ffffff;
    border-radius: 5px;
    box-shadow: 0 0 5px rgba(0, 0, 0, 0.1);
  }

  .permission-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 10px;
    border-bottom: 1px solid #ddd;
    border-radius: 5px;
    background-color: #f5f5f5;
    margin-bottom: 5px;
  }

  .permission-item:last-child {
    border-bottom: none;
  }

  .permission-button {
    background-color: #ff5722;
    color: white;
    border: none;
    border-radius: 5px;
    padding: 5px 10px;
    cursor: pointer;
  }

  .permission-button:hover {
    background-color: #e64a19;
  }

  .add-permission {
    margin-bottom: 15px;
  }
  .role-container{
    display: flex;
    justify-content: space-between;
  }
  .add-permission button {
    padding: 10px 15px;
    font-size: 16px;
    background-color: #4caf50;
    color: white;
    border: none;
    border-radius: 5px;
    cursor: pointer;
  }

  .add-permission button:hover {
    background-color: #388e3c;
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
            <li class="dropdown"> <a href="main.jsp" class="logout"><button class="drop-btn">Đăng xuất</button></a></li>
        </ul>
    </nav>
    <div class="container">
        <div class="sidebar">
            <ul>
                <li><a href="#">Member</a></li>
                <li><a href="#"class="menu-item active">Role</a></li>
                <li><a href="#" >Event</a></li>
                <li><a href="#" >Information</a></li>
            </ul>
        </div>
        <div class="content">
            <h1>Role Management</h1>
            <div class="permissions-container">
                <div class="role-container">
                    <div>
                <label for="role-select">Role:</label>
            <select id="role-select" onchange="fetchPermissionsOfRole()">
                <%  
                    List<String> roles = (List<String>) request.getAttribute("roles"); 
                    if (roles != null) { 
                        for (String role : roles) { 
                %>
                        <option value="<%= role %>"><%= role %></option>
                <%  
                        } 
                    } 
                %>
                <option value="" selected disabled >None</option>
            </select>
        </div>
            <div class="add-permission">
                <button onclick="addNewPermission()">Add New Permission</button>
              </div>
            </div>
                <div class="permissions-wrapper">
                    <div class="current-permissions">
                        <h3>Current Permissions</h3>
                        <ul id="current-permissions-list"></ul>
                    </div>
                    <div class="vertical-separator"></div>
                    <div class="available-permissions">
                        <h3>Available Permissions</h3>
                        <ul id="available-permissions-list"></ul>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <script>
let permissions = JSON.parse('<%=request.getAttribute("permissions")%>');
                            console.log(permissions)
let name = '<%=request.getParameter("name")%>';

async function fetchPermissionsOfRole() {
    const role = document.getElementById("role-select").value;
    const route = "<%=request.getContextPath()%>/guild/permission?name=" + name +"&role=" + role;
        const res = await fetch(route, {
        headers: {
            "Accept":"application/json", 
            "Content-Type":"application/json"
    }
        })
        .then(res => res.text())
        .then(text => {
            return JSON.parse(text);
        });
    if (res.status != "OK") {
        throw new Error("Failed to fetch permission of role")
    }
    const currentPermissions = res.data;
    const availablePermissions = permissions.filter(permission => !currentPermissions.includes(permission));
    const currentList = document.getElementById("current-permissions-list");
    const availableList = document.getElementById("available-permissions-list");
    currentList.innerHTML = "";
    availableList.innerHTML = "";

    currentPermissions.forEach(permission => {
        const listItem = document.createElement("li");
        listItem.textContent = permission;
        const removeButton = document.createElement("button");
        removeButton.textContent = "X";
        removeButton.onclick = () => removePermission(role, permission);
        listItem.appendChild(removeButton);
        currentList.appendChild(listItem);
    });
    availablePermissions.forEach(permission => {
        const listItem = document.createElement("li");
        listItem.textContent = permission;
        const addButton = document.createElement("button");
        addButton.textContent = "+";
        addButton.onclick = () => addPermission(role, permission);
        listItem.appendChild(addButton);
        availableList.appendChild(listItem);
    });
}

function addPermission(role, permission) {
    rolePermissions[role].current.push(permission);
    rolePermissions[role].available = rolePermissions[role].available.filter(
        perm => perm !== permission
    );
    fetchPermissions();
}

function removePermission(role, permission) {
    rolePermissions[role].available.push(permission);
    rolePermissions[role].current = rolePermissions[role].current.filter(
        perm => perm !== permission
    );
    fetchPermissions();
}
function addNewPermission() {
    const newPermission = prompt("Enter the new permission name:");
    if (newPermission) {
      const role = document.getElementById("role-select").value;
      rolePermissions[role].available.push(newPermission);
      fetchPermissions();
    }
  }
    </script>
</body>
</html>
