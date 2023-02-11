package davidcardd.chatAppbackend.controller;

import davidcardd.chatAppbackend.model.Room;
import davidcardd.chatAppbackend.model.User;
import davidcardd.chatAppbackend.service.RoomService;
import davidcardd.chatAppbackend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
public class RoomController {

    @Autowired
    RoomService roomService;

    @Autowired
    UserService userService;

    @PostMapping("/chatroom/new")
    public Room newRoom(@Valid @RequestParam(value = "roomName") String roomName) {
        return roomService.save(new Room(UUID.randomUUID().toString(), roomName));
    }

    @GetMapping("/chatroom")
    public Room getRoom(@Valid @RequestParam(value = "roomID") String roomID) {
        Room room = roomService.findRoomById(roomID);
        if (room != null) {
            return room;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "room not found");
        }
    }

//    // maybe not needed
//    @PostMapping("/chatroom/{roomId}/rejoin")
//    public String rejoinRoom(@Valid @RequestParam(value = "sessionID") String sessionID, @PathVariable String roomId) {
//        Room room = roomService.findRoomById(roomId);
//        User oldUser = userService.findUserBySessionID(sessionID);
//        if (room == null) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "room doesn't exist");
//        } else {
//            if (oldUser == null || !oldUser.getRoom().equals(room)) {
//                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user doesn't exist in specified room");
//            } else {
//                oldUser.setOnline(true);
//                userService.save(oldUser);
//                return oldUser.toString();
//            }
//        }
//    }
}
