package com.example.party_finder.controller;

import com.example.party_finder.dto.PostRequest;
import com.example.party_finder.dto.PostResponse;
import com.example.party_finder.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // 프론트엔드 연동을 위해 CORS 허용
public class PostController {

    private final PostService postService;

    // 게시글 등록
    @PostMapping
    public PostResponse create(@RequestBody PostRequest request) {
        return postService.create(request);
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
}