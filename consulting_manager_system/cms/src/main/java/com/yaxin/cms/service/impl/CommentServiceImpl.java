package com.yaxin.cms.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yaxin.cms.bean.Article;
import com.yaxin.cms.bean.Comment;
import com.yaxin.cms.bean.Subcomment;
import com.yaxin.cms.bean.User;
import com.yaxin.cms.bean.extend.CommentExtend;
import com.yaxin.cms.bean.extend.SubCommentExtend;
import com.yaxin.cms.bean.dto.CommentDeleteParam;
import com.yaxin.cms.bean.dto.CommentQueryParam;
import com.yaxin.cms.dao.ArticleDao;
import com.yaxin.cms.dao.CommentDao;
import com.yaxin.cms.dao.SubcommentDao;
import com.yaxin.cms.dao.UserDao;
import com.yaxin.cms.exception.ServiceException;
import com.yaxin.cms.service.ICommentService;
import com.yaxin.cms.util.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author shaoyb
 * @program: 230314-cms
 * @description TODO
 * @create 2023/3/22 16:07
 **/
@Service
public class CommentServiceImpl implements ICommentService {

    @Autowired
    private CommentDao commentDao;
    @Autowired
    private SubcommentDao subcommentDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private ArticleDao articleDao;

    // 新增一级评论
    @Override
    public void saveComment(Comment comment) {
        // 1.参数判断
        if(comment == null)
            throw new ServiceException(ResultCode.PARAM_IS_BLANK);

        // 2.用户存在及禁用状态判断
        User user = userDao.selectById(comment.getUserId());
        if(user == null)
            throw new ServiceException(ResultCode.USER_NOT_EXIST);
        if("禁用".equals(user.getStatus()))
            throw new ServiceException(ResultCode.USER_ACCOUNT_FORBIDDEN);

        // 3.文章存在判断
        if(articleDao.selectById(comment.getArticleId()) == null)
            throw new ServiceException(ResultCode.ARTICLE_NOT_EXIST);

        // 4.发表时间准备
        comment.setPublishTime(LocalDateTime.now());

        // 5.发表评论
        commentDao.insert(comment);
    }

    // 新增二级评论
    @Override
    public void saveSubComment(Subcomment comment) {
        // 1.参数判断
        if(comment == null)
            throw new ServiceException(ResultCode.PARAM_IS_BLANK);

        // 2.用户存在及禁用状态判断
        User user = userDao.selectById(comment.getUserId());
        if(user == null)
            throw new ServiceException(ResultCode.USER_NOT_EXIST);
        if("禁用".equals(user.getStatus()))
            throw new ServiceException(ResultCode.USER_ACCOUNT_FORBIDDEN);

        // 3.一级评论存在判断
        if(commentDao.selectById(comment.getParentId()) == null)
            throw new ServiceException(ResultCode.COMMENT_NOT_EXIST);

        // 4.回复评论id存在判断【replyId可以存在也可以不存在】
        Long replyId = comment.getReplyId();
        if(replyId != null &&
                subcommentDao.selectById(replyId) == null)
            throw new ServiceException(ResultCode.REPLYCOMMENT_NOT_EXIST);

        // 5.发表时间准备
        comment.setPublishTime(LocalDateTime.now());

        // 6.发表二级评论
        subcommentDao.insert(comment);
    }

    // 评论删除：type为1表示删除一级评论，type为2表示删除二级评论
    @Override
    public void deleteById(CommentDeleteParam param) {
        // 1.参数判断
        Long id = param.getId();
        Integer type = param.getType();
        if(param == null || type == null || id == null)
            throw new ServiceException(ResultCode.PARAM_IS_BLANK);

        // 2.类型判断
        if(type == 1) {
            // 需要删除的是1级评论
            if(commentDao.selectById(id) == null)
                throw new ServiceException(ResultCode.COMMENT_NOT_EXIST);

            // 一级评论删除
            commentDao.deleteById(id);
        }else if(type == 2) {
            // 需要删除的是2级评论
            if(subcommentDao.selectById(id) == null)
                throw new ServiceException(ResultCode.SUBCOMMENT_NOT_EXIST);

            // 二级评论删除
            subcommentDao.deleteById(id);
        }else {
            throw new ServiceException(ResultCode.PARAM_IS_INVALID);
        }
    }

    @Override
    public void deleteInBatch(List<CommentDeleteParam> list) {
        // 1.参数判断
        if(list == null || list.size() == 0)
            throw new ServiceException(ResultCode.PARAM_IS_BLANK);

        // 2.定义批量删除成功标记，默认为失败
        boolean flag = false;

        // 3.循环删除
        for (CommentDeleteParam comment : list) {
            try{
                deleteById(comment);
                // 只要有一条删除成功，批量删除就算成功
                flag = true;
            }catch (Exception e) {
                System.out.println("delete 失败, id：" + comment.getId());
            }
        }

        // 4.批量删除成功判断
        if(!flag)
            throw new ServiceException(ResultCode.COMMENT_NOT_EXIST);
    }

    // 获取有效用户的ids集合
    private List<Long> getUserIdList() {
        List<User> users = userDao.selectList(null);
        List<Long> userIds = users.stream().map(User::getId).collect(Collectors.toList());
        return userIds;
    }

    //根据一级评论id查询所有二级评论(含发表人信息)
    @Override
    public List<SubCommentExtend> queryByCommentId(Long id) {
        // 1.一级评论id必须存在
        Comment comment = commentDao.selectById(id);
        if(comment == null)
            throw new ServiceException(ResultCode.COMMENT_NOT_EXIST);

        // 2.一级评论所属的文章必须存在
        Long articleId = comment.getArticleId();
        if(articleDao.selectById(articleId) == null)
            throw new ServiceException(ResultCode.ARTICLE_NOT_EXIST);

        // 自定义sql语句实现
        List<SubCommentExtend> list = subcommentDao.queryByParentId(id);

        return list;
    }

    /*
      分页查询指定文章下的所有的1级评论（包含发表人及二级评论）
        1.文章必须存在且有效
        2.评论人必须存在且有效
     */
    @Override
    public IPage<CommentExtend> queryByArticleId(Integer pageNum, Integer pageSize, Long articleId) {
        // 1.参数判断
        if(pageNum == null || pageSize == null || articleId == null)
            throw new ServiceException(ResultCode.PARAM_IS_BLANK);

        // 2.文章及发表者判断
        Article article = articleDao.selectById(articleId);
        if(article == null)
            throw new ServiceException(ResultCode.ARTICLE_NOT_EXIST);

        // 文章作者判断
        User user = userDao.selectById(article.getUserId());
        if(user == null)
            throw new ServiceException(ResultCode.USER_NOT_EXIST);

        // 3.分页查询指定文章下所有的一级评论（含作者与二级评论s）
        IPage<CommentExtend> page = new Page<>(pageNum, pageSize);
        commentDao.queryByArticleId(page, articleId);
        //System.out.println("page: " + page);

        return page;
    }

    // 分页+条件查询
    @Override
    public IPage<CommentExtend> query(CommentQueryParam param) {
        // 1.参数判断
        if(param == null || param.getPageNum() == null || param.getPageSize() == null)
            throw new ServiceException(ResultCode.PARAM_IS_BLANK);

        // 2.分页+条件查询
        IPage<CommentExtend> page = new Page<>(param.getPageNum(), param.getPageSize());
        commentDao.query(page, param.getKeyword(),
                param.getUserId(), param.getArticleId(),
                param.getStartTime(), param.getEndTime());

        return page;
    }
}
