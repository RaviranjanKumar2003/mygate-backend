package com.example.demo.Controllers;

import com.example.demo.Entities.CallMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class CallController {

    private final SimpMessagingTemplate messagingTemplate;

    public CallController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/start-call")
    public void startCall(CallMessage message) {
        System.out.println("Incoming Call: " + message.getRoomName() + ", " + message.getCallerName() + ", " + message.getType());

        messagingTemplate.convertAndSend(
                "/topic/incoming-call",
                message
        );

    }
}