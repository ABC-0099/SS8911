package com.meta12.SS8911.service;

import com.meta12.SS8911.Dto.ChatMessageViewDTO;
import com.meta12.SS8911.entity.ChatMessage;
import com.meta12.SS8911.entity.SiteUser;
import com.meta12.SS8911.repository.ChatMessageRepository;
import com.meta12.SS8911.repository.SiteUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final SiteUserRepository siteUserRepository;

    /**
     * 메시지를 저장하고, broadcast에 바로 쓸 수 있는 ViewDTO로 변환해 반환.
     */
    @Transactional
    public ChatMessageViewDTO saveMessage(String username, String content) {
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("메시지 내용이 비어있습니다.");
        }

        SiteUser author = siteUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        ChatMessage saved = chatMessageRepository.save(
                ChatMessage.builder()
                        .author(author)
                        .content(content.trim())
                        .build()
        );

        return ChatMessageViewDTO.from(saved);
    }

    /**
     * 최근 메시지 N개를 오래된 순으로 정렬해서 반환.
     * 채팅창을 처음 열 때 이전 대화 내역을 보여주기 위함.
     */
    @Transactional(readOnly = true)
    public List<ChatMessageViewDTO> getRecentMessages(int limit) {
        List<ChatMessage> recent = chatMessageRepository.findAllByOrderByCreatedDateDesc(PageRequest.of(0, limit));
        Collections.reverse(recent); // 최신순 -> 오래된순으로 뒤집기
        return recent.stream().map(ChatMessageViewDTO::from).collect(Collectors.toList());
    }
}