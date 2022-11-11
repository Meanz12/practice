package com.week06.team01.controller;

import com.week06.team01.controller.request.PostRequestDto;
import com.week06.team01.controller.response.Message;
import com.week06.team01.controller.response.PostResponseDto;
import com.week06.team01.controller.response.ResponseDto;
import com.week06.team01.repository.PostRepository;
import com.week06.team01.repository.PostlikeRepository;
import com.week06.team01.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
public class PostController {
    private final PostService postService;
    private final PostRepository postRepository;
    private final PostlikeRepository postlikeRepository;
    @PostMapping("/api/moviepost")
    public ResponseEntity<Message> createPost(@RequestBody PostRequestDto requestDto, HttpServletRequest request) throws Exception {
        return postService.createPost(requestDto, request);
    }
    @GetMapping("/api/main")
    public ResponseDto<?> getAllPost() {
        return postService.getAllPost();
    }

    @GetMapping("/api/moviepost/{postId}")
    public PostResponseDto getOnePost(@PathVariable Long postId) {
        return postService.getOnePost(postId);
    }

    @DeleteMapping("/api/moviepost/{postId}")
    public ResponseEntity<Message> deletePost(@PathVariable Long postId, HttpServletRequest request) {
        return postService.deletePost(postId, request);
    }
    @PutMapping("/api/moviepost/{postId}")
    public ResponseEntity<Message> updatePost(@PathVariable Long postId, @RequestBody PostRequestDto requestDto, HttpServletRequest request) throws Exception {
        return postService.updatePost(postId, requestDto, request);
    }
}