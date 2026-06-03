package com.example.party_finder.service;

import com.example.party_finder.domain.Notification;
import com.example.party_finder.domain.NotificationRepository;
import com.example.party_finder.dto.NotificationResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    // 1. 프론트가 알림 목록 달라고 할 때 쏴주는 기능
    @Transactional(readOnly = true)
    public List<NotificationResponseDto> getNotifications(String nickname) {
        return notificationRepository.findByRecipientNicknameOrderByCreatedAtDesc(nickname)
                .stream()
                .map(NotificationResponseDto::new)
                .collect(Collectors.toList());
    }

    // 2. 프론트가 빨간 점(안 읽은 알림 갯수) 띄울 때 쓸 기능
    @Transactional(readOnly = true)
    public long getUnreadCount(String nickname) {
        return notificationRepository.findByRecipientNicknameOrderByCreatedAtDesc(nickname)
                .stream()
                .filter(n -> !n.isRead())
                .count();
    }

    // 3. 알림 탭 열었을 때 전부 '읽음'으로 바꾸는 기능
    @Transactional
    public void markAllAsRead(String nickname) {
        List<Notification> notifications = notificationRepository.findByRecipientNicknameOrderByCreatedAtDesc(nickname);
        for (Notification noti : notifications) {
            if (!noti.isRead()) {
                noti.markAsRead(); // 읽음 상태(true)로 변경
            }
        }
    }
}