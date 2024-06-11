package com.be.whereu.service;

import com.be.whereu.model.dto.MessageDto;

import java.util.List;

public interface MessageService {
    List<MessageDto> getMessageList (Long chatId);
    MessageDto messageSave(MessageDto messageDto, Long memberId, Long chatId);
}
