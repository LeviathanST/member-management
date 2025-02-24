<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
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

        .btn-ban {
            background-color: #ff9800;
        }

        .btn-edit {
            background-color: #007bff;
        }

        .btn-delete {
            background-color: #dc3545;
        }

        .add-crew {
            text-align: left;
            margin-bottom: 20px;
        }

        .add-crew button {
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
                li><a href="#">Member</a></li>
                <li><a href="#">Role</a></li>
                <li><a href="#" >Event</a></li>
                <li><a href="#" >Guild</a></li>
                <li><a href="#"class="active" >Crew</a></li>
            </ul>
        </div>
        <div class="crew-list-container">
            <h1>Crew List</h1>
    
            <div class="add-crew">
                <button onclick="addCrew()">Add crew</button>
            </div>
            <div id="crew-list"></div>
        </div>
    
        <script>
            function renderCrewList() {
                const crewListContainer = document.getElementById("crew-list");
                crewListContainer.innerHTML = "";
    
                crews.forEach(crew => {
                    const crewItem = document.createElement("div");
                    crewItem.classList.add("crew-item");
    
                    const crewInfo = document.createElement("div");
                    crewInfo.classList.add("crew-info");
                    crewInfo.innerHTML = `
                        <p><strong>Name:</strong> ${crew.name}</p>
                        <p><strong>Student Code:</strong> ${crew.studentCode}</p>
                    `;
    
                    const crewActions = document.createElement("div");
                    crewActions.classList.add("crew-actions");
    
                    const banButton = document.createElement("button");
                    banButton.textContent = "Ban";
                    banButton.classList.add("btn-ban");
                    banButton.onclick = () => banCrew(crew.studentCode);

                    const editButton = document.createElement("button");
                    editButton.textContent = "Edit";
                    editButton.classList.add("btn-edit");
                    editButton.onclick = () => editCrew(crew.studentCode);

                    const deleteButton = document.createElement("button");
                    deleteButton.textContent = "Delete";
                    deleteButton.classList.add("btn-delete");
                    deleteButton.onclick = () => deleteCrew(crew.studentCode);
                    crewActions.appendChild(banButton);
                    crewActions.appendChild(editButton);
                    crewActions.appendChild(deleteButton);
                    crewItem.appendChild(crewInfo);
                    crewItem.appendChild(crewActions);
                    crewListContainer.appendChild(crewItem);
                });
            }
            function deleteCrew(studentCode) {
                if (confirm(`Are you sure you want to delete crew with student code ${studentCode}?`)) {
                    alert(`crew with student code ${studentCode} has been deleted.`);
                    const index = crews.findIndex(crew => crew.studentCode === studentCode);
                    if (index !== -1) {
                        crews.splice(index, 1);
                        renderCrewList();
                    }
                }
            }
            renderCrewList();
        </script>
</body>
</html>
