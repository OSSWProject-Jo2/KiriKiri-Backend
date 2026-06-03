package com.example.party_finder.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    // 특정 게시글의 신청 목록 조회
    List<Application> findByPost(Post post);

    // 중복 신청 방지 (같은 사람이 같은 게시글에 이미 신청했는지 확인)
    boolean existsByPostAndApplicantId(Post post, String applicantId);

    // 내 신청 내역 단건 조회 (상태 확인 및 수락 시 링크 공개용)
    Optional<Application> findByPostAndApplicantId(Post post, String applicantId);
}