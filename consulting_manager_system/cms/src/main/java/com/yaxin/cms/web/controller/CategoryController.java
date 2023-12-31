package com.yaxin.cms.web.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yaxin.cms.bean.Category;
import com.yaxin.cms.bean.extend.CategoryExtend;
import com.yaxin.cms.service.ICategoryService;
import com.yaxin.cms.util.Result;
import com.yaxin.cms.util.excel.CategoryListener;
import com.yaxin.cms.util.excel.CategoryParentIdConverter;
import com.yaxin.cms.util.excel.ExcelUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author briup
 * @since 2023-03-10
 */
@Api(tags = "栏目模块")
@RestController
@RequestMapping("/auth/category")
public class CategoryController {
	@Autowired
	private ICategoryService categoryService;
	@Autowired
	private ExcelUtils excelUtils;

	@ApiOperation(value = "新增栏目", notes = "栏目名必须唯一，如果为二级栏目则其父栏目id必须有效")
	@PostMapping("/save")
	public Result save(@RequestBody Category category) {
		categoryService.insert(category);

		return Result.success("新增成功");
	}

	@ApiOperation("根据id查询栏目信息")
	//@GetMapping("/getCategoryById/{id}")
	@GetMapping("/queryById/{id}")
	public Result getCategoryById(@PathVariable("id") Integer id) {
		Category category = categoryService.getCategoryById(id);
		return Result.success(category);
	}

	@ApiOperation(value = "更新栏目", notes = "栏目名必须唯一，栏目级别不能改动")
	@PutMapping("/update")
	public Result update(@RequestBody Category category) {
		categoryService.update(category);

		return Result.success("修改成功");
	}

	@ApiOperation(value = "根据id删除栏目", notes = "id必须存在且有效")
	@DeleteMapping("/deleteById/{id}")
	public Result deleteById(@PathVariable Integer id) {
		categoryService.deleteById(id);

		return Result.success("删除成功");
	}

	@ApiOperation(value = "单个删除+批量删除栏目", notes = "需要提供一个或多个id值")
	//@DeleteMapping("/deleteByIdAll/{ids}")
	@DeleteMapping("/deleteByBatch/{ids}")
	public Result deleteCategoryInBatch(@PathVariable("ids") List<Integer> ids) {
		categoryService.deleteInBatch(ids);
		return Result.success("删除成功");
	}

	@ApiOperation(value = "分页查询所有栏目")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "pageNum", value = "当前页", dataType = "int", required = true, defaultValue = "1", paramType = "query"),
			@ApiImplicitParam(name = "pageSize", value = "每页数量", dataType = "int", required = true, defaultValue = "4", paramType = "query"),
			@ApiImplicitParam(name = "parentId", value = "父栏目id", dataType = "int", paramType = "query")
	})
	@GetMapping("/query")
	public Result query(Integer pageNum, Integer pageSize, Integer parentId) {
		IPage<Category> p = categoryService.query(pageNum, pageSize, parentId);

		return Result.success(p);
	}

	@ApiOperation(value = "查询所有1级栏目(含2级)", notes = "不需要分页")
	@GetMapping("/queryAllParent")
	public Result queryAllParent() {
		List<CategoryExtend> list = categoryService.queryAllParent();

		return Result.success(list);
	}

	@ApiOperation("获取所有父栏目")
	@GetMapping("/queryAllOneLevel")
	public Result queryAllParentWithoutTwo() {
		List<Category> list = categoryService.queryAllOneLevel();
		return Result.success(list);
	}

	@ApiOperation("导入栏目数据")
	@PostMapping( "/import")
	public Result imports(@RequestPart MultipartFile file) {
		//导入前,更新一级栏目集合,防止在导入前数据库中的一级栏目被更新
		CategoryParentIdConverter.list = categoryService.queryAllOneLevel();
		//获取数据
		List<Category> list = excelUtils.importData(file, Category.class, new CategoryListener());
		//导入数据到数据库中
		categoryService.InsertInBatch(list);

		return Result.success("数据导入成功");
	}

	@ApiOperation("导出栏目数据")
	@GetMapping(value = "/export", produces = "application/octet-stream")
	public void exports(HttpServletResponse response) {
		//导出前,更新一级栏目集合,防止在导出前数据库中的一级栏目被更新
		CategoryParentIdConverter.list = categoryService.queryAllOneLevel();
		//1.获取栏目数据
		List<Category> list = categoryService.queryAll();
		//2.导出数据
		excelUtils.exportExcel(response, list, Category.class, "栏目信息表");
	}
}

