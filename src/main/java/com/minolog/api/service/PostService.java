package com.minolog.api.service;

import com.minolog.api.domain.Post;
import com.minolog.api.domain.PostEditor;
import com.minolog.api.exception.PostNotFound;
import com.minolog.api.repository.PostRepository;
import com.minolog.api.request.PostCreate;
import com.minolog.api.request.PostEdit;
import com.minolog.api.request.PostSearch;
import com.minolog.api.response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public PostResponse get(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);

        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .build();
    }

    public void write(PostCreate postCreate) {
        // postCreate => domain (Post)
//        Post post = new Post(postCreate.getTitle(), postCreate.getContent());
        Post post = Post.builder()
                .title(postCreate.getTitle())
                .content(postCreate.getContent())
                .build();
        postRepository.save(post);

    }

    public List<PostResponse> getList() {
        return postRepository.findAll().stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }


    public List<PostResponse> getListPageable(Pageable page) {
        return postRepository.findAll(page).stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }

    public List<PostResponse> getList(PostSearch postSearch) {
        return postRepository.getList(postSearch).stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public PostResponse edit(Long id, PostEdit postEdit) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);

        PostEditor.PostEditorBuilder postEditorBuilder = post.toEditor();
        PostEditor postEditor = postEditorBuilder
                .title(postEdit.getTitle())
                .content(postEdit.getContent())
                .build();
        post.edit(postEditor);
        return new PostResponse(post);
    }

    public void delete(Long id) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFound::new);;
        postRepository.delete(post);
    }
}
