package com.example.demo.Services;

import com.example.demo.Payloads.ReactionDto;

import java.util.List;
import java.util.Map;

public interface MessageReactionService {

    void toggleReaction(Integer messageId, Integer userId, String emoji);

    Map<String, Long> getReactions(Integer messageId);

    List<ReactionDto> getReactionUsers(Integer messageId);

    void removeReaction(Integer messageId, Integer userId);

}