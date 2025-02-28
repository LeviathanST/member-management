<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="dto.crew.GetCrewEventDTO" %>
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
                <form action="<%=request.getContextPath()%>/auth/logout" method="post">
        <button type="submit" class="drop-btn">Logout</button>
    </form>
            </ul>
        </nav>
        <div class="container">
            <div class="sidebar">
                <ul>
                    <li><a href='<%=request.getContextPath()%>/crew/members?name=<%=request.getParameter("name")%>'>Member</a></li>
                    <li><a href='<%=request.getContextPath()%>/crew/roles?name=<%=request.getParameter("name")%>'>Role</a></li>
                    <li><a href="#"class="active" >Event</a></li>
                    <li><a href='<%=request.getContextPath()%>/crew/info?name=<%=request.getParameter("name")%>'>Information</a></li>
                </ul>
            </div>
            <div class="event-header">
                <% boolean ade = (Boolean) request.getAttribute("ade"); %>
                <% if (ade) { %>
                    <button id="add-event-btn">Nhấn vào đây để tạo</button>
                <% } %>
                <h2>Danh sách Sự Kiện</h2>
                <div id="event-list">
        <% 
            List<GetCrewEventDTO> crewEvents = (List<GetCrewEventDTO>) request.getAttribute("crewEvents");
            if (crewEvents != null && !crewEvents.isEmpty()) {
                for (GetCrewEventDTO event : crewEvents) {
        %>
        <div class="event-card">
            <div class="event-info">
                <h2><%= event.getTitle() %></h2>
                <h2><%= event.getCrewName() %></h2>
                <p><%= event.getDescription() %></p>
                <p><b><%= event.getStartedAt() %></b></p>
                <p><b><%= event.getEndedAt() %></b></p>
            </div>
    <div class="event-actions">
                <% if (ade) { %>
                <button class="edit-btn" onclick="editEvent(
                               '<%= event.getId() %>', 
                               '<%= event.getTitle() %>', 
                               '<%= event.getDescription() %>', 
                               '<%= event.getStartedAt() %>', 
                               '<%= event.getEndedAt() %>')">
                               Edit
                </button>
                <button class="delete-btn" onclick="deleteEvent('<%= event.getId() %>')">Delete</button>
                <% } %>
    </div>
        </div>
        <% 
                }
            } else { 
        %>
        <p>Nothing available.</p>
        <% } %>
                </div>
            </div>
            <div id="event-form-container" class="modal">
                <div class="modal-content">
                    <span class="close">&times;</span>
                    <h2 id="form-title">Add Event</h2>
                    <form id="event-form">
                        <input type="hidden" id="event-id">

                        <label for="title">Title:</label>
                        <input type="text" id="title" required>

                        <label for="content">Description:</label>
                        <textarea id="content" required></textarea>

                        <label for="startedAt">Started Datetime:</label>
                        <input type="datetime-local" id="startedAt" required>

                        <label for="endedAt">Ended Datetime:</label>
                        <input type="datetime-local" id="endedAt" required>

                        <button type="submit">Save</button>
                    </form>
                </div>
            </div>
            <script>
const name = '<%=request.getParameter("name")%>';
const eventFormContainer = document.getElementById("event-form-container");
const closeModal = document.querySelector(".close");
const eventForm = document.getElementById("event-form");

document.getElementById("add-event-btn").addEventListener("click", () => {
    resetForm();
    eventFormContainer.style.display = "flex";
});

closeModal.addEventListener("click", () => {
    eventFormContainer.style.display = "none";
});

function resetForm() {
    document.getElementById("event-id").value = "";
    document.getElementById("title").value = "";
    document.getElementById("content").value = "";
    document.getElementById("startedAt").value = "";
    document.getElementById("endedAt").value = "";
}

eventForm.addEventListener("submit", async function(event) {
    event.preventDefault();

    const eventId = document.getElementById("event-id").value.trim() || null;
    const title = document.getElementById("title").value;
    const content = document.getElementById("content").value;
    const startedAt = formatDateTime(document.getElementById("startedAt").value);
    const endedAt = formatDateTime(document.getElementById("endedAt").value);

    const eventData = { eventId, title, description: content, startedAt, endedAt };

    const res = await fetch(`<%=request.getContextPath()%>/crew/events?name=` + name, {
        method: eventId ? "PUT" : "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(eventData)
    });
    const json = await res.json();

    if (json.status == "OK") {
        eventFormContainer.style.display = "none";
    } else {
        alert(json.message)
    }
});

function formatDateTime(dateTimeString) {
    return dateTimeString ? dateTimeString.replace("T", " ") + ":00" : null;
}

function editEvent(id, title, description, startedAt, endedAt) {
    document.getElementById("event-id").value = id;
    document.getElementById("title").value = title;
    document.getElementById("content").value = description;
    document.getElementById("startedAt").value = startedAt.replace(" ", "T").slice(0, 16);
    document.getElementById("endedAt").value = endedAt.replace(" ", "T").slice(0, 16);
    
    document.getElementById("form-title").textContent = "Edit Event";
    eventFormContainer.style.display = "flex";
}
async function deleteEvent (id) {
    const data = {
        eventId: id
    }
    const res = await fetch(`<%=request.getContextPath()%>/crew/events?name=` + name, {
        method: "DELETE",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(data)
    });

    const json = await res.json();
    if (json.status != "OK") {
        location.reload()
    } else {
        alert(json.message);
    }
}
            </script>
    </body>
</html>
