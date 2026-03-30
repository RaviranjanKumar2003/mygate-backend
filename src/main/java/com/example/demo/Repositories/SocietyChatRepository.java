package com.example.demo.Repositories;

import com.example.demo.Entities.SocietyChat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SocietyChatRepository extends JpaRepository<SocietyChat, Integer> {

    List<SocietyChat> findBySocietyIdOrderByCreatedAtAsc(Integer societyId);

}