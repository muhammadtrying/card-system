package uz.muhammadtrying.cardsystem.repo;

import jakarta.persistence.EntityManager;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import static uz.muhammadtrying.cardsystem.config.DataLoader.emf;

public class BaseRepo<T, I> {
    public Class<T> persistenceClass;
    public EntityManager em = emf.createEntityManager();

    public BaseRepo() {
        Type type = getClass().getGenericSuperclass();
        ParameterizedType parameterizedType = (ParameterizedType) type;
        this.persistenceClass = (Class<T>) parameterizedType.getActualTypeArguments()[0];
    }

    public void begin() {
        em.getTransaction().begin();
    }

    public void commit() {
        em.getTransaction().commit();
    }

    public void save(T t) {
        begin();
        em.persist(t);
        commit();
    }

    public List<T> findAll() {
        return em.createQuery("from " + persistenceClass.getSimpleName(), persistenceClass).getResultList();
    }

    public T findById(I id) {
        return em.find(persistenceClass, id);
    }
    public void delete(I id) {
        em.remove(em.find(persistenceClass, id));
    }
}
