package com.piche.tasktracker.model;

import java.util.List;

public class Page<T>{
    private List<T> content;
    private int pageNum;
    private int pageSize;
    private int totalItems;
    private String sortField;
    private SortOrder sortOrder;

    public Page(List<T> content, int pageNum, int pageSize, int totalItems, String sortField, SortOrder sortOrder) {
        this.content = content;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.totalItems = totalItems;
        this.sortField = sortField;
        this.sortOrder = sortOrder;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public SortOrder getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(SortOrder sortOrder) {
        this.sortOrder = sortOrder;
    }
}
