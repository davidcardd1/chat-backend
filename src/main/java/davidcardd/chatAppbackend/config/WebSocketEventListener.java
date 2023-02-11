package davidcardd.chatAppbackend.config;

import davidcardd.chatAppbackend.model.Message;
import davidcardd.chatAppbackend.model.User;
import davidcardd.chatAppbackend.service.UserService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@Log
public class WebSocketEventListener {

    @Autowired
    private SimpMessageSendingOperations template;

    @Autowired
    private UserService userService;

    @EventListener
    public void handleConnect(SessionConnectedEvent event) {
        log.info("new connection");
    }

    @EventListener
    public void handleDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String user = (String) headerAccessor.getSessionAttributes().get("username");

        if (user != null) {

            log.info("User: " + user + "disconnected");
            User sender = userService.findUserBySessionID(user);

            Message message = new Message();
            message.setType(Message.MessageType.LEAVE);
            message.setSender(sender);

            template.convertAndSend("/topic/public", message);
        }
    }
}
