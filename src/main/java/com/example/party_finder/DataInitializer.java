package com.example.party_finder;

import com.example.party_finder.domain.Post;
import com.example.party_finder.domain.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final PostRepository postRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        // 이미 데이터가 있으면 중복 삽입 방지
        if (postRepository.count() > 0) return;

        // 1. 리그 오브 레전드
        Post post1 = new Post();
        post1.setTitle("롤 자유랭 같이 올릴 파티 구해요");
        post1.setCategory("게임");
        post1.setBoardType("GAME");
        post1.setCategoryTag("리그 오브 레전드");
        post1.setGameName("리그 오브 레전드");
        post1.setDescription("저녁 시간대에 꾸준히 같이 할 분들을 찾습니다. 분위기 좋게 피드백하면서 목표 티어까지 같이 올라가요.");
        post1.setAuthor("익명1");
        post1.setPassword("1234");
        post1.setCurrentMembers(3);
        post1.setMaxMembers(5);
        post1.setTargetScore("플래티넘");
        post1.setOpenChatLink("https://open.kakao.com/o/test1");

        // 2. 정보처리기사
        Post post2 = new Post();
        post2.setTitle("정보처리기사 필기 스터디 모집");
        post2.setCategory("공부");
        post2.setBoardType("STUDY");
        post2.setCategoryTag("정보처리기사");
        post2.setStudyName("정보처리기사");
        post2.setDescription("기출 문제와 오답 정리를 같이 할 스터디원을 모집합니다. 주 3회 온라인으로 진행할 예정입니다.");
        post2.setAuthor("익명2");
        post2.setPassword("1234");
        post2.setCurrentMembers(4);
        post2.setMaxMembers(6);
        post2.setTargetScore("필기 합격");
        post2.setOpenChatLink("https://open.kakao.com/o/test2");

        // 3. 러닝
        Post post3 = new Post();
        post3.setTitle("퇴근 후 러닝 크루 같이 해요");
        post3.setCategory("운동");
        post3.setBoardType("EXERCISE");
        post3.setCategoryTag("러닝");
        post3.setDescription("주 2회 한강 근처에서 가볍게 뛰는 모임입니다. 처음 시작하는 분도 편하게 오셔도 됩니다.");
        post3.setAuthor("익명3");
        post3.setPassword("1234");
        post3.setCurrentMembers(2);
        post3.setMaxMembers(8);
        post3.setTargetScore("5km 완주");
        post3.setOpenChatLink("https://open.kakao.com/o/test3");

        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);
    }
}