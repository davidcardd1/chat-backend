package davidcardd.chatAppbackend.service;

import davidcardd.chatAppbackend.model.Message;
import davidcardd.chatAppbackend.model.User;
import davidcardd.chatAppbackend.repository.MessageRepository;
import org.springframework.aop.target.LazyInitTargetSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }
    public Message save(Message toSave) { return messageRepository.save(toSave);}

    public List<Message> findAllBySenderAndReceiver(User a, User b) {
        List<User> users = new ArrayList<>();
        users.add(a);
        users.add(b);
        return messageRepository.findAllBySenderInAndReceiverIn(users, users);
    };

    public Integer countAllUnseen(User sender, User receiver) {
        return messageRepository.countAllBySenderAndReceiverAndSeen(sender, receiver, false);
    }

    public Message sendMessage(User sender, User receiver, String body) {
        if (sender.getRoom().getId().equals(receiver.getRoom().getId())) {
            Message message = new Message();
            message.setSender(sender);
            message.setReceiver(receiver);
            message.setBody(body);
            message.setTimeSent(LocalDateTime.now());
            return messageRepository.save(message);
        }
        return null;
    }

}
