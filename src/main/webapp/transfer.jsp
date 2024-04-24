<%@ page import="uz.muhammadtrying.cardsystem.repo.CardRepo" %>
<%@ page import="uz.muhammadtrying.cardsystem.entity.Card" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Comparator" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Adding Funds</title>
    <link rel="stylesheet" href="static/bootstrap.min.css">
    <style>
        .container {
            margin-top: 50px;
        }

        .form-container {
            max-width: 400px;
            margin: auto;
            padding: 20px;
            border: 1px solid #ccc;
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }

        .form-container label {
            font-weight: bold;
        }

        .form-container input[type="number"],
        .form-container select {
            width: 100%;
            padding: 8px;
            margin-top: 5px;
            margin-bottom: 15px;
            border: 1px solid #ccc;
            border-radius: 5px;
            transition: border-color 0.3s ease;
        }

        .form-container input[type="number"]:focus,
        .form-container select:focus {
            outline: none;
            border-color: #007bff;
        }

        .form-container input[type="submit"] {
            width: 100%;
            padding: 10px;
            border: none;
            border-radius: 5px;
            background-color: #007bff;
            color: #fff;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }

        .form-container input[type="submit"]:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>
<%
    CardRepo cardRepo = new CardRepo();
    List<Card> cards = cardRepo.findAll();
    cards.sort(Comparator.comparing(Card::getId));
%>
<div class="container">
    <div class="form-container">
        <h2 class="text-center mb-4">Add Funds</h2>
        <form action="${pageContext.request.contextPath}/card-servlet" method="post">
            <input type="hidden" name="function" value="addingFunds">
            <div class="form-group">
                <select id="card" name="cardId" required>
                    <option class="text-dark" disabled selected>Choose Card</option>
                    <%for (Card card : cards) {%>
                    <option value="<%=card.getId()%>">
                        <%=card.getName()%>
                    </option>
                    <% } %>
                </select>
            </div>
            <div class="form-group">
                <label for="amount">Amount:</label>
                <input autofocus type="number" id="amount" name="amount" placeholder="Enter amount" required>
            </div>
            <input type="submit" value="Submit">
        </form>
    </div>
</div>
</body>
</html>
