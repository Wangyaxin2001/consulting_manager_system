package com.yaxin.cms.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yaxin.cms.bean.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yaxin.cms.bean.extend.CommentExtend;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author briup
 * @since 2023-03-10
 */
public interface CommentDao extends BaseMapper<Comment> {

    //mp中分页查询的固定用法：第一个参数为IPage对象，返回值也是IPage对象
    // 分页查询指定文章下的所有一级评论
    IPage<CommentExtend> queryByArticleId(IPage<CommentExtend> page, @Param("articleId") Long articleId);

    // 分页+条件查询一级评论
    IPage<CommentExtend> query(IPage<CommentExtend> page,
                               @Param("keyword") String keyword,
                               @Param("userId") Long userId,
                               @Param("articleId") Long articleId,
                               @Param("startTime") LocalDateTime startTime,
                               @Param("endTime") LocalDateTime endTime);

   // IPage<CommentExtend> queryAll(IPage<UserExtend> page, @Param("keyword") String keyword,);
}
