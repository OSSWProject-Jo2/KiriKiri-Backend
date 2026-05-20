package com.example.party_finder.domain;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate; // 생성 시간 자동 기록 도구
import org.springframework.data.jpa.domain.support.AuditingEntityListener; // 감시자(시간 기록 대기조)

import java.time.LocalDateTime; // 자바의 날짜와 시간 타입

@Entity // 이 클래스는 DB의 테이블과 1:1로 매핑되는 '엔티티'임을 선언
@Getter @Setter // 필드에 대한 Getter/Setter를 자동으로 생성
@EntityListeners(AuditingEntityListener.class) // DB에 저장될 때 "시간 기록" 이벤트를 감지함


public class Post {
    @Id // 이 필드를 테이블의 기본키로 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 번호 자동 증가 (1, 2, 3...)
    private Long id; // 게시글의 고유 번호

    // ---  카테고리 (필터링용) ---
    @Column(nullable = false) // DB 칸을 만들 때 '비어있으면 안 됨(NOT NULL)' 규칙 적용
    private String boardType;   // "GAME" 또는 "STUDY" (전체 탭 분류용)

    @Column(nullable = false)
    private String categoryTag; // 예: "리그 오브 레전드", "정보처리기사", "발로란트"

    @Column(nullable = false)
    private String title; // 게시글 제목

    @Column(columnDefinition = "TEXT", nullable = false) // 일반 글자 칸보다 훨씬 큰 'TEXT' 타입으로 지정
    private String content; // 게시글 본문 내용

    // --- 비회원을 위한 필수 정보 ---

    @Column(nullable = false) // 닉네임 없으면 글 못 씀
    private String author; // 작성자 이름 (유동닉 닉네임)

    @Column(nullable = false) // 비밀번호 없으면 나중에 수정/삭제 못 함
    private String password; // 수정 및 삭제를 위한 비밀번호

    // --- 프론트엔드 맞춤 추가 정보 (단순 문자열/숫자로 타협) ---
    @Column(nullable = false)
    private int currentPeople;  // 현재 인원 (예: 3)

    @Column(nullable = false)
    private int maxPeople;      // 최대 인원 (예: 5)

    @Column(nullable = false)
    private String authorTier;  // 작성자 티어 (예: "골드 2")

    @Column(nullable = false)
    private String targetGoal;  // 목표 (예: "플래티넘", "실기 합격", "미정")

    @Column(nullable = false)
    private String openChatLink;// 오픈채팅 링크 (단순 텍스트 저장)

    @CreatedDate // 데이터가 처음 저장될 때 현재 시간을 자동으로 꽂아줌
    @Column(updatable = false) // 글을 수정한다고 해서 작성 시간이 바뀌면 안 되므로 '수정 불가' 설정
    private LocalDateTime createdAt; // 글 쓴 시간


    //모집 여부 필드
    @Enumerated(EnumType.STRING) // DB에 숫자가 아닌 "RECRUITING" 문자열 그대로 저장함
    @Column(nullable = false)
    private PostStatus status = PostStatus.RECRUITING; // 기본값은 '모집 중'

    /*
     * [핵심 로직] 인원수가 변경될 때 상태를 자동으로 업데이트하는 메서드
     * 서비스 계층에서 currentPeople을 수정할 때 이 메서드를 호출해주면 좋습니다.
     */
    public void updateStatusByPeople() {
        if (this.currentPeople >= this.maxPeople) {
            this.status = PostStatus.CLOSED;
        } else {
            this.status = PostStatus.RECRUITING;
        }
    }
}
