package com.example.party_finder.service;

import com.example.party_finder.domain.Application;
import com.example.party_finder.domain.ApplicationRepository;
import com.example.party_finder.domain.Post;
import com.example.party_finder.domain.PostRepository;
import com.example.party_finder.dto.PostRequest;
import com.example.party_finder.dto.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final ApplicationRepository applicationRepository;

    // 게시글 등록
// PostController에서 넘겨받은 userId(Clerk 사용자 ID)를 게시글에 저장
    public PostResponse create(PostRequest request, String userId) {
        Post post = request.toEntity(); // 요청 DTO를 엔티티로 변환
        post.setUserId(userId); // 토큰에서 꺼낸 Clerk 사용자 ID 저장
        Post saved = postRepository.save(post); // DB에 저장
        return new PostResponse(saved); // 저장된 엔티티를 응답 DTO로 변환해서 반환
    }

    // 전체 목록 조회
    public List<PostResponse> getAll() {
        return postRepository.findAll()
                .stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }

    // 단건 조회
    public PostResponse getOne(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("게시글을 찾을 수 없습니다."));
        return new PostResponse(post);
    }

    // 카테고리별 조회
    public List<PostResponse> getByCategory(String category) {
        return postRepository.findByCategory(category)
                .stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }

    // 게시글 삭제 (userId로 본인 확인 후 삭제)
    // 신청 내역이 있는 게시글도 삭제 가능하도록 관련 Application 먼저 삭제
    @Transactional
    public void delete(Long id, String userId) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("게시글을 찾을 수 없습니다."));

        // 본인 게시글인지 확인
        if (post.getUserId() == null || !post.getUserId().equals(userId)) {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }

        // 외래키 제약 해결: 게시글 삭제 전 관련 신청 내역 먼저 삭제
        List<Application> applications = applicationRepository.findByPost(post);
        applicationRepository.deleteAll(applications);

        postRepository.delete(post); // DB에서 삭제
    }

    // 게시글 수정 (userId로 본인 확인 후 수정)
    public PostResponse update(Long id, PostRequest request, String userId) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("게시글을 찾을 수 없습니다."));

        // 본인 게시글인지 확인
        if (post.getUserId() == null || !post.getUserId().equals(userId)) {
            throw new RuntimeException("수정 권한이 없습니다.");
        }

        // 수정할 내용 반영
        post.setTitle(request.getTitle());
        post.setCategory(request.getCategory());
        post.setCategoryTag(request.getCategoryTag());
        post.setDescription(request.getDescription());
        post.setMaxMembers(request.getMaxMembers());
        post.setTargetScore(request.getTargetScore());
        post.setOpenChatLink(request.getOpenChatLink());
        post.setGameName(request.getGameName());
        post.setStudyName(request.getStudyName());

        Post saved = postRepository.save(post); // 수정된 내용 DB에 저장
        return new PostResponse(saved);
    }

    // 키워드 검색 (제목, 분야, 모임명 중 하나라도 포함되면 결과에 포함)
    public List<PostResponse> search(String keyword) {
        return postRepository.findByTitleContainingOrCategoryContainingOrCategoryTagContaining(
                        keyword, keyword, keyword)
                .stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }

}