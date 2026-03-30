package com.example.demo.Controllers;

import com.example.demo.Payloads.SocietyChatDto;
import com.example.demo.Services.SocietyChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/society-chat")
@CrossOrigin("*")
public class SocietyChatController {

    @Autowired
    private SocietyChatService chatService;

    // Send message
    @PostMapping("/send")
    public SocietyChatDto sendMessage(@RequestBody SocietyChatDto dto) {

        return chatService.sendMessage(dto);
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
            @RequestBody SocietyChatDto dto) {

        chatService.hardDeleteMessage(societyId, messageId, dto.getSenderId());

        return "Message deleted for everyone";
    }

}