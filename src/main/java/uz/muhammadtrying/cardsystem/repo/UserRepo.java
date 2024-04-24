package uz.muhammadtrying.cardsystem.repo;

import jakarta.persistence.EntityManager;
import uz.muhammadtrying.cardsystem.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static uz.muhammadtrying.cardsystem.config.DataLoader.emf;

public class UserRepo extends BaseRepo<User, UUID> {

    public static Optional<User> findByEmail(String email) {
        EntityManager entityManager = emf.createEntityManager();
        List<User> list = entityManager.createQuery(
                        "select u from User u where u.email=:email", User.class)
                .setParameter("email", email).getResultList();
        if (list.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(list.get(0));
        }
    }
}
