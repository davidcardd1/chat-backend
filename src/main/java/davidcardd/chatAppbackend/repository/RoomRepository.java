package davidcardd.chatAppbackend.repository;

import davidcardd.chatAppbackend.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, String> {
    Room findRoomById(String id);
}
