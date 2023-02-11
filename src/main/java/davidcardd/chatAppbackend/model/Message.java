package davidcardd.chatAppbackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "messages")
@JsonSerialize(using = MessageSerializer.class)
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;
    private String body;
    private LocalDateTime timeSent = LocalDateTime.now();
    private boolean seen = false;

    public enum MessageType {CHAT, JOIN, LEAVE, JOIN_ACK}

    private MessageType type;

    @ManyToOne
    @JoinColumn(name = "senderID")
    @ToString.Exclude
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiverID")
    @ToString.Exclude
    private User receiver;
}
