package com.meta12.SS8911.repository;

import com.meta12.SS8911.entity.Content;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContentRepository extends JpaRepository<Content, Long> {

    List<Content> findByCategoryIdOrderBySequenceAsc(Long courseId);

}
