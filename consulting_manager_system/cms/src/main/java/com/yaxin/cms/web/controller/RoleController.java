package com.yaxin.cms.web.controller;


import com.yaxin.cms.service.IRoleService;
import com.yaxin.cms.util.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author briup
 * @since 2023-03-10
 */
@RestController
@RequestMapping("/auth/role")
public class RoleController {

	@Autowired
	private IRoleService roleService;

	@ApiOperation("获取所有角色信息")
	//@GetMapping("/getAllRole")
	@GetMapping("/getAll")
	public Result getAll(){
		return Result.success(roleService.list());
	}
}

