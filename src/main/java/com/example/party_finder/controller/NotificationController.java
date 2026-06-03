package com.example.party_finder.controller;

import com.example.party_finder.dto.NotificationResponseDto;
import com.example.party_finder.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    // 내 알림 목록 조회 - 닉네임 기반 조회 (MVP)
    // GET /api/notifications?nickname=유저닉네임
    @GetMapping
    public List<NotificationResponseDto> getNotifications(@RequestParam String nickname) {
        return notificationService.getNotifications(nickname);
    }

    // 안읽은 알림 수 조회
    // GET /api/notifications/unread-count?nickname=유저닉네임
    @GetMapping("/unread-count")
    public long getUnreadCount(@RequestParam String nickname) {
        return notificationService.getUnreadCount(nickname);
    }

    // 전체 읽음 처리
    // PATCH /api/notifications/read?nickname=유저닉네임
    @PatchMapping("/read")
    public void markAsRead(@RequestParam String nickname) {
        notificationService.markAllAsRead(nickname);
    }
}
