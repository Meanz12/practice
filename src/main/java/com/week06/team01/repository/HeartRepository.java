package com.week06.team01.repository;

import com.week06.team01.domain.Heart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HeartRepository extends JpaRepository<Heart, Long> {
    int countHeartByPostId(Long postId);
    Heart findByPostIdAndMemberId(Long postId, Long memberId);
}