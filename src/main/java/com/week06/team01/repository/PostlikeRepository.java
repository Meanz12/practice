package com.week06.team01.repository;


import com.week06.team01.domain.Post;
import com.week06.team01.domain.Postlike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostlikeRepository extends JpaRepository<Postlike, Long> {


}
