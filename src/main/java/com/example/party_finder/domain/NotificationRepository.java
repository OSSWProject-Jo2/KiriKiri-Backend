package com.example.party_finder.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // 특정 닉네임을 가진 유저의 알림만 최신순으로 싹 다 긁어오는 전용 망
    List<Notification> findByRecipientNicknameOrderByCreatedAtDesc(String recipientNickname);

    // 안읽은 알림 수만 DB에서 바로 집계 (전체 로드 없이 count 쿼리로 처리)
    long countByRecipientNicknameAndIsRead(String recipientNickname, boolean isRead);

}