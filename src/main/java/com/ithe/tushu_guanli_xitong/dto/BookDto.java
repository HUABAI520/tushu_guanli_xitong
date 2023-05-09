package com.ithe.tushu_guanli_xitong.dto;

import com.ithe.tushu_guanli_xitong.entity.Book;
import lombok.Data;

@Data
public class BookDto extends Book {
    private String className;
}
