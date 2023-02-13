package davidcardd.chatAppbackend.repository;

import davidcardd.chatAppbackend.model.Message;
import davidcardd.chatAppbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, String> {
    List<Message> findAllBySenderInAndReceiverIn(List<User> users, List<User> others);
    Integer countAllBySenderAndReceiverAndSeen(User sender, User receiver, boolean seen);
}
