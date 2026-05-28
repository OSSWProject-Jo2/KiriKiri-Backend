package com.example.party_finder.service;

import com.example.party_finder.domain.Application;
import com.example.party_finder.domain.ApplicationRepository;
import com.example.party_finder.domain.Post;
import com.example.party_finder.domain.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final PostRepository postRepository;

    // 참여 신청
    public void apply(Long postId, String applicantId, String nickname) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        // 본인 게시글에는 신청 불가
        if (post.getUserId() != null && post.getUserId().equals(applicantId)) {
            throw new RuntimeException("본인 게시글에는 신청할 수 없습니다.");
        }

        // 중복 신청 방지
        if (applicationRepository.existsByPostAndApplicantId(post, applicantId)) {
            throw new RuntimeException("이미 신청한 게시글입니다.");
        }

        Application application = new Application();
        application.setPost(post);
        application.setApplicantId(applicantId);
        application.setNickname(nickname);
        application.setStatus("PENDING"); // 기본 상태: 대기중

        applicationRepository.save(application);
    }

    // 신청자 목록 조회 (게시글 작성자만 가능)
    public List<Application> getApplicants(Long postId, String userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        // 게시글 작성자인지 확인
        if (post.getUserId() == null || !post.getUserId().equals(userId)) {
            throw new RuntimeException("조회 권한이 없습니다.");
        }

        return applicationRepository.findByPost(post);
    }

    // 신청 수락
    public void accept(Long postId, Long applicationId, String userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        // 게시글 작성자인지 확인
        if (post.getUserId() == null || !post.getUserId().equals(userId)) {
            throw new RuntimeException("수락 권한이 없습니다.");
        }

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("신청을 찾을 수 없습니다."));

        application.setStatus("ACCEPTED"); // 수락 처리
        applicationRepository.save(application);

        // 현재 인원 증가
        post.setCurrentMembers(post.getCurrentMembers() + 1);
        postRepository.save(post);
    }
}
