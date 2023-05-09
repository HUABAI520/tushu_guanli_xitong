package com.ithe.tushu_guanli_xitong.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ithe.tushu_guanli_xitong.common.R;
import com.ithe.tushu_guanli_xitong.entity.Collect;
import com.ithe.tushu_guanli_xitong.entity.CollectPage;
import com.ithe.tushu_guanli_xitong.service.CollectService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/collect")
public class CollectController {
    @Autowired
    private CollectService collectService;


    @PostMapping("/findPageUser")
    public R<Page> findPageUser(@RequestBody CollectPage collectPage){
        log.info(collectPage.toString());
        Page<Collect> pageInfo = new Page<>(collectPage.getCurrentPage(),collectPage.getPageSize());
        LambdaQueryWrapper<Collect> queryWrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotEmpty(collectPage.getBookName())) {
            queryWrapper.like(Collect::getBookName,collectPage.getBookName());
        }
        System.out.println("==========="+collectPage.getUsername());
        if(!Objects.equals(collectPage.getUsername(), "admin")){
            queryWrapper.eq(Collect::getUsername,collectPage.getUsername());
        }
        queryWrapper.orderByAsc(Collect::getCreateTime);

        collectService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }


    @GetMapping("/delete")
    public R<String> delete(int collectId){
        log.info("{}",collectId);
        LambdaQueryWrapper<Collect> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Collect::getCollectId,collectId);
        collectService.remove(queryWrapper);
        return R.success("取消收藏成功！");
    }

}
