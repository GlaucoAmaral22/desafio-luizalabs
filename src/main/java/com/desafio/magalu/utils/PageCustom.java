package com.desafio.magalu.utils;

import java.io.Serializable;
import java.util.List;

public class PageCustom implements Serializable {

    public List<?> content;
    public Info info = new Info();


    public PageCustom(List<?> list, int pageNumber, int pageSize) {
        this.content = list;
        this.info.pageNumber = pageNumber;
        this.info.pageSize = pageSize;
    }

    public static class Info implements Serializable{
        public int pageNumber;

        public int pageSize;
    }


}
