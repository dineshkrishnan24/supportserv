package com.growfin.supportserv.constants.vo;

import lombok.Data;

import java.util.List;

@Data
public class ResourceCollectionVO<T> {

    private List<T> items;

    private long totalItems;

    private long totalPages;

    private long currentPage;

}

