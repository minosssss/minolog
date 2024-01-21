package com.minolog.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minolog.api.domain.Post;
import com.minolog.api.request.PostCreate;
import com.minolog.api.response.PostResponse;
import com.minolog.api.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final ObjectMapper objectMapper;

    @PostMapping("/posts")
    public void post(@RequestBody @Valid PostCreate create) {
        postService.write(create);
    }

    @GetMapping("/posts/{postId}")
    public PostResponse get(@PathVariable("postId") Long postId) {
        return postService.get(postId);
    }

//    @GetMapping("/posts")
//    public List<PostResponse> getList() {
//        return postService.getList();
//    }

    @GetMapping("/posts")
    public List<PostResponse> getList(Pageable page) {
        return postService.getListPageable(page);
    }

    @GetMapping("/postsTest")
    public ResponseEntity<?> getPosts(Pageable pageable) {
        List<PostResponse> posts = postService.getTestPageable(pageable);
        return ResponseEntity.ok().body(posts);
    }
}
