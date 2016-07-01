package com.github.coderoute.messages;

import com.github.coderoute.ResourceNotFoundException;
import com.github.coderoute.users.UserEntity;
import com.github.coderoute.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    public void create(InputMessage inputMessage) {
        MessageEntity messageEntity = new MessageEntity();
        UserEntity sender = userRepository.findOne(inputMessage.getSender());
        UserEntity receiver = userRepository.findOne(inputMessage.getReceiver());

        if(sender == null || receiver == null) {
            throw new InvalidMessageException("Unknown sender / recipient");
        }
        messageEntity.setSender(sender);
        messageEntity.setReceiver(receiver);
        messageEntity.setMessage(inputMessage.getMessage());
        messageEntity.setUuid(UUID.randomUUID());

        messageRepository.save(messageEntity);
    }

    public List<OutputMessage> getMessagesFor(UUID userUUID) {
        UserEntity user = userRepository.findOne(userUUID);
        if(user == null) {
            throw new ResourceNotFoundException("No such user: " + userUUID);
        }

        List<MessageEntity> userMessages = user.getReceivedMessages();
        return userMessages.stream().map(m -> convertToDisplayMessage(m)).collect(Collectors.toList());
    }

    private OutputMessage convertToDisplayMessage(MessageEntity messageEntity) {
        OutputMessage outputMessage = new OutputMessage();
        outputMessage.setSender(messageEntity.getSender().getName());
        outputMessage.setReceiver(messageEntity.getReceiver().getName());
        outputMessage.setMessage(messageEntity.getMessage());
        return outputMessage;
    }
}
