package davidcardd.chatAppbackend.controller;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import davidcardd.chatAppbackend.model.Message;
import davidcardd.chatAppbackend.model.User;
import davidcardd.chatAppbackend.service.MessageService;
import davidcardd.chatAppbackend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.*;
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
    public List<Message> getMessages(@Valid @RequestParam String sessionID, @PathVariable("userName") String userName) {
        User receiver = userService.findUserBySessionID(sessionID);

        if (receiver == null) { throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "no auth");}
        User sender = userService.findUserByNicknameAndRoom(userName, receiver.getRoom());
        if (receiver == null) { throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "other user incorrect");}
        return messageService.findAllBySenderAndReceiver(receiver, sender);
    }

    @MessageMapping("/status")
    @SendTo("/chatroom/public")
    public Message sendPublic(@Payload Message msg) {
        return msg;
    }

    @MessageMapping("/chat/{to}")
    public Message sendSpecific(@Payload String msg, @DestinationVariable String to) throws Exception {
        System.out.println("handling send from: " + msg);
        JsonObject message = new JsonParser().parse(msg).getAsJsonObject();

        User sender = userService.findUserBySessionID(message.get("sender").getAsString());
        User receiver = userService.findUserByNicknameAndRoom(message.get("receiver").getAsString(), sender.getRoom());
        if (sender != null && receiver != null && !message.get("body").getAsString().trim().isBlank()) {
            Message sent = new Message();
            sent.setTimeSent(LocalDateTime.now());
            sent.setSender(sender);
            sent.setReceiver(receiver);
            sent.setBody(message.get("body").getAsString());
            template.convertAndSend("/user/messages/" + to, sent);
            return messageService.save(sent);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "missing valid sender, receiver or non-empty body");
        }
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
