package com.exadel.sandbox.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterRequest {

    private Long locationId = 0L;

    private boolean isCity;

    private Set<Long> categoriesIdSet = new HashSet<>();

    private Set<Long> tagsIsSet = new HashSet<>();

    private Set<Long> vendorsIdSet = new HashSet<>();

    private String timeFilter = "";

//    private SortType sortingType;

}
