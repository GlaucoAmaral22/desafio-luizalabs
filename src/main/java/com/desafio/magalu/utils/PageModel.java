package com.desafio.magalu.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PageModel implements Serializable {
    private int pageNumber = 1;

    public int totalPages = 0;

    private int pageSize = 5;

    private boolean hasNextPage = false;

    private int totalRows = 0;

    private int pageStartRow = 0;

    private int pageEndRow = 0;

    private List list;

    public PageModel(List list, int pageSize, int pageNumber) {
        init(list, pageSize, pageNumber);
    }

    public void init(List list, int pageSize, int pageNumber) {
        this.pageSize = pageSize;
        this.list = list;
        this.totalRows = list.size();
        this.pageNumber = pageNumber;
        if ((totalRows % pageSize) == 0) {
            totalPages = totalRows / pageSize;
        } else {
            totalPages = totalRows / pageSize + 1;
        }

    }

    public PageCustom getPagination() {

        List content = this.getObjects(pageNumber);

        PageCustom pageCustom = new PageCustom(content, pageNumber, pageSize);

        return pageCustom;

    }


    public List getObjects(int page) {
        if(pageNumber > totalPages){
            return null;
        }

        if (page * pageSize < totalRows) {//  Judge if it is last 1 page
            pageEndRow = page * pageSize;
            pageStartRow = pageEndRow - pageSize;
        } else {
            pageEndRow = totalRows;
            pageStartRow = pageSize * (totalPages - 1);
        }

        List objects = null;
        if (!list.isEmpty()) {
            objects = new ArrayList(list.subList(pageStartRow, pageEndRow));
        }
        return objects;
    }



    public List getList() {
        return list;
    }


    public void setList(List list) {
        this.list = list;
    }


    public int getPageNumber() {
        return pageNumber;
    }


    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }


    public int getTotalPages() {
        return totalPages;
    }


    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }


    public int getTotalRows() {
        return totalRows;
    }


    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageStartRow() {
        return pageStartRow;
    }

    public void setPageStartRow(int pageStartRow) {
        this.pageStartRow = pageStartRow;
    }

    public int getPageEndRow() {
        return pageEndRow;
    }

    public void setPageEndRow(int pageEndRow) {
        this.pageEndRow = pageEndRow;
    }
}
