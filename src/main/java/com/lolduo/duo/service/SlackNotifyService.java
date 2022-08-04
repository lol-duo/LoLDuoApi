package com.lolduo.duo.service;

import com.slack.api.Slack;
import com.slack.api.methods.SlackApiException;
import com.slack.api.model.Conversation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class SlackNotifyService {

    String notifyChannelName = "notification";
    String notifyChannelId;
    @Value("${slack.token}")
    String token;
    @Value("${spring.profiles.active}")
    String springProfile;

    @EventListener(ContextRefreshedEvent.class)
    public void onContextRefreshedEvent(ContextRefreshedEvent event) {
        if (springProfile.equals("server")) {
            if (notifyChannelId == null)
                initChannelId();

            sendMessage(ZonedDateTime.now(ZoneId.of("Asia/Seoul"))
                    .format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH:mm:ss"))
                    + " - 서버를 기동합니다.\n프론트 페이지: http://13.124.202.156/\nSwagger: http://52.78.55.104/swagger-ui.html#/\nDatadog: https://www.datadoghq.com/");
        }
    }

    @EventListener(ContextClosedEvent.class)
    public void onContextClosedEvent(ContextClosedEvent event) {
        if (springProfile.equals("server")) {
            if (notifyChannelId == null)
                initChannelId();

            sendMessage(ZonedDateTime.now(ZoneId.of("Asia/Seoul"))
                    .format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH:mm:ss"))
                    + " - 서버가 종료되었습니다.\nDatadog: https://www.datadoghq.com/");
        }
    }

    public void sendMessage(String text) {
        if (notifyChannelId == null) {
            log.error("[SlackAPI] error: channelId is null");
            return;
        }

        var client = Slack.getInstance().methods();
        try {
            var result = client.chatPostMessage( requestConfig ->
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

    private void initChannelId() {
        log.info("[SlackAPI] channelName: {}", notifyChannelName);
        var client = Slack.getInstance().methods();

        try {
            var result = client.conversationsList( requestConfig ->
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
    }
}