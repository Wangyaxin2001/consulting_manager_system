package com.yaxin.cms.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yaxin.cms.bean.Category;
import com.yaxin.cms.bean.extend.CategoryExtend;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author briup
 * @since 2023-03-10
 */
public interface CategoryDao extends BaseMapper<Category> {

    //查询栏目表中最大的order_num值
    Integer getMaxOrderNum();

    //查询父栏目下的子栏目
    List<Category> queryByParentId(Integer parentId);

    //查询所有一级栏目（含二级栏目s）
    List<CategoryExtend> queryAllWithCates();

}
