package dtos;

import entities.Dog;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DogDTO {
    private Integer id;
    private final String name;
    private final String breed;
    private final String image;
    private final String gender;
    private final LocalDateTime birthdate;
    private final Integer userId;

    public DogDTO(Dog dog) {
        this.name = dog.getName();
        this.breed = dog.getBreed();
        this.image = dog.getImage();
        this.gender = dog.getGender();
        this.birthdate = dog.getBirthdate();

        if (dog.getUser() != null) {
            this.userId = dog.getUser().getId();
        } else {
            this.userId = null;
        }
    }

    public static List<DogDTO> listToDTOs(List<Dog> dogsList) {
        return dogsList.stream().map(DogDTO::new).collect(Collectors.toList());
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBreed() {
        return breed;
    }

    public String getImage() {
        return image;
    }

    public String getGender() {
        return gender;
    }

    public LocalDateTime getBirthdate() {
        return birthdate;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DogDTO entity = (DogDTO) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.name, entity.name) &&
                Objects.equals(this.breed, entity.breed) &&
                Objects.equals(this.image, entity.image) &&
                Objects.equals(this.gender, entity.gender) &&
                Objects.equals(this.birthdate, entity.birthdate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, breed, image, gender, birthdate);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "name = " + name + ", " +
                "breed = " + breed + ", " +
                "image = " + image + ", " +
                "gender = " + gender + ", " +
                "birthdate = " + birthdate + ", ";
    }
}
