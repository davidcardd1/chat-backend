package davidcardd.chatAppbackend.controller;

import davidcardd.chatAppbackend.model.Message;
import davidcardd.chatAppbackend.model.User;
import davidcardd.chatAppbackend.service.MessageService;
import davidcardd.chatAppbackend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@CrossOrigin
public class MessagingController {

    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private SimpMessagingTemplate template;

    @PostMapping("/messages/send/{receiverName}")
    public Message sendMessage(@Valid @RequestBody User sender, @Valid @PathVariable String receiverName, @Valid @RequestParam String body) {
        User send = userService.findUserBySessionID(sender.getSessionID());
        User receiver = userService.findUserByNicknameAndRoom(receiverName, send.getRoom());

        if (body != null && !body.isBlank()) {
            return messageService.sendMessage(send, receiver, body);
        }
        return null;
    }

    @GetMapping("/messages/{userName}")
    public List<Message> getMessages(@Valid @RequestBody User auth, @PathVariable("userName") String userName) {
        User receiver = userService.findUserBySessionID(auth.getSessionID());

        if (receiver == null) { throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "no auth");}
        User sender = userService.findUserByNicknameAndRoom(userName, receiver.getRoom());
        if (receiver == null) { throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "other user incorrect");}
        return messageService.findAllBySenderAndReceiver(auth, sender);
    }

    @MessageMapping("/status")
    @SendTo("/chatroom/public")
    public Message sendPublic(@Payload Message message) {
        return message;
    }

    @MessageMapping("/message")
    public Message sendSpecific(@Payload Message msg, Principal user, @Header("simpSessionId") String sessionID) throws Exception {
        msg.setTimeSent(LocalDateTime.now());
        template.convertAndSendToUser(msg.getReceiver().getSessionID(), "/specific-user", msg);
        return messageService.save(msg);
    }



    public Message sendMsg(String body, String receiverName, User auth) throws Exception {
        User sender = userService.findUserBySessionID(auth.getSessionID());
        User receiver = userService.findUserByNicknameAndRoom(receiverName, sender.getRoom());

        if (body != null && !body.isBlank()) {
            return messageService.sendMessage(sender, receiver, body);
        }
        return null;
    }


}
