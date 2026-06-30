package com.meta12.SS8911.controller;

import com.meta12.SS8911.Dto.ChatMessageViewDTO;
import com.meta12.SS8911.entity.ChatMessage;
import com.meta12.SS8911.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ChatHistoryApiController {

    private final ChatMessageRepository chatMessageRepository;

    /**
     * 최근 채팅 메시지 N개를 오래된 순으로 정렬해서 반환.
     * 채팅창을 처음 열 때 이전 대화 내역을 보여주기 위함.
     */
    @GetMapping("/api/chat/history")
    public List<ChatMessageViewDTO> getHistory(@RequestParam(defaultValue = "50") int limit) {
        List<ChatMessage> recent = chatMessageRepository.findAllByOrderByCreatedDateDesc(PageRequest.of(0, limit));
        Collections.reverse(recent); // 최신순 -> 오래된순으로 뒤집기
        return recent.stream().map(ChatMessageViewDTO::from).collect(Collectors.toList());
    }
}