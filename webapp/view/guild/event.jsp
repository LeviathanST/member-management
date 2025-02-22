
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
            .event-header {
                font-size: 24px;
                color: #ffffff;
                margin: auto;
                margin-top: 50px;
            }
            #add-event-btn {
                background-color: #04ffb8;
                color: white;
                border: none;
                padding: 12px 20px;
                font-size: 16px;
                cursor: pointer;
                border-radius: 5px;
                transition: 0.3s;
                width: 300px;
                height: 50px;
            }

            #add-event-btn:hover {
                background-color: #02c48d;
            }

            #event-list {
                display: flex;
                flex-direction: column;
                align-items: center;
                gap: 20px;
                margin-top: 20px;
            }

            .event-card {
                background: white;
                width: 350px;
                border-radius: 10px;
                padding: 20px;
                box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);
                transition: 0.3s;
                text-align: left;
            }

            .event-card:hover {
                transform: translateY(-5px);
                box-shadow: 0px 6px 15px rgba(0, 0, 0, 0.2);
            }

            .event-info h2 {
                color: #333;
                font-size: 20px;
                margin-bottom: 5px;
            }

            .event-info p {
                color: #666;
                font-size: 14px;
                margin: 5px 0;
            }

            .event-info b {
                color: #007bff;
            }

            .event-actions {
                margin-top: 10px;
                text-align: right;
            }

            .event-actions button {
                border: none;
                padding: 8px 12px;
                cursor: pointer;
                font-size: 14px;
                border-radius: 5px;
                transition: 0.3s;
            }

            .event-actions .edit-btn {
                background-color: #ffc107;
                color: black;
            }

            .event-actions .edit-btn:hover {
                background-color: #e0a800;
            }

            .event-actions .delete-btn {
                background-color: #dc3545;
                color: white;
            }

            .event-actions .delete-btn:hover {
                background-color: #c82333;
            }

            .modal {
                display: none;
                position: fixed;
                top: 0;
                left: 0;
                width: 100%;
                height: 100%;
                background-color: rgba(0, 0, 0, 0.5);
                justify-content: center;
                align-items: center;
            }

            .modal-content {
                background: white;
                padding: 25px;
                border-radius: 10px;
                width: 40%;
                text-align: center;
                box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.2);
                color: #000;
            }

            .close {
                float: right;
                font-size: 24px;
                cursor: pointer;
                color: red;
            }

            .close:hover {
                color: darkred;
            }

            #event-form label {
                display: block;
                text-align: left;
                margin-top: 10px;
                font-weight: bold;
            }

            #event-form input,
            #event-form textarea {
                width: 100%;
                padding: 10px;
                margin-top: 5px;
                border: 1px solid #ccc;
                border-radius: 5px;
                font-size: 14px;
            }

            #event-form textarea {
                resize: vertical;
                height: 80px;
            }

            #event-form button {
                margin-top: 15px;
                background-color: #28a745;
                color: white;
                border: none;
                padding: 10px 15px;
                font-size: 16px;
                cursor: pointer;
                border-radius: 5px;
                transition: 0.3s;
            }

            #event-form button:hover {
                background-color: #218838;
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
                    <li><a href="#">Role</a></li>
                    <li><a href="#"class="active" >Event</a></li>
                    <li><a href="#" >Guild</a></li>
                    <li><a href="#" >Crew</a></li>
                </ul>
            </div>
            <div class="event-header">
                <button id="add-event-btn">Nhấn vào đây để tạo</button>
                <h2>Danh sách Sự Kiện</h2>
                <div id="event-list">
                </div>
            </div>
            <div id="event-form-container" class="modal">
                <div class="modal-content">
                    <span class="close">&times;</span>
                    <h2 id="form-title">Thêm Sự kiện</h2>
                    <form id="event-form">
                        <input type="hidden" id="event-id">

                        <label for="title">Tiêu đề:</label>
                        <input type="text" id="title" required>

                        <label for="content">Nội dung:</label>
                        <textarea id="content" required></textarea>

                        <label for="author">Tác giả:</label>
                        <input type="text" id="author" required>

                        <button type="submit">Lưu</button>
                    </form>
                </div>
            </div>
            <script>
                document.addEventListener("DOMContentLoaded", function () {
                    const eventList = document.getElementById("event-list");
                    const addEventBtn = document.getElementById("add-event-btn");
                    const eventFormContainer = document.getElementById("event-form-container");
                    const closeModal = document.querySelector(".close");
                    const eventForm = document.getElementById("event-form");

                    function fetchEvents() {
                        fetch("<%=request.getContextPath()%>/guild/event")
                                .then(response => response.json())
                                .then(events => {
                                    eventList.innerHTML = "";
                                    events.forEach(event => {
                                        const eventCard = document.createElement("div");
                                        eventCard.classList.add("event-card");

                                        eventCard.innerHTML = `
                        <div class="event-info">
                            <h2>${event.title}</h2>
                            <p>${event.content}</p>
                            <p><b>${event.author}</b> - ${event.date}</p>
                        </div>
                    `;
                                        eventList.appendChild(eventCard);
                                    });
                                });
                    }

                    eventForm.addEventListener("submit", (e) => {
                        e.preventDefault();
                        const formData = new FormData(eventForm);
                        formData.append("date", new Date().toLocaleString());

                        fetch("<%=request.getContextPath()%>/guild/event", {
                            method: "POST",
                            body: formData
                        }).then(() => {
                            eventFormContainer.style.display = "none";
                            fetchEvents();
                        });
                    });

                    addEventBtn.addEventListener("click", () => eventFormContainer.style.display = "flex");
                    closeModal.addEventListener("click", () => eventFormContainer.style.display = "none");

                    fetchEvents();
                });
            </script>
    </body>
</html>
