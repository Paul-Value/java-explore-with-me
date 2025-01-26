package ru.practicum.service.event;

import ru.practicum.dto.event.*;
import ru.practicum.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.dto.request.ParticipationRequestDto;

import java.util.List;

public interface EventService {
    List<EventShortDto> findAll(long userId, int from, int size);

    EventFullDto create(long userId, NewEventDto requestBody);

    EventFullDto findById(long userId, long eventId);

    EventFullDto update(long userId, long eventId, UpdateEventUserRequest requestBody);

    List<ParticipationRequestDto> findRequests(long userId, long eventId);

    EventRequestStatusUpdateResult updateStatusRequest(long userId, long eventId, EventRequestStatusUpdateRequest requestBody);

    List<EventFullDto> findAll(AdminParamEvent build);

    List<EventFullDto> findAllById(List<Long> eventIds);

    EventFullDto update(Long eventId, UpdateEventAdminRequest requestBody);

    List<EventShortDto> findAll(PublicParamEvent build);

    EventFullDto findById(Long id);
}
