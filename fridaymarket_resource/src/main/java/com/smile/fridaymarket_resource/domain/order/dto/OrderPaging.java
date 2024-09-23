package com.smile.fridaymarket_resource.domain.order.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class OrderPaging {

    private int contentCnt;
    private List<?> content = new ArrayList<>();
    private int pageSize;
    private int page;
    private int totalPage;

    public OrderPaging(Page<?> pageList) {
        this.contentCnt = (int) pageList.getTotalElements();
        this.content = pageList.getContent();
        this.pageSize = pageList.getSize();
        this.page = pageList.getPageable().getPageNumber();
        this.totalPage = pageList.getTotalPages();
    }
}
