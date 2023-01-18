package dtos;

import entities.Dog;
import entities.User;
import entities.Walk;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class UserDTO implements Serializable {
    private Integer id;
    private final String username;
    private final String firstName;
    private final String lastName;
    private final String street;
    private final Integer zip;
    private final String city;
    private final String roles;
    private Set<Walk> walks;
    private Set<Dog> dogs;

    public UserDTO(Integer id, String username, String firstName, String lastName, String street, Integer zip, String city, String roles) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.street = street;
        this.zip = zip;
        this.city = city;
        this.roles = roles;
    }

    public UserDTO(String username, String firstName, String lastName, String street, Integer zip, String city, String roles) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.street = street;
        this.zip = zip;
        this.city = city;
        this.roles = roles;
    }


    public UserDTO(User user) {
        if (user.getId() != null)
            this.id = user.getId();

        this.username = user.getUsername();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.street = user.getStreet();
        this.zip = user.getZip();
        this.city = user.getCity();
        this.roles = user.getRoles();
        this.walks = user.getWalks();
        this.dogs = user.getDogs();
    }

    public static List<UserDTO> listToDTOs(List<User> usersList) {
        return usersList.stream().map(UserDTO::new).collect(Collectors.toList());
    }

    //get User from UserDTO and set all fields that arent null
    public User getUserFromDTO() {
        User user = new User();
        if (this.id != null)
            user.setId(this.id);
        if (this.username != null)
            user.setUsername(this.username);
        if (this.firstName != null)
            user.setFirstName(this.firstName);
        if (this.lastName != null)
            user.setLastName(this.lastName);
        if (this.street != null)
            user.setStreet(this.street);
        if (this.zip != null)
            user.setZip(this.zip);
        if (this.city != null)
            user.setCity(this.city);
        if (this.roles != null)
            user.setRoles(this.roles);
        if (this.walks != null)
            user.setWalks(this.walks);
        if (this.dogs != null)
            user.setDogs(this.dogs);
        return user;
    }


    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getStreet() {
        return street;
    }

    public Integer getZip() {
        return zip;
    }

    public String getCity() {
        return city;
    }

    public String getRoles() {
        return roles;
    }

    public Set<Walk> getWalks() {
        return walks;
    }

    public Set<Dog> getDogs() {
        return dogs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO entity = (UserDTO) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.username, entity.username) &&
                Objects.equals(this.firstName, entity.firstName) &&
                Objects.equals(this.lastName, entity.lastName) &&
                Objects.equals(this.street, entity.street) &&
                Objects.equals(this.city, entity.city) &&
                Objects.equals(this.roles, entity.roles) &&
                Objects.equals(this.walks, entity.walks) &&
                Objects.equals(this.dogs, entity.dogs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, firstName, lastName, street, city, roles, walks, dogs);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "username = " + username + ", " +
                "firstName = " + firstName + ", " +
                "lastName = " + lastName + ", " +
                "street = " + street + ", " +
                "city = " + city + ", " +
                "roles = " + roles + ", " +
                "walks = " + walks + ", " +
                "dogs = " + dogs + ")";
    }
}
