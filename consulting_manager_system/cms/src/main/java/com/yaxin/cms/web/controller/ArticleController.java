package com.yaxin.cms.web.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yaxin.cms.aop.Logging;
import com.yaxin.cms.bean.Article;
import com.yaxin.cms.bean.extend.ArticleExtend;
import com.yaxin.cms.bean.dto.ArticleParam;
import com.yaxin.cms.service.IArticleService;
import com.yaxin.cms.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author briup
 * @since 2023-03-10
 */
@Api(tags = "资讯模块")
@RestController
@RequestMapping("/auth/article")
public class ArticleController {

	@Autowired
	private IArticleService articleService;

	@ApiOperation(value = "新增或修改文章", notes = "文章id存在则为修改，不存在为新增")
	@PostMapping("/saveOrUpdate")
	//public Result saveOrUpdate(@RequestBody Article article, //第一种方式获取token
	//                           @RequestHeader(value = "token", required = false) String token) {
	public Result saveOrUpdate(@RequestBody Article article) {
		articleService.saveOrUpdate(article);

		return Result.success("新增或修改成功");
	}

	@ApiOperation(value = "审核文章", notes = "文章id必须有效，status: 审核通过、审核未通过")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "文章id", required = true, dataType = "String"), //body query ??
			@ApiImplicitParam(name = "status", value = "审核状态", required = true, dataType = "String")
	})
	//@PostMapping("/reviewArticle")
	@PutMapping("/review")
	public Result reviewArticle(Long id, String status) {
		articleService.reviewArticle(id, status);

		return Result.success("审核完成");
	}

	@ApiOperation(value = "根据id批量删除文章", notes = "id必须存在且有效")
	//@DeleteMapping("/deleteById/{ids}")
	@DeleteMapping("/deleteByBatch/{ids}")
	public Result deleteInBatch(@PathVariable("ids") List<Long> ids) {
		articleService.deleteInBatch(ids);

		return Result.success("删除成功");
	}

	@Logging("查询指定文章")
	@ApiOperation(value = "查询指定文章", notes = "文章要包含3条一级评论")
	@GetMapping("/queryById/{id}")
	public Result queryById(@PathVariable Long id) {
		ArticleExtend articleExtend = articleService.queryById(id);

		return Result.success(articleExtend);
	}

	@ApiOperation(value = "分页+条件查询文章", notes = "")
	@PostMapping("/query")
	public Result queryById(@RequestBody ArticleParam articleParam) {
		IPage<ArticleExtend> page = articleService.query(articleParam);

		return Result.success(page);
	}

	@ApiOperation("查询所有文章")
	@GetMapping("/getAllArticle")
	public Result getAllArticle(){
		return Result.success(articleService.list());
	}

}

