package com.minolog.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minolog.api.domain.Post;
import com.minolog.api.repository.PostRepository;
import com.minolog.api.request.PostCreate;
import com.minolog.api.request.PostEdit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }


    @Test
    @DisplayName("/posts 요청 시 400에러")
    void whenPostInvalidDataThrowBadRequest() throws Exception {
        PostCreate invalidRequest = PostCreate.builder().title("").content("").build();

        // expected
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest))
                )
                .andExpect(status().isBadRequest())
                .andDo(print());
    }




    @Test
    @DisplayName("POST /posts 요청 시 ValidError 리턴")
    void whenPostInvalidDataThrowErrors() throws Exception {
        PostCreate invalidRequest = PostCreate.builder().title("").content("").build();
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andDo(print());;
    }

    @Test
    @DisplayName("POST /posts 요청 시 정상적인 데이터는 성공 후 Post 객체반환")
    void whenPostDataIsOk() throws Exception {
        PostCreate invalidRequest = PostCreate.builder().title("제목").content("내용").build();
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest))
                )
                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.title").value("제목"))
                .andDo(print());;
    }

    @Test
    @DisplayName("글 1개 조회")
    void getPostsById() throws Exception {
        // given
        Post post = Post.builder()
                .title("foo")
                .content("bar")
                .build();

        Post save = postRepository.save(post);
        Long postId = save.getId();
        // expected
        assertEquals(save.getId(), post.getId());
        mockMvc.perform(get("/posts/{postId}", postId)
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andExpect(jsonPath("$.id").value(save.getId()))
                .andExpect(jsonPath("$.title").value("foo"))
                .andExpect(jsonPath("$.content").value("bar"))
                .andDo(print());
    }

    @Test
    @DisplayName("글 여러개 조회")
    void getPostsAll() throws Exception {
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

        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);

        // expected
        mockMvc.perform(get("/posts")
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$[0].title", is("foo1"))) // Check first post's title
                .andExpect(jsonPath("$[0].content", is("bar1"))) // Check first post's content
                .andExpect(jsonPath("$[1].title", is("foo2"))) // Check second post's title
                .andExpect(jsonPath("$[1].content", is("bar2"))) // Check second post's content
                .andExpect(jsonPath("$[2].title", is("foo3"))) // Check third post's title
                .andExpect(jsonPath("$[2].content", is("bar3"))) // Check third post's content
                .andDo(print());

    }

    @Test
    @DisplayName("QueryDSL 글 1페이지 조회")
    void queryDslGetPage1() throws Exception {
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

        // expected
        mockMvc.perform(get("/posts?page=1&perPage=30")
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(30)))
                .andDo(print());

    }


    @Test
    @DisplayName("글 제목 수정")
    void postTitleEdit() throws Exception {
        // given
        Post post = Post.builder().title("농농").content("아루미").build();

        postRepository.save(post);
        PostEdit editPost = PostEdit.builder().title("마이노").content("요우").build();
        // expected
        mockMvc.perform(patch("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON_VALUE)
                        .characterEncoding("utf-8")
                        .content(objectMapper.writeValueAsString(editPost))
                )
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    @DisplayName("글 삭제")
    void postDelete() throws Exception {
        // given
        Post post = Post.builder().title("농농").content("아루미").build();

        postRepository.save(post);
        // expected
        mockMvc.perform(delete("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON_VALUE)
                        .characterEncoding("utf-8")
                )
                .andExpect(status().isOk())
                .andDo(print());

    }


    @Test
    @DisplayName("글 1개 조회")
    void handleException() throws Exception {
        // given
        Post post = Post.builder()
                .title("농농")
                .content("호우")
                .build();

        Post save = postRepository.save(post);
        Long postId = save.getId();
        // expected
        assertEquals(save.getId(), post.getId());
        mockMvc.perform(get("/posts/{postId}", postId)
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andExpect(jsonPath("$.id").value(save.getId()))
                .andExpect(jsonPath("$.title").value("foo"))
                .andExpect(jsonPath("$.content").value("bar"))
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 게시글 삭제")
    void notFountPostDelete() throws Exception {
        mockMvc.perform(delete("/posts/{postId}", 1L)
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 게시글 수정")
    void notFountPostEdit() throws Exception {
        // given
        Post post = Post.builder().title("농농").content("아루미").build();

        postRepository.save(post);
        PostEdit editPost = PostEdit.builder().title("마이노").content("요우").build();
        // expected
        mockMvc.perform(patch("/posts/{postId}", post.getId() + 1L)
                        .contentType(APPLICATION_JSON_VALUE)
                        .characterEncoding("utf-8")
                        .content(objectMapper.writeValueAsString(editPost))
                )
                .andExpect(status().isNotFound())
                .andDo(print());
    }


    @Test
    @DisplayName("POST 글 작성 시, 농농 validation 체크")
    void whenPostTextCheck() throws Exception {
        PostCreate invalidRequest = PostCreate.builder().title("농농").content("내용").build();
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest))
                )
                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.title").value("제목"))
                .andDo(print());;
    }



}