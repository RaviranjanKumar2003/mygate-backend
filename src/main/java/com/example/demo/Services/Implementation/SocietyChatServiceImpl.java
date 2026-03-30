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
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SocietyChatServiceImpl implements SocietyChatService {

    @Autowired
    private SocietyChatRepository chatRepository;

    @Autowired
    @Lazy
    private MessageReactionService reactionService;

    @Override
    public SocietyChatDto sendMessage(SocietyChatDto dto) {

        SocietyChat chat = new SocietyChat();

        chat.setSocietyId(dto.getSocietyId());
        chat.setSenderId(dto.getSenderId());
        chat.setSenderName(dto.getSenderName());
        chat.setRole(dto.getRole());
        chat.setUserType(dto.getUserType());
        chat.setMessage(dto.getMessage());
        chat.setCreatedAt(LocalDateTime.now());

        SocietyChat saved = chatRepository.save(chat);

        return new SocietyChatDto(
                saved.getId(),
                saved.getSocietyId(),
                saved.getSenderId(),
                saved.getSenderName(),
                saved.getRole(),
                saved.getUserType(),
                saved.getMessage(),
                saved.getCreatedAt()
        );
    }

    @Override
    public List<SocietyChatDto> getMessages(Integer societyId, Integer userId) {

        List<SocietyChat> chats =
                chatRepository.findBySocietyIdOrderByCreatedAtAsc(societyId);

        return chats.stream().map(chat -> {

            String messageText = chat.getMessage();

            if (chat.isDeletedForEveryone()) {
                messageText = " This message was deleted";
            }

            if (chat.getDeletedForUsers().contains(userId)) {
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

            // ⭐ ADD THIS (REACTIONS)
            dto.setReactions(
                    reactionService.getReactions(chat.getId())
            );

            return dto;

        }).collect(Collectors.toList());
    }

    // UPDATE MESSAGE (ONLY SENDER)
    @Override
    public SocietyChatDto updateMessage(Integer societyId,
                                        Integer messageId,
                                        Integer senderId,
                                        String newMessage) {

        SocietyChat chat = chatRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));

        // Check society
        if (!chat.getSocietyId().equals(societyId)) {
            throw new RuntimeException("Message does not belong to this society");
        }

        // Check sender
        if (!chat.getSenderId().equals(senderId)) {
            throw new RuntimeException("You can update only your message");
        }

        // Prevent editing deleted message
        if (chat.isDeletedForEveryone()) {
            throw new RuntimeException("Deleted message cannot be edited");
        }

        chat.setMessage(newMessage);

        SocietyChat updatedChat = chatRepository.save(chat);

        return mapToDto(updatedChat);
    }

    // SOFT DELETE
    @Override
    public void softDeleteMessage(Integer societyId, Integer messageId, Integer userId) {

        SocietyChat chat = chatRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));

        if(!chat.getSocietyId().equals(societyId)){
            throw new RuntimeException("Invalid society message");
        }

        List<Integer> deletedUsers = chat.getDeletedForUsers();

        if(!deletedUsers.contains(userId)){
            deletedUsers.add(userId);
        }

        chat.setDeletedForUsers(deletedUsers);

        chatRepository.save(chat);
    }

    // HARD DELETE
    @Override
    public void hardDeleteMessage(Integer societyId, Integer messageId, Integer senderId) {

        SocietyChat chat = chatRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));

        // Society validation
        if (!chat.getSocietyId().equals(societyId)) {
            throw new RuntimeException("Invalid society message");
        }

        // Sender validation
        if (!chat.getSenderId().equals(senderId)) {
            throw new RuntimeException("Only sender can delete message for everyone");
        }

        chat.setDeletedForEveryone(true);

        chatRepository.save(chat);
    }

    // DTO MAPPER
    private SocietyChatDto mapToDto(SocietyChat chat) {

        SocietyChatDto dto = new SocietyChatDto();

        dto.setSocietyId(chat.getSocietyId());
        dto.setSenderId(chat.getSenderId());
        dto.setSenderName(chat.getSenderName());
        dto.setRole(chat.getRole());
        dto.setUserType(chat.getUserType());
        dto.setMessage(chat.getMessage());
        dto.setCreatedAt(chat.getCreatedAt());

        return dto;
    }
}