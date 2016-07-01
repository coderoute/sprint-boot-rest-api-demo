package com.github.coderoute.messages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class MessagesController {

    @Autowired
    private MessageService messageService;

    @RequestMapping(value = "/messages", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody InputMessage inputMessage) {
        messageService.create(inputMessage);
    }

    @RequestMapping(value = "/messages/users/{user-uuid}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.CREATED)
    public List<OutputMessage> getUserMessages(@PathVariable("user-uuid") UUID userUUID) {
        return messageService.getMessagesFor(userUUID);
    }


}
