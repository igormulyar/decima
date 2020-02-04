package com.imuliar.decima.service.impl;

import com.vdurmont.emoji.EmojiParser;
import java.util.Locale;
import java.util.Optional;
import org.apache.commons.lang3.LocaleUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

/**
 * <p>Facade for access to message resources</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Service
public class DecimaMessageSourceFacade {

    private final MessageSource messageSource;

    @Autowired
    public DecimaMessageSourceFacade(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getMsg(String code, String localeCode) {
        return getPrepared(code, new String[]{}, localeCode);
    }

    public String getMsg(String code, String localeCode, String... params) {
        return getPrepared(code, params, localeCode);
    }

    private String getPrepared(String code, String[] msgParams, String localeCode) {
        Locale locale = parseLocale(localeCode);
        String rawMessage = messageSource.getMessage(code, msgParams, locale);
        return EmojiParser.parseToUnicode(rawMessage);
    }

    private Locale parseLocale(String stringCode) {
        return Optional
                .ofNullable(LocaleUtils.toLocale(stringCode))
                .orElse(new Locale("en"));
    }
}
