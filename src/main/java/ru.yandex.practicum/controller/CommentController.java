package ru.yandex.practicum.controller;

import ru.yandex.practicum.dto.CommentDto;
import ru.yandex.practicum.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
@CrossOrigin(origins = "*")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping
    public CommentDto createComment(@PathVariable Long postId, @RequestBody CommentDto commentDto) {
        return commentService.createComment(postId, commentDto);
    }

    @GetMapping
    public List<CommentDto> getComments(@PathVariable Long postId) {
        return commentService.getComments(postId);
    }

    @GetMapping("/{commentId}")
    public CommentDto getComment(@PathVariable Long postId, @PathVariable Long commentId) {
        return commentService.getComment(postId, commentId);
    }

    @PutMapping("/{commentId}")
    public CommentDto updateComment(@PathVariable Long postId, @PathVariable Long commentId, @RequestBody CommentDto commentDto) {
        return commentService.updateComment(postId, commentId, commentDto);
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable Long postId, @PathVariable Long commentId) {
        commentService.deleteComment(postId, commentId);
    }
}
