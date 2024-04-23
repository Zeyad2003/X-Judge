package com.xjudge.util;

import java.util.List;

public class Pagination<T> {

    public List<T> getPagingData(List<T> data , int pageNumber , int sizeOfPage){
        int startIndex = pageNumber * sizeOfPage;
        int endIndex = Math.min(startIndex + sizeOfPage , data.size());
        return data.subList(startIndex , endIndex);
    }
}
