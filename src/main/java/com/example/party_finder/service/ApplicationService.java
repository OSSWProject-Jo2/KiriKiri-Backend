package com.example.party_finder.service;

import com.example.party_finder.domain.Application;
import com.example.party_finder.domain.ApplicationRepository;
import com.example.party_finder.domain.Post;
import com.example.party_finder.domain.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final PostRepository postRepository;

    // 참여 신청 - 성공 시 오픈채팅 링크 반환 (프론트 JoinResponse 타입과 일치)
    public String apply(Long postId, String applicantId, String nickname) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("게시글을 찾을 수 없습니다."));

        // 본인 게시글에는 신청 불가
        if (post.getUserId() != null && post.getUserId().equals(applicantId)) {
            throw new RuntimeException("본인 게시글에는 신청할 수 없습니다.");
        }

        // 정원 마감 검사
        if (post.getCurrentMembers() >= post.getMaxMembers()) {
            throw new RuntimeException("정원이 마감되었습니다.");
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

        // 수락 전이므로 링크 미공개 - 수락 후 /my-application API로 확인 가능
        return null;
    }

    // 내 신청 상태 조회 - 수락됐을 때만 오픈채팅 링크 반환
    public Application getMyApplication(Long postId, String applicantId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("게시글을 찾을 수 없습니다."));

        return applicationRepository.findByPostAndApplicantId(post, applicantId)
                .orElseThrow(() -> new NoSuchElementException("신청 내역이 없습니다."));
    }

    // 신청자 목록 조회 (게시글 작성자만 가능)
    public List<Application> getApplicants(Long postId, String userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("게시글을 찾을 수 없습니다."));

        // 게시글 작성자인지 확인
        if (post.getUserId() == null || !post.getUserId().equals(userId)) {
            throw new RuntimeException("조회 권한이 없습니다.");
        }

        return applicationRepository.findByPost(post);
    }

    // 신청 수락
    @Transactional
    public void accept(Long postId, Long applicationId, String userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("게시글을 찾을 수 없습니다."));

        // 게시글 작성자인지 확인
        if (post.getUserId() == null || !post.getUserId().equals(userId)) {
            throw new RuntimeException("수락 권한이 없습니다.");
        }

        // 정원 재확인 (트랜잭션 내 최종 검증)
        if (post.getCurrentMembers() >= post.getMaxMembers()) {
            throw new RuntimeException("정원이 이미 마감되었습니다.");
        }

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new NoSuchElementException("신청을 찾을 수 없습니다."));

        if ("ACCEPTED".equals(application.getStatus())) {
            throw new RuntimeException("이미 수락된 신청입니다.");
        }

        application.setStatus("ACCEPTED");
        applicationRepository.save(application);

        post.setCurrentMembers(post.getCurrentMembers() + 1);
        postRepository.save(post);
    }
}
