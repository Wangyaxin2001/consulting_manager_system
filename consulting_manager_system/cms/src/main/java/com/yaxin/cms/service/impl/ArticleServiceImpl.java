package com.yaxin.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yaxin.cms.bean.Article;
import com.yaxin.cms.bean.Category;
import com.yaxin.cms.bean.Comment;
import com.yaxin.cms.bean.User;
import com.yaxin.cms.bean.extend.ArticleExtend;
import com.yaxin.cms.bean.dto.ArticleParam;
import com.yaxin.cms.dao.ArticleDao;
import com.yaxin.cms.dao.CategoryDao;
import com.yaxin.cms.dao.CommentDao;
import com.yaxin.cms.dao.UserDao;
import com.yaxin.cms.exception.ServiceException;
import com.yaxin.cms.service.IArticleService;
import com.yaxin.cms.util.JwtUtil;
import com.yaxin.cms.util.RedisUtil;
import com.yaxin.cms.util.ResultCode;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author shaoyb
 * @program: 230314-cms
 * @description TODO
 * @create 2023/3/19 21:31
 **/
@Service
public class ArticleServiceImpl implements IArticleService {
	@Autowired
	private ArticleDao articleDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private CategoryDao categoryDao;
	@Autowired
	private CommentDao commentDao;
	@Autowired
	private RedisUtil redisUtil;

	private final String REDIS_KEY = "Article_Read_Num";

	public static String getToken() {
		// 第二种方式获取token
		ServletRequestAttributes requestAttributes =
				(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = requestAttributes.getRequest();

		return request.getHeader("Authorization");
	}

	@Override
	public void saveOrUpdate(Article article) {
		// 文章状态判断：如果文章已经发表，普通用户不能再修改其标题、内容等信息
		Map<String, Object> info = JwtUtil.parseJWT(getToken());
		// 1.参数判断
		if (article == null) {
			throw new ServiceException(ResultCode.PARAM_IS_BLANK);
		}

		// 2.用户判断：如果用户不存在，则抛异常
		Integer userId = (Integer) info.get("userId");
		System.out.println("userId = " + userId);
		if (userId != null && userDao.selectById(userId) == null) {
			throw new ServiceException(ResultCode.USER_NOT_EXIST);
		}
		article.setUserId(userId.longValue());
		// 3.栏目判断：如果栏目不存在，或栏目不是2级栏目，则抛异常
		Integer categoryId = article.getCategoryId();
		if (categoryId != null) {
			Category category = categoryDao.selectById(categoryId);
			if (category == null || category.getParentId() == null)
				throw new ServiceException(ResultCode.CATEGORY_NOT_EXIST);
		}

		Long id = article.getId();
		if (id == null) {
			// 4.新增操作
			// 准备文章发表时间
			article.setPublishTime(LocalDateTime.now());

			// 新增文章
			articleDao.insert(article);
		} else {
			// 5.修改操作
			// 判断文章id是否存在
			Article oldArticle = articleDao.selectById(id);
			if (oldArticle == null)
				throw new ServiceException(ResultCode.ARTICLE_NOT_EXIST);


			Integer roleId = (Integer) info.get("roleId");
			//System.out.println("roleId: " + roleId);
			if (roleId == 3 && "审核通过".equals(oldArticle.getStatus()))
				throw new ServiceException(ResultCode.PARAM_IS_INVALID);

			// 修改文章
			articleDao.updateById(article);
		}
	}

	@Override
	public void reviewArticle(Long id, String status) {
		// 1.参数判断
		if (id == null || status == null)
			throw new ServiceException(ResultCode.PARAM_IS_BLANK);

		// 2.文章必须存在
		if (articleDao.selectById(id) == null)
			throw new ServiceException(ResultCode.ARTICLE_NOT_EXIST);

		// 3.修改文章审核状态
		Article article = new Article();
		article.setId(id);
		article.setStatus(status);
		articleDao.updateById(article);
	}

	@Override
	public void deleteInBatch(List<Long> ids) {
		// 1.参数判断
		if (ids == null || ids.size() == 0)
			throw new ServiceException(ResultCode.PARAM_IS_BLANK);

		// 2.执行批量删除，只要成功删除1条，就算成功，一条都没有删除掉，算失败
		int num = articleDao.deleteBatchIds(ids);
		//System.out.println("num: " + num);

		if (num == 0)
			throw new ServiceException(ResultCode.PARAM_IS_INVALID);
	}

	//用户端也可以使用
	@Override
	public ArticleExtend queryByIdForCustomer(Long id) {
		// 1.参数判断
		if (id == null)
			throw new ServiceException(ResultCode.PARAM_IS_BLANK);

		// 2.先查询文章表，后续查询一级评论表
		Article article = articleDao.selectById(id);
		if (article == null)
			throw new ServiceException(ResultCode.ARTICLE_NOT_EXIST);

		// 3.判断文章审核状态是否为”审核通过“，如果不是则不能查看
		if (!"审核通过".equals(article.getStatus()))
			throw new ServiceException(ResultCode.ARTICLE_IS_NOT_VISIBLE);

		// 4.判断文章的发表人是否存在，如果不存在，则无法查看该文章
		Long userId = article.getUserId();
		User author = userDao.selectById(userId);
		if (author == null)
			throw new ServiceException(ResultCode.USER_NOT_EXIST);

		// 5.判断当前用户是否能查看当前文章
		String token = getToken();
		System.out.println("token============== = " + token);

		if (token == null) {
			// 5.1 如果当前用户为游客，则不能查看收费文章
			if (article.getCharged() == 1)
				throw new ServiceException(ResultCode.ARTICLE_IS_NOT_VISIBLE);
		} else {
			// 5.2 如果当前用户为登录用户（非会员），则不能查看收费文章

			//  根据token获取userId及isVip
			//System.out.println("userId =========== " + userId);
			Long currUserId = Long.parseLong(JwtUtil.getUserId(token));
			// 获取当前登录账户的isVip值
			Integer isVip = userDao.selectById(currUserId).getIsVip();

			// bug: 用户充值成为vip以后，要重新登录，更新token中isVip值
			//Map<String, Object> map = JwtUtil.getInfo(token);
			//Integer isVip = (Integer) map.get("isVip");

			// 如果当前用户不是文章作者，也不是Vip，同时文章收费，查看失败
			// 注意：Long值比较使用 equals方法进行
			if (!currUserId.equals(article.getUserId()) && article.getCharged() == 1 && isVip == 0) {
				throw new ServiceException(ResultCode.ARTICLE_IS_NOT_VISIBLE);
			}
		}

		// 6.复制文章对象属性 到 扩展类对象中
		ArticleExtend articleExtend = new ArticleExtend();
		BeanUtils.copyProperties(article, articleExtend);

		// 7.往扩展类对象中补充作者
		//额外注释密码，不能返回给前端
		author.setPassword(null);
		articleExtend.setAuthor(author);

		// 8.根据文章id查询一级评论，按发表时间倒序，取最近3条
		LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper();
		wrapper.eq(Comment::getArticleId, id)
				.orderByDesc(Comment::getPublishTime)
				.last("limit 3");
		List<Comment> comments = commentDao.selectList(wrapper);

		// 9.填入查询到的一级评论到扩展类对象中
		articleExtend.setComments(comments);

		// 10.浏览量自增
		articleExtend.setReadNum(redisUtil.increment(REDIS_KEY, article.getId().toString()));
		System.out.println("浏览量: " + articleExtend.getReadNum());

		return articleExtend;
	}

	//查询指定的文章（包含3条1级评论，还有作者）
	//专门为前端提供：
	@Override
	public ArticleExtend queryByIdWithComments(Long id) {
		// 1.参数判断
		if (id == null)
			throw new ServiceException(ResultCode.PARAM_IS_BLANK);

		// 2.先查询文章表，后续查询一级评论表
		Article article = articleDao.selectById(id);
		if (article == null)
			throw new ServiceException(ResultCode.ARTICLE_NOT_EXIST);

		// 3.判断文章审核状态是否为”审核通过“，如果不是则不能查看
		if (!"审核通过".equals(article.getStatus()))
			throw new ServiceException(ResultCode.ARTICLE_IS_NOT_VISIBLE);

		// 4.判断文章的发表人是否存在，如果不存在，则无法查看该文章
		Long userId = article.getUserId();
		if (userDao.selectById(userId) == null)
			throw new ServiceException(ResultCode.USER_NOT_EXIST);

		// 5.判断当前用户是否能查看当前文章
		//  获取token，进而获取userId及isVip
		String token = getToken();
		Long currUserId = Long.parseLong(JwtUtil.getUserId(token));
		// 获取当前登录账户的isVip值
		Integer isVip = userDao.selectById(currUserId).getIsVip();

		// bug: 用户充值成为vip以后，要重新登录，更新token中isVip值
		//Map<String, Object> map = JwtUtil.getInfo(token);
		//Integer isVip = (Integer) map.get("isVip");

		// 如果当前用户不是文章的拥有者，同时文章收费，当前用户还不是Vip，查看失败
		// 注意：Long值比较使用 equals方法进行
		if (!currUserId.equals(article.getUserId()) && article.getCharged() == 1 && isVip == 0) {
			throw new ServiceException(ResultCode.ARTICLE_IS_NOT_VISIBLE);
		}

		// 6.复制文章对象属性 到 扩展类对象中
		ArticleExtend articleExtend = new ArticleExtend();
		BeanUtils.copyProperties(article, articleExtend);

		// 7.根据文章id查询一级评论，按发表时间倒序，取最近3条
		LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(Comment::getArticleId, id)
				.orderByDesc(Comment::getPublishTime)
				.last("limit 3");
		List<Comment> comments = commentDao.selectList(wrapper);

		// 8.填入查询到的一级评论到扩展类对象中
		articleExtend.setComments(comments);
		//9.浏览量自增
		articleExtend.setReadNum(redisUtil.increment(REDIS_KEY, article.getId().toString()));

		return articleExtend;
	}

	@Override
	public IPage<ArticleExtend> query(ArticleParam param) {
		Page<Article> page = new Page<>(param.getPageNum(), param.getPageSize());

		// 准备查询条件
		LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(param.getUserId() != null, Article::getUserId, param.getUserId());
		wrapper.eq(StringUtils.hasText(param.getStatus()), Article::getStatus, param.getStatus());
		wrapper.like(StringUtils.hasText(param.getTitle()), Article::getTitle, param.getTitle());
		wrapper.eq(param.getCharged() != null, Article::getCharged, param.getCharged());
		wrapper.eq(param.getCategoryId() != null, Article::getCategoryId, param.getCategoryId());

		wrapper.le(param.getEndTime() != null, Article::getPublishTime, param.getEndTime());
		wrapper.ge(param.getStartTime() != null, Article::getPublishTime, param.getStartTime());
		//wrapper.between(Article::getPublishTime, param.getStartTime(), param.getEndTime());

		//执行查询
		articleDao.selectPage(page, wrapper);

		//获取查询的文章列表
		List<Article> records = page.getRecords();
		List<ArticleExtend> list = new ArrayList<>();

		//遍历所有文章，添加作者信息
		for (Article art : records) {
			Long userId = art.getUserId();
			User user = userDao.queryUserById(userId);
			//如果用户已经被删除，则文章不可见
			if (user == null)
				continue;

			ArticleExtend articleExtend = new ArticleExtend();
			BeanUtils.copyProperties(art, articleExtend);

			//从redis中获取浏览量 更新对象属性值
			//1.先查看redis中是否存在文章浏览量，如果没有则写入
			Object obj = redisUtil.getHash(REDIS_KEY)
					.get(articleExtend.getId().toString());
			if(obj == null) {
				redisUtil.hset(REDIS_KEY, articleExtend.getId().toString(), articleExtend.getReadNum());
			}
			//2.从redis中获取文章浏览量
			Integer readNum = (Integer) redisUtil.getHash(REDIS_KEY)
										.get(articleExtend.getId().toString());
			//System.out.println("in page, read obj: " + obj);
			articleExtend.setReadNum(readNum);

			//额外注释密码，不能返回给前端
			user.setPassword(null);
			articleExtend.setAuthor(user);
			list.add(articleExtend);
		}

		Page<ArticleExtend> pageInfo = new Page<>();
		pageInfo.setRecords(list);
		pageInfo.setTotal(page.getTotal());
		pageInfo.setCurrent(page.getCurrent());

		return pageInfo;
	}

	/**
	 * 根据文章ID查询文章信息
	 *
	 * @param id
	 * @return
	 */
	@Override
	public ArticleExtend queryById(Long id) {
		//根据ID查询指定文章
		Article article = articleDao.selectById(id);
		//创建文章扩展类对象
		ArticleExtend articleExtend = new ArticleExtend();
		//Bean拷贝
		BeanUtils.copyProperties(article, articleExtend);
		//返回文章扩展类对象
		return articleExtend;
	}

	@Override
	public List<Article> list() {
		return articleDao.selectList(null);
	}
}
