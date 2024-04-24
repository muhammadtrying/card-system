package uz.muhammadtrying.cardsystem.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import uz.muhammadtrying.cardsystem.entity.Card;
import uz.muhammadtrying.cardsystem.entity.User;

import static jakarta.persistence.Persistence.createEntityManagerFactory;

@WebListener
public class DataLoader implements ServletContextListener {
    public static EntityManagerFactory emf;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        emf = createEntityManagerFactory("default");
//        initData();
        ServletContextListener.super.contextInitialized(sce);
    }

    private void initData() {
        EntityManager entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();
        User user = User.builder()
                .email("muhammadtrying@gmail.com")
                .password("1")
                .build();
        entityManager.persist(user);
        Card card1 = Card.builder()
                .name("SQB HUMO")
                .balance(1_000_000)
                .build();
        Card card2 = Card.builder()
                .name("SQB UZCARD")
                .balance(2_000_000)
                .build();
        Card card3 = Card.builder()
                .name("SQB VISA")
                .balance(3_000_000)
                .build();
        entityManager.persist(card1);
        entityManager.persist(card2);
        entityManager.persist(card3);

        entityManager.getTransaction().commit();
    }
}
