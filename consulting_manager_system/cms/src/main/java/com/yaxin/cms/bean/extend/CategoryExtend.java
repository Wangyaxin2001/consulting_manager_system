package com.yaxin.cms.bean.extend;

import com.yaxin.cms.bean.Category;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author shaoyb
 * @program: 230314-cms
 * @description TODO
 * @create 2023/3/17 1:36
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class CategoryExtend extends Category {
    //子栏目
    private List<Category> cates;
}
