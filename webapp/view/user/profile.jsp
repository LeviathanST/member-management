<%@page import="models.UserProfile"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.io.*,java.util.*,java.sql.*"%>
<%@ page import="jakarta.servlet.http.*,jakarta.servlet.*" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
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
            .content {
                padding: 2rem;
                display: flex;
                gap: 2rem;
            }
            .summary-container {
                background-color: #f5f5f5;
                color: #333333;
                padding: 0px 2rem 2rem 3rem;
                border-radius: 0.5rem;
                height: 350px;
                width: 500px;
            }
            .summary-container h3 {
                margin-bottom: 1rem;
                color: #e32b2b;
            }
            .summary-container p {
                margin-bottom: 0.5rem;
            }
            .summary-container button {
                margin-top: 1rem;
                padding: 0.5rem 1rem;
                background-color: #e32b2b;
                color: #fff;
                border: none;
                border-radius: 0.5rem;
                cursor: pointer;
            }
            .summary-container button:hover {
                background-color: #c22b2b;
            }
            h3{
                font-size: 30px;
                color: #e32b2b;
            }
            .form-group {
                color: #000000;
                padding: 5px;
            }
            .form-container {
                width: 100%;
                padding: 0px 50px 50px 50px;
                background-color: #fff;
                border: 1px solid #ccc;
                border-radius: 8px;
            }
            label {
                display: block;
                font-weight: bold;
                margin-bottom: 8px;
            }
            input,
            select,
            textarea {
                width: 100%;
                padding: 8px;
                font-size: 14px;
                border: 1px solid #ccc;
                border-radius: 4px;
                box-sizing: border-box;
            }
            .form-row {
                display: flex;
                gap: 20px;
            }
            .form-row .form-group {
                flex: 1;
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
                        <a href="${pageContext.request.contextPath}/userList.jsp">Danh sách tài khoản</a>
                    </div>
                </li>
                <li class="dropdown">
                    <button class="drop-btn">Quản lý sự kiện</button>
                    <div class="dropdown-content">
                        <a href="${pageContext.request.contextPath}/roleList.jsp">Danh sách sự kiện</a>
                    </div>
                </li>
                <li class="dropdown">
                    <button class="drop-btn">Quản lý khóa học</button>
                    <div class="dropdown-content">
                        <a href="${pageContext.request.contextPath}/courseList.jsp">Danh sách khóa học</a>
                    </div>
                </li>
                <li class="dropdown"> <a href="${pageContext.request.contextPath}/main.jsp" class="logout"><button class="drop-btn">Đăng xuất</button></a></li>
            </ul>
        </nav>
        <div class="content">
<div class="summary-container">
    <h3>Profile</h3>
    <%
    UserProfile userProfile = (UserProfile) request.getAttribute("profile");
    if (userProfile != null) {
%>
<p><strong>Full Name:</strong> <%= userProfile.getFullName() == null ? "Empty" : userProfile.getFullName()%></p>

<p><strong>Date of Birth:</strong> <%= userProfile.getDateOfBirth()%></p>
<p><strong>Sex:</strong> <%= userProfile.getSex() %></p>
<p><strong>Student Code:</strong> <%= userProfile.getStudentCode() == null ? "Empty" : userProfile.getStudentCode()%></p>
<p><strong>Personal Email:</strong> <%= userProfile.getEmail() == null ? "Empty" : userProfile.getEmail()%></p>
<p><strong>Contact Email:</strong> <%= userProfile.getContactEmail() == null ? "Empty" : userProfile.getContactEmail()%></p>
<p><strong>Generation:</strong> F-<%= userProfile.getGenerationId() %></p>
<%
    } else {
%>
    <p>Profile data not found.</p>
<%
    }
%>
    <button style="width: 200px; height: 40px;" type="button" onclick="saveChanges()">Save</button>
    <button type="button" onclick="resetForm()" style="background-color: #444; width: 100px; height: 40px;">Cancel</button>
</div>
            
<form class="form-container">
    <div class="form-group">
        <h3>Edit Profile</h3>
        <label>Full Name</label>
        <input type="text" id="full-name" placeholder="Nguyen Van A" value="<%= userProfile.getFullName() != null ? userProfile.getFullName() : "" %>" />
    </div>
    <div class="form-row">
        <div class="form-group">
            <label for="dob">Dob</label>
            <%
    String formattedDob = "";
    if (userProfile != null && userProfile.getDateOfBirth() != null) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        formattedDob = sdf.format(userProfile.getDateOfBirth());
    }
%>

<input type="date" id="dob" value="<%= formattedDob %>" />        </div>
        <div class="form-group">
            <label>Sex</label>
            <select id="gender">
                <option value="male" <%= userProfile != null && "male".equalsIgnoreCase(userProfile.getSex().name()) ? "selected" : "" %>>Male</option>
                <option value="female" <%= userProfile != null && "female".equalsIgnoreCase(userProfile.getSex().name()) ? "selected" : "" %>>Female</option>
                <option value="none" <%= userProfile != null && "none".equalsIgnoreCase(userProfile.getSex().name()) ? "selected" : "" %>>None</option>
            </select>
        </div>
    </div>
    <div class="form-row">
        <div class="form-group">
            <label>Personal Email</label>
            <input type="email" id="personal-email" placeholder="nguyenvana123@gmail.com" value="<%= userProfile.getEmail() != null ? userProfile.getEmail() : "" %>" />
        </div>
        <div class="form-group">
            <label>Contact Email</label>
            <input type="email" id="contact-email" placeholder="nguyenvana123@gmail.com" value="<%= userProfile.getContactEmail() != null ? userProfile.getContactEmail() : "" %>" />
        </div>
        <div class="form-group">
                    <label>Student Code</label>
                    <input type="text" id="student-code" placeholder="SE123456" value="<%= userProfile.getStudentCode() != null ? userProfile.getStudentCode() : "" %>" maxlength="8" />
                </div>
    </div>
</form>
</div>
    </div>
    <script>
        function saveChanges() {
            const data = {
                fullName: document.getElementById("full-name").value,
                dateOfBirth: document.getElementById("dob").value,
                sex: document.getElementById("gender").value,
                email: document.getElementById("personal-email").value,
                contactEmail: document.getElementById("contact-email").value,
                studentCode: document.getElementById("student-code").value
            };

            fetch("<%=request.getContextPath()%>/app/profile", {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(data)
            })
                    .then((response) => response.text())
                    .then((text) => {
                        let json = JSON.parse(text);
                        if (json.status != "OK") {
                            alert(json.message);
                            location.reload
                        } else {
                            alert(json.message);
                        }
                    });
            }
    </script>
</body>
</html>
