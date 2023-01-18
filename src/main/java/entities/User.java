package entities;

import org.mindrot.jbcrypt.BCrypt;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "first_name", length = 45)
    private String firstName;

    @Column(name = "last_name", length = 45)
    private String lastName;

    @Column(name = "street")
    private String street;

    @Column(name = "zip")
    private Integer zip;

    @Column(name = "city")
    private String city;

    @Lob
    @Column(name = "Roles", nullable = false)
    private String roles;

    @OneToMany(mappedBy = "user")
    private Set<Walk> walks = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Dog> dogs = new LinkedHashSet<>();

    //constructor and pasword with bcrypt
    public User(String username, String password, String firstName, String lastName, String street, Integer zip, String city, String roles) {
        this.username = username;
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
        this.firstName = firstName;
        this.lastName = lastName;
        this.street = street;
        this.zip = zip;
        this.city = city;
        this.roles = roles;
    }

    //emptry constructor
    public User() {
    }

    public Set<Dog> getDogs() {
        return dogs;
    }

    public void setDogs(Set<Dog> dogs) {
        this.dogs = dogs;
    }

    public Set<Walk> getWalks() {
        return walks;
    }

    public void setWalks(Set<Walk> walks) {
        this.walks = walks;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getZip() {
        return zip;
    }

    public void setZip(Integer zip) {
        this.zip = zip;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean verifyPassword(String checkPassword) {
        return BCrypt.checkpw(checkPassword, this.password);
    }
}
