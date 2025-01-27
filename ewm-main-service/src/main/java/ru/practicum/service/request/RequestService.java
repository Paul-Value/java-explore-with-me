package ru.practicum.service.request;

import ru.practicum.dto.request.ParticipationRequestDto;

import java.util.List;

public interface RequestService {
    List<ParticipationRequestDto> findAll(Long userId);

    ParticipationRequestDto create(long userId, long eventId);

    ParticipationRequestDto cancelRequest(long userId, long requestId);
}
