package com.meta12.SS8911.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
@Setter
public class ContentDTO {

    private Long id;
    private String title;
    private String videoUrl;
    private Integer sequence;
    private Long categoryId;
    private LocalDateTime createdDate;


    // 화면에서 넘어오는 파일 덩어리를 받습니다.
    private MultipartFile videoFile;
    private MultipartFile thumbFile;
    private MultipartFile attachFile;

    // 서비스 로직에서 가공된 파일명을 담습니다.
    private String fileName;
    private String thumbFileName;
    private String attachFileName;
}
