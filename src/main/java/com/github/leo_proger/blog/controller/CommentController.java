package com.github.leo_proger.blog.controller;

import com.github.leo_proger.blog.dto.CommentDTO;
import com.github.leo_proger.blog.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@Controller()
@RequestMapping("/posts/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @ResponseBody
    @PostMapping("/create/{postID}")
    public ResponseEntity<?> createComment(@RequestParam("text") String text, @PathVariable Long postID, Principal principal) {
        CommentDTO commentDTO = commentService.createComment(principal.getName(), postID, text);
        return new ResponseEntity<>(Map.of("Comment", commentDTO), HttpStatus.OK);
    }
}
