package com.ithe.tushu_guanli_xitong.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ithe.tushu_guanli_xitong.common.R;
import com.ithe.tushu_guanli_xitong.entity.Book;
import com.ithe.tushu_guanli_xitong.entity.BookPage;
import com.ithe.tushu_guanli_xitong.entity.ClassPage;
import com.ithe.tushu_guanli_xitong.entity.Classinfo;
import com.ithe.tushu_guanli_xitong.service.BookService;
import com.ithe.tushu_guanli_xitong.service.ClassinfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/class")
public class ClassinfoController {
    @Autowired
    private ClassinfoService classinfoService;

    @Autowired
    private BookService bookService;

    @PostMapping("/findPage")
    private R<Page> page(@RequestBody ClassPage classPage){
        log.info(classPage.toString());
        //构造分页构造器
        Page<Classinfo> pageInfo = new Page<>(classPage.getCurrentPage(),classPage.getPageSize());
        //构造条件构造器
        LambdaQueryWrapper<Classinfo> queryWrapper = new LambdaQueryWrapper<>();
        //添加一个过滤条件
        queryWrapper.like(StringUtils.isNotEmpty(classPage.getQueryString()), Classinfo::getClassName,classPage.getQueryString());
        //添加一个排序条件
        queryWrapper.orderByAsc(Classinfo::getClassId);
        //执行查询
        classinfoService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);

    }

    @GetMapping("/list")
    public R<List<Classinfo>> list(){
        LambdaQueryWrapper<Classinfo> queryWrapper =new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Classinfo::getClassId).orderByDesc(Classinfo::getUpdateTime);
        List<Classinfo> list = classinfoService.list(queryWrapper);
        return R.success(list);
    }
    @GetMapping("listId")
    public R<String> listId(int id){
        LambdaQueryWrapper<Classinfo> queryWrapper =new LambdaQueryWrapper<>();
        queryWrapper.eq(Classinfo::getClassId,id);
        Classinfo one = classinfoService.getOne(queryWrapper);
        return R.success(one.getClassName());
    }

    @PostMapping("/add")
    public R<String> add(@RequestBody Classinfo classinfo){
        log.info(classinfo.toString());
        LambdaQueryWrapper<Classinfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Classinfo::getClassId).last("limit 1");
        Classinfo cl = classinfoService.getOne(queryWrapper);
        classinfo.setClassId(cl.getClassId()+1);
        classinfoService.save(classinfo);
        return R.success("添加新种类成功！");
    }
    @GetMapping("/delete")
    public R<String> delete(int classId){
        log.info("{}",classId);
        LambdaQueryWrapper<Book> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Book::getClassId,classId);
        List<Book> list = bookService.list(queryWrapper);
        System.out.println(list);
        if(!list.isEmpty()){
            return R.error("删除分类信息失败！该分类关联了书籍！");
        }
        LambdaQueryWrapper<Classinfo> classinfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        classinfoLambdaQueryWrapper.eq(Classinfo::getClassId,classId);
        classinfoService.remove(classinfoLambdaQueryWrapper);
        return R.success("删除分类信息成功！");
    }
    /*
    回显
    * */
    @GetMapping("/findById")
    public R<Classinfo> find(Long id){
        System.out.println("根据id查询分类信息"+id);
        Classinfo classinfo = classinfoService.getById(id);
        if(classinfo != null){
            return R.success(classinfo);
        }
        return R.error("分类不存在！");
    }
    /*
   修改
     */
    @PostMapping("/edit")
    public R<String> ClassUpdate(@RequestBody Classinfo classinfo){
        System.out.println(classinfo.toString());
        classinfoService.updateById(classinfo);
        return R.success("修改分类信息成功！");
    }
}
