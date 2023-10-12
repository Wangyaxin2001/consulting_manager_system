package com.yaxin.cms.bean.extend;

import com.yaxin.cms.bean.Comment;
import com.yaxin.cms.bean.User;
import lombok.Data;

import java.util.List;

/**
 * @author shaoyb
 * @program: 230314-cms
 * @description TODO
 * @create 2023/3/22 16:02
 **/
@Data
public class CommentExtend extends Comment {
    // 评论人
    private User author;

    // 子评论s
    private List<SubCommentExtend> childComments;
}
