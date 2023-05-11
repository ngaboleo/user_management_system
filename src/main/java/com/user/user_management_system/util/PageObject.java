package com.user.user_management_system.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class PageObject {
    private static final int defaultSize = 10;
    private static final int defaultPageNumber = 0;
    public static Pageable getPageable(Integer pageNumber, Integer pageSize){
        if (pageNumber != null && pageSize != null){
            pageNumber --;
            return PageRequest.of(pageNumber, pageSize);
        }else if (pageNumber != null){
            pageNumber --;
            return PageRequest.of(pageNumber, defaultSize);
        } else if (pageSize != null) {
            return PageRequest.of(defaultPageNumber, pageSize);
        }else {
            return PageRequest.of(defaultPageNumber, defaultSize);
        }
    }
}
