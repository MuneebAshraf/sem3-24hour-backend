package entities;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "Dog")
@NamedQuery(name = "Dog.deleteAllRows", query = "DELETE from Dog")
public class Dog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false, length = 45)
    private String name;

    @Column(name = "breed", nullable = false, length = 45)
    private String breed;

    @Column(name = "image")
    private String image;

    @Column(name = "gender", nullable = false, length = 45)
    private String gender;

    @Column(name = "birthdate")
    private LocalDateTime birthdate;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "User_id")
    private User user;

    @ManyToMany
    @JoinTable(name = "Walks_has_Dog",
            joinColumns = @JoinColumn(name = "Dog_id"),
            inverseJoinColumns = @JoinColumn(name = "Walks_id"))
    private Set<Walk> walks = new LinkedHashSet<>();

    public Dog() {
    }

    public Dog(Integer id, String name, String breed, String image, String gender, LocalDateTime birthdate) {
        this.id = id;
        this.name = name;
        this.breed = breed;
        this.image = image;
        this.gender = gender;
        this.birthdate = birthdate;
    }

    public Dog(String name, String breed, String image, String gender, LocalDateTime birthdate,Integer user_id) {
        this.name = name;
        this.breed = breed;
        this.image = image;
        this.gender = gender;
        this.birthdate = birthdate;
        User user = new User();
        user.setId(user_id);
        this.user = user;
    }

    public void setDog(Dog dog) {
        this.id = dog.getId();
        this.name = dog.getName();
        this.breed = dog.getBreed();
        this.image = dog.getImage();
        this.gender = dog.getGender();
        this.birthdate = dog.getBirthdate();
    }

    public Set<Walk> getWalks() {
        return walks;
    }

    public void setWalks(Set<Walk> walks) {
        this.walks = walks;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDateTime birthdate) {
        this.birthdate = birthdate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
