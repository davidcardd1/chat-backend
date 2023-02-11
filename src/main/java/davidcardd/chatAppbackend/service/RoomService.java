package davidcardd.chatAppbackend.service;

import davidcardd.chatAppbackend.model.Room;
import davidcardd.chatAppbackend.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public Room findRoomById(String id) { return roomRepository.findRoomById(id);}

    public Room save(Room toSave) { return roomRepository.save(toSave);}
}
