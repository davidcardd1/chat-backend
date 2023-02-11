package davidcardd.chatAppbackend.controller;

import davidcardd.chatAppbackend.model.Room;
import davidcardd.chatAppbackend.model.User;
import davidcardd.chatAppbackend.service.RoomService;
import davidcardd.chatAppbackend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private SimpMessagingTemplate template;

    @GetMapping("/")
    public String index() {
        return "Welcome to the home page!";
    }

    @GetMapping("/{roomID}/users")
    public Map<String, Boolean> listRoomUsers(@PathVariable String roomID, @RequestParam String sessionID) {
        Room room = roomService.findRoomById(roomID);
        if (room == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "room doesn't exist");
        } else {
            User user = userService.findUserBySessionID(sessionID);
            if ( user == null || !user.getRoom().getId().equals(roomID)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "wrong user or room");
            } else {
                Map<String, Boolean> userList = new HashMap<>();
                for (User u : room.getRoomUsers()) {
                    if (u.getSessionID() != user.getSessionID()) {
                        userList.put(u.getNickname(), u.getOnline());
                    }
                }
                return userList;
            }
        }
    }

    @PutMapping("/status")
    public String updateStatus(@Valid @RequestBody User auth, @Valid @RequestParam boolean status) {
        User user = userService.findUserBySessionID(auth.getSessionID());
        if (user == null ) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "wrong user");
        } else {
            user.setOnline(status);
            return userService.save(user).toString();
        }
    }

    @PostMapping("/login")
    public User login(@Valid @RequestBody User user) {
        User loginUser = userService.findUserBySessionID(user.getSessionID());
        if (loginUser == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user doesn't exist in specified room");
        } else {
            loginUser.setOnline(true);
            userService.save(loginUser);
            //template.convertAndSend("/room/login", loginUser.getNickname());
            return loginUser;
        }
    }

    @PostMapping("/logout")
    public String logout(@Valid @RequestBody User user) {
        User logoutUser = userService.findUserBySessionID(user.getSessionID());
        logoutUser.setOnline(false);
        userService.save(logoutUser);
        return "User: " + logoutUser.getNickname() + " logged out";
        //template.convertAndSend("/room/logout", logoutUser.getNickname());
    }

    @PostMapping("/{roomId}/register")
    public User joinRoom(@Valid @RequestParam(value = "userName") String userName, @PathVariable String roomId) {
        Room room = roomService.findRoomById(roomId);
        if (room == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "room doesn't exist");
        } else {
            User newUser = new User();
            newUser.setNickname(userName);
            List<User> userList = room.getRoomUsers();
            for (User user : userList) {
                if (userName.equals(user.getNickname())) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "name should be unique");
                }
            }
            newUser.setSessionID(UUID.randomUUID().toString());
            newUser.setRoom(room);
            newUser.setOnline(true);
            userService.save(newUser);
            return newUser;
        }
    }

    @GetMapping("/user")
    public User getUser(@Valid @RequestParam(value = "sessionID") String sessionID) {
        User user = userService.findUserBySessionID(sessionID);
        if (user != null) {
            return user;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user doesn't exist");
        }
    }

}
