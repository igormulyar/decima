package com.imuliar.decima.service.impl;

import com.imuliar.decima.DecimaBot;
import com.imuliar.decima.service.AccessProvider;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.objects.ChatMember;
import org.telegram.telegrambots.meta.api.objects.User;

/**
 * @author imuliar
 * @since 0.0.1
 */
@Service
public class AccessProviderImpl implements AccessProvider {

    private static final List<String> PERMITTED_STATUSES = Arrays.asList("creator", "administrator", "member");

    @Value("${decima.groupChatId}")
    private Long groupChatId;

    @Autowired
    @Lazy
    private DecimaBot decimaBot;

    @Override
    public boolean isPermitted(User telegramUser) {
        GetChatMember method = new GetChatMember()
                .setUserId(telegramUser.getId())
                .setChatId(groupChatId);

        return decimaBot
                .requestChatMember(method)
                .map(ChatMember::getStatus)
                .filter(PERMITTED_STATUSES::contains)
                .isPresent();
    }
}
