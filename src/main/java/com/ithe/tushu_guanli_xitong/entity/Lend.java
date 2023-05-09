package com.ithe.tushu_guanli_xitong.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Lend implements Serializable {
    @TableId(value = "lend_id",type = IdType.AUTO)

    private Integer lendId;

    private Integer bookId;

    private String bookName;

    private String ibsn;

    private String username;

    private Integer number;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    private LocalDateTime returnTime;
}
