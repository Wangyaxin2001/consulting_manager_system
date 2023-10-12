package com.yaxin.cms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yaxin.cms.bean.Comment;
import com.yaxin.cms.bean.Subcomment;
import com.yaxin.cms.bean.extend.CommentExtend;
import com.yaxin.cms.bean.extend.SubCommentExtend;
import com.yaxin.cms.bean.dto.CommentDeleteParam;
import com.yaxin.cms.bean.dto.CommentQueryParam;

import java.util.List;

public interface ICommentService {
    void saveComment(Comment comment);

    void saveSubComment(Subcomment comment);

    void deleteById(CommentDeleteParam param);

    void deleteInBatch(List<CommentDeleteParam> list);

    List<SubCommentExtend> queryByCommentId(Long id);

    // 分页查询
    IPage<CommentExtend> queryByArticleId(Integer pageNum, Integer pageSize, Long id);

    IPage<CommentExtend> query(CommentQueryParam param);
}
