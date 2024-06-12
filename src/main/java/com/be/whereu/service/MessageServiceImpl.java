package com.be.whereu.service;
import com.be.whereu.exception.ResourceNotFoundException;
import com.be.whereu.model.dto.MemberDto;
import com.be.whereu.model.dto.MessageDto;
import com.be.whereu.model.entity.MemberEntity;
import com.be.whereu.model.entity.MessageEntity;
import com.be.whereu.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService{

    private final MessageRepository messageRepository;

    @Override
    public List<MessageDto> getMessageList(Long chatId) {
        List<MessageEntity> entities= messageRepository.findByChatIdOrderByCreateAtAsc(chatId).orElseThrow(
                () -> new ResourceNotFoundException("Not found message")
        );
        return entities.stream()
                .map(MessageDto::toDto)
                .toList();
    }

    @Override
    public MessageDto messageSave(MessageDto messageDto, Long memberId, Long chatId) {

        MessageEntity entity=messageRepository.save(MessageEntity.toEntity(messageDto,memberId,chatId));
        return   MessageDto.toDto(entity);

    }
}
