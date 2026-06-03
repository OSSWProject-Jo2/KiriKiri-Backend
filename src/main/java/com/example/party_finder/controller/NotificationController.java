package com.example.party_finder.controller;

import com.example.party_finder.dto.NotificationResponseDto;
import com.example.party_finder.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    // 내 알림 목록 조회 - JWT에서 닉네임 추출하여 본인 알림만 반환
    // GET /api/notifications
    @GetMapping
    public List<NotificationResponseDto> getNotifications(@AuthenticationPrincipal Jwt jwt) {
        String nickname = jwt.getClaim("nickname");
        return notificationService.getNotifications(nickname);
    }

    // 안읽은 알림 수 조회 - JWT 기반 본인 확인
    // GET /api/notifications/unread-count
    @GetMapping("/unread-count")
    public long getUnreadCount(@AuthenticationPrincipal Jwt jwt) {
        String nickname = jwt.getClaim("nickname");
        return notificationService.getUnreadCount(nickname);
    }

    // 전체 읽음 처리 - JWT 기반 본인 확인
    // PATCH /api/notifications/read
    @PatchMapping("/read")
    public void markAsRead(@AuthenticationPrincipal Jwt jwt) {
        String nickname = jwt.getClaim("nickname");
        notificationService.markAllAsRead(nickname);
    }
}
