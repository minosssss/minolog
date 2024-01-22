package com.minolog.api.request;

import lombok.*;

import static java.lang.Math.max;
import static java.lang.Math.min;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostSearch {

    private static final Integer MAX_SIZE = 100;

    @Builder.Default
    private Integer page = 1;

    @Builder.Default
    private Integer perPage = 10;

    public long getOffset() {
        return (long) (max(1, page) - 1) * min(MAX_SIZE, perPage);
    }

}
