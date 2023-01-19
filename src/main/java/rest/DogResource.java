package rest;

import dtos.DogDTO;
import errorhandling.API_Exception;
import errorhandling.GenericExceptionMapper;
import facades.DogFacade;
import security.Permission;
import utils.EMF_Creator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import utils.GsonLocalDateTime;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * @author cph-ma670
 */
@Path("dog")
public class DogResource {
    
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    public static final DogFacade DOG_FACADE = DogFacade.getInstance(EMF);
    private static final Gson GSON = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTime()).setPrettyPrinting().create();

    @Context
    private UriInfo context;

    @Context
    SecurityContext securityContext;

    //permit only for User with roles ADMIN or WALKER

    @GET
    @RolesAllowed({Permission.Types.ADMIN, Permission.Types.WALKER})
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<DogDTO> list = DOG_FACADE.getAllDogs();
        return Response.ok().entity(GSON.toJson(list)).header(MediaType.CHARSET_PARAMETER, StandardCharsets.UTF_8.name()).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createDog(String content) throws API_Exception {
        String name, breed, image, gender, birthdate;
        try {
            JsonObject json = JsonParser.parseString(content).getAsJsonObject();
            name = json.get("name").getAsString();
            breed = json.get("breed").getAsString();
            image = json.get("image").getAsString();
            gender = json.get("gender").getAsString();
            birthdate = json.get("birthdate").getAsString();


        } catch (Exception e) {
            throw new API_Exception("Malformed JSON Supplied", 400, e);
        }

        try {
            DogDTO dogDTO = DOG_FACADE.create(name, breed, image, gender, LocalDateTime.parse(birthdate), 1);
            return Response.ok(GSON.toJson(dogDTO)).build();

        } catch (Exception ex) {
            Logger.getLogger(GenericExceptionMapper.class.getName()).log(Level.SEVERE, null, ex);
        }
        throw new API_Exception("Failed to create a new dog!");
    }

    //Get by id
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") Integer id) throws API_Exception {
        DogDTO dogDTO = DOG_FACADE.getById(id);
        return Response.ok(GSON.toJson(dogDTO)).build();
    }

    //delete by id
    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") Integer id) throws API_Exception {
        boolean isDeleted = DOG_FACADE.delete(id);
        if (isDeleted) {
            return Response.ok().build();
        } else {
            throw new API_Exception("Failed to delete dog with id: " + id);
        }
    }

    //update by id
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") Integer id, String content) throws API_Exception {
        String name, breed, image, gender, birthdate;
        Integer userId;
        try {
            JsonObject json = JsonParser.parseString(content).getAsJsonObject();
            name = json.get("name").getAsString();
            breed = json.get("breed").getAsString();
            image = json.get("image").getAsString();
            gender = json.get("gender").getAsString();
            birthdate =  json.get("birthdate").getAsString();
            userId = json.get("userId").getAsInt();

            //update with new values by id
            boolean isUpdated = DOG_FACADE.update(id, name, breed, image, gender, LocalDateTime.parse(birthdate), userId);
            if (isUpdated) {
                return Response.ok().build();
            } else {
                throw new API_Exception("Failed to update dog with id: " + id);
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
