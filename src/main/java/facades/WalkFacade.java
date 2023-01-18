package facades;

import dtos.DogDTO;
import dtos.WalkDTO;
import entities.Dog;
import entities.Walk;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class WalkFacade {
    private static EntityManagerFactory emf;
    private static WalkFacade instance;

    public static WalkFacade getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new WalkFacade();
        }
        return instance;
    }

    //get all walks
    public List<WalkDTO> getAllWalks() {
        List<Walk> walksList = executeWithClose(em -> {
            TypedQuery<Walk> query = em.createQuery("SELECT w FROM Walk w", Walk.class);
            return query.getResultList();
        });
        return WalkDTO.listToDTOs(walksList);
    }


    private <R> R executeWithClose(Function<EntityManager, R> action) {
        EntityManager em = emf.createEntityManager();
        R result = action.apply(em);
        em.close();
        return result;
    }

    private void executeInsideTransaction(Consumer<EntityManager> action) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            action.accept(em);
            tx.commit();
        } catch (RuntimeException e) {
            tx.rollback();
            e.printStackTrace();
            throw e;
        } finally {
            em.close();
        }
    }
}
