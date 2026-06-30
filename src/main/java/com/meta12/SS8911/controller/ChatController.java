package com.meta12.SS8911.controller;

import com.meta12.SS8911.Dto.ChatMessageDTO;
import com.meta12.SS8911.Dto.ChatMessageViewDTO;
import com.meta12.SS8911.entity.ChatMessage;
import com.meta12.SS8911.repository.ChatMessageRepository;
import com.meta12.SS8911.entity.SiteUser;
import com.meta12.SS8911.repository.SiteUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageRepository chatMessageRepository;
    private final SiteUserRepository siteUserRepository;

    /**
     * 클라이언트가 "/app/chat/send"로 메시지를 보내면 호출됨.
     * DB에 저장 후 "/topic/chat"을 구독 중인 모든 클라이언트에게 broadcast.
     */
    @MessageMapping("/chat/send")
    public void sendMessage(ChatMessageDTO dto, Authentication authentication) {

        if (authentication == null || dto.getContent() == null || dto.getContent().isBlank()) {
            return;
        }

        String username = authentication.getName();
        SiteUser author = siteUserRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 사용자입니다."));

        ChatMessage saved = chatMessageRepository.save(
                ChatMessage.builder()
                        .author(author)
                        .content(dto.getContent().trim())
                        .build()
        );

        messagingTemplate.convertAndSend("/topic/chat", ChatMessageViewDTO.from(saved));
    }
}