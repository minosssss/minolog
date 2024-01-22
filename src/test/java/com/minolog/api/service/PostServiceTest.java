package com.minolog.api.service;

import com.minolog.api.domain.Post;
import com.minolog.api.repository.PostRepository;
import com.minolog.api.request.PostCreate;
import com.minolog.api.request.PostSearch;
import com.minolog.api.response.PostResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.data.domain.Sort.Direction.DESC;

@SpringBootTest
class PostServiceTest {
    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Test
    @DisplayName("글 저장 후 조회")
    void getPostsById() {
        // given
        Post requestPost = Post.builder()
                .title("foo").content("bar").build();
        postRepository.save(requestPost);

        // when
        PostResponse post = postService.get(requestPost.getId());

        // then
        Assertions.assertNotNull(post);
        assertEquals(post.getId(), requestPost.getId());
    }

    @Test
    @DisplayName("글 여러개 저장 후 조회")
    void getList() {
        // given
        Post post1 = Post.builder()
                .title("foo1")
                .content("bar1")
                .build();
        Post post2 = Post.builder()
                .title("foo2")
                .content("bar2")
                .build();
        Post post3 = Post.builder()
                .title("foo3")
                .content("bar3")
                .build();

        postRepository.saveAll(List.of(
                Post.builder()
                        .title("foo1")
                        .content("bar1")
                        .build(),
                Post.builder()
                        .title("foo2")
                        .content("bar2")
                        .build(),
                Post.builder()
                        .title("foo3")
                        .content("bar3")
                        .build()
        ));
//        postRepository.save(post2);
//        postRepository.save(post3);

        // when
        List<PostResponse> postList = postService.getList();

        // then
        Assertions.assertNotNull(postList);
        assertEquals(postList.size(), 3L);
    }

    @Test
    @DisplayName("글 작성")
    void writePost() {
        // given
        PostCreate postCreate = PostCreate.builder()
                        .title("제목").content("내용").build();
        // when
        postService.write(postCreate);

        // then
        assertEquals(1L, postRepository.count());
    }

    /**
     * 페이징 처리
     */
    @Test
    @DisplayName("글 1페이지 조회")
    void getListPagination() {
        // given
        List<Post> posts = IntStream.range(1, 31)
                .mapToObj(i -> {
                    return Post.builder()
                            .title("제목-" + i)
                            .content("내용-" + i)
                            .build();
                })
                .collect(Collectors.toList());

        postRepository.saveAll(posts);

        Pageable pageable = PageRequest.of(0, 5, DESC, "id");
        // when
        List<PostResponse> postList = postService.getListPageable(pageable);

        // then
        Assertions.assertNotNull(postList);
        assertEquals(postList.size(), 5);
    }

    /**
     * QueryDSL 적용
     */
    @Test
    @DisplayName("QueryDSL Page조회 / 10개")
    void queryDslGetPageList() {
        // given
        List<Post> posts = IntStream.range(0, 20)
                .mapToObj(i -> {
                    return Post.builder()
                            .title("제목-" + i)
                            .content("내용-" + i)
                            .build();
                })
                .collect(Collectors.toList());

        postRepository.saveAll(posts);

        PostSearch search = PostSearch.builder().build();
        // when
        List<PostResponse> postList = postService.getList(search);

        // then
        Assertions.assertNotNull(postList);
        assertEquals(postList.size(), 10);
        assertEquals("제목-19", postList.get(0).getTitle());
    }
}