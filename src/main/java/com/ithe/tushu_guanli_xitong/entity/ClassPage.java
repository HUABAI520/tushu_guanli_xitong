package com.ithe.tushu_guanli_xitong.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class ClassPage implements Serializable {

    private Integer currentPage;

    private Integer pageSize;

    private String queryString;
}
