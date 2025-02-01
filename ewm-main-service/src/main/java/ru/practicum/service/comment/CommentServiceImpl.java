package ru.practicum.service.comment;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.NewCommentDto;
import ru.practicum.dto.comment.UpdateCommentDto;
import ru.practicum.enums.StateOfPublication;
import ru.practicum.exception.AccessCommentException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.PublicationEventException;
import ru.practicum.mapper.CommentMapper;
import ru.practicum.model.Comment;
import ru.practicum.model.Event;
import ru.practicum.model.QComment;
import ru.practicum.model.User;
import ru.practicum.repository.CommentRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository usersRepository;
    private final EventRepository eventRepository;

    @Transactional
    @Override
    public CommentDto create(Long userId, Long eventId, NewCommentDto requestBody) {
        log.debug("==> Create comment for event: user: {} event: {}, request body: {}", userId, eventId, requestBody);
        User author = usersRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found"));
        if (event.getState() != StateOfPublication.PUBLISHED) {
            throw new PublicationEventException("Event not published");
        }
        Comment model = CommentMapper.dtoToMapper(author, event, requestBody);
        model = commentRepository.save(model);
        int commentsCount = commentRepository.countByEventId(eventId);
        CommentDto result = CommentMapper.modelToDto(model, commentsCount);
        log.debug("<== Created comment: {}", result);
        return result;
    }

    @Transactional
    @Override
    public CommentDto change(Long userId, Long comId, UpdateCommentDto requestBody) {
        log.debug("==> Change comment: user: {} comment id: {}, request body: {} ", userId, comId, requestBody);
        if (!comId.equals(requestBody.getId())) {
            throw new IllegalArgumentException("The comment ID in the URL and the request body do not match.");
        }
        Comment model = commentRepository.findById(comId).orElseThrow(() -> new NotFoundException("Comment not found"));
        if (model.getAuthor().getId() != userId) {
            throw new AccessCommentException("You do not have permission to edit this comment.");
        }
        model.setText(requestBody.getText());
        model.setUpdatedAt(LocalDateTime.now());
        model = commentRepository.save(model);
        int commentsCount = commentRepository.countByEventId(model.getEvent().getId());
        CommentDto result = CommentMapper.modelToDto(model, commentsCount);
        log.debug("<== Changed comment: {}", result);
        return result;
    }

    @Transactional
    @Override
    public void delete(Long userId, Long comId) {
        log.debug("==> Delete comment: user: {} comment id: {}", userId, comId);
        Comment model = commentRepository.findById(comId).orElseThrow(() -> new NotFoundException("Comment not found"));
        if (model.getAuthor().getId() != userId) {
            throw new AccessCommentException("You do not have permission to edit this comment.");
        }
        commentRepository.deleteById(comId);
        log.debug("<== Comment deleted )");
    }

    @Override
    public void delete(Long comId) {
        log.debug("==> Delete comment id: {}", comId);
        commentRepository.deleteById(comId);
        log.debug("<== Comment deleted )");
    }

    @Override
    public List<CommentDto> findAllByEventId(Long eventId, Integer from, Integer size) {
        log.debug("==> Find all comments by event id: {}", eventId);
        BooleanExpression predicate = QComment.comment.event.id.eq(eventId);
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageRequest = PageRequest.of(from / size, size, sort);
        List<Comment> models = commentRepository.findAll(predicate, pageRequest).stream().toList();
        if (models.isEmpty()) {
            return List.of();
        }
        int commentsCount = commentRepository.countByEventId(eventId);
        List<CommentDto> result = models.stream()
                .map((Comment model) -> CommentMapper.modelToDto(model, commentsCount))
                .toList();
        log.debug("<== Found all comments: {}", result);
        return result;
    }

    @Override
    public CommentDto getCommentByUserAndCommentId(Long userId, Long comId) {
        log.debug("==> Get comment by user {} and comment id {}", userId, comId);
        Comment comment = commentRepository.findById(comId)
                .orElseThrow(() -> new NotFoundException("Comment not found"));
        if (!comment.getAuthor().getId().equals(userId)) {
            throw new AccessCommentException("Access denied");
        }
        int commentsCount = commentRepository.countByEventId(comment.getEvent().getId());
        return CommentMapper.modelToDto(comment, commentsCount);
    }

    @Override
    public List<CommentDto> findAllByUserIdAndEventId(Long userId, Long eventId, Integer from, Integer size) {
        log.debug("==> Find comments by user {} and event {}", userId, eventId);
        BooleanExpression predicate = QComment.comment.author.id.eq(userId)
                .and(QComment.comment.event.id.eq(eventId));
        Pageable pageable = PageRequest.of(from / size, size, Sort.by("created").ascending());
        int commentsCount = commentRepository.countByEventId(eventId);
        return commentRepository.findAll(predicate, pageable).stream()
                .map((Comment model) -> CommentMapper.modelToDto(model, commentsCount))
                .toList();
    }
}
