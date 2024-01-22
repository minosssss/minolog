package com.minolog.api.repository;

import com.minolog.api.domain.Post;
import com.minolog.api.request.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getList(PostSearch postSearch);
}
