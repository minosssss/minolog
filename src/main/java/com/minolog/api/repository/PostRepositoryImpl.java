package com.minolog.api.repository;

import com.minolog.api.domain.Post;
import com.minolog.api.domain.QPost;
import com.minolog.api.request.PostSearch;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.minolog.api.domain.QPost.post;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Post> getList(PostSearch postSearch) {
        return jpaQueryFactory.selectFrom(post)
                .limit(postSearch.getPerPage())
                .offset((long) (postSearch.getPage() - 1) * postSearch.getPerPage())
                .orderBy(post.id.asc())
                .fetch();
    }


}
