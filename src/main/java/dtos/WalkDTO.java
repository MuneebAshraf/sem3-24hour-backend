package dtos;

import entities.Dog;
import entities.User;
import entities.Walk;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class WalkDTO {
    private Integer id;
    private LocalDateTime date;
    private List<Dog> dogs;
    private User user;

    //constructor that takes a walk entity
    public WalkDTO(Walk walk) {
        this.id = walk.getId();
        this.date = walk.getDate();
//        this.dogs = walk.getDogs();
        this.user = walk.getUser();
    }


    //empty constructor
    public WalkDTO() {
    }

    //



    //from list to DTO list
    public static List<WalkDTO> listToDTOs(List<Walk> walks) {
        return walks.stream().map(WalkDTO::new).collect(Collectors.toList());
    }

}
