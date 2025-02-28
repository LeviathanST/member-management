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

  .update-permission {
    margin-bottom: 15px;
  }
  .role-container{
    display: flex;
    justify-content: space-between;
  }
  .update-permission button {
    padding: 10px 15px;
    font-size: 16px;
    background-color: #4caf50;
    color: white;
    border: none;
    border-radius: 5px;
    cursor: pointer;
  }

  .update-permission button:hover {
    background-color: #388e3c;
  }
.add-role button {
            padding: 10px 15px;
            font-size: 16px;
            background-color: #28a745;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }
        .add-role button:hover {
            background-color: #218838;
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
        <ul class="navbar-links">
            <form action="<%=request.getContextPath()%>/auth/logout" method="post">
        <button type="submit" class="drop-btn">Logout</button>
    </form>
        </ul>
    </nav>
    <div class="container">
        <div class="sidebar">
            <ul>
                <li><a href='<%=request.getContextPath()%>/app/members'>Member</a></li>
                <li><a href="#"class="menu-item active">Role</a></li>
                <li><a href='<%=request.getContextPath()%>/app/events' >Event</a></li>
                <li><a href='<%=request.getContextPath()%>/app/guilds' >Guild</a></li>
                <li><a href='<%=request.getContextPath()%>/app/crews' >Crew</a></li>
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
            <div class="update-permission">
                <button onclick="applyChange()">Apply Changes</button>
                <button onclick="deleteRole()" style="background-color: #ff4444; margin-left: 10px;">Delete Role</button>
              </div>
            <div class="add-role">
                            <button onclick="showAddRoleModal()">Add Role</button>
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
<div id="addRoleModal" class="modal">
        <div class="modal-content">
            <h2>Create New Role</h2>
            <input type="text" id="roleName" placeholder="Role Name" required>
            <button class="btn-submit" onclick="addRole()">Submit</button>
            <button class="btn-close" onclick="closeAddRoleModal()">Close</button>
        </div>
    </div>
    <script>
let permissions = JSON.parse('<%=request.getAttribute("permissions")%>');
let name = '<%=request.getParameter("name")%>';
let permissionForAdding = new Set(); 
let permissionForDeleting = new Set();

async function fetchPermissionsOfRole() {
    const role = document.getElementById("role-select").value;
    const route = "<%=request.getContextPath()%>/app/permissions?role=" + role;
    const res = await fetch(route, {
        headers: {
            "Accept":"application/json", 
            "Content-Type":"application/json"
    }
        })
        .then(res => res.text())
        .then(text => JSON.parse(text));
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
        listItem.dataset.permission = permission;
        const removeButton = document.createElement("button");
        removeButton.textContent = "X";
        removeButton.onclick = () => removePermission(permission, true);
        listItem.appendChild(removeButton);
        currentList.appendChild(listItem);
    });
    availablePermissions.forEach(permission => {
        const listItem = document.createElement("li");
        listItem.textContent = permission;
        listItem.dataset.permission = permission;
        const addButton = document.createElement("button");
        addButton.textContent = "+";
        addButton.onclick = () => addPermission(permission, true);
        listItem.appendChild(addButton);
        availableList.appendChild(listItem);
    });
}
function fetchPermissions() {
    const currentList = document.getElementById("current-permissions-list")
    const currentItem= [...currentList.children].map(item => item);

    const availableList = document.getElementById("available-permissions-list")
    let availableItem = [...availableList.children].map(item => item);

    currentList.innerHTML = ""
    availableList.innerHTML = ""
    
    currentItem.forEach(item => {
        currentList.appendChild(item)
    })

    availableItem = availableItem.filter(item => !currentItem.includes(item))
    availableItem.forEach(item => {
        availableList.appendChild(item)
    })
}
function addPermission(permission, notsaved) {
    if (notsaved) {
        permissionForAdding.add(permission);
    } else {
        permissionForDeleting.delete(permission);
    }

    const currentList = document.getElementById("current-permissions-list");
    const availableList = document.getElementById("available-permissions-list");
    const itemToRemove = [...availableList.children].find(item => item.dataset.permission === permission);

    if (itemToRemove) {
        availableList.removeChild(itemToRemove);
    }

    const listItem = document.createElement("li");
    listItem.textContent = permission;
    listItem.dataset.permission = permission;

    if (notsaved) {
        const notSavingLabel = document.createElement("span");
        notSavingLabel.textContent = "Not Saved";
        notSavingLabel.style.color = "red";
        notSavingLabel.style.marginLeft = "10px";
        listItem.appendChild(notSavingLabel);
    }

    const removeButton = document.createElement("button");
    removeButton.textContent = "X";
    removeButton.onclick = () => removePermission(permission, !notsaved);
    listItem.appendChild(removeButton);

    currentList.appendChild(listItem);
}

function removePermission(permission, notsaved) {
    if (notsaved) {
        permissionForDeleting.add(permission);
    } else {
        permissionForAdding.delete(permission);
    }

    const currentList = document.getElementById("current-permissions-list");
    const availableList = document.getElementById("available-permissions-list");
    const itemToRemove = [...currentList.children].find(item => item.dataset.permission === permission);

    if (itemToRemove) {
        currentList.removeChild(itemToRemove);
    }

    const listItem = document.createElement("li");
    listItem.textContent = permission;
    listItem.dataset.permission = permission;

    if (notsaved) {
        const notSavingLabel = document.createElement("span");
        notSavingLabel.textContent = "Not Saved";
        notSavingLabel.style.color = "red";
        notSavingLabel.style.marginLeft = "10px";
        listItem.appendChild(notSavingLabel);
    }

    const addButton = document.createElement("button");
    addButton.textContent = "+";
    addButton.onclick = () => addPermission(permission, !notsaved);
    listItem.appendChild(addButton);

    availableList.appendChild(listItem);
}
async function applyChange() {
    const role = document.getElementById("role-select").value;
    const route = "<%=request.getContextPath()%>/app/permissions";

    const data1 = {
        roleName: role,
        permissions: Array.from(permissionForAdding)
    }
    const res1 = await fetch(route, {
        method: "POST",
        headers: {
            "Accept":"application/json", 
            "Content-Type":"application/json"
        },
        body: JSON.stringify(data1)
    }).then(res => res.text()).then(text => JSON.parse(text))

    const data2 = {
        roleName: role,
        permissions: Array.from(permissionForDeleting)
    }
    const res2 = await fetch(route, {
        method: "DELETE",
        headers: {
            "Accept":"application/json", 
            "Content-Type":"application/json"
        },
        body: JSON.stringify(data2)
    }).then(res => res.text()).then(text => JSON.parse(text))

    if (res1.status == "OK") {
        permissionForAdding.clear()
        alert("Added permission successfully!")
    } else {
        alert("Added permission failed!")
    }
    if (res2.status == "OK") {
        permissionForDeleting.clear()
        alert("Removed permission successfully!")
    } else {
        alert("Removed permission failed!")
    }
  }
function showAddRoleModal() {
            const modal = document.getElementById("addRoleModal");
            modal.style.display = "block";
        }

        function closeAddRoleModal() {
            const modal = document.getElementById("addRoleModal");
            modal.style.display = "none";
            document.getElementById("roleName").value = "";
        }

        function addRole() {
            const route = "<%=request.getContextPath()%>/app/roles";
            const roleName = document.getElementById("roleName").value;

            if (!roleName) {
                alert("Role name is required!");
                return;
            }

            const formData = {
                roleName: roleName
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
                    closeAddRoleModal(); 
                    location.reload(); 
                } else {
                    alert("Error: " + data.message);
                }
            })
            .catch(error => {
                alert("Failed to create role: " + error.message);
            });
        }
async function deleteRole() {
    const role = document.getElementById("role-select").value;
    if (!role || role === "") {
        alert("Please select a role to delete");
        return;
    }

    if (!confirm(`Are you sure you want to delete the role "${role}"? This action cannot be undone.`)) {
        return;
    }

    const route = "<%=request.getContextPath()%>/app/roles";
    const data = {
        roleName: role,
    }
    
    try {
        const res = await fetch(route, {
            method: "DELETE",
            headers: {
                "Accept": "application/json",
                "Content-Type": "application/json"
            },
            body: JSON.stringify(data)
        }).then(res => res.text()).then(text => JSON.parse(text));

        if (res.status === "OK") {
            const select = document.getElementById("role-select");
            const optionToRemove = select.querySelector(`option[value="${role}"]`);
            if (optionToRemove) {
                optionToRemove.remove();
            }
            
            document.getElementById("current-permissions-list").innerHTML = "";
            document.getElementById("available-permissions-list").innerHTML = "";
            
            permissionForAdding.clear();
            permissionForDeleting.clear();
            
            alert("Role deleted successfully");
            
            select.value = "";
        } else {
            throw new Error(res.message || "Failed to delete role");
        }
    } catch (error) {
        alert("Failed to delete role: " + error.message);
    }
}
    </script>
</body>
</html>
