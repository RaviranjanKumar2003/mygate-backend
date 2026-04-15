package com.example.demo.Services;

import com.example.demo.Payloads.NoticeSeenDto;

import java.util.List;

public interface NoticeSeenService {

    void markAsSeen(Integer noticeId, Integer userId, String role);

    List<NoticeSeenDto> getSeenUsers(Integer noticeId);

}
