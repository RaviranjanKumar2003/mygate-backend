package com.example.demo.Services.Implementation;

import com.example.demo.Entities.SocietyChat;
import com.example.demo.Payloads.SocietyChatDto;
import com.example.demo.Repositories.SocietyChatRepository;
import com.example.demo.Services.MessageReactionService;
import com.example.demo.Services.SocietyChatService;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SocietyChatServiceImpl implements SocietyChatService {

    @Autowired
    private SocietyChatRepository chatRepository;

    @Autowired
    @Lazy
    private MessageReactionService reactionService;

    // ✅ SEND MESSAGE
    @Override
    public SocietyChatDto sendMessage(SocietyChatDto dto) {

        if ((dto.getMessage() == null || dto.getMessage().trim().isEmpty())
                && dto.getFileType() == null) {
            throw new RuntimeException("Message or file required");
        }

        SocietyChat chat = new SocietyChat();

        chat.setSocietyId(dto.getSocietyId());
        chat.setSenderId(dto.getSenderId());
        chat.setSenderName(dto.getSenderName());
        chat.setRole(dto.getRole());
        chat.setUserType(dto.getUserType());
        chat.setMessage(
                dto.getMessage() != null ? dto.getMessage().trim() : null
        );
        chat.setFileType(dto.getFileType());   // ⭐ ADD THIS
        chat.setCreatedAt(
                ZonedDateTime.now(ZoneId.of("Asia/Kolkata")).toLocalDateTime()
        );
        chat.setSeenByUsers(new HashSet<>());
        chat.setDeletedForUsers(new ArrayList<>());
        chat.setDeletedForEveryone(false);

        SocietyChat saved = chatRepository.save(chat);
        System.out.println("✅ After save ID: " + chat.getId());

        return mapToDto(saved);
    }

    // ✅ GET MESSAGES
    @Override
    public List<SocietyChatDto> getMessages(Integer societyId, Integer userId) {

        List<SocietyChat> chats =
                chatRepository.findBySocietyIdOrderByCreatedAtAsc(societyId);

        List<SocietyChat> updatedChats = new ArrayList<>();

        List<SocietyChatDto> result = chats.stream().map(chat -> {

            // Initialize sets if null
            if (chat.getSeenByUsers() == null) {
                chat.setSeenByUsers(new HashSet<>());
            }

            if (chat.getDeletedForUsers() == null) {
                chat.setDeletedForUsers(new ArrayList<>());
            }

            // Mark as seen
            if (!chat.getSenderId().equals(userId)) {
                if (chat.getSeenByUsers().add(userId)) {
                    updatedChats.add(chat);
                }
            }

            // Handle deleted messages
            String messageText = chat.getMessage();

            if (messageText == null) {
                messageText = "";
            }

            if (chat.isDeletedForEveryone() ||
                    chat.getDeletedForUsers().contains(userId)) {
                messageText = "This message was deleted";
            }

            SocietyChatDto dto = new SocietyChatDto(
                    chat.getId(),
                    chat.getSocietyId(),
                    chat.getSenderId(),
                    chat.getSenderName(),
                    chat.getRole(),
                    chat.getUserType(),
                    messageText,
                    chat.getCreatedAt()
            );
            dto.setFileType(chat.getFileType());   // ⭐ IMPORTANT

            // Add reactions
            dto.setReactions(
                    reactionService.getReactions(chat.getId())
            );

            // Correct seen logic
            dto.setSeen(chat.getSeenByUsers().contains(userId));

            return dto;

        }).collect(Collectors.toList());

        // Save only updated chats
        if (!updatedChats.isEmpty()) {
            chatRepository.saveAll(updatedChats);
        }

        return result;
    }

    // ✅ UPDATE MESSAGE (ONLY SENDER)
    @Override
    public SocietyChatDto updateMessage(Integer societyId,
                                        Integer messageId,
                                        Integer senderId,
                                        String newMessage) {

        if (newMessage == null || newMessage.trim().isEmpty()) {
            throw new RuntimeException("Message cannot be empty");
        }

        SocietyChat chat = chatRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));

        if (!chat.getSocietyId().equals(societyId)) {
            throw new RuntimeException("Message does not belong to this society");
        }

        if (!chat.getSenderId().equals(senderId)) {
            throw new RuntimeException("You can update only your message");
        }

        if (chat.isDeletedForEveryone()) {
            throw new RuntimeException("Deleted message cannot be edited");
        }

        chat.setMessage(newMessage.trim());

        SocietyChat updatedChat = chatRepository.save(chat);

        return mapToDto(updatedChat);
    }

    // ✅ SOFT DELETE (FOR USER ONLY)
    @Override
    public void softDeleteMessage(Integer societyId, Integer messageId, Integer userId) {

        SocietyChat chat = chatRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));

        if (!chat.getSocietyId().equals(societyId)) {
            throw new RuntimeException("Invalid society message");
        }

        if (chat.getDeletedForUsers() == null) {
            chat.setDeletedForUsers(new ArrayList<>());
        }

        if (!chat.getDeletedForUsers().contains(userId)) {
            chat.getDeletedForUsers().add(userId);
        }

        chatRepository.save(chat);
    }

    @Override
    public void hardDeleteMessage(Integer societyId, Integer messageId, Integer senderId) {
        SocietyChat chat = chatRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));

        if (!chat.getSocietyId().equals(societyId)) {
            throw new RuntimeException("Invalid society message");
        }

        if (!chat.getSenderId().equals(senderId)) {
            throw new RuntimeException("Only sender can delete message for everyone");
        }

        chat.setDeletedForEveryone(true);
        chat.setMessage(null);
        chat.setFileType(null);

        chatRepository.save(chat);
    }



    // ✅ MARK AS SEEN (BULK)
    @Override
    public void markMessagesAsSeen(Integer societyId, Integer userId) {

        List<SocietyChat> messages = chatRepository.findBySocietyIdOrderByCreatedAtAsc(societyId);

        List<SocietyChat> updatedMessages = new ArrayList<>();

        for (SocietyChat msg : messages) {

            if (msg.getSenderId().equals(userId)) continue;

            if (msg.getSeenByUsers() == null) {
                msg.setSeenByUsers(new HashSet<>());
            }

            if (msg.getSeenByUsers().add(userId)) {
                updatedMessages.add(msg);
            }
        }

        if (!updatedMessages.isEmpty()) {
            chatRepository.saveAll(updatedMessages);
        }
    }

    // ✅ DTO MAPPER
    private SocietyChatDto mapToDto(SocietyChat chat) {

        SocietyChatDto dto = new SocietyChatDto();

        dto.setId(chat.getId()); // IMPORTANT FIX
        dto.setSocietyId(chat.getSocietyId());
        dto.setSenderId(chat.getSenderId());
        dto.setSenderName(chat.getSenderName());
        dto.setRole(chat.getRole());
        dto.setUserType(chat.getUserType());
        dto.setMessage(chat.getMessage());
        dto.setCreatedAt(chat.getCreatedAt());
        dto.setFileType(chat.getFileType());

        return dto;
    }
}