package com.example.party_finder.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter // 1. 데이터를 쉽게 넣고 뺄 수 있게 롬복(Lombok) 도구를 쓴다.
public class PostResponse {

    // 2. 프론트엔드의 타입(Type) 선언과 이름, 자료형을 똑같이 맞춘다.
    private String id;           // 글 번호 (프론트에서 string으로 원해서 String 처리한다)
    private String category;     // "게임" 또는 "공부"
    private String title;        // 제목
    private String description;  // 본문 (엔티티의 content 역할)
    private String author;       // 작성자 별명
    private String authorTier;   // 작성자 티어
    private int currentMembers;  // 현재 인원
    private int maxMembers;      // 최대 인원
    private String targetScore;  // 목표 점수/티어
    private String createdAt;    // 작성일 ("2026.05.06" 형태의 문자열)
    private String openChatLink; // 오픈채팅 링크
    private String gameName;     // 게임 이름 (선택 사항)
    private String studyName;    // 스터디 이름 (선택 사항)
    private String userId; // 프론트엔드에 전달할 회원 식별 ID 추가
}
