package com.example.demo.Controllers;

import com.example.demo.Payloads.SocietyChatDto;
import com.example.demo.Services.SocietyChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatWebSocketController {

    @Autowired
    private SocietyChatService chatService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/send-message")
    public void sendMessage(SocietyChatDto dto) {

        SocietyChatDto saved = chatService.sendMessage(dto);

        messagingTemplate.convertAndSend(
                "/topic/society-chat-" + dto.getSocietyId(),
                saved
        );
    }
}