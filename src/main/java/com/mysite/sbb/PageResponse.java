package com.mysite.sbb;

import lombok.Getter;

import java.util.List;

@Getter
public class PageResponse<T> {
    private final List<T> content;      // 게시물 데이터 (content)
    private final Long totalElements;   // 전체 게시물 개수
    private final int totalPages;       // 전체 페이지 개수
    private final int size;             // 페이지당 보여줄 개수
    private final int number;           // 현재 페이지 번호 (0부터 시작)

    // 생성자에서 모든 계산을 끝냅니다.
    public PageResponse(List<T> content, Long totalElements, int page, int size) {
        this.content = content;
        this.totalElements = totalElements;
        this.number = page;
        this.size = size;

        if (totalElements == 0) {
            this.totalPages = 0;
        } else {
            this.totalPages = (int) Math.ceil((double) totalElements / size);
        }
    }

    public boolean isEmpty() {
        return content == null || content.isEmpty();
    }

    public boolean hasPrevious() {
        return number > 0;
    }

    public boolean hasNext() {
        return number < totalPages - 1;
    }
}
