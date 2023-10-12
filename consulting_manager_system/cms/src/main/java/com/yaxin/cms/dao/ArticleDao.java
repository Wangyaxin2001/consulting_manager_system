package com.yaxin.cms.dao;

import com.yaxin.cms.bean.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yaxin.cms.bean.extend.ArticleExtend;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author briup
 * @since 2023-03-10
 */
public interface ArticleDao extends BaseMapper<Article> {
    // 根据2级栏目id查询有效的资讯数量(发布的用户存在)
    Integer getArticleNumByCategoryId(Integer categoryId);

    // 查询文章信息（含3条一级评论）
    ArticleExtend selectByIdWithComments(Long id);
}
