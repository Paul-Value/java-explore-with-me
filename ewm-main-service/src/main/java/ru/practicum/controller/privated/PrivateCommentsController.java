package ru.practicum.controller.privated;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.NewCommentDto;
import ru.practicum.dto.comment.UpdateCommentDto;
import ru.practicum.service.comment.CommentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/users/{userId}/comments")
@Slf4j
public class PrivateCommentsController {
    private final CommentService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto create(@PathVariable @NotNull
                             @Positive Long userId, @RequestParam("eventId") @NotNull
                             @Positive
                             Long eventId,
                             @RequestBody
                             @Valid NewCommentDto requestBody) {
        log.info("==> Created new comment: user id {}, event id {}, request body {}", userId, eventId, requestBody);
        return service.create(userId, eventId, requestBody);
    }

    @PatchMapping("/{comId}")
    public CommentDto change(@PathVariable @NotNull @Positive Long userId,
                             @PathVariable @NotNull @Positive Long comId,
                             @RequestBody @Valid UpdateCommentDto requestBody) {
        log.info("==> Change comment: user id {}, comment id {}, request body {}", userId, comId, requestBody);
        return service.change(userId, comId, requestBody);
    }

    @DeleteMapping("/{comId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @NotNull
                       @Positive Long userId, @PathVariable @NotNull
                       @Positive Long comId) {
        log.info("==> Delete comment: user id {}, comment id {}", userId, comId);
        service.delete(userId, comId);
    }

    @GetMapping("/{comId}")
    public CommentDto getComment(
            @PathVariable @NotNull @Positive Long userId,
            @PathVariable @NotNull @Positive Long comId) {
        log.info("==> Get comment: user id {}, comment id {}", userId, comId);
        return service.getCommentByUserAndCommentId(userId, comId);
    }

    @GetMapping
    public List<CommentDto> getCommentsByUserAndEvent(
            @PathVariable @NotNull @Positive Long userId,
            @RequestParam @NotNull @Positive Long eventId,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size) {
        log.info("==> Get comments by user id {} and event id {}", userId, eventId);
        return service.findAllByUserIdAndEventId(userId, eventId, from, size);
    }
}
