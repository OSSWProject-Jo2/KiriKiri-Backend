package com.example.party_finder.controller;

import com.example.party_finder.domain.Application;
import com.example.party_finder.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    // 참여 신청
    // POST /api/posts/{postId}/applications
    @PostMapping("/{postId}/applications")
    public void apply(@PathVariable Long postId,
                      @RequestBody Map<String, String> body,
                      @AuthenticationPrincipal Jwt jwt) {
        String applicantId = jwt.getSubject();
        String nickname = body.get("nickname");
        applicationService.apply(postId, applicantId, nickname);
    }

    // 신청자 목록 조회 (게시글 작성자만 가능)
    // GET /api/posts/{postId}/applications
    @GetMapping("/{postId}/applications")
    public List<Application> getApplicants(@PathVariable Long postId,
                                           @AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        return applicationService.getApplicants(postId, userId);
    }

    // 신청 수락
    // PATCH /api/posts/{postId}/applications/{applicationId}/accept
    @PatchMapping("/{postId}/applications/{applicationId}/accept")
    public Map<String, Object> accept(@PathVariable Long postId,
                                      @PathVariable Long applicationId,
                                      @AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        Map<String, Object> response = new HashMap<>();
        try {
            applicationService.accept(postId, applicationId, userId);
            response.put("success", true);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("error", e.getMessage());
        }
        return response;
    }
}

/*
POST  /api/posts/{postId}/applications        → 참여 신청 (로그인 필요)
GET   /api/posts/{postId}/applications        → 신청자 목록 조회 (작성자만)
PATCH /api/posts/{postId}/applications/{applicationId}/accept → 신청 수락 (작성자만)
 */