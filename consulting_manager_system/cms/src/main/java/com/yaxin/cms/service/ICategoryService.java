package com.yaxin.cms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yaxin.cms.bean.Category;
import com.yaxin.cms.bean.extend.CategoryExtend;

import java.util.List;

public interface ICategoryService {

	void insert(Category category);

	Category getCategoryById(Integer id);

	void update(Category category);

	void deleteById(Integer id);

	void deleteInBatch(List<Integer> ids);

	IPage<Category> query(Integer page, Integer pageSize, Integer parentId);

	List<CategoryExtend> queryAllParent();

	List<Category> queryAllOneLevel();

	List<Category> queryAll();

	void InsertInBatch(List<Category> list);
}
