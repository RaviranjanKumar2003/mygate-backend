package com.example.demo.Controllers;

import com.example.demo.Payloads.ReactionDto;
import com.example.demo.Repositories.MessageReactionRepository;
import com.example.demo.Services.MessageReactionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reactions")
@CrossOrigin("*")
public class MessageReactionController {

    @Autowired
    private MessageReactionService messageReactionService;

    @Autowired
    private MessageReactionRepository messageReactionRepository;


    /* ================= ADD / TOGGLE REACTION ================= */

    @PostMapping("/react")
    public String react(@RequestBody ReactionDto dto) {

        messageReactionService.toggleReaction(
                dto.getMessageId(),
                dto.getUserId(),
                dto.getEmoji()
        );

        return "Reaction updated";
    }


    /* ================= GET REACTIONS ================= */

    @GetMapping("/{messageId}")
    public Map<String, Long> getReactions(
            @PathVariable Integer messageId
    ) {

        return messageReactionService.getReactions(messageId);

    }


    @GetMapping("/users/{messageId}")
    public ResponseEntity<List<ReactionDto>> getReactionUsers(
            @PathVariable Integer messageId
    ){

        return ResponseEntity.ok(
                messageReactionService.getReactionUsers(messageId)
        );

    }



    @DeleteMapping("/remove/{messageId}/{userId}")
    public void removeReaction(
            @PathVariable Integer messageId,
            @PathVariable Integer userId
    ){
        messageReactionService.removeReaction(messageId,userId);
    }

    @PostMapping("/toggle")
    public void toggleReaction(@RequestBody ReactionDto dto){

        messageReactionService.toggleReaction(
                dto.getMessageId(),
                dto.getUserId(),
                dto.getEmoji()
        );

    }

}