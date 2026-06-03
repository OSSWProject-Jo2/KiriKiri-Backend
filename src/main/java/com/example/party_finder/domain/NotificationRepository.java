package com.example.party_finder.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // 특정 닉네임을 가진 유저의 알림만 최신순으로 싹 다 긁어오는 전용 망
    List<Notification> findByRecipientNicknameOrderByCreatedAtDesc(String recipientNickname);

}