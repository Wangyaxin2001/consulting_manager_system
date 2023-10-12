package com.yaxin.cms.bean.extend;

import com.yaxin.cms.bean.Article;
import com.yaxin.cms.bean.Comment;
import com.yaxin.cms.bean.User;
import lombok.Data;

import java.util.List;

/**
 * @author shaoyb
 * @program: 230314-cms
 * @description TODO
 * @create 2023/3/19 23:46
 **/
@Data
public class ArticleExtend extends Article {
    //包含3条一级评论
    private List<Comment> comments;
    //新增文章作者
    private User author;
}
