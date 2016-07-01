package com.github.coderoute.messages;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Pattern;
import java.util.UUID;

@Validated
public class InputMessage {

    @NotBlank
    private UUID sender;

    @NotBlank
    private UUID receiver;

    @Pattern(regexp = "^[A..Z]*$", message ="Must contain only uppercase letters")
    private String message;

    public UUID getSender() {
        return sender;
    }

    public void setSender(UUID sender) {
        this.sender = sender;
    }

    public UUID getReceiver() {
        return receiver;
    }

    public void setReceiver(UUID receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
