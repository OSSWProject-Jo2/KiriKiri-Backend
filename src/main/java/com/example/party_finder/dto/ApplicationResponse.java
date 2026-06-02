package com.example.party_finder.dto;

import com.example.party_finder.domain.Application;
import lombok.Getter;

import java.time.format.DateTimeFormatter;

// 신청자 목록 조회 응답 DTO - 프론트엔드 Applicant 타입과 필드명/형식을 맞춤
@Getter
public class ApplicationResponse {

    private final String id;        // Long → String 변환 (프론트 타입이 string)
    private final String nickname;
    private final String status;    // "PENDING"/"ACCEPTED" → "pending"/"accepted" 소문자 변환
    private final String createdAt;

    public ApplicationResponse(Application application) {
        this.id = String.valueOf(application.getId());
        this.nickname = application.getNickname();
        // 프론트엔드가 "pending" | "accepted" 소문자를 기대하므로 소문자로 변환
        this.status = application.getStatus().toLowerCase();
        this.createdAt = application.getCreatedAt() != null
                ? application.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
                : "";
    }
}
