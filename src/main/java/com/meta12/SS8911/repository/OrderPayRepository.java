package com.meta12.SS8911.repository;

import com.meta12.SS8911.entity.Category;
import com.meta12.SS8911.entity.OrderPay;
import com.meta12.SS8911.entity.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderPayRepository extends JpaRepository<OrderPay, Long> {

    List<OrderPay> findBySiteUser(SiteUser siteUser);

    List<OrderPay> findBySiteUser_Username(String username);

    // 🛠️ [서버 기동 에러 해결]: String 대신 Category 객체 타입을 받도록 수정합니다!
    List<OrderPay> findByCategory(Category category);
    // Category 객체 안의 'title' 필드 문자열과 매칭하여 리스트를 뽑아오는 내장 문법입니다!
    List<OrderPay> findByCategory_Title(String categoryTitle);

    // 1. 기존 메서드 (객체 기반)
    boolean existsBySiteUserAndCategory(SiteUser siteUser, Category category);

    // 2. 새 메서드 (String 기반 - 이걸 추가하세요!)
    boolean existsBySiteUserAndCategory_Title(SiteUser siteUser, String categoryTitle);

    List<OrderPay> findByCategoryId(Long categoryId);
    Optional<OrderPay> findBySiteUserAndCategory(SiteUser siteUser, Category category);

    void deleteByCategoryId(Long categoryId);
}
