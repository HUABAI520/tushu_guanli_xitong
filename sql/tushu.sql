-- 图书表
create table book
(
    book_id      int auto_increment comment '书id'
        primary key,
    book_name    varchar(100) not null comment '书名',
    author       varchar(28)  not null comment '作者',
    publish      varchar(40)  null comment '出版社',
    introduction text         null comment '介绍',
    class_id     int          null comment '类别',
    number       int          null comment '数量',
    book_img     varchar(100) null comment '封面',
    create_time  datetime     not null comment '创建时间',
    update_time  datetime     not null comment '更新时间',
    ibsn         varchar(19)  null comment 'ibsn码'
);
create index bookID
    on book (book_id);

-- 分类表
create table classinfo
(
    id          bigint auto_increment comment '主键'
        primary key,
    class_id    int         not null,
    class_name  varchar(15) not null,
    create_time datetime    not null comment '创建时间',
    update_time datetime    not null comment '更新时间',
    constraint class_name
        unique (class_name)
);

-- 收藏表
create table collect
(
    collect_id  int auto_increment comment '收藏主键'
        primary key,
    book_name   varchar(100) not null comment '书名',
    username    varchar(100) not null comment '用户名',
    book_id     int          null comment '书id',
    create_time datetime     not null comment '收藏时间',
    update_time datetime     not null comment '更新时间'
);

-- 借还表
create table lend
(
    lend_id     int auto_increment comment '主键'
        primary key,
    book_id     int          not null comment '借阅书id',
    book_name   varchar(100) not null comment '借阅书名',
    ibsn        varchar(19)  null comment '借阅书ibsn码',
    username    varchar(32)  not null comment '借阅者用户名',
    number      int          not null comment '借阅数量',
    create_time datetime     not null comment '创建时间',
    update_time datetime     not null comment '更新时间',
    return_time datetime     not null comment '归还时间'
);

-- 用户表
create table yonghu
(
    id          bigint        not null comment '主键'
        primary key,
    username    varchar(32)   not null comment '用户名',
    school      varchar(32)   not null comment '学校',
    password    varchar(64)   not null comment '密码',
    phone       varchar(11)   not null comment '手机号',
    sex         varchar(2)    null comment '性别',
    status      int default 1 not null comment '状态 0:禁用，1:正常',
    create_time datetime      not null comment '创建时间',
    update_time datetime      not null comment '更新时间',
    constraint idx_username
        unique (username)
);
