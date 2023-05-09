package com.ithe.tushu_guanli_xitong.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class CollectPage implements Serializable {
    private Integer currentPage;

    private Integer pageSize;

    private String bookName;

    private String username;
}
