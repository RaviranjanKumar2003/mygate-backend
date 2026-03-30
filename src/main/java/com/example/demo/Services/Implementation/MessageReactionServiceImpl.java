package com.example.demo.Services.Implementation;

import com.example.demo.Entities.MessageReaction;
import com.example.demo.Entities.User;
import com.example.demo.Payloads.ReactionDto;
import com.example.demo.Repositories.MessageReactionRepository;
import com.example.demo.Repositories.UserRepository;
import com.example.demo.Services.MessageReactionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MessageReactionServiceImpl implements MessageReactionService {

    @Autowired
    private MessageReactionRepository reactionRepository;

    @Autowired
    private UserRepository userRepository;


    /* ================= TOGGLE REACTION ================= */

    @Override
    public void toggleReaction(Integer messageId, Integer userId, String emoji) {

        Optional<MessageReaction> existing =
                reactionRepository.findByMessageIdAndUserId(messageId, userId);

        if (existing.isPresent()) {

            MessageReaction reaction = existing.get();

            // Same emoji -> remove reaction
            if (reaction.getEmoji().equals(emoji)) {

                reactionRepository.delete(reaction);

            } else {

                // Change emoji
                reaction.setEmoji(emoji);
                reactionRepository.save(reaction);
            }

        } else {

            // New reaction
            MessageReaction reaction = new MessageReaction();

            reaction.setMessageId(messageId);
            reaction.setUserId(userId);
            reaction.setEmoji(emoji);

            reactionRepository.save(reaction);
        }
    }

    /* ================= GET REACTIONS ================= */

    @Override
    public Map<String, Long> getReactions(Integer messageId) {

        List<MessageReaction> reactions =
                reactionRepository.findByMessageId(messageId);

        return reactions.stream()
                .collect(Collectors.groupingBy(
                        MessageReaction::getEmoji,
                        Collectors.counting()
                ));
    }

    @Override
    public List<ReactionDto> getReactionUsers(Integer messageId) {

        List<MessageReaction> reactions =
                reactionRepository.findByMessageId(messageId);

        return reactions.stream().map(r -> {

            ReactionDto dto = new ReactionDto();

            User user = userRepository.findById(r.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            dto.setUserId(user.getId());
            dto.setUserName(user.getName());   // ⭐ user name
            dto.setEmoji(r.getEmoji());
            dto.setMessageId(messageId);

            return dto;

        }).collect(Collectors.toList());
    }

    @Override
    public void removeReaction(Integer messageId, Integer userId) {

        Optional<MessageReaction> reaction =
                reactionRepository.findByMessageIdAndUserId(messageId,userId);

        reaction.ifPresent(reactionRepository::delete);
    }
}