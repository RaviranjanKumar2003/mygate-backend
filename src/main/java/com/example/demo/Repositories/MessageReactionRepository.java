package com.example.demo.Repositories;

import com.example.demo.Entities.MessageReaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MessageReactionRepository
        extends JpaRepository<MessageReaction,Integer> {

    Optional<MessageReaction> findByMessageIdAndUserId(
            Integer messageId,
            Integer userId
    );

    List<MessageReaction> findByMessageId(Integer messageId);


}