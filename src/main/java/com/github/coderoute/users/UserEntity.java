package com.github.coderoute.users;

import com.github.coderoute.messages.MessageEntity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "user")
public class UserEntity {

    @Id
    private UUID uuid;
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "receiver", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MessageEntity> receivedMessages;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MessageEntity> sentMessages;

    public UserEntity() {
        // used by JPA during query
    }

    public UserEntity(String name) {
        uuid = UUID.randomUUID();
        this.name = name;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public List<MessageEntity> getReceivedMessages() {
        return receivedMessages;
    }

    public List<MessageEntity> getSentMessages() {
        return sentMessages;
    }

}
