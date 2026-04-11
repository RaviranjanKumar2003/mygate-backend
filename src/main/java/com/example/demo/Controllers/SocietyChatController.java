package com.example.demo.Controllers;

import com.example.demo.Payloads.SocietyChatDto;
import com.example.demo.Services.SocietyChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/society-chat")
@CrossOrigin("*")
public class SocietyChatController {

    @Autowired
    private SocietyChatService chatService;

    // Send message
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.send")
    public void sendMessage(SocietyChatDto dto) {

        SocietyChatDto saved = chatService.sendMessage(dto);

        // ⭐ send tempId back
        saved.setTempId(dto.getTempId());

        messagingTemplate.convertAndSend(
                "/topic/messages/" + dto.getSocietyId(),
                saved
        );
    }

    // Get all messages
    @GetMapping("/society/{societyId}/{userId}")
    public List<SocietyChatDto> getMessages(
            @PathVariable Integer societyId,
            @PathVariable Integer userId
    ) {
        return chatService.getMessages(societyId, userId);
    }

    // UPDATE MESSAGE
    @PutMapping("/society/{societyId}/update/{messageId}")
    public SocietyChatDto updateMessage(
            @PathVariable Integer societyId,
            @PathVariable Integer messageId,
            @RequestBody SocietyChatDto dto) {

        return chatService.updateMessage(
                societyId,
                messageId,
                dto.getSenderId(),
                dto.getMessage()
        );
    }

    // SOFT DELETE
    @PutMapping("/society/{societyId}/soft-delete/{messageId}")
    public String softDelete(
            @PathVariable Integer societyId,
            @PathVariable Integer messageId,
            @RequestBody SocietyChatDto dto) {

        chatService.softDeleteMessage(societyId, messageId, dto.getSenderId());

        return "Message deleted for me";
    }

    // HARD DELETE
    @DeleteMapping("/society/{societyId}/hard-delete/{messageId}")
    public String hardDelete(
            @PathVariable Integer societyId,
            @PathVariable Integer messageId,
            @RequestParam Integer senderId) {   // ✅ FIX

        chatService.hardDeleteMessage(societyId, messageId, senderId);

        return "Message deleted for everyone";
    }


    @PostMapping("/seen/{societyId}/{userId}")
    public void markSeen(@PathVariable Integer societyId,
                         @PathVariable Integer userId) {

        chatService.markMessagesAsSeen(societyId, userId);
    }


}