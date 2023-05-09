package com.ithe.tushu_guanli_xitong.controller;




import com.ithe.tushu_guanli_xitong.entity.Book;
import com.ithe.tushu_guanli_xitong.entity.Echars;
import com.ithe.tushu_guanli_xitong.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;



@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    private BookService bookService;

    @RequestMapping(value = "/EcharsShow")
    public List<Echars> findById(Model model) {

        List<Echars> list = new ArrayList<>();
        List<Book> allLimit = bookService.list();
        for (Book book : allLimit) {
            list.add(new Echars(book.getBookName(),book.getNumber()));
        }
        return list;
    }



}

