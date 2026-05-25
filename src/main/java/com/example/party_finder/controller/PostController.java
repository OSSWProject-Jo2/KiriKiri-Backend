package com.example.party_finder.controller;

import com.example.party_finder.dto.PostRequest;
import com.example.party_finder.dto.PostResponse;
import com.example.party_finder.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // 게시글 등록
    // @AuthenticationPrincipal Jwt jwt : Clerk가 발급한 JWT 토큰을 자동으로 받아옴
    // jwt.getSubject() : 토큰 안에 담긴 Clerk 사용자 고유 ID를 꺼냄
    // userId를 Service로 넘겨서 게시글 작성자 ID로 저장
    @PostMapping
    public PostResponse create(@RequestBody PostRequest request,
                               @AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        return postService.create(request, userId);
    }

    // 전체 목록 조회
    @GetMapping
    public List<PostResponse> getAll() {
        return postService.getAll();
    }

    // 단건 조회
    @GetMapping("/{id}")
    public PostResponse getOne(@PathVariable Long id) {
        return postService.getOne(id);
    }

    // 카테고리 필터링 조회
    @GetMapping("/category/{category}")
    public List<PostResponse> getByCategory(@PathVariable String category) {
        return postService.getByCategory(category);

        /*
        GET /api/posts/category/게임  → 게임만
        GET /api/posts/category/공부  → 공부만
        GET /api/posts/category/운동  → 운동만
         */
    }
    // 게시글 삭제
    // DELETE /api/posts/{id} 요청이 오면 토큰에서 userId 꺼내서 본인 확인 후 삭제
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id,
                       @AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        postService.delete(id, userId);
    }

    // 게시글 수정
    // PUT /api/posts/{id} 요청이 오면 토큰에서 userId 꺼내서 본인 확인 후 수정
    @PutMapping("/{id}")
    public PostResponse update(@PathVariable Long id,
                               @RequestBody PostRequest request,
                               @AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        return postService.update(id, request, userId);
    }

    // 키워드 검색
    // GET /api/posts/search?keyword=리그 → 제목, 분야, 모임명에 키워드 포함된 게시글 반환
    @GetMapping("/search")
    public List<PostResponse> search(@RequestParam String keyword) {
        return postService.search(keyword);
    }

    /* API 정리
    GET    /api/posts                    → 전체 목록 조회
    GET    /api/posts/{id}               → 단건 조회
    POST   /api/posts                    → 게시글 등록 (로그인 필요)
    PUT    /api/posts/{id}               → 게시글 수정 (본인만)
    DELETE /api/posts/{id}               → 게시글 삭제 (본인만)
    GET    /api/posts/category/{category} → 카테고리 필터링
    GET    /api/posts/search?keyword={keyword} → 키워드 검색
    */
}

