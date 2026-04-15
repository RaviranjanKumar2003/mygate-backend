package com.example.demo.Services;

import com.example.demo.Payloads.SocietyChatDto;

import java.util.List;
import java.util.Map;

public interface SocietyChatService {

    // SEND MESSAGE
    SocietyChatDto sendMessage(SocietyChatDto dto);

    // GET ALL MESSAGES
    List<SocietyChatDto> getMessages(Integer societyId,Integer userId);

    // UPDATE MESSAGE (ONLY SENDER)
    SocietyChatDto updateMessage(Integer societyId, Integer messageId, Integer senderId, String newMessage);

    // SOFT DELETE (ONLY SENDER)
    void softDeleteMessage(Integer societyId, Integer messageId, Integer senderId);

    // HARD DELETE (ONLY SENDER)
    void hardDeleteMessage(Integer societyId, Integer messageId, Integer senderId);

    // MARK AS SEEN
    void markMessagesAsSeen(Integer societyId, Integer userId);

    List<Map<String, Object>> getSeenUsers(Integer messageId);

}