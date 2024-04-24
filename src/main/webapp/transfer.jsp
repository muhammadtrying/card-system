<%@ page import="uz.muhammadtrying.cardsystem.repo.CardRepo" %>
<%@ page import="uz.muhammadtrying.cardsystem.entity.Card" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Comparator" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Transfer Funds</title>
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

        .error-message {
            color: red;
            font-size: 12px;
        }
    </style>
</head>
<body>
<%
    CardRepo cardRepo = new CardRepo();
    List<Card> cards = cardRepo.findAll();
    cards.sort(Comparator.comparing(Card::getId));
    Map<String, String> errors = new HashMap<>();
    Object object = request.getAttribute("errors");
    if (object != null) {
        errors = (Map<String, String>) object;
    }
%>
<div class="container">
    <div class="form-container">
        <h2 class="text-center mb-4">Transfer Funds</h2>
        <form action="${pageContext.request.contextPath}/card-servlet" method="post">
            <input type="hidden" name="function" value="transferFunds">
            <div class="form-group">
                <label for="fromCard">From Card:</label>
                <select id="fromCard" name="fromCardId" required>
                    <% for (Card card : cards) { %>
                    <option value="<%= card.getId() %>">
                        <%= card.getName() %>
                    </option>
                    <% } %>
                </select>
                <span class="error-message"><%= errors.get("fromCardId") != null ? errors.get("fromCardId") : "" %></span>
            </div>
            <div class="form-group">
                <label for="toCard">To Card:</label>
                <select id="toCard" name="toCardId" required>
                    <% for (Card card : cards) {
                        if (!card.getId().equals(cards.get(0).getId())) { %>
                    <option value="<%= card.getId() %>">
                        <%= card.getName() %>
                    </option>
                    <% }
                    } %>
                </select>
                <span class="error-message"><%= errors.get("toCardId") != null ? errors.get("toCardId") : "" %></span>
            </div>
            <div class="form-group">
                <label for="amount">Amount:</label>
                <input type="number" id="amount" name="amount" placeholder="Enter amount" required>
                <span class="error-message"><%= errors.get("amount") != null ? errors.get("amount") : "" %></span>
            </div>
            <div class="form-group">
                <span class="error-message"><%= errors.get("insufficientFunds") != null ? errors.get("insufficientFunds") : "" %></span>
            </div>
            <input type="submit" value="Transfer Funds">
        </form>
    </div>
</div>


<script>
    document.getElementById("fromCard").addEventListener("change", function () {
        var fromCardId = this.value;
        var toCardSelect = document.getElementById("toCard");

        toCardSelect.innerHTML = "";

        <% for (Card card : cards) {
            if (!card.getId().equals(cards.get(0).getId())) { %>
        var option = document.createElement("option");
        option.value = "<%= card.getId() %>";
        option.text = "<%= card.getName() %>";
        toCardSelect.appendChild(option);
        <% }
    } %>
    });
</script>

</body>
</html>
