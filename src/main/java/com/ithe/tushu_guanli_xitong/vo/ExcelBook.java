package com.ithe.tushu_guanli_xitong.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;




@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExcelBook {
    private Integer bookId;
    private String bookName;
    private String ibsn;
    private String author;
    private String publish;
    private String introduction;
    private Integer classId;
    private Integer number;
    private LocalDateTime createTime;
}
