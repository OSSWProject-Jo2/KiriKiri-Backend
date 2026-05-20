package com.example.party_finder.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;

@Entity // 이 클래스는 DB의 테이블과 1:1로 매핑되는 '엔티티'임을 선언
@Getter @Setter // 필드에 대한 Getter/Setter를 자동으로 생성
@EntityListeners(AuditingEntityListener.class) // DB에 저장될 때 "시간 기록" 이벤트를 감지함
public class Post {

    @Id // 이 필드를 테이블의 기본키로 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 번호 자동 증가 (1, 2, 3...)
    private Long id; // 게시글의 고유 번호

    @Column(nullable = false) // DB 칸을 만들 때 '비어있으면 안 됨(NOT NULL)' 규칙 적용
    private String title; // 게시글 제목

    // 프론트엔드 명칭에 맞춰서 content를 description으로 변경한다.
    @Column(columnDefinition = "TEXT", nullable = false)
    private String description; // 게시글 본문 내용

    // --- 비회원을 위한 필수 정보 (기존 유지) ---
    @Column(nullable = false)
    private String author; // 작성자 이름 (유동닉 닉네임)

    @Column(nullable = false)
    private String password; // 수정 및 삭제를 위한 비밀번호

    // --- 프론트엔드 연동을 위해 추가된 필드들 ---
    @Column(nullable = false)
    private String category; // "게임" 또는 "공부"

    private String authorTier; // 작성자 티어

    private int currentMembers; // 현재 모집된 인원수

    private int maxMembers; // 목표 모집 인원수

    private String targetScore; // 목표 점수/티어

    private String openChatLink; // 오픈채팅 링크

    private String gameName; // 게임 이름 (선택 사항)

    private String studyName; // 스터디 이름 (선택 사항)

    private LocalDateTime createdAt = LocalDateTime.now(); // DB 저장 시 현재 시간 자동 기록
}