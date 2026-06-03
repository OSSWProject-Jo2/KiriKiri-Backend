package com.example.party_finder.service;

import com.example.party_finder.domain.Application;
import com.example.party_finder.domain.ApplicationRepository;
import com.example.party_finder.domain.Notification;
import com.example.party_finder.domain.NotificationRepository;
import com.example.party_finder.domain.Post;
import com.example.party_finder.domain.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final PostRepository postRepository;
    private final NotificationRepository notificationRepository; // 알림 DB 연결

    // 참여 신청
    public String apply(Long postId, String applicantId, String nickname) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("게시글을 찾을 수 없습니다."));

        if (post.getUserId() != null && post.getUserId().equals(applicantId)) {
            throw new RuntimeException("본인 게시글에는 신청할 수 없습니다.");
        }
        if (post.getCurrentMembers() >= post.getMaxMembers()) {
            throw new RuntimeException("정원이 마감되었습니다.");
        }
        if (applicationRepository.existsByPostAndApplicantId(post, applicantId)) {
            throw new RuntimeException("이미 신청한 게시글입니다.");
        }

        Application application = new Application();
        application.setPost(post);
        application.setApplicantId(applicantId);
        application.setNickname(nickname);
        application.setStatus("PENDING");

        applicationRepository.save(application);

        // 🚨 [추가된 로직] 방장에게 '신청 도착' 알림 저장
        notificationRepository.save(Notification.builder()
                .kind("application")
                .postId(post.getId())
                .postTitle(post.getTitle())
                .recipientNickname(post.getAuthor()) // 방장 닉네임 (Post 엔티티에 author 필드가 있다고 가정)
                .actorNickname(nickname)             // 신청자 닉네임
                .message(nickname + "님이 참여를 신청했습니다.")
                .openChatLink(null)
                .build());

        return post.getOpenChatLink();
    }

    // 신청자 목록 조회
    public List<Application> getApplicants(Long postId, String userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("게시글을 찾을 수 없습니다."));

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

        if (post.getUserId() == null || !post.getUserId().equals(userId)) {
            throw new RuntimeException("수락 권한이 없습니다.");
        }
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

        // 🚨 [추가된 로직] 신청자에게 '수락 완료' 알림 저장 및 카톡 링크 전달
        notificationRepository.save(Notification.builder()
                .kind("accepted")
                .postId(post.getId())
                .postTitle(post.getTitle())
                .recipientNickname(application.getNickname()) // 신청자 닉네임
                .actorNickname(post.getAuthor())              // 방장 닉네임
                .message(post.getTitle() + " 모임에 수락되었습니다!")
                .openChatLink(post.getOpenChatLink())
                .build());
    }
}