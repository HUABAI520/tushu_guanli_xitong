package com.ithe.tushu_guanli_xitong.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ithe.tushu_guanli_xitong.common.R;
import com.ithe.tushu_guanli_xitong.dto.BookDto;
import com.ithe.tushu_guanli_xitong.entity.*;
import com.ithe.tushu_guanli_xitong.service.BookService;
import com.ithe.tushu_guanli_xitong.service.ClassinfoService;
import com.ithe.tushu_guanli_xitong.service.CollectService;
import com.ithe.tushu_guanli_xitong.service.LendService;
import com.ithe.tushu_guanli_xitong.utils.ExcelUtils;
import com.ithe.tushu_guanli_xitong.vo.ExcelBook;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@RestController
@RequestMapping("/book")
public class BookController {
    @Autowired
    private BookService bookService;
    @Autowired
    private ClassinfoService classinfoService;

    @Autowired
    private CollectService collectService;

    @Autowired
    private LendService lendService;

    @PostMapping("/add")
    public R<String> addSave(@RequestBody Book book) {
        log.info("新建图书信息：{}", book.toString());
        bookService.save(book);
        return R.success("新建成功！");
    }

    /*
    根据图书名称/作者/出版社分页查询
    * */
    @PostMapping("/findPage")
    public R<Page> page(@RequestBody BookPage bookPage) {
        log.info(bookPage.toString());
        //构造分页构造器
        Page<Book> pageInfo = new Page<>(bookPage.getCurrentPage(), bookPage.getPageSize());
        Page<BookDto> dtoPage = new Page<>();
        //构造条件构造器
        LambdaQueryWrapper<Book> queryWrapper = new LambdaQueryWrapper<>();
        //添加一个过滤条件
        log.info("===={}", bookPage.getSearchType());
        System.out.println("=======================" + bookPage.getSearchType());
        if (StringUtils.isNotEmpty(bookPage.getQueryString())) {
            if (Objects.equals(bookPage.getSearchType(), "title")) {
                queryWrapper.like(Book::getBookName, bookPage.getQueryString());
            } else if (Objects.equals(bookPage.getSearchType(), "author")) {
                queryWrapper.like(Book::getAuthor, bookPage.getQueryString());
            } else if (Objects.equals(bookPage.getSearchType(), "publish")) {
                queryWrapper.like(Book::getPublish, bookPage.getQueryString());
            } else if (Objects.equals(bookPage.getSearchType(), "ibsn")) {
                queryWrapper.like(Book::getIbsn, bookPage.getQueryString());
            }
        }
        //添加一个排序条件
        queryWrapper.orderByAsc(Book::getBookId);

        //执行查询
        bookService.page(pageInfo, queryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo, dtoPage, "records");//将除了Book数据全部传给bookdto
        List<Book> records = pageInfo.getRecords();
        List<BookDto> list = records.stream().map((item) -> {
            BookDto bookDto = new BookDto();
            BeanUtils.copyProperties(item, bookDto);//将book也传递给bookdto

            log.info(String.valueOf(item.getClassId()));
            //根据classId查询分类对象
            LambdaQueryWrapper<Classinfo> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(Classinfo::getClassId, item.getClassId());
            Classinfo classinfo = classinfoService.getOne(queryWrapper1);

            String className = classinfo.getClassName();
            bookDto.setClassName(className);//通过查询将获得的分类名称传给bookdto
            return bookDto;//返回bookdto
        }).collect(Collectors.toList());

        dtoPage.setRecords(list);//将返回值获得的数据添加到bookdto
        log.info(dtoPage.toString());
        log.info(String.valueOf(dtoPage.getTotal()));
        return R.success(dtoPage);
//        log.info(String.valueOf(pageInfo.getTotal()));
//        return R.success(pageInfo);
    }

    /*
     * 收藏
     */
    @GetMapping("/collect")
    public R<String> Collect(int bookId, String username) {
        log.info("{},{}", bookId, username);
        LambdaQueryWrapper<Book> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Book::getBookId, bookId);
        Book book = bookService.getOne(queryWrapper);
        Collect collect = new Collect();
        collect.setBookId(bookId);
        collect.setUsername(username);
        collect.setBookName(book.getBookName());
        collectService.save(collect);
        return R.success("收藏成功！");
    }
    /*
     *
     * 导出
     * */


    @RequestMapping("/export")
    public void export(HttpServletResponse response) {
        try {

            List<Book> list = bookService.list();
            //System.out.println(list.toString());
            String[] columnNames = new String[]{"bookId", "bookName", "ibsn", "author", "publish", "introduction", "classId", "number", "createTime"};
            String[] keys = new String[]{"图书号", "图书名", "IBSN", "作者", "出版社", "简介", "类别号", "数量", "添加日期"};
            List<ExcelBook> excelBooks = new ArrayList<>();
            for (Book book : list) {
                ExcelBook excelBook = new ExcelBook();
                BeanUtils.copyProperties(book, excelBook);
                //时间导出乱码未处理
                excelBooks.add(excelBook);
            }

            ExcelUtils.export(response, "图书信息表1", excelBooks, columnNames, keys, "图书信息");
            //return  new Result(true, MessageConstant.EXPORT_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            //服务调用失败
            //return new Result(false, MessageConstant.EXPORT_FAIL);
        }
    }

    /*
    借阅
    * */
    @GetMapping("/lend")
    public R<String> lend(int bookId, String username, int number) {
        log.info("借阅书：{}，用户：{}，数量：{}", bookId, username, number);
        LambdaQueryWrapper<Book> bookLambdaQueryWrapper = new LambdaQueryWrapper<>();
        bookLambdaQueryWrapper.eq(Book::getBookId, bookId);

        Book book = bookService.getOne(bookLambdaQueryWrapper);

        if ((book.getNumber() - number) < 0) {
            if (book.getNumber() == 0)
                return R.error("借阅失败！图书储藏量现为0！请后续借阅！");
            else
                return R.error("借阅失败！图书储藏量储藏量少于" + number + "！请更改数量借阅！");

        }
        if (number > 3) {
            return R.error("借阅失败！每人每次同本书借阅不得超于三本！请重新借阅！");
        }
        Lend lend = new Lend();
        lend.setBookId(book.getBookId());
        lend.setBookName(book.getBookName());
        lend.setIbsn(book.getIbsn());
        lend.setUsername(username);
        lend.setNumber(number);

        LocalDateTime returnTime = LocalDateTime.now().plusDays(15);

        System.out.println("十五天后的时间：" + returnTime);
        lend.setReturnTime(returnTime);

        lendService.save(lend);
        LambdaUpdateWrapper<Book> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Book::getBookId, bookId);
        updateWrapper.set(Book::getNumber, book.getNumber() - number);
        bookService.update(updateWrapper);
        return R.success("借阅成功！请在15天之内归还！！！！！" + "及" + returnTime + "之前归还！");
    }

    @GetMapping("/findBookById")
    public R<Book> findBookById(int bookId) {
        System.out.println(bookId);
        LambdaQueryWrapper<Book> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Book::getBookId, bookId);
        Book book = bookService.getOne(queryWrapper);
        if (book != null) {
            return R.success(book);
        }
        return R.error("书籍不存在！");
    }

    /*
    编辑修改书籍信息
    * */
    @PutMapping("/edit")
    public R<String> bookUpdate(@RequestBody Book book) {
        System.out.println("===========" + book.toString());
        bookService.updateById(book);
        return R.success("书籍信息修改成功！");
    }
/*
  删除信息
* */
    @DeleteMapping("/delete")
    public R<String> delete(int bookId) {
        log.info("{}", bookId);
        LambdaQueryWrapper<Book> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Book::getBookId, bookId);
        bookService.remove(queryWrapper);
        return R.success("删除书籍信息成功！");
    }

    @GetMapping("/findById")
    public R<Book> find(int id) {
        System.out.println("================="+id);
        Book book = bookService.getById(id);
        if (book != null)
            return R.success(book);
        return R.error("书籍信息不存在！");
    }

}