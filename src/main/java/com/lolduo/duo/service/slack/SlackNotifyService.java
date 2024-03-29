package com.lolduo.duo.service.slack;

import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.slack.api.methods.response.conversations.ConversationsListResponse;
import com.slack.api.model.Conversation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class SlackNotifyService {

    @Value("${slack.token}")
    String token;
    @Value("${spring.profiles.active}")
    String springProfile;

    @EventListener(ContextRefreshedEvent.class)
    public void onContextRefreshedEvent(ContextRefreshedEvent event) {
        String text;
        if (springProfile.equals("server")) {
            text = ZonedDateTime.now(ZoneId.of("Asia/Seoul"))
                    .format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH:mm:ss"))
                    + " - 메인 API 서버를 기동합니다.\n프론트 페이지: https://lolduo.net/\nSwagger: http://api.lolduo.net/swagger-ui.html#\nDatadog: https://www.datadoghq.com/";
        }
        else if (springProfile.equals("devserver")) {
            text = ZonedDateTime.now(ZoneId.of("Asia/Seoul"))
                    .format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH:mm:ss"))
                    + " - DEV API 서버를 기동합니다.\n프론트 페이지: https://lolduo.net:1000/\nSwagger: http://api.lolduo.net:1000/swagger-ui.html#\nDatadog: https://www.datadoghq.com/";
        }
        else
            return;

        sendMessage(text);
    }

    @EventListener(ContextClosedEvent.class)
    public void onContextClosedEvent(ContextClosedEvent event) {
        String text;
        if (springProfile.equals("server")) {
            text = ZonedDateTime.now(ZoneId.of("Asia/Seoul"))
                    .format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH:mm:ss"))
                    + " - 메인 API 서버가 종료되었습니다.\nDatadog: https://www.datadoghq.com/";
        }
        else if (springProfile.equals("devserver")) {
            text = ZonedDateTime.now(ZoneId.of("Asia/Seoul"))
                    .format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH:mm:ss"))
                    + " - DEV API 서버가 종료되었습니다.\nDatadog: https://www.datadoghq.com/";
        }
        else
            return;

        sendMessage(text);
    }

    public void sendMessage(String text) {
        String notifyChannelId = initChannelId("notification");
        if (notifyChannelId == null) {
            log.error("[SlackAPI] error: channelId is null");
            return;
        }

        MethodsClient client = Slack.getInstance().methods();
        try {
            ChatPostMessageResponse result = client.chatPostMessage(requestConfig ->
                    requestConfig
                            .token(token)
                            .channel(notifyChannelId)
                            .text(text)
            );
            log.info("[SlackAPI] result {}", result);
        } catch (IOException | SlackApiException e) {
            log.error("[SlackAPI] error: {}", e.getMessage(), e);
        }
    }

    private String initChannelId(String notifyChannelName) {
        log.info("[SlackAPI] channelName: {}", notifyChannelName);
        MethodsClient client = Slack.getInstance().methods();
        String notifyChannelId = null;
        try {
            ConversationsListResponse result = client.conversationsList(requestConfig ->
                    requestConfig.token(token)
            );
            for (Conversation channel : result.getChannels()) {
                if (channel.getName().equals(notifyChannelName)) {
                    notifyChannelId = channel.getId();
                    log.info("[SlackAPI] Channel ID: {}", notifyChannelId);
                    break;
                }
            }
        } catch (IOException | SlackApiException | NullPointerException e) {
            log.error("[SlackAPI] error: {}", e.getMessage(), e);
        }
        return notifyChannelId;
    }
    public String nowTime() {
        return ZonedDateTime.now(ZoneId.of("Asia/Seoul")).format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH:mm:ss"));
    }
}
