package davidcardd.chatAppbackend.service;

import davidcardd.chatAppbackend.model.Room;
import davidcardd.chatAppbackend.model.User;
import davidcardd.chatAppbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findUserBySessionID(String id) { return userRepository.findUserBySessionID(id);}

    public User findUserByNicknameAndRoom(String name, Room room) { return userRepository.findUserByNicknameAndRoom(name, room);}
    public User save(User toSave) { return userRepository.save(toSave);}

}
