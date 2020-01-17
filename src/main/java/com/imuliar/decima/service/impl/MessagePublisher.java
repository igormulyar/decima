package com.imuliar.decima.service.impl;

import com.imuliar.decima.DecimaBot;
import com.imuliar.decima.service.util.InlineKeyboardMarkupBuilder;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import static com.imuliar.decima.service.util.Callbacks.EMPTY_CALLBACK;
import static com.imuliar.decima.service.util.Callbacks.MONTH_BACK_CALLBACK;
import static com.imuliar.decima.service.util.Callbacks.MONTH_FORWARD_CALLBACK;
import static com.imuliar.decima.service.util.Callbacks.TO_BEGINNING;
import static com.imuliar.decima.service.util.Callbacks.YEAR_BACK_CALLBACK;
import static com.imuliar.decima.service.util.Callbacks.YEAR_FORWARD_CALLBACK;
import static java.lang.Math.toIntExact;

/**
 * <p>Facade for sending the messages</p>
 *
 * @author imuliar
 * @since 0.0.1
 */
@Service
public class MessagePublisher {

    @Value("${decima.groupChatId}")
    private String groupChatId;

    @Autowired
    @Lazy
    private DecimaBot bot;

    @Autowired
    @Lazy
    public MessagePublisher(DecimaBot decimaBot) {
        this.bot = decimaBot;
    }

    public void sendSimpleMessage(Long chatId, String message) {
        bot.sendBotResponse(new SendMessage()
                .enableMarkdown(true)
                .setChatId(chatId)
                .setParseMode(ParseMode.MARKDOWN)
                .setText(message));
    }

    public void sendMessageWithKeyboard(Long chatId, String message, InlineKeyboardMarkup keyboardMarkup) {
        bot.sendBotResponse(new SendMessage()
                .enableMarkdown(true)
                .setChatId(chatId)
                .setParseMode(ParseMode.MARKDOWN)
                .setText(message)
                .setReplyMarkup(keyboardMarkup));
    }

    public void sendMsgWithBackBtn(Long chatId, String message) {
        sendMessageWithKeyboard(chatId, message, new InlineKeyboardMarkupBuilder()
                .addButton(new InlineKeyboardButton("Back").setCallbackData(TO_BEGINNING)).build());
    }

    public void sendMessageWithKeyboardToGroup(String message, InlineKeyboardMarkup keyboardMarkup) {
        sendMessageWithKeyboard(Long.valueOf(groupChatId), message, keyboardMarkup);
    }

    public void sendSimpleMessageToGroup(String message) {
        sendSimpleMessage(Long.valueOf(groupChatId), message);
    }

    public void popUpNotify(String callbackQueryId, String messageText) {
        bot.sendBotResponse(new AnswerCallbackQuery()
                .setCallbackQueryId(callbackQueryId)
                .setShowAlert(true)
                .setText(messageText));
    }

    public void sendImage(Long chatId, String caption, String imageUrl) {
        bot.sendPhoto(new SendPhoto()
                .setChatId(chatId)
                .setCaption(caption)
                .setPhoto(imageUrl));
    }

    public void publishCalendar(Long chatId, String message, LocalDate calendarViewDate) {
        sendMessageWithKeyboard(chatId, message, buildCalendarMarkup(calendarViewDate));
    }

    public void reRenderCalendar(Long chatId, int messageId, String text, LocalDate calendarViewDate) {
        bot.sendBotResponse(new EditMessageText()
                .setChatId(chatId)
                .setMessageId(toIntExact(messageId))
                .setText(text)
                .setReplyMarkup(buildCalendarMarkup(calendarViewDate)));
    }

    public void reRenderMessage(Long chatId, int messageId, String text, InlineKeyboardMarkup keyboard) {
        bot.sendBotResponse(new EditMessageText()
                .setChatId(chatId)
                .setMessageId(toIntExact(messageId))
                .setText(text)
                .setReplyMarkup(keyboard));
    }

    private InlineKeyboardMarkup buildCalendarMarkup(LocalDate date) {
        //calculations
        date = Optional.ofNullable(date).orElse(LocalDate.now());
        int daysInMonthAmount = date.getMonth().length(date.isLeapYear());
        LocalDate firstDayOfMonth = date.minusDays(date.getDayOfMonth() - 1L);
        int firstDayOffset = firstDayOfMonth.getDayOfWeek().getValue() - 1;
        int totalDayButtonsAmount = daysInMonthAmount + firstDayOffset;
        while (totalDayButtonsAmount % 7 != 0) {
            totalDayButtonsAmount++;
        }

        //markup
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        markupInline.setKeyboard(keyboard);

        InlineKeyboardButton yearToLeft = new InlineKeyboardButton().setText(" <<< ").setCallbackData(YEAR_BACK_CALLBACK);
        InlineKeyboardButton calendarYear = new InlineKeyboardButton().setText(String.valueOf(date.getYear())).setCallbackData(EMPTY_CALLBACK);
        InlineKeyboardButton yearToRight = new InlineKeyboardButton().setText(" >>> ").setCallbackData(YEAR_FORWARD_CALLBACK);
        List<InlineKeyboardButton> yearRow = new ArrayList<>(Arrays.asList(yearToLeft, calendarYear, yearToRight));
        keyboard.add(yearRow);

        InlineKeyboardButton monthToLeft = new InlineKeyboardButton().setText(" <<<<< ").setCallbackData(MONTH_BACK_CALLBACK);
        InlineKeyboardButton calendarMonth = new InlineKeyboardButton().setText(date.getMonth().getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault())).setCallbackData(EMPTY_CALLBACK);
        InlineKeyboardButton monthToRight = new InlineKeyboardButton().setText(" >>>>> ").setCallbackData(MONTH_FORWARD_CALLBACK);
        List<InlineKeyboardButton> monthRow = new ArrayList<>(Arrays.asList(monthToLeft, calendarMonth, monthToRight));
        keyboard.add(monthRow);

        List<InlineKeyboardButton> daysOfWeekRow = new ArrayList<>();
        daysOfWeekRow.add(new InlineKeyboardButton().setText("mon").setCallbackData(EMPTY_CALLBACK));
        daysOfWeekRow.add(new InlineKeyboardButton().setText("tue").setCallbackData(EMPTY_CALLBACK));
        daysOfWeekRow.add(new InlineKeyboardButton().setText("wed").setCallbackData(EMPTY_CALLBACK));
        daysOfWeekRow.add(new InlineKeyboardButton().setText("thu").setCallbackData(EMPTY_CALLBACK));
        daysOfWeekRow.add(new InlineKeyboardButton().setText("fri").setCallbackData(EMPTY_CALLBACK));
        daysOfWeekRow.add(new InlineKeyboardButton().setText("sat").setCallbackData(EMPTY_CALLBACK));
        daysOfWeekRow.add(new InlineKeyboardButton().setText("sun").setCallbackData(EMPTY_CALLBACK));
        keyboard.add(daysOfWeekRow);

        int dayOfWeekCounter = 1;
        List<InlineKeyboardButton> daysRow = new ArrayList<>(7);
        for (int i = 1 - firstDayOffset; i < totalDayButtonsAmount + 1; i++) {
            if (i <= 0 || (i > daysInMonthAmount && dayOfWeekCounter != 1)) {
                daysRow.add(new InlineKeyboardButton().setText(" . ").setCallbackData(EMPTY_CALLBACK));
            } else if (i <= daysInMonthAmount) {
                LocalDate buttonDate = date.withDayOfMonth(i);
                daysRow.add(new InlineKeyboardButton().setText(" " + i + " ").setCallbackData(buttonDate.toString()));
            }
            if (dayOfWeekCounter == 7) {
                keyboard.add(daysRow);
                daysRow = new ArrayList<>(7);
                dayOfWeekCounter = 1;
            } else {
                dayOfWeekCounter++;
            }
        }
        keyboard.add(Collections.singletonList(new InlineKeyboardButton("Back").setCallbackData(TO_BEGINNING)));
        return markupInline;
    }
}
