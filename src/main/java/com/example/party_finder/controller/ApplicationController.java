package com.example.party_finder.controller;

import com.example.party_finder.dto.ApplicationResponse;
import com.example.party_finder.dto.MyApplicationResponse;
import com.example.party_finder.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    // 참여 신청 - 프론트가 { success, openChatLink?, error? } 형태의 JoinResponse를 기대하므로 동일하게 반환
    // POST /api/posts/{postId}/applications
    @PostMapping("/{postId}/applications")
    public Map<String, Object> apply(@PathVariable Long postId,
                                     @RequestBody Map<String, String> body,
                                     @AuthenticationPrincipal Jwt jwt) {
        String applicantId = jwt.getSubject();
        String nickname = body.get("nickname");
        Map<String, Object> response = new HashMap<>();
        try {
            String openChatLink = applicationService.apply(postId, applicantId, nickname);
            response.put("success", true);
            response.put("openChatLink", openChatLink);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("error", e.getMessage());
        }
        return response;
    }

    // 신청자 목록 조회 (게시글 작성자만 가능)
    // ApplicationResponse DTO로 변환하여 반환 - id를 string으로, status를 소문자로 맞춤
    // GET /api/posts/{postId}/applications
    @GetMapping("/{postId}/applications")
    public List<ApplicationResponse> getApplicants(@PathVariable Long postId,
                                                   @AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        return applicationService.getApplicants(postId, userId)
                .stream()
                .map(ApplicationResponse::new)
                .collect(Collectors.toList());
    }

    // 내 신청 상태 조회 - 수락됐을 때만 오픈채팅 링크 반환 (프론트 "신청 후 공개" 섹션용)
    // GET /api/posts/{postId}/applications/me
    @GetMapping("/{postId}/applications/me")
    public MyApplicationResponse getMyApplication(@PathVariable Long postId,
                                                  @AuthenticationPrincipal Jwt jwt) {
        String applicantId = jwt.getSubject();
        return new MyApplicationResponse(applicationService.getMyApplication(postId, applicantId));
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