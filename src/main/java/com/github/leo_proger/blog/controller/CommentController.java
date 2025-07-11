package com.github.leo_proger.blog.controller;

import com.github.leo_proger.blog.dto.CommentDTO;
import com.github.leo_proger.blog.service.CommentService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/posts/comments")
public class CommentController {

    private final CommentService commentService;
    private final Validator validator;

    public CommentController(CommentService commentService, Validator validator) {
        this.commentService = commentService;
        this.validator = validator;
    }

    @ResponseBody
    @PostMapping("/create/{postID}")
    public ResponseEntity<?> createComment(@RequestParam("text") String text, @PathVariable Long postID, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        CommentDTO commentDTO = new CommentDTO(principal.getName(), postID, text);

        Set<ConstraintViolation<CommentDTO>> violations = validator.validate(commentDTO);
        if (!violations.isEmpty()) {
            Map<String, String> errors = new HashMap<>();

            for (ConstraintViolation<CommentDTO> violation : violations) {
                String field = violation.getPropertyPath().toString();
                String message = violation.getMessage();
                errors.put(field, message);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }

        CommentDTO processedCommentDTO = commentService.createComment(principal.getName(), postID, text);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("Comment", processedCommentDTO));
    }
}
