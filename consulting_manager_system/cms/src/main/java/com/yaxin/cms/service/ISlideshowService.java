package com.yaxin.cms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yaxin.cms.bean.Slideshow;

import java.util.List;

public interface ISlideshowService {

    Slideshow queryById(Integer id);

    void deleteInBatch(List<Integer> ids);

    void saveOrUpdate(Slideshow slideshow);

    List<Slideshow> queryAllEnable();

    IPage<Slideshow> query(Integer page, Integer pageSize, String status, String desc);
}
