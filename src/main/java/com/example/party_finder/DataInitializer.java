package com.example.party_finder;

import com.example.party_finder.domain.Post;
import com.example.party_finder.domain.PostRepository;
import com.example.party_finder.domain.User;
import com.example.party_finder.domain.UserRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component // 1. 이게 없으면 스프링이 이 파일을 무시한다.
public class DataInitializer implements CommandLineRunner { // 2. 서버 켜지자마자 실행하라는 약속

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    // 3. 생성자를 통해 User의 Repository, Post의 Repository를 데려온다.
    public DataInitializer(UserRepository userRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }
    @Override
    public void run(String... args) {
        // 4. 저장할 유저 정보를 담은 객체를 만든다.
        if (userRepository.findByUsername("test_user").isEmpty()) {
            User user = new User();
            user.setUsername("test_user");
            user.setPassword("1234");
            user.setNickname("초보개발자");
            userRepository.save(user);
            System.out.println("유저 데이터 DB입력 작동 여부 확인");
        }


        if (postRepository.count() == 0) {
            Post samplePost = new Post();
            samplePost.setTitle("첫 번째 게시글"); // 제목 설정
            samplePost.setContent("비회원 게시판 테스트 중입니다."); // 본문 설정
            samplePost.setAuthor("ㅇㅇ"); // 비회원 이름
            samplePost.setPassword("5678"); // 나중에 삭제할 때 쓸 비밀번호

            // 4. 비서에게 저장 명령 (이때 SQL의 INSERT 문이 실행됩니다)
            postRepository.save(samplePost);

            System.out.println("게시글 데이터 DB입력 작동 여부 확인");
        }



    }
}