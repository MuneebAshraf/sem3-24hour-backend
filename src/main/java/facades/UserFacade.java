package facades;

import dtos.UserDTO;
import entities.User;
import security.errorhandling.AuthenticationException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author cph-ma670
 */
public class UserFacade {

    private static EntityManagerFactory emf;
    private static UserFacade instance;

    private UserFacade() {
    }

    /**
     *
     * @param _emf
     * @return the instance of this facade.
     */
    public static UserFacade getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new UserFacade();
        }
        return instance;
    }

    //getAllUsers
    public List<UserDTO> getAllUsers() {
        List<User> usersList = executeWithClose(em -> {
            // get all users where roles enum is not equal to "ADMIN"
            TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.roles != 'ADMIN'", User.class);
            return query.getResultList();
        });
        return UserDTO.listToDTOs(usersList);
    }

    //create a new user that can be either Owner or Walker
    public UserDTO createUser(String username, String password, String firstName, String lastName, String street, Integer zip, String city, String roles) {
        User user = new User(username, password, firstName, lastName, street, zip, city, roles);
        executeInsideTransaction(em -> {
            em.persist(user);
        });
        return new UserDTO(user);
    }

    public UserDTO getVerifiedUser(String username, String password) throws AuthenticationException {
      //get a single user from database with username and confirm password with bcrypt
        List<User> users = executeWithClose(em -> {
            TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class);
            query.setParameter("username", username);
            return query.getResultList();
        });
        if (users.isEmpty() || !users.get(0).verifyPassword(password)) {
            throw new AuthenticationException("Invalid username or password");
        }
        return new UserDTO(users.get(0)) ;
    }

    //get a user by id
    public UserDTO getById(Integer id) {
        User user = executeWithClose(em -> em.find(User.class, id));
        return new UserDTO(user);
    }

    //delete a user by id
    public boolean delete(Integer id) {

        try {
            User user = executeWithClose(em -> em.find(User.class, id));
            executeInsideTransaction(em -> {
                em.remove(user);
            });
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //update a user by id insted transaction
    public boolean update(UserDTO userDTO) {
        try {
            User user = executeWithClose(em -> em.find(User.class, userDTO.getId()));
            user.setFirstName(userDTO.getFirstName());
            user.setLastName(userDTO.getLastName());
            user.setStreet(userDTO.getStreet());
            user.setZip(userDTO.getZip());
            user.setCity(userDTO.getCity());
            user.setRoles(userDTO.getRoles());
            executeInsideTransaction(em -> {
                em.merge(user);
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
