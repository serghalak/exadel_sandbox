package com.exadel.sandbox.dto.pagelist;

import com.exadel.sandbox.dto.CategoryDto;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class CategoryPagedList extends PageImpl<CategoryDto> {

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public CategoryPagedList(@JsonProperty("content") List<CategoryDto> content,
                             @JsonProperty("number") int number,
                             @JsonProperty("size") int size,
                             @JsonProperty("totalElements") Long totalElements,
                             @JsonProperty("pageable") JsonNode pageable,
                             @JsonProperty("last") boolean last,
                             @JsonProperty("totalPages") int totalPages,
                             @JsonProperty("sort") JsonNode sort,
                             @JsonProperty("first") boolean first,
                             @JsonProperty("numberOfElements") int numberOfElements) {

        super(content, PageRequest.of(number, size), totalElements);
    }

    public CategoryPagedList(List<CategoryDto> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    public CategoryPagedList(List<CategoryDto> content) {
        super(content);
    }
}
