package ru.practicum.service.request;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.request.ConfirmedRequest;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.enums.StateOfPublication;
import ru.practicum.enums.StatusParticipationRequest;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.RequestModificationException;
import ru.practicum.mapper.RequestMapper;
import ru.practicum.model.Event;
import ru.practicum.model.Request;
import ru.practicum.model.User;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.RequestRepository;
import ru.practicum.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UserRepository usersRepository;

    @Transactional(readOnly = true)
    @Override
    public List<ParticipationRequestDto> findAll(Long userId) {
        log.debug("==> Find all requests by user id {}", userId);
        BooleanExpression predicate = QRequest.request.requester.id.eq(userId);
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Iterable<Request> requests = requestRepository.findAll(predicate, sort);
        List<ParticipationRequestDto> result = new ArrayList<>();
        requests.forEach(request -> result.add(RequestMapper.modelToDto(request)));
        log.debug("<== Found all requests  {}", result);
        return result;
    }

    @Transactional
    @Override
    public ParticipationRequestDto create(long userId, long eventId) {
        log.debug("==> Create request by user id {} for event id {}", userId, eventId);
        if (requestRepository.existsByRequesterIdAndEventId(userId, eventId)) {
            throw new RequestModificationException("It is not possible to add a repeated request to participate in an event");
        }
        Event eventModel = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found by id: " + eventId));
        if (eventModel.getInitiator().getId() == userId) {
            throw new RequestModificationException("The user cannot add a request to participate in their event");
        }
        if (!eventModel.getState().equals(StateOfPublication.PUBLISHED)) {
            throw new RequestModificationException("The event is not published");
        }
        List<ConfirmedRequest> listConfirmedRequests = requestRepository.getConfirmedRequestsByStatus(List.of(eventId), StatusParticipationRequest.CONFIRMED);
        long confirmedRequest = listConfirmedRequests.isEmpty() ? 0 : listConfirmedRequests.get(0).getConfirmedCountRequests();
        int participantLimit = eventModel.getParticipantLimit();
        if (participantLimit != 0 && participantLimit <= confirmedRequest) {
            throw new RequestModificationException("The event has reached the limit of requests for participation");
        }
        StatusParticipationRequest status = (eventModel.isRequestModeration() && eventModel.getParticipantLimit() != 0) ? StatusParticipationRequest.PENDING : StatusParticipationRequest.CONFIRMED;
        User requester = usersRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found by id: " + userId));
        Request requestModel = requestRepository.save(Request.builder()
                .created(LocalDateTime.now())
                .event(eventModel)
                .requester(requester)
                .status(status)
                .build());
        log.debug("<== Created request {}", requestModel);
        return RequestMapper.modelToDto(requestModel);
    }

    @Transactional
    @Override
    public ParticipationRequestDto cancelRequest(long userId, long requestId) {
        log.debug("==> Update request id {}", requestId);
        Request requestModel = requestRepository.findById(requestId).orElseThrow(() -> new NotFoundException("Request not found by id: " + requestId));
        if (requestModel.getRequester().getId() != userId) {
            throw new RequestModificationException("It is not possible to update a request");
        }
        requestModel.setStatus(StatusParticipationRequest.CANCELED);
        requestModel = requestRepository.save(requestModel);
        log.debug("<== Updated request {}", requestModel);
        return RequestMapper.modelToDto(requestModel);
    }
}
