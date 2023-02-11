package davidcardd.chatAppbackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "rooms")
public class Room {
    @Id
    private String id;

    private String name;

    @OneToMany(mappedBy = "room")
    @JsonIgnore
    @ToString.Exclude
    private List<User> roomUsers = new ArrayList<>();

    public Room (String id, String name) {
        this.id = id;
        this.name = name;
    }
}
