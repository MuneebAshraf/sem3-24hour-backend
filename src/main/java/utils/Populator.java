/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import dtos.UserDTO;
import entities.User;
import facades.DogFacade;
import facades.UserFacade;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.time.LocalDateTime;

import static rest.DogResource.DOG_FACADE;

/**
 *
 * @author cph-ma670
 */
public class Populator {
    public static boolean populate(){
        try {
            EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();

            UserFacade USER_FACADE = UserFacade.getInstance(emf);
            DogFacade DOG_FACADE = DogFacade.getInstance(emf);

            UserDTO admin = USER_FACADE.createUser("admin", "test", "Test", "Admin", "Teststreet", 1234, "Testcity", "ADMIN");
            USER_FACADE.createUser("owner", "test", "Test", "Owner", "Teststreet", 1234, "Testcity", "OWNER");
            USER_FACADE.createUser("walker", "test", "Test", "Walker", "Teststreet", 1234, "Testcity", "WALKER");

            DOG_FACADE.create("Doggy", "Poodle", "https://www.pexels.com/da-dk/foto/dyr-hund-sodt-liggende-6568501/", "male", LocalDateTime.now(), admin.getId());
            DOG_FACADE.create("Doggy2", "Labrador", "https://www.pexels.com/da-dk/foto/dyr-hund-sodt-liggende-6568501/", "male", LocalDateTime.now(), admin.getId());
            DOG_FACADE.create("Doggy2", "Golden", "https://www.pexels.com/da-dk/foto/dyr-hund-sodt-liggende-6568501/", "male", LocalDateTime.now(), admin.getId());
            DOG_FACADE.create("Doggy2", "Pitbull", "https://www.pexels.com/da-dk/foto/dyr-hund-sodt-liggende-6568501/", "male", LocalDateTime.now(), admin.getId());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
