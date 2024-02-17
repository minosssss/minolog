package com.minolog.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minolog.api.config.data.UserSession;
import com.minolog.api.request.PostCreate;
import com.minolog.api.request.PostEdit;
import com.minolog.api.request.PostSearch;
import com.minolog.api.response.PostResponse;
import com.minolog.api.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final ObjectMapper objectMapper;

    @GetMapping("/hello")
    public Long hello(UserSession userSession) {
        log.info("[@UserSession] : {}", userSession);
        return userSession.id;
    }

    @PostMapping("/posts")
    public void post(@RequestBody @Valid PostCreate create, @RequestHeader String auth) {
        if (auth.equals("minolog")) {
            create.validate();
            postService.write(create);
        }
    }

    @GetMapping("/posts/{postId}")
    public PostResponse get(@PathVariable("postId") Long postId) {
        return postService.get(postId);
    }

    @GetMapping("/posts")
    public ResponseEntity<?> getPosts(@ModelAttribute PostSearch postSearch) {
        List<PostResponse> posts = postService.getList(postSearch);
        return ResponseEntity.ok().body(posts);
    }

    @PatchMapping("/posts/{postId}")
    public PostResponse post(@PathVariable("postId") Long postId, @RequestBody @Valid PostEdit request) {
        log.info(String.valueOf(postService.get(postId).getTitle()));
        PostResponse edit = postService.edit(postId, request);
        log.info(String.valueOf(postService.get(postId).getTitle()));
        return edit;
    }

    @DeleteMapping("/posts/{postId}")
    public void post(@PathVariable("postId") Long postId) {
        postService.delete(postId);
    }

}
