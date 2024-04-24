package uz.muhammadtrying.cardsystem.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(value = "/remove-chosen-card")
public class RemoveChosenCard extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.getSession().removeAttribute("chosenCard");
        resp.sendRedirect("/homepage.jsp");
    }
}
