package davidcardd.chatAppbackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "users")
public class User {
    @Id
    @ToString.Exclude
    private String sessionID;
    private String nickname;
    private Boolean online = false;

    public User(String sessionID) {
        this.sessionID = sessionID;
    }

    @ManyToOne
    @JoinColumn(name = "roomID")
    @ToString.Exclude
    private Room room;

    @OneToMany(mappedBy = "sender")
    @JsonIgnore
    @ToString.Exclude
    private List<Message> sentMessages = new ArrayList<>();

    @OneToMany(mappedBy = "receiver")
    @JsonIgnore
    @ToString.Exclude
    private List<Message> receivedMessages = new ArrayList<>();
}
