package com.example.party_finder.service;

import com.example.party_finder.domain.Application;
import com.example.party_finder.domain.ApplicationRepository;
import com.example.party_finder.domain.Notification;
import com.example.party_finder.domain.NotificationRepository;
import com.example.party_finder.domain.Post;
import com.example.party_finder.dto.PostRequest;
import com.example.party_finder.dto.PostResponse;
import com.example.party_finder.domain.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final ApplicationRepository applicationRepository; // 추가
    private final NotificationRepository notificationRepository; // 추가

    public PostResponse create(PostRequest request, String userId) {
        Post post = request.toEntity();
        post.setUserId(userId);
        Post saved = postRepository.save(post);
        return new PostResponse(saved);
    }

    public List<PostResponse> getAll() {
        return postRepository.findAll().stream().map(PostResponse::new).collect(Collectors.toList());
    }

    public PostResponse getOne(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("게시글을 찾을 수 없습니다."));
        return new PostResponse(post);
    }

    public List<PostResponse> getByCategory(String category) {
        return postRepository.findByCategory(category).stream().map(PostResponse::new).collect(Collectors.toList());
    }

    public void delete(Long id, String userId) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("게시글을 찾을 수 없습니다."));

        if (post.getUserId() == null || !post.getUserId().equals(userId)) {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }

        // 🚨 [추가된 로직] 삭제 전, 신청자들에게 '게시글 삭제됨' 알림 일괄 저장
        List<Application> applications = applicationRepository.findByPost(post);
        for (Application app : applications) {
            notificationRepository.save(Notification.builder()
                    .kind("deleted")
                    .postId(post.getId())
                    .postTitle(post.getTitle())
                    .recipientNickname(app.getNickname())
                    .actorNickname(post.getAuthor())
                    .message(post.getTitle() + " 모임이 삭제되었습니다.")
                    .openChatLink(null)
                    .build());
        }

        postRepository.delete(post);
    }

    public PostResponse update(Long id, PostRequest request, String userId) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("게시글을 찾을 수 없습니다."));

        if (post.getUserId() == null || !post.getUserId().equals(userId)) {
            throw new RuntimeException("수정 권한이 없습니다.");
        }

        post.setTitle(request.getTitle());
        post.setCategory(request.getCategory());
        post.setCategoryTag(request.getCategoryTag());
        post.setDescription(request.getDescription());
        post.setMaxMembers(request.getMaxMembers());
        post.setTargetScore(request.getTargetScore());
        post.setOpenChatLink(request.getOpenChatLink());
        post.setGameName(request.getGameName());
        post.setStudyName(request.getStudyName());

        Post saved = postRepository.save(post);
        return new PostResponse(saved);
    }

    public List<PostResponse> search(String keyword) {
        return postRepository.findByTitleContainingOrCategoryContainingOrCategoryTagContaining(
                        keyword, keyword, keyword)
                .stream().map(PostResponse::new).collect(Collectors.toList());
    }
}