package com.example.party_finder.service;

import com.example.party_finder.domain.Post;
import com.example.party_finder.dto.PostRequest;
import com.example.party_finder.dto.PostResponse;
import com.example.party_finder.domain.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    // 게시글 등록
    public PostResponse create(PostRequest request) {
        Post post = request.toEntity();
        Post saved = postRepository.save(post);
        return new PostResponse(saved);
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
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
        return new PostResponse(post);
    }

    // 카테고리별 조회
    public List<PostResponse> getByCategory(String category) {
        return postRepository.findByCategory(category)
                .stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }
}