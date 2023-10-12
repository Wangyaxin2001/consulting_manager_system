package com.yaxin.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yaxin.cms.bean.Slideshow;
import com.yaxin.cms.dao.SlideshowDao;
import com.yaxin.cms.exception.ServiceException;
import com.yaxin.cms.service.ISlideshowService;
import com.yaxin.cms.util.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author shaoyb
 * @program: 230308-CMS
 * @description TODO
 * @create 2023/3/7 15:40
 **/
@Slf4j
@Service
public class SlideshowServiceImpl implements ISlideshowService {
	@Autowired
	private SlideshowDao slideshowDao;

	@Override
	public Slideshow queryById(Integer id) {
		return slideshowDao.selectById(id);
	}

	@Override
	public void saveOrUpdate(Slideshow slideshow) {
		Integer id = slideshow.getId();

		//1.判断轮播图url是否唯一
		String url = slideshow.getUrl();
		//url唯一标识
		boolean flag = false;
		if (url != null) {
			//判断是否是原来的轮播图url
			if(id != null) {
				Slideshow oldSlideshow = slideshowDao.selectById(id);
				if(oldSlideshow != null && url.equals(oldSlideshow.getUrl())) {
					flag = true;
				}
			}

			//判断url是否唯一
			if(flag == false) {
				LambdaQueryWrapper<Slideshow> qw = new LambdaQueryWrapper<>();
				qw.eq(Slideshow::getUrl, url);
				Slideshow s = slideshowDao.selectOne(qw);
				if (s != null)
					throw new ServiceException(ResultCode.SLIDESHOW_URL_EXISTED);

				// 重置图片url更新时间
				slideshow.setUploadTime(LocalDateTime.now());
			}
		}

		if (id == null) {
			//2.新增操作
			if (slideshow.getStatus() == null)
				slideshow.setStatus("启用");

			slideshowDao.insert(slideshow);
		} else {
			//3.更新操作
			//3.1 判断当前轮播图是否有效
			Slideshow s = slideshowDao.selectById(id);
			if (s == null)
				throw new ServiceException(ResultCode.SLIDESHOW_NOT_EXISTED);

			//3.2 更新操作
			slideshowDao.updateById(slideshow);
		}
	}

	@Override
	public List<Slideshow> queryAllEnable() {
		LambdaQueryWrapper<Slideshow> qw = new LambdaQueryWrapper<>();
		qw.eq(Slideshow::getStatus, "启用");
		qw.orderByDesc(Slideshow::getUploadTime);
		List<Slideshow> list = slideshowDao.selectList(qw);

		if (list == null || list.size() == 0)
			throw new ServiceException(ResultCode.DATA_NONE);

		return list;
	}

	@Transactional
	@Override
	public IPage<Slideshow> query(Integer page, Integer pageSize, String status, String desc) {
		IPage<Slideshow> p = new Page<>(page, pageSize);
		LambdaQueryWrapper<Slideshow> qw = new LambdaQueryWrapper<>();
		qw.eq(StringUtils.hasText(status), Slideshow::getStatus, status)
				.like(StringUtils.hasText(desc), Slideshow::getDescription, desc)
				.orderByDesc(Slideshow::getUploadTime);

		slideshowDao.selectPage(p, qw);

		if (p.getTotal() == 0)
			throw new ServiceException(ResultCode.DATA_NONE);

		return p;
	}

	@Override
	public void deleteInBatch(List<Integer> ids) {
		if (ids == null ||ids.isEmpty()){
			throw new ServiceException(ResultCode.PARAM_IS_BLANK);
		}

		//根据ids查找轮播图
		LambdaQueryWrapper<Slideshow> qw = new LambdaQueryWrapper<>();
		qw.in(Slideshow::getId, ids);
		int len = slideshowDao.selectCount(qw);

		log.info("len:{}",len);

		if (len <= 0) {
			throw new ServiceException(ResultCode.SLIDESHOW_NOT_EXISTED);
		}

		slideshowDao.deleteBatchIds(ids);
	}
}
