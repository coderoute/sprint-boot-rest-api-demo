package com.github.coderoute.messages;

import com.github.coderoute.users.UserEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "message")
public class MessageEntity {

    @Id
    private UUID uuid;

    @JoinColumn(name = "sender_uuid", nullable = false)
    @ManyToOne
    private UserEntity sender;

    @JoinColumn(name = "receiver_uuid", nullable = false)
    @ManyToOne
    private UserEntity receiver;

    @Column(name = "message", nullable = false)
    private String message;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public UserEntity getSender() {
        return sender;
    }

    public void setSender(UserEntity sender) {
        this.sender = sender;
    }

    public UserEntity getReceiver() {
        return receiver;
    }

    public void setReceiver(UserEntity receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
