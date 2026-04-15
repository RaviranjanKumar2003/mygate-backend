package com.example.demo.Repositories;

import com.example.demo.Entities.NoticeSeen;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoticeSeenRepository extends JpaRepository<NoticeSeen, Integer> {

    boolean existsByNoticeIdAndUserId(Integer noticeId, Integer userId);

    List<NoticeSeen> findByNoticeId(Integer noticeId);

    @Transactional
    void deleteByNoticeId(Integer noticeId);

    boolean existsByNoticeIdAndUserIdAndUserRole(Integer noticeId, Integer userId, String userRole);
}