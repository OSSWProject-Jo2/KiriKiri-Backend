package com.example.party_finder.dto;

import com.example.party_finder.domain.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor // 스프링이 리액트가 보낸 JSON을 이 객체로 변환할 때 꼭 필요한 빈 생성자
public class PostRequest {

    // 💡 화면 입력창에서 사용자가 직접 타이핑하거나 선택하는 항목들만 딱 모았습니다.
    private String title;        // 제목
    private String category;     // 분야 ("게임", "공부", "운동" 등)
    private String categoryTag;  // 모임명 (예: "리그 오브 레전드", "정보처리기사")
    private String targetScore;  // 목표 (예: "플래티넘", "필기 합격")
    private int maxMembers;      // 최대 인원
    private String description;  // 상세 설명 (본문 내용)
    private String openChatLink; // 오픈채팅 링크

    // 비회원 전용 필수 정보
    private String author;       // 작성자 별명
    private String password;     // 글 수정/삭제용 비밀번호

    private String gameName;     // 선택 사항
    private String studyName;    // 선택 사항

    // 🔥 핵심: 리액트가 준 데이터(Request DTO)를 진짜 DB 금고에 넣을 수 있는 '엔티티(Post)'로 조립하는 메서드입니다.
    public Post toEntity() {
        Post post = new Post();
        post.setTitle(this.title);
        post.setCategory(this.category);
        post.setCategoryTag(this.categoryTag);

        // 프론트에서 넘어온 탭 구분을 boardType에도 매칭시켜 줍니다.
        if ("게임".equals(this.category)) post.setBoardType("GAME");
        else if ("공부".equals(this.category)) post.setBoardType("STUDY");
        else post.setBoardType("EXERCISE"); // 운동 등 예외 처리

        post.setTargetScore(this.targetScore);
        post.setMaxMembers(this.maxMembers);
        post.setCurrentMembers(1); // 💡 중요: 글쓴이 본인이 있으니 처음엔 무조건 '현재 인원 1명'으로 세팅!

        post.setDescription(this.description);
        post.setOpenChatLink(this.openChatLink);
        post.setAuthor(this.author);
        post.setPassword(this.password); // 암호화 없이 일단 생으로 저장

        post.setGameName(this.gameName);
        post.setStudyName(this.studyName);

        return post;
    }
}