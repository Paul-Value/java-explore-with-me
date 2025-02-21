package ru.practicum.service.comment;

import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.NewCommentDto;
import ru.practicum.dto.comment.UpdateCommentDto;

import java.util.List;

public interface CommentService {
    CommentDto create(Long userId, Long eventId, NewCommentDto requestBody);

    CommentDto change(Long userId, Long comId, UpdateCommentDto requestBody);

    void delete(Long userId, Long comId);

    void delete(Long comId);

    List<CommentDto> findAllByEventId(Long eventId, Integer from, Integer size);

    CommentDto getCommentByUserAndCommentId(Long userId, Long comId);

    List<CommentDto> findAllByUserIdAndEventId(Long userId, Long eventId, Integer from, Integer size);
}
