package uz.muhammadtrying.cardsystem.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uz.muhammadtrying.cardsystem.entity.Card;
import uz.muhammadtrying.cardsystem.entity.Expenditure;
import uz.muhammadtrying.cardsystem.entity.Income;
import uz.muhammadtrying.cardsystem.repo.CardRepo;
import uz.muhammadtrying.cardsystem.repo.ExpenditureRepo;
import uz.muhammadtrying.cardsystem.repo.IncomeRepo;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@WebServlet(value = "/card-servlet")
public class CardServlet extends HttpServlet {
    CardRepo cardRepo = new CardRepo();
    ExpenditureRepo expenditureRepo = new ExpenditureRepo();
    IncomeRepo incomeRepo = new IncomeRepo();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        switch (req.getParameter("function")) {
            case "choosingCard" -> chooseCard(req, resp);
            case "spending" -> spend(req, resp);
            case "addingFunds" -> addFunds(req, resp);
            case "transferFunds" -> transfer(req, resp);
        }
        resp.sendRedirect("/homepage.jsp");
    }

    private void transfer(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        Map<String, String> errors = new HashMap<>();

        String amountParam = req.getParameter("amount");
        String fromCardIdParam = req.getParameter("fromCardId");
        String toCardIdParam = req.getParameter("toCardId");

        if (amountParam == null || amountParam.isEmpty()) {
            errors.put("amount", "Amount is required.");
        } else {
            int amount = Integer.parseInt(amountParam);
            if (amount <= 0) {
                errors.put("amount", "Amount must be a positive number.");
            }

            if (fromCardIdParam == null || fromCardIdParam.isEmpty()) {
                errors.put("fromCardId", "From Card ID is required.");
            }
            if (toCardIdParam == null || toCardIdParam.isEmpty()) {
                errors.put("toCardId", "To Card ID is required.");
            }

            if (!errors.isEmpty()) {
                req.setAttribute("errors", errors);
                req.getRequestDispatcher("/transfer.jsp").forward(req, resp);
                return;
            }

            amount = Integer.parseInt(amountParam);
            Card fromCard = cardRepo.findById(UUID.fromString(fromCardIdParam));
            Card toCard = cardRepo.findById(UUID.fromString(toCardIdParam));

            if (amount > fromCard.getBalance()) {
                errors.put("amount", "Insufficient funds in the from card.");
            }
            if (fromCard.getId().equals(toCard.getId())) {
                errors.put("toCardId", "To Card ID must be different from From Card ID.");
            }

            if (!errors.isEmpty()) {
                req.setAttribute("errors", errors);
                req.getRequestDispatcher("/transfer.jsp").forward(req, resp);
                return;
            }

            spend(req, resp);
            addFunds(req, resp);
        }
    }

    private void addFunds(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (req.getParameter("toCardId") == null || (Integer.parseInt(req.getParameter("amount")) <= 0)) {
            resp.sendRedirect("/add-funds.jsp");
            return;
        }
        UUID cardId = UUID.fromString(req.getParameter("toCardId"));
        Integer amount = Integer.parseInt(req.getParameter("amount"));
        Card chosenCard = cardRepo.findById(cardId);
        Income income = Income.builder()
                .card(chosenCard)
                .amount(amount)
                .build();
        incomeRepo.save(income);
        cardRepo.begin();
        chosenCard.setBalance(chosenCard.getBalance() + amount);
        cardRepo.commit();
    }

    private void spend(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        Map<String, String> errors = new HashMap<>();

        String fromCardId = req.getParameter("fromCardId");
        String amountStr = req.getParameter("amount");
        if (fromCardId == null || amountStr == null) {
            errors.put("nullAmount", "Please provide both card ID and amount.");
        } else {
            int amount = Integer.parseInt(amountStr);
            if (amount <= 0) {
                errors.put("negativeAmount", "Amount must be a positive number.");
            } else {
                UUID cardId = UUID.fromString(fromCardId);
                Card chosenCard = cardRepo.findById(cardId);
                if (chosenCard == null) {
                    errors.put("invalidCard", "Invalid card ID.");
                } else {
                    if (amount > chosenCard.getBalance()) {
                        errors.put("insufficientFunds", "Insufficient funds in the chosen card.");
                    }
                }
            }
        }

        if (!errors.isEmpty()) {
            req.setAttribute("errors", errors);
            req.getRequestDispatcher("/spend.jsp").forward(req, resp);
            return;
        }

        UUID cardId = UUID.fromString(fromCardId);
        Integer amount = Integer.parseInt(amountStr);
        Card chosenCard = cardRepo.findById(cardId);
        Expenditure expenditure = Expenditure.builder()
                .card(chosenCard)
                .amount(amount)
                .build();
        expenditureRepo.save(expenditure);

        cardRepo.begin();
        chosenCard.setBalance(chosenCard.getBalance() - amount);
        cardRepo.commit();
    }


    private void chooseCard(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        UUID cardId = UUID.fromString(req.getParameter("cardId"));
        Card chosenCard = cardRepo.findById(cardId);
        req.getSession().setAttribute("chosenCard", chosenCard);
    }
}
