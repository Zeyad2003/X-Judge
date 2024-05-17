package com.xjudge.service.email.util;

import java.util.List;

public class Pagination<T> {

    private final List<T> data;

    public Pagination(List<T> data){
        this.data = data;
    }
    public List<T> getPagingData(int pageNumber , int sizeOfPage){
        int startIndex = pageNumber * sizeOfPage;
        int endIndex = Math.min(startIndex + sizeOfPage , data.size());
        return (startIndex > endIndex)? List.of() :data.subList(startIndex , endIndex);
    }
}
