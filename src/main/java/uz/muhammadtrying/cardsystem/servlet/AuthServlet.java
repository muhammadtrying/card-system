package uz.muhammadtrying.cardsystem.servlet;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.SneakyThrows;
import uz.muhammadtrying.cardsystem.entity.User;
import uz.muhammadtrying.cardsystem.repo.UserRepo;

import java.io.IOException;
import java.util.*;


@WebServlet(value = "/auth-servlet")
public class AuthServlet extends HttpServlet {
    @SneakyThrows
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        Map<String, String> errors = validateCredentials(email, password);

        Optional<User> optionalUser = UserRepo.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.getPassword().equals(password)) {
                String code = sendEmail(email);
                req.getSession().setAttribute("code", code);
                req.getSession().setAttribute("temp", user);
                resp.sendRedirect("/email.jsp");
                return;
            }
        }
        req.setAttribute("errors", errors);
        req.getRequestDispatcher("/").forward(req, resp);
    }

    private Map<String, String> validateCredentials(String email, String password) {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        Map<String, String> errors = new HashMap<>();
        for (ConstraintViolation<User> violation : violations) {
            errors.put(violation.getPropertyPath().toString(), violation.getMessage());
        }
        return errors;
    }


    private String sendEmail(String recipientEmail) throws MessagingException {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("muhammadtrying@gmail.com",
                        "eajkgnvbwwcfwvuu");
            }
        });
        Random random = new Random();
        String text = String.valueOf(random.nextInt(9000) + 1000);
        Message message = new MimeMessage(session);
        message.setSubject("VERIFICATION EMAIL");
        message.setText("Your code is: " + text+". Don't share it to anyone!");
        message.setFrom(new InternetAddress("muhammadtrying@gmail.com"));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
        Transport.send(message);
        return text;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        User temp = (User) req.getSession().getAttribute("temp");
        String code = (String) req.getSession().getAttribute("code");
        String codeEntered = req.getParameter("code");
        if (code.equals(codeEntered)) {
            req.getSession().setAttribute("currentUser", temp);
            resp.sendRedirect("/homepage.jsp");
        } else {
            req.setAttribute("wrongCode", codeEntered);
            req.getRequestDispatcher("/email.jsp").forward(req, resp);
        }
    }
}
