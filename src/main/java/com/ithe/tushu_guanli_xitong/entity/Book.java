package com.ithe.tushu_guanli_xitong.entity;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;


@Data

public class Book implements Serializable {
//    @ExcelProperty(value = "主键id",index = 0)
@TableId(value = "book_id",type = IdType.AUTO)
    private Integer bookId;

//    @ExcelProperty(value = "图书名称",index = 1)
    private String bookName;

    private String ibsn;

//    @ExcelProperty(value = "图书作者",index = 2)
    private String author;

//    @ExcelProperty(value = "图书出版社",index = 3)
    private String publish;

//    @ExcelProperty(value = "图书简介",index = 4)
    private String introduction;

//    @ExcelProperty(value = "图书所属类",index = 5)
    private Integer classId;

//    @ExcelProperty(value = "图书数量",index = 6)
    private Integer number;

    private String bookImg;

//    @ExcelProperty(value = "图书创建时间",index = 7)
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
