package facades;

import dtos.DogDTO;
import entities.Dog;
import entities.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class DogFacade {
    private static EntityManagerFactory emf;
    private static DogFacade instance;

    public static DogFacade getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new DogFacade();
        }
        return instance;
    }

    public List<DogDTO> getAllDogs() {
        List<Dog> dogsList = new ArrayList<>();
        try {
            dogsList = executeWithClose(em -> {
                TypedQuery<Dog> query = em.createQuery("SELECT d FROM Dog d", Dog.class);
                return query.getResultList();
            });
        } catch (Exception e) {
            System.out.println("Error while trying to fetch all dogs: " + e.getMessage());
        }
        return DogDTO.listToDTOs(dogsList);
    }

    public DogDTO create(String name, String breed, String image, String gender, LocalDateTime birthdate) {
        Dog dog = new Dog(name, breed, image, gender, birthdate);
        executeInsideTransaction(em -> em.persist(dog));
        return new DogDTO(dog);
    }

    //get by id with
    public DogDTO getById(Integer id) {
        Dog dog = executeWithClose(em -> em.find(Dog.class, id));
        return new DogDTO(dog);
    }

    //delete by id
    public boolean delete(Integer id) {
        //surround with trycatch and return false on exception
        try{
            executeInsideTransaction(em -> {
                em.remove(em.find(Dog.class, id));
            });
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //update all fields of a dog by id
    public boolean update(Integer id, String name, String breed, String image, String gender, LocalDateTime birthdate, Integer userId) {
        //create DOG object with new values
        Dog newDog = new Dog(id, name, breed, image, gender, birthdate);
        if (userId != null) {
            User user = executeWithClose(em -> em.find(User.class, userId));
            newDog.setUser(user);
        }

        //update insde a transacetion by id
        try{
            executeInsideTransaction(em -> {
                Dog dogToUpdate = em.find(Dog.class, id);
                dogToUpdate.setDog(newDog);
                em.merge(dogToUpdate);
            });
            return true;
        } catch (Exception e) {
            return false;
        }
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
