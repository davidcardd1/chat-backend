package davidcardd.chatAppbackend.repository;

import davidcardd.chatAppbackend.model.Room;
import davidcardd.chatAppbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

    User findUserBySessionID(String id);

    User findUserByNicknameAndRoom(String name, Room room);

}
