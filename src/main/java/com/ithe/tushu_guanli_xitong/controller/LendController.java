package com.ithe.tushu_guanli_xitong.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ithe.tushu_guanli_xitong.common.R;
import com.ithe.tushu_guanli_xitong.entity.*;
import com.ithe.tushu_guanli_xitong.service.BookService;
import com.ithe.tushu_guanli_xitong.service.LendService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/lend")
public class LendController {
    @Autowired
    private LendService lendService;

    @Autowired
    private BookService bookService;

    /*
    借阅分页查询
    * */
    @PostMapping("/findPageUser")
    public R<Page> findPageUser(@RequestBody LendPage lendPage) {
        log.info(lendPage.toString());
        Page<Lend> pageInfo = new Page<>(lendPage.getCurrentPage(),lendPage.getPageSize());
        LambdaQueryWrapper<Lend> queryWrapper = new LambdaQueryWrapper<>();
//        if(StringUtils.isNotEmpty(lendPage.getBookName())) {
//            queryWrapper.like(Lend::getBookName,lendPage.getBookName());
//        }
        if (StringUtils.isNotEmpty(lendPage.getQueryString())) {
            if (Objects.equals(lendPage.getSearchType(), "ibsn")) {
//                ibsn采用模糊查询
                queryWrapper.like(Lend::getIbsn,lendPage.getQueryString());
            }
            else if(Objects.equals(lendPage.getSearchType(), "bookId")){
//                图书编号采用等值查询
                queryWrapper.eq(Lend::getBookId,lendPage.getQueryString());
            }
            else if(Objects.equals(lendPage.getSearchType(), "bookName")){
//                图书名字采用模糊查询
                queryWrapper.like(Lend::getBookName,lendPage.getQueryString());
            }
            else if(Objects.equals(lendPage.getSearchType(), "yonghu")){
//                用户采用等值查询
                queryWrapper.eq(Lend::getUsername,lendPage.getQueryString());
            }
        }
        if(!Objects.equals(lendPage.getUsername(), "admin")){
            queryWrapper.eq(Lend::getUsername,lendPage.getUsername());
        }
        queryWrapper.orderByAsc(Lend::getCreateTime);
        lendService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }

    /*
    归还
    * */
    @GetMapping("/back")
    public R<String> back(int lendId){
        log.info("{}",lendId);
        /*lend表查询*/
        LambdaQueryWrapper<Lend> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Lend::getLendId,lendId);
        Lend lend = lendService.getOne(queryWrapper);
        /*book表查询*/
        LambdaQueryWrapper<Book> bookLambdaQueryWrapper = new LambdaQueryWrapper<>();
        bookLambdaQueryWrapper.eq(Book::getBookId, lend.getBookId());
        Book book = bookService.getOne(bookLambdaQueryWrapper);
        /*归还的数量*/
        int number = lend.getNumber();
        /*归还的时间是否超时*/
        LocalDateTime returnTime = lend.getReturnTime();
        LocalDateTime nowTime = LocalDateTime.now();

        LambdaUpdateWrapper<Book> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Book::getBookId,lend.getBookId());
        updateWrapper.set(Book::getNumber,book.getNumber()+number);

        bookService.update(updateWrapper);
        lendService.remove(queryWrapper);
        //比较  如今的时间 在 设定的时间 之后  返回的类型是Boolean类型
        if(nowTime.isAfter(returnTime)){
            return R.success("归还成功，超时归还！请准时归还！");
        }
        /*准时*/
        return R.success("归还成功！感谢你准时归还！");
    }

    /*
    删除借阅信息
    * */

    @GetMapping("/delete")
    private R<String> delete(int lendId){
        log.info("{}",lendId);
        LambdaQueryWrapper<Lend> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Lend::getLendId,lendId);
        lendService.remove(queryWrapper);

        return R.success("删除成功！");
    }
}
