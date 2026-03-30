package com.example.demo.Repositories;

import com.example.demo.Entities.DeletedMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeletedMessageRepository
        extends JpaRepository<DeletedMessage, Integer> {

    boolean existsByMessageIdAndUserId(Integer messageId, Integer userId);

    List<DeletedMessage> findByUserId(Integer userId);
}