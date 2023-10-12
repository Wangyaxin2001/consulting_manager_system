package com.yaxin.cms.dao;

import com.yaxin.cms.bean.Slideshow;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author briup
 * @since 2023-03-10
 */
public interface SlideshowDao extends BaseMapper<Slideshow> {
    public int getSizeByIds(List<Integer> ids);
}
