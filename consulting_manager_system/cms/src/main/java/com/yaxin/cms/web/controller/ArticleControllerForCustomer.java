package com.yaxin.cms.web.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yaxin.cms.bean.extend.ArticleExtend;
import com.yaxin.cms.bean.dto.ArticleParam;
import com.yaxin.cms.service.IArticleService;
import com.yaxin.cms.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author shaoyb
 * @program: 230821-cms
 * @description TODO
 * @create 2023/8/23 14:57
 **/
@Api(tags = "资讯ForCustomer")
@RestController
@RequestMapping("/articles")
public class ArticleControllerForCustomer {

    @Autowired
    private IArticleService articleService;

    //查询指定的文章（收费文章只能会员才可以查看）
    @GetMapping("/{id}")
    public Result getArticleById(@PathVariable Long id) {
        ArticleExtend articleExtend = articleService.queryByIdForCustomer(id);

        return Result.success(articleExtend);
    }

    //查询所有文章（包含作者信息）
    @ApiOperation(value = "分页+条件查询所有文章", notes = "")
    @PostMapping("/query")
    public Result queryById(@RequestBody ArticleParam articleParam) {
        IPage<ArticleExtend> page = articleService.query(articleParam);

        return Result.success(page);
    }
}
