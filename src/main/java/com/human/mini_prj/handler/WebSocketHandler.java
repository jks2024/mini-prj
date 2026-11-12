package com.human.mini_prj.handler;

import com.human.mini_prj.dto.ChatMessageDto;
import com.human.mini_prj.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import tools.jackson.databind.json.JsonMapper;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {
    private final JsonMapper jsonMapper;
    private final ChatService chatService;
    private final Map<WebSocketSession, String> sessionRoomIdMap = new ConcurrentHashMap<>();

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        ChatMessageDto chat = jsonMapper.readValue(message.getPayload(), ChatMessageDto.class);
        log.info("수신: {} / {} / {}", chat.getType(), chat.getSender(), chat.getMessage());

        switch (chat.getType()) {
            case ENTER -> {
                sessionRoomIdMap.put(session, chat.getRoomId());
                chatService.addSessionAndHandleEnter(chat.getRoomId(), session, chat);
            }
            case TALK -> {
                chatService.sendMessageToAll(chat.getRoomId(), chat);
            }
            case CLOSE -> {
                String roomId = sessionRoomIdMap.remove(session);  // 먼저 제거 → 연결 종료 시 퇴장 중복 방지
                if (roomId != null) chatService.removeSessionAndHandleExit(roomId, session, chat);
            }
        }

    }

    // 브라우저 종료 등으로 CLOSE 없이 연결이 끊긴 경우
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String roomId = sessionRoomIdMap.remove(session);
        if (roomId != null) {
            ChatMessageDto chat = new ChatMessageDto();
            chat.setType(ChatMessageDto.MessageType.CLOSE);
            chatService.removeSessionAndHandleExit(roomId, session, chat);   // sender 없음 → 퇴장 메시지 없이 세션만 정리
        }
        log.info("연결 종료: {} ({})", session.getId(), status);
    }
}
