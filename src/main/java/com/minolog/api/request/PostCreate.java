package com.minolog.api.request;

import com.minolog.api.domain.Post;
import com.minolog.api.exception.InvalidRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class PostCreate {

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @Builder
    private PostCreate(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void validate() {
        if (title.contains("농농")) {
            throw new InvalidRequest("title", "제목에 '농농'을 포함할 수 없습니다.");
        }
    }



}
