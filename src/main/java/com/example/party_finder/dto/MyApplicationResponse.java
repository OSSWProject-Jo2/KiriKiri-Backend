package com.example.party_finder.dto;

import com.example.party_finder.domain.Application;
import lombok.Getter;

// 내 신청 상태 조회 응답 DTO - 수락됐을 때만 오픈채팅 링크 포함
@Getter
public class MyApplicationResponse {

    private final String status;        // "pending" 또는 "accepted" (소문자)
    private final String openChatLink;  // 수락된 경우에만 링크 반환, 대기 중이면 null

    public MyApplicationResponse(Application application) {
        this.status = application.getStatus().toLowerCase();
        // 수락된 경우에만 오픈채팅 링크 공개
        this.openChatLink = "ACCEPTED".equals(application.getStatus())
                ? application.getPost().getOpenChatLink()
                : null;
    }
}
