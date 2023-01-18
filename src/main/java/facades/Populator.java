/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import utils.EMF_Creator;

import javax.persistence.EntityManagerFactory;

/**
 *
 * @author cph-ma670
 */
public class Populator {
    public static boolean populate(){
        try {
            EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
            UserFacade USER_FACADE = UserFacade.getInstance(emf);
            USER_FACADE.createUser("owner", "test", "Test", "Owner", "Teststreet", 1234, "Testcity", "OWNER");
            USER_FACADE.createUser("admin", "test", "Test", "Admin", "Teststreet", 1234, "Testcity", "ADMIN");
            USER_FACADE.createUser("walker", "test", "Test", "Walker", "Teststreet", 1234, "Testcity", "WALKER");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
