package com.imuliar.decima.service.impl;

import com.imuliar.decima.DecimaBot;
import com.imuliar.decima.service.AccessProvider;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
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

    /**
     * <p>Checks if passed telegram user is a valid member of the group chat.</p>
     * <p>Access is permitted only for valid members of the group chat.</p>
     *
     * @param telegramUser telegram user to be examined
     * @return {@literal TRUE} if user is a valid group member, {@literal FALSE} if not
     */
    @Override
    public boolean isPermitted(User telegramUser) {
        Assert.notNull(telegramUser, "telegramUser is NULL");
        GetChatMember method = new GetChatMember()
                .setUserId(telegramUser.getId())
                .setChatId(groupChatId);

        return decimaBot
                .requestChatMember(method)
                .map(ChatMember::getStatus)
                .filter(PERMITTED_STATUSES::contains)
                .isPresent() && !telegramUser.getBot();
    }
}
