package com.example.party_finder.dto;

import com.example.party_finder.domain.Post;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor // 빈 생성자를 자동으로 만들어줍니다 (스프링 내부에서 필요할 때가 있음)
public class PostResponse {

    private String id;           // 문자로 변환된 글 번호
    private String category;     // "게임", "공부", "운동" 등
    private String title;        // 제목
    private String description;  // 상세 설명
    private String author;       // 작성자 닉네임
    private String authorTier;   // 작성자 티어
    private int currentMembers;  // 현재 인원
    private int maxMembers;      // 최대 인원
    private String targetScore;  // 목표 점수/티어
    private String createdAt;    // 날짜 문자열 (예: "2026.05.06")
    private String openChatLink; // 오픈채팅 링크
    private String gameName;     // 게임 이름 (선택)
    private String studyName;    // 스터디 이름 (선택)
    private String userId; // 프론트엔드에 전달할 회원 식별 ID 추가

    // 🔥 핵심: 엔티티(DB 내용)를 통째로 받아서 DTO 필드들에 쏙쏙 꽂아주는 생성자입니다.
    public PostResponse(Post post) {
        this.id = String.valueOf(post.getId()); // Long 타입을 프론트가 원하는 String으로 변환!
        this.category = post.getCategory();
        this.title = post.getTitle();
        this.description = post.getDescription();
        this.author = post.getAuthor();
        this.authorTier = post.getAuthorTier();
        this.currentMembers = post.getCurrentMembers();
        this.maxMembers = post.getMaxMembers();
        this.targetScore = post.getTargetScore();
        this.openChatLink = post.getOpenChatLink();
        this.gameName = post.getGameName();
        this.studyName = post.getStudyName();

        // LocalDateTime 날짜 객체를 프론트가 원하는 "yyyy.MM.dd" 형식의 문자로 변환!
        if (post.getCreatedAt() != null) {
            this.createdAt = post.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        }
    }
}