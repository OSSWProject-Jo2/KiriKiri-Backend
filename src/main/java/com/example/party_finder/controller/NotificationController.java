package com.example.party_finder.controller;

import com.example.party_finder.dto.NotificationResponseDto;
import com.example.party_finder.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications") // 프론트가 찌를 기본 주소
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    // GET /api/notifications?nickname=유저닉네임
    @GetMapping
    public List<NotificationResponseDto> getNotifications(@RequestParam String nickname) {
        return notificationService.getNotifications(nickname);
    }

    // GET /api/notifications/unread-count?nickname=유저닉네임
    @GetMapping("/unread-count")
    public long getUnreadCount(@RequestParam String nickname) {
        return notificationService.getUnreadCount(nickname);
    }

    // PATCH /api/notifications/read?nickname=유저닉네임
    @PatchMapping("/read")
    public void markAsRead(@RequestParam String nickname) {
        notificationService.markAllAsRead(nickname);
    }
}