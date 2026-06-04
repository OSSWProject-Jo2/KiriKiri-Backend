package com.example.party_finder.dto;

import com.example.party_finder.domain.Notification;
import lombok.Getter;

import java.time.format.DateTimeFormatter;

@Getter
public class NotificationResponseDto {
    private String id;
    private String kind;
    private String postId;
    private String postTitle;
    private String recipientNickname;
    private String actorNickname;
    private String message;
    private String openChatLink;
    private boolean read;
    private String createdAt;

    public NotificationResponseDto(Notification notification) {
        this.id = "notice-" + notification.getId();
        this.kind = notification.getKind();
        this.postId = String.valueOf(notification.getPostId());
        this.postTitle = notification.getPostTitle();
        this.recipientNickname = notification.getRecipientNickname();
        this.actorNickname = notification.getActorNickname();
        this.message = notification.getMessage();
        this.openChatLink = notification.getOpenChatLink();
        this.read = notification.isRead();
        this.createdAt = notification.getCreatedAt().format(DateTimeFormatter.ISO_DATE_TIME);
    }
}