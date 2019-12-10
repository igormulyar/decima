package com.imuliar.decima.service.util;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Convenient builder</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
public class InlineKeyboardMarkupBuilder {

    List<List<InlineKeyboardButton>> buttonsScheme;

    /**
     * Creates first initial row
     */
    public InlineKeyboardMarkupBuilder() {
        buttonsScheme = new ArrayList<>();
        buttonsScheme.add(new ArrayList<>());
    }

    /**
     * Add new button to the last (current) row
     */
    public InlineKeyboardMarkupBuilder addButton(InlineKeyboardButton button) {
        buttonsScheme.get(buttonsScheme.size() - 1).add(button);
        return this;
    }

    /**
     * Create new raw and add new button to it
     */
    public InlineKeyboardMarkupBuilder addButtonAtNewRaw(InlineKeyboardButton button) {
        List<InlineKeyboardButton> newRaw = new ArrayList<>();
        newRaw.add(button);
        buttonsScheme.add(newRaw);
        return this;
    }

    public InlineKeyboardMarkup build(){
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(buttonsScheme);
        return markup;
    }
}
