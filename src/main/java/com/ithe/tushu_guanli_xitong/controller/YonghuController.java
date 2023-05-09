package com.ithe.tushu_guanli_xitong.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ithe.tushu_guanli_xitong.common.R;
import com.ithe.tushu_guanli_xitong.entity.UserDate;
import com.ithe.tushu_guanli_xitong.entity.Yonghu;
import com.ithe.tushu_guanli_xitong.entity.YonghuPage;
import com.ithe.tushu_guanli_xitong.service.YonghuService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/user")
public class YonghuController {
    @Autowired
    private YonghuService yonghuService;

    @PostMapping("/register")
    public R<String> register(@RequestBody Yonghu yonghu) {
        log.info(yonghu.toString());
        //对密码进行md5加密
        yonghu.setPassword(DigestUtils.md5DigestAsHex(yonghu.getPassword().getBytes()));
//        yonghu.setCreateTime(LocalDateTime.now());
//        yonghu.setUpdateTime(LocalDateTime.now());
        yonghu.setStatus(1);
        yonghuService.save(yonghu);
        return R.success("注册成功!");
    }

    @PostMapping("/login")
    public R<Yonghu> login(HttpServletRequest request, @RequestBody Yonghu yonghu) {
        log.info(yonghu.toString());
        String password = yonghu.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        LambdaQueryWrapper<Yonghu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Yonghu::getUsername, yonghu.getUsername());
        Yonghu yh = yonghuService.getOne(queryWrapper);
        if (yh == null) {
            return R.error("登录失败！用户不存在！");
        }
        if (!yh.getPassword().equals(password)) {
            return R.error("登录失败！密码错误！");
        }
        if (yh.getStatus() == 0) {
            return R.error("用户已禁用！");
        }
        request.getSession().setAttribute("yonghu", yh.getId());
        return R.success(yh);
    }

    @GetMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        log.info("退出系统！");
        request.getSession().removeAttribute("yonghu");
        return R.success("退出成功！");
    }


    @PostMapping("/findPage")
    public R<Page> findPage(@RequestBody YonghuPage yonghuPage) {
        log.info("分页：{}", yonghuPage);
        Page<Yonghu> pageInfo = new Page<>(yonghuPage.getCurrentPage(), yonghuPage.getPageSize());
        LambdaQueryWrapper<Yonghu> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(yonghuPage.getQueryString())) {
            queryWrapper.like(Yonghu::getUsername, yonghuPage.getQueryString());
        }
        queryWrapper.orderByDesc(Yonghu::getCreateTime);
        yonghuService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }

    /*
    根据id删除
    * */
    @GetMapping("/delete")
    public R<String> delete(Long id) {
        System.out.println(id);
        yonghuService.removeById(id);
        return R.success("删除成功！");
    }

    /*
    根据id查询回显
    * */
    @GetMapping("/findById")
    public R<Yonghu> find(Long id) {
        System.out.println("根据id查询用户信息");
        Yonghu yonghu = yonghuService.getById(id);
        if (yonghu != null) {
            return R.success(yonghu);
        }
        return R.error("用户不存在！");
    }

    /*
    编辑修改用户
    * */
    @PostMapping("/edit")
    public R<String> YonghuUpdate(@RequestBody Yonghu yonghu) {
        System.out.println(yonghu.toString());
        yonghuService.updateById(yonghu);
        return R.success("修改用户信息成功！");
    }

    /*
    修改密码
    * */
    @PostMapping("/updatepwd")
    public R<String> updateS(@RequestBody UserDate userDate) {
        log.info(userDate.toString());
        String username = userDate.getUsername();
        LambdaUpdateWrapper<Yonghu>updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Yonghu::getUsername,username);
        Yonghu yonghu = yonghuService.getOne(updateWrapper);
        String s = DigestUtils.md5DigestAsHex(userDate.getOldpwd().getBytes());
        if (!yonghu.getPassword().equals(s)) {
            return R.error("更改失败！原密码错误！");
        }
        String s1 = DigestUtils.md5DigestAsHex(userDate.getConfirm().getBytes());
        updateWrapper.set(Yonghu::getPassword,s1);
        yonghuService.update(updateWrapper);
        return R.success("修改成功！");
    }
}
