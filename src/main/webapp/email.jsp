<%-- Email Form Page --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Email Form</title>
    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="static/bootstrap.min.css">
    <!-- Custom CSS for styling -->
    <style>
        body {
            background-color: #f8f9fa;
        }

        .container {
            margin-top: 100px;
        }

        .card {
            border: none;
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            transition: all 0.3s ease;
        }

        .card:hover {
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }

        .card-header {
            background-color: #007bff;
            color: #fff;
            border-radius: 10px 10px 0 0;
        }

        .form-control {
            border-radius: 0;
            border-top: none;
            border-left: none;
            border-right: none;
            transition: border-color 0.3s ease;
        }

        .form-control:focus {
            border-color: #007bff;
            box-shadow: none;
        }

        .btn-primary {
            border-radius: 0;
            background-color: #007bff;
            border: none;
            transition: background-color 0.3s ease;
        }

        .btn-primary:hover {
            background-color: #0056b3;
        }

        .error-message {
            color: red;
        }
    </style>
</head>
<body>
<%
    Object object = request.getAttribute("wrongCode");
%>
<div class="container">
    <div class="row justify-content-center">
        <div class="col-md-4">
            <div class="card">
                <a class="btn-dark btn-lg text-center" href="${pageContext.request.contextPath}/index.jsp">BACK</a>
                <div class="card-header text-center">
                    <h4>Enter Code</h4>
                </div>
                <div class="card-body">
                    <form action="${pageContext.request.contextPath}/auth-servlet" method="get">
                        <% if (object != null) { %>
                        <div class="form-group">
                            <input value="<%= object %>" autofocus type="text" class="form-control" name="code" required>
                            <span class="error-message">Incorrect code. Please try again.</span>
                        </div>
                        <% } else { %>
                        <div class="form-group">
                            <input autofocus type="text" class="form-control" name="code" required>
                        </div>
                        <% } %>
                        <button type="submit" class="btn btn-primary btn-block">Send</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Bootstrap JS -->
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
