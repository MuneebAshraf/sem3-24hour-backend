package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.DogDTO;
import dtos.UserDTO;
import entities.Dog;
import entities.User;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import utils.EMF_Creator;
import utils.GsonLocalDateTime;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

class dogResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api/";

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;
    private  Dog dog1, dog2, dog3, dog4;
    private User user, admin;
    private static String securityToken;


    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    private static void login(String username, String password) {
        String json = String.format("{username: \"%s\", password: \"%s\"}", username, password);
        securityToken = given()
                .contentType("application/json")
                .body(json)
                //.when().post("/api/login")
                .when().post("/login")
                .then()
                .extract().path("token");
        //System.out.println("TOKEN ---> " + securityToken);
    }

    @BeforeAll
    public static void setUpClass() {
        //This method must be called before you request the EntityManagerFactory
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactoryForTest();

        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
    }

    @AfterAll
    public static void closeTestServer() {
        //Don't forget this, if you called its counterpart in @BeforeAll
        EMF_Creator.endREST_TestWithDB();

        httpServer.shutdownNow();
    }

    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            //Delete existing users and roles to get a "fresh" database
            em.createQuery("delete from Dog").executeUpdate();
            em.createQuery("delete from User").executeUpdate();
            em.createQuery("delete from Walk").executeUpdate();

            //create a user
            user = new User("owner", "test", "Test", "Owner", "Teststreet", 1234, "Testcity", "OWNER");
            em.persist(user);
            admin =(new User("admin", "test", "Test", "Admin", "Teststreet", 1234, "Testcity", "ADMIN"));
            em.persist(admin);
            em.persist(new User("walker", "test", "Test", "Walker", "Teststreet", 1234, "Testcity", "WALKER"));


            dog1 = new Dog("Doggy", "Poodle", "https://images.dog.ceo/breeds/poodle-toy/n021136", "male", LocalDateTime.now(), admin.getId());
            dog2 = new Dog("Doggy2", "Labrador", "https://images.dog.ceo/breeds/poodle-toy/n021136", "male", LocalDateTime.now(), admin.getId());
            dog3 = new Dog("Doggy2", "Golden", "https://images.dog.ceo/breeds/poodle-toy/n021136", "male", LocalDateTime.now(), admin.getId());
            dog4 = new Dog("Doggy2", "Pitbull", "https://images.dog.ceo/breeds/poodle-toy/n021136", "male", LocalDateTime.now(), admin.getId());

            //persist all dogs
            em.persist(dog1);
            em.persist(dog2);
            em.persist(dog3);
            em.persist(dog4);

            //System.out.println("Saved test data to database");
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }


    @Test
    public void serverIsRunning() {
        given().when().get("/info").then().statusCode(200);
    }


    @Test
    void getDogsAsAdmin() {
        login("admin", "test");
        given()
                .contentType("application/json")
                .header("x-access-token", securityToken)
                .when()
                .get("/dog").then()
                .statusCode(200)
                //assert that body has 3 dogs
                .body("", hasSize(4));

    }

    @Test
    void getDogById() {
        given()
                .contentType("application/json")
                .when()
                .get("/dog/"+dog1.getId()).then()
                .statusCode(200)
                //test of the body has the key "name" with the value "Doggy2" and the key "breed" with the value "Golden"
                .body("name", equalTo(dog1.getName()))
                .body("breed", equalTo(dog1.getBreed()));
    }

    @Test
    void createDog() {
        //create a new user user object
        Dog dog = new Dog("newDoggo", "testBreed", "https://images.dog.ceo/breeds/poodle-toy/n021136", "Female", LocalDateTime.now(), admin.getId());
        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTime()).setPrettyPrinting().create();
        String dogDTO = gson.toJson((new DogDTO(dog)));

        given()
                .contentType("application/json")
                .body(dogDTO)
                .when()
                .post("/dog").then()
                .statusCode(200)
                //assert that the body has the key "name" with the value "Doggy2" and the key "breed" with the value "Golden"
                .body("name", equalTo("newDoggo"))
                .body("breed", equalTo("testBreed"));
    }
    //test when calling delete on dog with id 1
    @Test
    void deleteDog() {
        given()
                .contentType("application/json")
                .when()
                .delete("/dog/"+dog1.getId()).then()
                .statusCode(200);

    }

    //test for assigning a dog to a user with the role "OWNER"
    @Test
    void assignDogToOwner() {
        //test that the dog has no owner
        given()
                .contentType("application/json")
                .when()
                .get("/dog/"+dog1.getId()).then()
                .statusCode(200)
                .body("owner", nullValue());
    }



}
