package com.ithe.tushu_guanli_xitong.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserDate implements Serializable {
    private String username;

    private String oldpwd;

    private String confirm;
}
