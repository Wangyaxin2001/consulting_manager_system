package com.yaxin.cms.service.impl;

import com.yaxin.cms.bean.Role;
import com.yaxin.cms.dao.RoleDao;
import com.yaxin.cms.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class RoleServiceImpl implements IRoleService {

	@Autowired
	private RoleDao roleDao;

	@Override
	public List<Role> list() {
		return roleDao.selectList(null);
	}
}
