package com.example.demo.Controllers;

import com.example.demo.Entities.NoticeSeen;
import com.example.demo.Payloads.NoticeSeenDto;
import com.example.demo.Services.NoticeSeenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notice-seen")
public class NoticeSeenController {

    @Autowired
    private NoticeSeenService noticeSeenService;

    // ✅ MARK AS SEEN
    @PostMapping("/{noticeId}/seen")
    public ResponseEntity<?> markSeen(
            @PathVariable Integer noticeId,   // 🔥 Integer → Long
            @RequestParam Integer userId,     // 🔥 Integer → Long
            @RequestParam String role
    ) {
        noticeSeenService.markAsSeen(noticeId, userId, role);
        return ResponseEntity.ok("Seen marked successfully");
    }

    // ✅ GET SEEN USERS
    @GetMapping("/{noticeId}/seen-users")
    public ResponseEntity<List<NoticeSeenDto>> getSeenUsers(
            @PathVariable Integer noticeId
    ) {
        return ResponseEntity.ok(
                noticeSeenService.getSeenUsers(noticeId)
        );
    }
}