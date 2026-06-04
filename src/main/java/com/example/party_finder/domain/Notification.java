package com.example.party_finder.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 알림 종류: "application", "accepted", "deleted"
    @Column(nullable = false)
    private String kind;

    @Column(nullable = false)
    private Long postId;

    @Column(nullable = false)
    private String postTitle;

    @Column(nullable = false)
    private String recipientNickname; // 알림 받는 사람

    @Column(nullable = false)
    private String actorNickname;     // 알림 원인 제공자 (신청자 또는 수락자)

    @Column(nullable = false)
    private String message;

    private String openChatLink;      // 수락 시에만 들어오는 카톡 링크

    @Column(nullable = false)
    private boolean isRead;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Builder
    public Notification(String kind, Long postId, String postTitle, String recipientNickname,
                        String actorNickname, String message, String openChatLink) {
        this.kind = kind;
        this.postId = postId;
        this.postTitle = postTitle;
        this.recipientNickname = recipientNickname;
        this.actorNickname = actorNickname;
        this.message = message;
        this.openChatLink = openChatLink;
        this.isRead = false; // 새 알림은 무조건 안 읽음(false) 상태로 꽂아 넣음
        this.createdAt = LocalDateTime.now();
    }

    // 알림 읽음 처리 스위치
    public void markAsRead() {
        this.isRead = true;
    }
}