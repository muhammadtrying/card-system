<%@ page import="uz.muhammadtrying.cardsystem.entity.Card" %>
<%@ page import="java.util.List" %>
<%@ page import="uz.muhammadtrying.cardsystem.repo.CardRepo" %>
<%@ page import="uz.muhammadtrying.cardsystem.entity.Transaction" %>
<%@ page import="uz.muhammadtrying.cardsystem.repo.TransactionRepo" %>
<%@ page import="java.util.Comparator" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="uz.muhammadtrying.cardsystem.entity.enums.Type" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>MUHAMMAD'S CARD APP</title>
    <link rel="stylesheet" href="static/bootstrap.min.css">
    <style>
        .card-container {
            display: flex;
            justify-content: space-around;
            align-items: center;
            margin-bottom: 20px;
        }

        .card-item {
            flex: 0 0 calc(33.33% - 20px);
            max-width: calc(33.33% - 20px);
            padding: 10px;
            border-radius: 10px;
            background-color: #f8f9fa; /* Light gray background color */
            color: #343a40; /* Dark text color */
            transition: all 0.3s ease;
        }

        .card-item:hover {
            transform: translateY(-5px);
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }

        .chosen-card {
            background-color: #343a40 !important; /* Dark background color */
            color: black; /* White text color for chosen card */
        }

        /* Style for the History text */
        .history-text {
            font-weight: bold;
            margin-top: 10px;
        }

        /* Center the homepage button */
        .homepage-button {
            position: absolute;
            top: 20px;
            left: 50%;
            transform: translateX(-50%);
            margin-bottom: auto;
        }

        /* Button hover animation */
        .btn:hover {
            transform: translateY(-3px);
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }
    </style>
</head>
<body>
<%
    int i = 1;
    CardRepo cardRepo = new CardRepo();
    List<Card> cards = cardRepo.findAll();
    cards.sort(Comparator.comparing(Card::getId));
    Card chosenCard = (Card) session.getAttribute("chosenCard");
    List<Transaction> transactions = new ArrayList<>(TransactionRepo.fetchTransactions());
    transactions.sort(Comparator.comparing(Transaction::getCreatedAt).reversed());
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
%>

<div class="container">
    <button class="btn btn-primary homepage-button">
        <a class="text-white" href="${pageContext.request.contextPath}/remove-chosen-card">
            Homepage
        </a>
    </button>

    <div class="card-container mt-5">
        <% for (Card card : cards) { %>
        <div class="card-item
        <% if (chosenCard != null && chosenCard.equals(card)) { %> chosen-card<% } %>">
            <div class="card mb-4">
                <div class="card-body">
                    <form action="${pageContext.request.contextPath}/card-servlet" method="post">
                        <input type="hidden" name="cardId" value="<%= card.getId() %>">
                        <input type="hidden" name="function" value="choosingCard">
                        <button type="submit" class="btn btn-link"><%= card.getName() %>
                        </button>
                    </form>
                    <p class="card-text">Balance: <%= String.format("%,d", card.getBalance()) %>
                    </p>
                </div>
            </div>
        </div>
        <% } %>
    </div>

    <% if (chosenCard != null) { %>
    <div class="row">
        <div class="col-md-12">
            <p><strong>Chosen Card:</strong> <%= chosenCard.getName() %>
            </p>
        </div>
    </div>

    <div class="row">
        <div class="col-md-6">
            <p class="history-text">History</p>
        </div>
        <div class="col-md-6 text-md-right">
            <div class="mt-4">
                <button type="submit" class="btn btn-primary mr-2">
                    <a class="text-white" href="spend.jsp">SPEND</a>
                </button>
                <button type="submit" class="btn btn-secondary mr-2">
                    <a class="text-white" href="transfer.jsp">
                        TRANSFER
                    </a>
                </button>
                <button type="submit" class="btn btn-info">
                    <a class="text-white" href="add-funds.jsp">
                        ADD FUNDS
                    </a>
                </button>
            </div>
        </div>
    </div>
    <div class="row mt-4">
        <div class="col-md-12">
            <%if (!transactions.isEmpty()) {%>
            <table class="table table-striped">
                <thead class="thead-dark">
                <tr>
                    <th>#</th>
                    <th scope="col">Amount</th>
                    <th scope="col">Date</th>
                    <th scope="col">Type</th>
                </tr>
                </thead>
                <tbody>
                <% for (Transaction transaction : transactions) {
                    if (transaction.getCardId().equals(chosenCard.getId())) {
                        String rowColor = transaction.getType().equals(Type.EXPENDITURE) ? "table-danger" : "table-success";
                %>
                <tr class="<%= rowColor %>">
                    <td><%=i++%>.</td>
                    <td>
                        <%=String.format("%,d", transaction.getAmount())%>
                    </td>
                    <td><%=transaction.getCreatedAt().format(formatter) %>
                    </td>
                    <td><%= transaction.getType() %>
                    </td>
                </tr>
                <%}}
                %>
                </tbody>
            </table>
            <% }%>
        </div>
    </div>
    <% } %>
</div>

<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
