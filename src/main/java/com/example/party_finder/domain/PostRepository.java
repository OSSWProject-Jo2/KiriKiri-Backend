package com.example.party_finder.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository // 스프링에게 이 인터페이스가 DB 담당 비서임을 알립니다.
public interface PostRepository extends JpaRepository<Post, Long> {
    // JpaRepository<엔티티타입, PK타입>을 상속받으면
    // 우리가 SQL을 짜지 않아도 save(), findAll(), findById()를 자동으로 쓸 수 있게 됩니다.
    List<Post> findByCategory(String category); // 카테고리로 필터링
}