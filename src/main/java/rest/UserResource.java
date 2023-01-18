package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dtos.UserDTO;
import errorhandling.API_Exception;
import errorhandling.GenericExceptionMapper;
import facades.UserFacade;
import utils.EMF_Creator;
import utils.GsonLocalDateTime;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author cph-ma670
 */
@Path("user")
public class UserResource {
    
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    public static final UserFacade USER_FACADE = UserFacade.getInstance(EMF);
    private static final Gson GSON = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTime()).setPrettyPrinting().create();
    @Context
    private UriInfo context;

    @Context
    SecurityContext securityContext;

    //Just to verify if the database is setup
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<UserDTO> list = USER_FACADE.getAllUsers();
        return Response.ok().entity(GSON.toJson(list)).header(MediaType.CHARSET_PARAMETER, StandardCharsets.UTF_8.name()).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(String content) throws API_Exception {
        String username, password, firstName, lastName, street, city, roles;
        Integer zip;

        try {
            JsonObject json = JsonParser.parseString(content).getAsJsonObject();
            //get all parameters from json
            username = json.get("username").getAsString();
            password = json.get("password").getAsString();
            firstName = json.get("firstName").getAsString();
            lastName = json.get("lastName").getAsString();
            street = json.get("street").getAsString();
            zip = Integer.parseInt(json.get("zip").getAsString());
            city = json.get("city").getAsString();
            roles = json.get("roles").getAsString();

        } catch (Exception e) {
            throw new API_Exception("Malformed JSON Supplied", 400, e);
        }

        try {
            //create user
            UserDTO userDTO = USER_FACADE.createUser(username, password, firstName, lastName, street, zip, city, roles);
            return Response.ok(GSON.toJson(userDTO)).build();

        } catch (Exception ex) {
            Logger.getLogger(GenericExceptionMapper.class.getName()).log(Level.SEVERE, null, ex);
        }
        throw new API_Exception("Failed to create a new User!");
    }

    //Get by id
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") Integer id) throws API_Exception {
        UserDTO userDTO = USER_FACADE.getById(id);
        return Response.ok(GSON.toJson(userDTO)).build();
    }

    //delete by id
    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") Integer id) throws API_Exception {
        boolean isDeleted = USER_FACADE.delete(id);
        if (isDeleted) {
            return Response.ok().build();
        } else {
            throw new API_Exception("Failed to delete User with id: " + id);
        }
    }

    //update by id
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") Integer id, String content) throws API_Exception {
        String username, password, firstName, lastName, street, city, roles;
        Integer zip;

        LocalDateTime birthdate;
        try {
            JsonObject json = JsonParser.parseString(content).getAsJsonObject();
            //get all parameters from json
            username = json.get("username").getAsString();
            password = json.get("password").getAsString();
            firstName = json.get("firstName").getAsString();
            lastName = json.get("lastName").getAsString();
            street = json.get("street").getAsString();
            zip = Integer.parseInt(json.get("zip").getAsString());
            city = json.get("city").getAsString();
            roles = json.get("roles").getAsString();

            //update with new values by id
            boolean isUpdated = USER_FACADE.update(new UserDTO(id, username, firstName, lastName, street, zip, city, roles));
            if (isUpdated) {
                return Response.ok().build();
            } else {
                throw new API_Exception("Failed to update User with id: " + id);
            }

        } catch (Exception e) {
            throw new API_Exception("Malformed JSON Supplied", 400, e);
        }
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("user")
    @RolesAllowed("user")
    public String getFromUser() {
        String thisuser = securityContext.getUserPrincipal().getName();
        return "{\"msg\": \"Hello to User: " + thisuser + "\"}";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("admin")
    @RolesAllowed("admin")
    public String getFromAdmin() {
        String thisuser = securityContext.getUserPrincipal().getName();
        return "{\"msg\": \"Hello to (admin) User: " + thisuser + "\"}";
    }
}
