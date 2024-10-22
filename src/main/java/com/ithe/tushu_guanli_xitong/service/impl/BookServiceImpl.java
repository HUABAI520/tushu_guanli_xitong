package com.ithe.tushu_guanli_xitong.service.impl;




import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ithe.tushu_guanli_xitong.common.R;
import com.ithe.tushu_guanli_xitong.entity.Book;
import com.ithe.tushu_guanli_xitong.entity.BookPage;

import com.ithe.tushu_guanli_xitong.mapper.BookMapper;
import com.ithe.tushu_guanli_xitong.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.util.StringUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author L
 */
@Slf4j
@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book>implements BookService {

}


