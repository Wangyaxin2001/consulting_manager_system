package com.yaxin.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yaxin.cms.bean.User;
import com.yaxin.cms.bean.extend.UserExtend;
import com.yaxin.cms.dao.UserDao;
import com.yaxin.cms.exception.ServiceException;
import com.yaxin.cms.service.IUserService;
import com.yaxin.cms.util.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @author shaoyb
 * @program: 230314-cms
 * @description TODO
 * @create 2023/3/14 15:04
 **/
@Service
public class UserServiceImpl implements IUserService {
	@Autowired
	private UserDao userDao;

	// 新增用户：username存在不为空且唯一 password不为空
	@Override
	public void save(User user) {
		//1.判断username、password是否存在且不为空
		String username = user.getUsername();
		String password = user.getPassword();
		//1.1 null值判断
		if (username == null || password == null)
			throw new ServiceException(ResultCode.PARAM_IS_BLANK);
		//1.2 空值判断 “”、“ ”、“   ”、“\t”
		username = username.trim();
		password = password.trim();
		if ("".equals(username) || "".equals(password))
			throw new ServiceException(ResultCode.PARAM_IS_BLANK);

		//1.3 修改user中username、password值
		user.setUsername(username);
		user.setPassword(password);

		//2.判断username是否唯一
		LambdaQueryWrapper<User> qw = new LambdaQueryWrapper<>();
		qw.eq(User::getUsername, username);
		User u = userDao.selectOne(qw);
		if (u != null)
			throw new ServiceException(ResultCode.USERNAME_HAS_EXISTED);

		//3.补充注册时间
		user.setRegisterTime(LocalDateTime.now());

		//4.新增用户
		userDao.insert(user);
	}

	@Override
	public void setVip(Long id) {
		//1.id判断是否有效
		User user = userDao.selectById(id);
		if (user == null)
			throw new ServiceException(ResultCode.DATA_NONE);

		//2.判断是否已经设置为vip
		if (user.getIsVip() == 1)
			throw new ServiceException(ResultCode.PARAM_IS_INVALID);

		//3.修改用户为vip，同时修改过期时间expires_time
		User u = new User();
		u.setId(id);
		u.setIsVip(1);
		LocalDateTime expiresTime = LocalDateTime.now().plusMonths(1);
		u.setExpiresTime(expiresTime);
		userDao.updateById(u);
	}

	//更新用户信息
	@Override
	public void update(User user) {
		//1.id判断（非空且有效存在）
		Long id = user.getId();
		User dbUser = userDao.selectById(id);
		if (id == null || dbUser == null)
			throw new ServiceException(ResultCode.PARAM_IS_INVALID);

		//2.username空白字符去除【实际开发中交给前端处理,此处纯粹配合学习】
		String newName = user.getUsername();
		String oldName = dbUser.getUsername();
		if (newName != null && !newName.equals(oldName)) {
			String trimName = newName.trim();
			if ("".equals(trimName))
				throw new ServiceException(ResultCode.PARAM_IS_INVALID);

			//更新username(已经去除前后空白字符)
			user.setUsername(trimName);

			//3.用户名唯一判断: 查询username是否唯一(除当前user外)
			LambdaQueryWrapper<User> qw = new LambdaQueryWrapper<>();
			qw.eq(User::getUsername, trimName);
			if (userDao.selectOne(qw) != null)
				throw new ServiceException(ResultCode.USERNAME_HAS_EXISTED);
		}

		//4.更新用户信息
		userDao.updateById(user);
	}

	@Override
	public void deleteByBatch(List<Long> ids) {
		//1.有效参数判断
		if (ids == null || ids.isEmpty()) {
			throw new ServiceException(ResultCode.PARAM_IS_BLANK);
		}
		//2.删除指定用户
		userDao.deleteBatchIds(ids);
	}

	@Override
	public User login(String username, String password) {
		//1.条件准备
		LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<User>();
		lqw.eq(User::getUsername, username)
				.eq(User::getPassword, password);

		//2.查询校验
		User user = userDao.selectOne(lqw);
		if (user == null)
			throw new ServiceException(ResultCode.USER_LOGIN_ERROR);

		return user;
	}

	@Override
	public User queryById(Long id) {
		//1.有效参数判断
		if (id == null)
			throw new ServiceException(ResultCode.PARAM_IS_BLANK);

		//2.查找用户
		User user = userDao.selectById(id);
		if (user == null)
			throw new ServiceException(ResultCode.DATA_NONE);

		return user;
	}

	@Override
	public List<User> getAllUser() {
		LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(User::getStatus,"启用");
		return userDao.selectList(queryWrapper);
	}

	//分页+条件查询
	@Override
	public IPage<UserExtend> query(Integer pageNum, Integer pageSize, String username, String status, Integer roleId, Integer isVip) {
		if (pageNum == null || pageSize == null)
			throw new ServiceException(ResultCode.PARAM_IS_BLANK);
		//关键：程序员自行书写sql语句 完成多表连接查询，如何进行分页处理？
		IPage<UserExtend> page = new Page<>(pageNum, pageSize);
		userDao.queryAllUserWithRole(page, username, status, roleId, isVip);
        System.out.println(page.getTotal());
        System.out.println(page.getRecords().size());
		return page;
	}
}
