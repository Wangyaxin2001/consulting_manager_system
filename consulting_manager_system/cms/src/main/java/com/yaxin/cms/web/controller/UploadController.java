package com.yaxin.cms.web.controller;

import com.yaxin.cms.util.Result;
import com.yaxin.cms.util.UploadUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author horry
 * @Description 文件上传接口
 * @date 2023/8/18-16:37
 */
@Api(tags = "文件上传模块")
@Slf4j
@RestController
public class UploadController {

	@Autowired
	private UploadUtils uploadUtils;

	@ApiOperation("文件上传")
	//@PostMapping("/upload")
	@PostMapping("/auth/upload")
	@SneakyThrows  //帮助处理 编译时异常
	public Result upload(@RequestPart MultipartFile img){
		return Result.success(uploadUtils.fileToOSS(img));
	}
}
