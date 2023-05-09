package com.ithe.tushu_guanli_xitong.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Echars implements Serializable {
    private String name;
    private Integer num;
}
