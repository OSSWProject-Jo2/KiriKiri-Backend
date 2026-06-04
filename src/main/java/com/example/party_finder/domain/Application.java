package com.example.party_finder.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne // 여러 신청이 하나의 게시글에 속함
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(nullable = false)
    private String applicantId; // 신청자 Clerk userId

    @Column(nullable = false)
    private String nickname; // 신청자 닉네임

    @Column(nullable = false)
    private String status; // "PENDING" / "ACCEPTED" / "REJECTED"

    private LocalDateTime createdAt = LocalDateTime.now();
}