package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.NewCommentDto;
import ru.practicum.model.Comment;
import ru.practicum.model.Event;
import ru.practicum.model.User;

import java.time.LocalDateTime;

@UtilityClass
public class CommentMapper {
    public static Comment dtoToMapper(User author, Event event, NewCommentDto requestBody) {
        return Comment.builder()
                .author(author)
                .event(event)
                .text(requestBody.getText())
                .created(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static CommentDto modelToDto(Comment model, int commentsCount) {
        return CommentDto.builder()
                .id(model.getId())
                .created(model.getCreated())
                .updatedAt(model.getUpdatedAt())
                .text(model.getText())
                .author(UserMapper.modelToUserShortDto(model.getAuthor()))
                .event(EventMapper.modelToEventTitleDto(model.getEvent(), commentsCount))
                .build();
    }
}
