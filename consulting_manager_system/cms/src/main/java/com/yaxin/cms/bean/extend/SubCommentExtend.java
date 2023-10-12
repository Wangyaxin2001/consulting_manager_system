package com.yaxin.cms.bean.extend;

import com.yaxin.cms.bean.Subcomment;
import com.yaxin.cms.bean.User;
import lombok.Data;

/**
 * @author shaoyb
 * @program: 230314-cms
 * @description TODO
 * @create 2023/3/23 10:28
 **/
@Data
public class SubCommentExtend extends Subcomment {
    // 评论人
    private User author;

}
